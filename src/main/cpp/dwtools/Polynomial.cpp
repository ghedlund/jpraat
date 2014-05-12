/* Polynomial.cpp
 *
 * Copyright (C) 1993-2013 David Weenink
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 djmw 20020813 GPL header
 djmw 20030619 Added SVD_compute before SVD_solve
 djmw 20060510 Polynomial_to_Roots: changed behaviour. All roots found are now saved.
 	In previous version a NULL pointer was returned. New error messages.
 djmw 20061021 printf expects %ld for 'long int'
 djmw 20071012 Added: o_CAN_WRITE_AS_ENCODING.h
 djmw 20071201 Melder_warning<n>
 djmw 20080122 float -> double
  djmw 20110304 Thing_new
*/

#include "Polynomial.h"
#include "SVD.h"
#include "NUMclapack.h"
#include "TableOfReal_extensions.h"
#include "NUMmachar.h"

#include "oo_DESTROY.h"
#include "Polynomial_def.h"
#include "oo_COPY.h"
#include "Polynomial_def.h"
#include "oo_EQUAL.h"
#include "Polynomial_def.h"
#include "oo_CAN_WRITE_AS_ENCODING.h"
#include "Polynomial_def.h"
#include "oo_WRITE_TEXT.h"
#include "Polynomial_def.h"
#include "oo_WRITE_BINARY.h"
#include "Polynomial_def.h"
#include "oo_READ_TEXT.h"
#include "Polynomial_def.h"
#include "oo_READ_BINARY.h"
#include "Polynomial_def.h"
#include "oo_DESCRIPTION.h"
#include "Polynomial_def.h"

#define MAX(m,n) ((m) > (n) ? (m) : (n))
#define MIN(m,n) ((m) < (n) ? (m) : (n))

/* Evaluate polynomial and derivative jointly
	c[1..n] -> degree n-1 !!
*/
static void Polynomial_evaluate2 (I, double x, double *f, double *df) {
	iam (Polynomial);
	long double p = my coefficients[my numberOfCoefficients], dp = 0, xc = x;

	for (long i = my numberOfCoefficients - 1; i > 0; i--) {
		dp = dp * xc + p;
		p =  p * xc + my coefficients[i];
	}
	*f = p; *df = dp;
}

/* Get value and derivative */
static void Polynomial_evaluate2_z (I, dcomplex *z, dcomplex *p, dcomplex *dp) {
	iam (Polynomial);
	long double pr = my coefficients[my numberOfCoefficients], pi = 0;
	long double dpr = 0, dpi = 0, x = z -> re, y = z -> im;

	for (long i = my numberOfCoefficients - 1; i > 0; i--) {
		long double tr   = dpr;
		dpr  =  dpr * x -  dpi * y + pr;
		dpi  =   tr * y +  dpi * x + pi;
		tr   = pr;
		pr   =   pr * x -   pi * y + my coefficients[i];
		pi   =   tr * y +   pi * x;
	}
	p -> re =   pr;  p -> im =  pi;
	dp -> re =  dpr; dp -> im = dpi;
}

/*
	void polynomial_divide (double *u, long m, double *v, long n, double *q, double *r);

	Purpose:
		Find the quotient q(x) and the remainder r(x) polynomials that result from the division of
		the polynomial u(x) = u[1] + u[2]*x^1 + u[3]*x^2 + ... + u[m]*x^(m-1) by the
		polynomial v(x) = v[1] + v[2]*x^1 + v[3]*x^2 + ... + v[n]*x^(n-1), such that
			u(x) = v(x)*q(x) + r(x).

	The arrays u, v, q and r have to be dimensioned as u[1...m], v[1..n], q[1...m] and r[1...m],
	respectively.
	On return, the q[1..m-n] and r[1..n-1] contain the quotient and the remainder
	polynomial coefficients, repectively.
	See Knuth, The Art of Computer Programming, Volume 2: Seminumerical algorithms,
	Third edition, Section 4.6.1 Algorithm D (the algorithm as described has been modified
	to prevent overwriting of the u-polynomial).
*/
static void polynomial_divide (double *u, long m, double *v, long n, double *q, double *r) {
	// Copy u[1..m] into r[1..n] to prevent overwriting of u.
	// Put the q coefficients to zero for cases n > m.

	for (long k = 1; k <= m; k++) {
		r[k] = u[k];
		q[k] = 0;
	}

	for (long k = m - n + 1; k > 0; k--) { /* D1 */
		q[k] = r[n + k - 1] / v[n]; /* D2 with u -> r*/
		for (long j = n + k - 1; j >= k; j--) {
			r[j] -= q[k] * v[j - k + 1];
		}
	}
}

static void Polynomial_polish_realroot (I, double *x, long maxit) {
	iam (Polynomial);
	double xbest = *x, pmin = 1e38;
	if (! NUMfpp) {
		NUMmachar ();
	}

	for (long i = 1; i <= maxit; i++) {
		double p, dp;
		Polynomial_evaluate2 (me, *x, &p, &dp);
		double fabsp = fabs (p);
		if (fabsp > pmin || fabs (fabsp - pmin) < NUMfpp -> eps) {
			// We stop because the approximation gets worse or we cannot get closer anymore
			// Return previous (best) value for x.

			*x = xbest; return;
		}
		pmin = fabsp; xbest = *x;
		if (fabs (dp) == 0) {
			return;
		}
		double dx = p / dp; /* Newton -Raphson */
		*x -= dx;
	}
	// Melder_throw ("Maximum number of iterations exceeded.");
}

static void Polynomial_polish_complexroot_nr (I, dcomplex *z, long maxit) {
	iam (Polynomial);
	dcomplex zbest = *z;
	double pmin = 1e38;
	if (! NUMfpp) {
		NUMmachar ();
	}

	for (long i = 1; i <= maxit; i++) {
		dcomplex p, dp;
		Polynomial_evaluate2_z (me, z, &p, &dp);
		double fabsp = dcomplex_abs (p);
		if (fabsp > pmin || fabs (fabsp - pmin) < NUMfpp -> eps) {
			// We stop because the approximation gets worse.
			// Return previous (best) value for z.

			*z = zbest; return;
		}
		pmin = fabsp; zbest = *z;
		if (dcomplex_abs (dp) == 0) {
			return;
		}
		dcomplex dz = dcomplex_div (p , dp); /* Newton -Raphson */
		*z = dcomplex_sub (*z, dz);
	}
	// Melder_throw ("Maximum number of iterations exceeded.");
}

/*
	Symbolic evaluation of polynomial coefficients.
	Recurrence: P[n] = (a[n] * x + b[n]) P[n-1] + c[n] P[n-2],
		where P[n] is any orthogonal polynomial of degree n.
	P[n] is an array of coefficients p[k] representing: p[1] + p[2] x + ... p[n+1] x^n.
	Preconditions:
		degree > 1;
		pnm1 : polynomial of degree n - 1
		pnm2 : polynomial of degree n - 2
*/
static void NUMpolynomial_recurrence (double *pn, long degree, double a,
                                      double b, double c, double *pnm1, double *pnm2) {
	Melder_assert (degree > 1);

	pn[1] = b * pnm1[1] + c * pnm2[1];
	for (long i = 2; i <= degree - 1; i++) {
		pn[i] = a * pnm1[i - 1] + b * pnm1[i] + c * pnm2[i];
	}
	pn[degree] = a * pnm1[degree - 1] + b * pnm1[degree];
	pn[degree + 1] = a * pnm1[degree];
}


/* frozen[1..ma] */
static void svdcvm (double **v, long mfit, long ma, int *frozen, double *w, double **cvm) {
	autoNUMvector<double> wti (1, mfit);

	for (long i = 1; i <= mfit; i++) {
		if (w[i]) {
			wti[i] = 1.0 / (w[i] * w[i]);
		}
	}
	for (long i = 1; i <= mfit; i++) {
		for (long j = 1; j <= i; j++) {
			double sum = 0;
			for (long k = 1; k <= mfit; k++) {
				sum += v[i][k] * v[j][k] * wti[k];
			}
			cvm[j][i] = cvm[i][j] = sum;
		}
	}

	for (long i = mfit + 1; i <= ma; i++) {
		for (long j = 1; j <= i; j++) {
			cvm[j][i] = cvm[i][j] = 0;
		}
	}

	long k = mfit;
	for (long j = ma; j > 0; j--) {
		//			if (! frozen || ! frozen[i]) why i?? TODO
		if (! frozen || ! frozen[j]) {
			for (long i = 1; i <= ma; i++) {
				double t = cvm[i][k]; cvm[i][k] = cvm[i][j]; cvm[i][j] = t;
			}
			for (long i = 1; i <= ma; i++) {
				double t = cvm[k][i]; cvm[k][i] = cvm[j][i]; cvm[j][i] = t;
			}
			k--;
		}
	}
}

/********* FunctionTerms *********************************************/

Thing_implement (FunctionTerms, Function, 0);

double structFunctionTerms :: v_evaluate (double x) {
	(void) x;
	return NUMundefined;
}

void structFunctionTerms :: v_evaluate_z (dcomplex *z, dcomplex *p) {
	(void) z;
	p -> re = p -> im = NUMundefined;
}

void structFunctionTerms :: v_evaluateTerms (double x, double terms[]) {
	(void) x;
	for (long i = 1; i <= numberOfCoefficients; i++) {
		terms[i] = NUMundefined;
	}
}

long structFunctionTerms :: v_getDegree () {
	return numberOfCoefficients - 1;
}

void structFunctionTerms :: v_getExtrema (double x1, double x2, double *lxmin, double *ymin, double *lxmax, double *ymax) { // David, geen aparte naam hier nodig: ???
	long numberOfPoints = 1000;

	// Melder_warning (L"defaultGetExtrema: extrema calculated by sampling the interval");

	double x = x1, dx = (x2 - x1) / (numberOfPoints - 1);
	*lxmin = *lxmax = x; *ymin = *ymax = v_evaluate (x);
	for (long i = 2; i <= numberOfPoints; i++) {
		x += dx;
		double y = v_evaluate (x);
		if (y > *ymax) {
			*ymax = y; *lxmax = x;
		} else if (y < *ymin) {
			*ymin = y; *lxmin = x;
		}
	}
}

void FunctionTerms_init (I, double xmin, double xmax, long numberOfCoefficients) {
	iam (FunctionTerms);
	my coefficients = NUMvector<double> (1, numberOfCoefficients);
	my numberOfCoefficients = numberOfCoefficients;
	my xmin = xmin; my xmax = xmax;
}

FunctionTerms FunctionTerms_create (double xmin, double xmax, long numberOfCoefficients) {
	try {
		autoFunctionTerms me = Thing_new (FunctionTerms);
		FunctionTerms_init (me.peek(), xmin, xmax, numberOfCoefficients);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("FunctionTerms not created.");
	}
}

void FunctionTerms_initFromString (I, double xmin, double xmax, const wchar_t *s, int allowTrailingZeros) {
	iam (FunctionTerms);
	long numberOfCoefficients;
	autoNUMvector<double> numbers (NUMstring_to_numbers (s, &numberOfCoefficients), 1);
	if (! allowTrailingZeros) {
		while (numbers[numberOfCoefficients] == 0 && numberOfCoefficients > 1) {
			numberOfCoefficients--;
		}
	}

	FunctionTerms_init (me, xmin, xmax, numberOfCoefficients);
	NUMvector_copyElements (numbers.peek(), my coefficients, 1, numberOfCoefficients);
}

long FunctionTerms_getDegree (I) {
	iam (FunctionTerms);
	return my v_getDegree ();
}

void FunctionTerms_setDomain (I, double xmin, double xmax) {
	iam (FunctionTerms);
	my xmin = xmin; my xmax = xmax;
}

double FunctionTerms_evaluate (I, double x) {
	iam (FunctionTerms);
	return my v_evaluate (x);
}

void FunctionTerms_evaluate_z (I, dcomplex *z, dcomplex *p) {
	iam (FunctionTerms);
	my v_evaluate_z (z, p);
}

void FunctionTerms_evaluateTerms (I, double x, double terms[]) {
	iam (FunctionTerms);
	my v_evaluateTerms (x, terms);
}

void FunctionTerms_getExtrema (I, double x1, double x2, double *xmin, double *ymin, double *xmax, double *ymax) {
	iam (FunctionTerms);
	if (x2 <= x1) {
		x1 = my xmin; x2 = my xmax;
	}
	my v_getExtrema (x1, x2, xmin, ymin, xmax, ymax);
}

double FunctionTerms_getMinimum (I, double x1, double x2) {
	iam (FunctionTerms);
	double xmin, xmax, ymin, ymax;
	FunctionTerms_getExtrema (me, x1, x2, &xmin, &ymin, &xmax, &ymax);
	return ymin;
}

double FunctionTerms_getXOfMinimum (I, double x1, double x2) {
	iam (FunctionTerms);
	double xmin, xmax, ymin, ymax;
	FunctionTerms_getExtrema (me, x1, x2, &xmin, &ymin, &xmax, &ymax);
	return xmin;
}

double FunctionTerms_getMaximum (I, double x1, double x2) {
	iam (FunctionTerms);
	double xmin, xmax, ymin, ymax;
	FunctionTerms_getExtrema (me, x1, x2, &xmin, &ymin, &xmax, &ymax);
	return ymax;
}

double FunctionTerms_getXOfMaximum (I, double x1, double x2) {
	iam (FunctionTerms);
	double xmin, xmax, ymin, ymax;
	FunctionTerms_getExtrema (me, x1, x2, &xmin, &ymin, &xmax, &ymax);
	return xmax;
}

static void Graphics_polyline_clipTopBottom (Graphics g, double *x, double *y, long numberOfPoints, double ymin, double ymax) {
	long index = 0;

	if (numberOfPoints < 2) {
		return;
	}
	double x1 = x[0], y1 = y[0];
	double xb = x1, yb = y1;

	for (long i = 1; i < numberOfPoints; i++) {
		double x2 = x[i], y2 = y[i];

		if (! ( (y1 > ymax && y2 > ymax) || (y1 < ymin && y2 < ymin))) {
			double dxy = (x2 - x1) / (y1 - y2);
			double xcros_max = x1 - (ymax - y1) * dxy;
			double xcros_min = x1 - (ymin - y1) * dxy;
			if (y1 > ymax && y2 < ymax) {
				// Line enters from above: start new segment. Save start values.

				xb = x[i - 1]; yb = y[i - 1]; index = i - 1;
				y[i - 1] = ymax; x[i - 1] = xcros_max;
			}
			if (y1 > ymin && y2 < ymin) {
				// Line leaves at bottom: draw segment. Save end values and restore them
				// Origin of arrays for Graphics_polyline are at element 0 !!!

				double xe = x[i], ye = y[i];
				y[i] = ymin; x[i] = xcros_min;

				Graphics_polyline (g, i - index + 1, x + index, y + index);

				x[index] = xb; y[index] = yb; x[i] = xe; y[i] = ye;
			}
			if (y1 < ymin && y2 > ymin) {
				// Line enters from below: start new segment. Save start values

				xb = x[i - 1]; yb = y[i - 1]; index = i - 1; // David, dit klopt toch niet? xb is tweemaal gedefinieerd, en hier verdwijnt een toekenning; Klopt, nu niet meer (was overigens geen probleem vanwege nieuwe toekenning!)
				y[i - 1] = ymin; x[i - 1] = xcros_min;
			}
			if (y1 < ymax && y2 > ymax) {
				// Line leaves at top: draw segment. Save and restore

				double xe = x[i], ye = y[i];
				y[i] = ymax; x[i] =  xcros_max;

				Graphics_polyline (g, i - index + 1, x + index, y + index);

				x[index] = xb; y[index] = yb; x[i] = xe; y[i] = ye;
			}
		} else {
			index = i;
		}
		y1 = y2; x1 = x2;
	}
	if (index < numberOfPoints - 1) {
		Graphics_polyline (g, numberOfPoints - index, x + index, y + index);
		x[index] = xb; y[index] = yb;
	}
}

void FunctionTerms_draw (I, Graphics g, double xmin, double xmax, double ymin,
                         double ymax, int extrapolate, int garnish) {
	iam (FunctionTerms);
	long numberOfPoints = 1000;

	autoNUMvector<double> y (1, numberOfPoints + 1);
	autoNUMvector<double> x (1, numberOfPoints + 1);

	if (xmax <= xmin) {
		xmin = my xmin; xmax = my xmax;
	}

	double fxmin = xmin, fxmax = xmax;
	if (! extrapolate) {
		if (xmax < my xmin || xmin > my xmax) {
			return;
		}
		if (xmin < my xmin) {
			fxmin = my xmin;
		}
		if (xmax > my xmax) {
			fxmax = my xmax;
		}
	}

	if (ymax <= ymin) {
		double x1, x2;
		FunctionTerms_getExtrema (me, fxmin, fxmax, &x1, &ymin, &x2, &ymax);
	}

	Graphics_setInner (g);
	Graphics_setWindow (g, xmin, xmax, ymin, ymax);

	// Draw only the parts within [fxmin, fxmax] X [ymin, ymax].

	double dx = (fxmax - fxmin) / (numberOfPoints - 1);
	for (long i = 1; i <= numberOfPoints; i++) {
		x[i] = fxmin + (i - 1.0) * dx;
		y[i] = FunctionTerms_evaluate (me, x[i]);
	}
	//Graphics_polyline_clipTopBottom (g, x+1, y+1, numberOfPoints, ymin, ymax);
	Graphics_polyline_clipTopBottom (g, &x[1], &y[1], numberOfPoints, ymin, ymax);
	Graphics_unsetInner (g);

	if (garnish) {
		Graphics_drawInnerBox (g);
		Graphics_marksBottom (g, 2, 1, 1, 0);
		Graphics_marksLeft (g, 2, 1, 1, 0);
	}
}

void FunctionTerms_drawBasisFunction (I, Graphics g, long index, double xmin,
                                      double xmax, double ymin, double ymax, int extrapolate, int garnish) {
	iam (FunctionTerms);
	if (index < 1 || index > my numberOfCoefficients) {
		return;
	}
	autoFunctionTerms thee = (FunctionTerms) Data_copy (me);

	for (long i = 1; i <= my numberOfCoefficients; i++) {
		thy coefficients[i] = 0;
	}
	thy coefficients[index] = 1;
	thy numberOfCoefficients = index;
	FunctionTerms_draw (thee.peek(), g, xmin, xmax, ymin, ymax, extrapolate, garnish);
}

void FunctionTerms_setCoefficient (I, long index, double value) {
	iam (FunctionTerms);
	if (index < 1 || index > my numberOfCoefficients) {
		Melder_throw (L"Idex out of range [1, ", my numberOfCoefficients, L"].");
	}
	if (index == my numberOfCoefficients && value == 0) {
		Melder_throw (L"You cannot remove the highest degree term.");
	}
	my coefficients[index] = value;
}

/********** Polynomial ***********************************************/

Thing_implement (Polynomial, FunctionTerms, 0);

double structPolynomial :: v_evaluate (double x) {
	long double p = coefficients [numberOfCoefficients];

	for (long i = numberOfCoefficients - 1; i > 0; i--) {
		p = p * x + coefficients [i];
	}
	return p;
}

void structPolynomial :: v_evaluate_z (dcomplex *z, dcomplex *p) {
	long double x = z -> re, y = z -> im;

	long double pr = coefficients [numberOfCoefficients];
	long double pi = 0;
	for (long i = numberOfCoefficients - 1; i > 0; i--) {
		long double prtmp = pr;
		pr =  pr * x - pi * y + coefficients[i];
		pi = prtmp * y + pi * x;
	}
	p -> re = pr; p -> im = pi;
}

void structPolynomial :: v_evaluateTerms (double x, double terms[]) {
	terms[1] = 1;
	for (long i = 2; i <= numberOfCoefficients; i++) {
		terms[i] = terms[i - 1] * x;
	}
}

void structPolynomial :: v_getExtrema (double x1, double x2, double *lxmin, double *ymin, double *lxmax, double *ymax) {
	try {
		long degree = numberOfCoefficients - 1;

		*lxmin = x1; *ymin = v_evaluate (x1);
		*lxmax = x2; *ymax = v_evaluate (x2);
		if (*ymin > *ymax) {
			/* Swap */
			double t = *ymin; *ymin = *ymax; *ymax = t;
			t = *lxmin; *lxmin = *lxmax; *lxmax = t;
		}

		if (degree < 2) {
			return;
		}
		autoPolynomial d = Polynomial_getDerivative (this);
		autoRoots r = Polynomial_to_Roots (d.peek());

		for (long i = 1; i <= degree - 1; i++) {
			double x = (r -> v[i]).re;
			if (x > x1 && x < x2) {
				double y = v_evaluate (x);
				if (y > *ymax) {
					*ymax = y; *lxmax = x;
				} else if (y < *ymin) {
					*ymin = y; *lxmin = x;
				}
			}
		}
	} catch (MelderError) {
		structFunctionTerms :: v_getExtrema (x1, x2, lxmin, ymin, lxmax, ymax);
		Melder_clearError ();
	}
}

Polynomial Polynomial_create (double lxmin, double lxmax, long degree) {
	try {
		autoPolynomial me = Thing_new (Polynomial);
		FunctionTerms_init (me.peek(), lxmin, lxmax, degree + 1);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("Polynomial not created.");
	}
}

Polynomial Polynomial_createFromString (double lxmin, double lxmax, const wchar_t *s) {
	try {
		autoPolynomial me = Thing_new (Polynomial);
		FunctionTerms_initFromString (me.peek(), lxmin, lxmax, s, 0);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("Polynomial not created from string.");
	}
}

void Polynomial_scaleCoefficients_monic (Polynomial me) {
	double cn = my coefficients[my numberOfCoefficients];

	if (cn == 1 || my numberOfCoefficients <= 1) {
		return;
	}

	for (long i = 1; i < my numberOfCoefficients; i++) {
		my coefficients[i] /= cn;
	}
	my coefficients[my numberOfCoefficients] = 1;
}

/*
	Transform the polynomial as if the domain were [xmin, xmax].
	Some polynomials (Legendre) are defined on the domain [-1,1]. The domain
	for x may be extended to [xmin, xmax] by a transformation such as
		x' = (2 * x - (xmin + xmax)) / (xmax - xmin)   -1 < x' < x.
	This procedure transforms x' back to x.
*/
Polynomial Polynomial_scaleX (Polynomial me, double xmin, double xmax) {
	try {
		Melder_assert (xmin < xmax);

		autoPolynomial thee = Polynomial_create (xmin, xmax, my numberOfCoefficients - 1);
		thy coefficients[1] = my coefficients[1];
		if (my numberOfCoefficients == 1) {
			return thee.transfer();
		}

		// x = a x + b
		// Constraints:
		// my xmin = a xmin + b;    a = (my xmin - my xmax) / (xmin - xmax);
		// my xmax = a xmax + b;    b = my xmin - a * xmin

		double a = (my xmin - my xmax) / (xmin - xmax);
		double b = my xmin - a * xmin;
		thy coefficients[2] = my coefficients[2] * a;
		thy coefficients[1] += my coefficients[2] * b;
		if (my numberOfCoefficients == 2) {
			return thee.transfer();
		}
		autoNUMvector<double> buf (1, 3 * my numberOfCoefficients);
		double *pn = buf.peek();
		double *pnm1 = pn   + my numberOfCoefficients;
		double *pnm2 = pnm1 + my numberOfCoefficients;

		// Start the recursion: P[1] = a x + b; P[0] = 1;

		pnm1[2] = a; pnm1[1] = b; pnm2[1] = 1;
		for (long n = 2; n <= my numberOfCoefficients - 1; n++) {
			double *t1 = pnm1, *t2 = pnm2;
			NUMpolynomial_recurrence (pn, n, a, b, 0, pnm1, pnm2);
			if (my coefficients[n + 1] != 0) {
				for (long j = 1; j <= n + 1; j++) {
					thy coefficients[j] += my coefficients[n + 1] * pn[j];
				}
			}
			pnm1 = pn;
			pnm2 = t1;
			pn = t2;
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw ("");
	}
}

double Polynomial_evaluate (I, double x) {
	iam (Polynomial);
	return my v_evaluate (x);
}

void Polynomial_evaluate_z (Polynomial me, dcomplex *z, dcomplex *p) {
	my v_evaluate_z (z, p);
}

static void Polynomial_evaluate_z_cart (Polynomial me, double r, double phi, double *re, double *im) {
	double rn = 1;

	*re = my coefficients[1]; *im = 0;
	if (r == 0) {
		return;
	}
	for (long i = 2; i <= my numberOfCoefficients; i++) {
		rn *= r;
		double arg = (i - 1) * phi;
		*re += my coefficients[i] * rn * cos (arg);
		*im += my coefficients[i] * rn * sin (arg);
	}
}


Polynomial Polynomial_getDerivative (Polynomial me) {
	try {
		if (my numberOfCoefficients == 1) {
			return Polynomial_create (my xmin, my xmax, 0);
		}
		autoPolynomial thee = Polynomial_create (my xmin, my xmax, my numberOfCoefficients - 2);
		for (long i = 1; i <= thy numberOfCoefficients; i++) {
			thy coefficients[i] = i * my coefficients[i + 1];
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no derivative created.");
	}
}

Polynomial Polynomial_getPrimitive (Polynomial me) {
	try {
		autoPolynomial thee = Polynomial_create (my xmin, my xmax, my numberOfCoefficients);
		for (long i = 1; i <= my numberOfCoefficients; i++) {
			thy coefficients[i + 1] = my coefficients[i] / i;
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no primitive created.");
	}
}

double Polynomial_getArea (Polynomial me, double xmin, double xmax) {
	if (xmax >= xmin) {
		xmin = my xmin; xmax = my xmax;
	}
	autoPolynomial p = Polynomial_getPrimitive (me);
	double area = FunctionTerms_evaluate (p.peek(), xmax) - FunctionTerms_evaluate (p.peek(), xmin);
	return area;
}

Polynomial Polynomials_multiply (Polynomial me, Polynomial thee) {
	try {
		long n1 = my numberOfCoefficients, n2 = thy numberOfCoefficients;

		if (my xmax <= thy xmin || my xmin >= thy xmax) {
			Melder_throw ("Domains do not overlap.");
		}
		double xmin = my xmin > thy xmin ? my xmin : thy xmin;
		double xmax = my xmax < thy xmax ? my xmax : thy xmax;
		autoPolynomial him = Polynomial_create (xmin, xmax, n1 + n2 - 2);
		for (long i = 1; i <= n1; i++) {
			for (long j = 1; j <= n2; j++) {
				long k = i + j - 1;
				his coefficients [k] += my coefficients[i] * thy coefficients[j];
			}
		}
		return him.transfer();
	} catch (MelderError) {
		Melder_throw ("Polynomials not multiplied.");
	}
}

void Polynomials_divide (Polynomial me, Polynomial thee, Polynomial *q, Polynomial *r) {
	long degree, m = my numberOfCoefficients, n = thy numberOfCoefficients;

	if (*q == 0 && *r == 0) {
		return;
	}
	autoNUMvector<double> qc (1, m);
	autoNUMvector<double> rc (1, m);
	autoPolynomial aq = 0, ar = 0;
	polynomial_divide (my coefficients, m, thy coefficients, n, qc.peek(), rc.peek());
	if (*q) {
		degree = MAX (m - n, 0);
		aq.reset (Polynomial_create (my xmin, my xmax, degree));
		if (degree >= 0) {
			NUMvector_copyElements (qc.peek(), aq -> coefficients, 1, degree + 1);
		}
	}
	if (*r) {
		degree = n - 2;
		if (m >= n) {
			degree --;
		}
		if (degree < 0) {
			degree = 0;
		}
		while (degree > 1 && rc[degree] == 0) {
			degree--;
		}
		ar.reset (Polynomial_create (my xmin, my xmax, degree));
		NUMvector_copyElements (rc.peek(), ar -> coefficients, 1, degree + 1);
	}
	*q = aq.transfer();
	*r = ar.transfer();
}


/******** LegendreSeries ********************************************/

Thing_implement (LegendreSeries, FunctionTerms, 0);

double structLegendreSeries :: v_evaluate (double x) {
	double p = coefficients[1];

	// Transform x from domain [xmin, xmax] to domain [-1, 1]

	if (x < xmin || x > xmax) {
		return NUMundefined;
	}

	double pim1 = x = (2 * x - xmin - xmax) / (xmax - xmin);

	if (numberOfCoefficients > 1) {
		double pim2 = 1, twox = 2 * x, f2 = x, d = 1.0;
		p += coefficients[2] * pim1;
		for (long i = 3; i <= numberOfCoefficients; i++) {
			double f1 = d++;
			f2 += twox;
			double pi = (f2 * pim1 - f1 * pim2) / d;
			p += coefficients[i] * pi;
			pim2 = pim1; pim1 = pi;
		}
	}
	return p;
}

void structLegendreSeries :: v_evaluateTerms (double x, double terms[]) {
	if (x < xmin || x > xmax) {
		for (long i = 1; i <= numberOfCoefficients; i++) {
			terms[i] = NUMundefined;
		}
		return;
	}

	// Transform x from domain [xmin, xmax] to domain [-1, 1]

	x = (2 * x - xmin - xmax) / (xmax - xmin);

	terms[1] = 1;
	if (numberOfCoefficients > 1) {
		double twox = 2 * x, f2 = x, d = 1.0;
		terms[2] = x;
		for (long i = 3; i <= numberOfCoefficients; i++) {
			double f1 = d++;
			f2 += twox;
			terms[i] = (f2 * terms[i - 1] - f1 * terms[i - 2]) / d;
		}
	}
}

void structLegendreSeries :: v_getExtrema (double x1, double x2,
        double *lxmin, double *ymin, double *lxmax, double *ymax) {
	try {
		autoPolynomial p = LegendreSeries_to_Polynomial (this);
		FunctionTerms_getExtrema (p.peek(), x1, x2, lxmin, ymin, lxmax, ymax);
	} catch (MelderError) {
		structFunctionTerms :: v_getExtrema (x1, x2, lxmin, ymin, lxmax, ymax);
		Melder_clearError ();
	}
}

LegendreSeries LegendreSeries_create (double xmin, double xmax, long numberOfPolynomials) {
	try {
		autoLegendreSeries me = Thing_new (LegendreSeries);
		FunctionTerms_init (me.peek(), xmin, xmax, numberOfPolynomials);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("LegendreSeries not created.");
	}
}

LegendreSeries LegendreSeries_createFromString (double xmin, double xmax, const wchar_t *s) {
	try {
		autoLegendreSeries me = Thing_new (LegendreSeries);
		FunctionTerms_initFromString (me.peek(), xmin, xmax, s, 0);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("LegendreSeries not created from string.");
	}
}

LegendreSeries LegendreSeries_getDerivative (LegendreSeries me) {
	try {
		autoLegendreSeries thee = LegendreSeries_create (my xmin, my xmax, my numberOfCoefficients - 1);

		for (long n = 1; n <= my numberOfCoefficients - 1; n++) {
			// P[n]'(x) = Sum (k=0..nonNegative, (2n - 4k - 1) P[n-2k-1](x))

			long n2 = n - 1;
			for (long k = 0; n2 >= 0; k++, n2 -= 2) {
				thy coefficients [n2 + 1] += (2 * n - 4 * k - 1) * my coefficients[n + 1];
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no derivative created.");
	}
}

Polynomial LegendreSeries_to_Polynomial (LegendreSeries me) {
	try {
		double xmin = -1, xmax = 1;
		autoPolynomial thee = Polynomial_create (xmin, xmax, my numberOfCoefficients - 1);

		thy coefficients[1] = my coefficients[1]; /* * p[1] */
		if (my numberOfCoefficients == 1) {
			return thee.transfer();
		}

		thy coefficients[2] = my coefficients[2]; /* * p[2] */
		if (my numberOfCoefficients > 2) {
			autoNUMvector<double> buf (1, 3 * my numberOfCoefficients);

			double *pn = buf.peek();
			double *pnm1 = pn   + my numberOfCoefficients;
			double *pnm2 = pnm1 + my numberOfCoefficients;

			// Start the recursion: P[1] = x; P[0] = 1;

			pnm1[2] = 1; pnm2[1] = 1;
			for (long n = 2; n <= my numberOfCoefficients - 1; n++) {
				double a = (2 * n - 1.0) / n;
				double c = - (n - 1.0) / n;
				double *t1 = pnm1, *t2 = pnm2;
				NUMpolynomial_recurrence (pn, n, a, 0, c, pnm1, pnm2);
				if (my coefficients[n + 1] != 0) {
					for (long j = 1; j <= n + 1; j++) {
						thy coefficients[j] += my coefficients[n + 1] * pn[j];
					}
				}
				pnm1 = pn; pnm2 = t1; pn = t2;
			}
		}
		if (my xmin != xmin || my xmax != xmax) {
			thee.reset (Polynomial_scaleX (thee.peek(), my xmin, my xmax));
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": not converted to Polynomial.");
	}
}

/********* Roots ****************************************************/

Thing_implement (Roots, ComplexVector, 0);

Roots Roots_create (long numberOfRoots) {
	try {
		autoRoots me = Thing_new (Roots);
		ComplexVector_init (me.peek(), 1, numberOfRoots);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("Roots not created.");
	}
}

void Roots_fixIntoUnitCircle (Roots me) {
	dcomplex z10 = dcomplex_create (1, 0);
	for (long i = my min; i <= my max; i++) {
		if (dcomplex_abs (my v[i]) > 1.0) {
			my v[i] = dcomplex_div (z10, dcomplex_conjugate (my v[i]));
		}
	}
}

static void NUMdcvector_extrema_re (dcomplex v[], long lo, long hi, double *min, double *max) {
	*min = *max = v[lo].re;
	for (long i = lo + 1; i <= hi; i++) {
		if (v[i].re < *min) {
			*min = v[i].re;
		} else if (v[i].re > *max) {
			*max = v[i].re;
		}
	}
}

static void NUMdcvector_extrema_im (dcomplex v[], long lo, long hi, double *min, double *max) {
	*min = *max = v[lo].im;
	for (long i = lo + 1; i <= hi; i++) {
		if (v[i].im < *min) {
			*min = v[i].im;
		} else if (v[i].im > *max) {
			*max = v[i].im;
		}
	}
}

void Roots_draw (Roots me, Graphics g, double rmin, double rmax, double imin,
                 double imax, const wchar_t *symbol, int fontSize, int garnish) {
	int oldFontSize = Graphics_inqFontSize (g);
	double eps = 1e-6;

	if (rmax <= rmin) {
		NUMdcvector_extrema_re (my v, 1, my max, &rmin, &rmax);
	}
	double denum = fabs (rmax) > fabs (rmin) ? fabs (rmax) : fabs (rmin);
	if (denum == 0) {
		denum = 1;
	}
	if (fabs ( (rmax - rmin) / denum) < eps) {
		rmin -= 1; rmax += 1;
	}
	if (imax <= imin) {
		NUMdcvector_extrema_im (my v, 1, my max, &imin, &imax);
	}
	denum = fabs (imax) > fabs (imin) ? fabs (imax) : fabs (imin);
	if (denum == 0) {
		denum = 1;
	}
	if (fabs ( (imax - imin) / denum) < eps) {
		imin -= 1; imax += 1;
	}
	Graphics_setInner (g);
	Graphics_setWindow (g, rmin, rmax, imin, imax);
	Graphics_setFontSize (g, fontSize);
	Graphics_setTextAlignment (g, Graphics_CENTRE, Graphics_HALF);
	for (long i = 1; i <= my max; i++) {
		double re = my v[i].re, im = my v[i].im;
		if (re >= rmin && re <= rmax && im >= imin && im <= imax) {
			Graphics_text (g, re, im, symbol);
		}
	}
	Graphics_setFontSize (g, oldFontSize);
	Graphics_unsetInner (g);
	if (garnish) {
		Graphics_drawInnerBox (g);
		if (rmin * rmax < 0) {
			Graphics_markLeft (g, 0, 1, 1, 1, L"0");
		}
		if (imin * imax < 0) {
			Graphics_markBottom (g, 0, 1, 1, 1, L"0");
		}
		Graphics_marksLeft (g, 2, 1, 1, 0);
		Graphics_textLeft (g, 1, L"Imaginary part");
		Graphics_marksBottom (g, 2, 1, 1, 0);
		Graphics_textBottom (g, 1, L"Real part");
	}
}

Roots Polynomial_to_Roots (Polynomial me) {
	try {
		long np1 = my numberOfCoefficients, n = np1 - 1, n2 = n * n;

		if (n < 1) {
			Melder_throw ("Cannot find roots of a constant function.");
		}

		// Allocate storage for Hessenberg matrix (n * n) plus real and imaginary
		// parts of eigenvalues wr[1..n] and wi[1..n].

		autoNUMvector<double> hes (1, n2 + n + n);
		double *wr = &hes[n2];
		double *wi = &hes[n2 + n];

		// Fill the upper Hessenberg matrix (storage is Fortran)
		// C: [i][j] -> Fortran: (j-1)*n + i

		for (long i = 1; i <= n; i++) {
			hes[ (i - 1) *n + 1] = - (my coefficients[np1 - i] / my coefficients[np1]);
			if (i < n) {
				hes[ (i - 1) *n + 1 + i] = 1;
			}
		}

		// Find out the working storage needed

		char job = 'E', compz = 'N';
		long ilo = 1, ihi = n, ldh = n, ldz = n, lwork = -1, info;
		double *z = 0, wt[1];
		NUMlapack_dhseqr (&job, &compz, &n, &ilo, &ihi, &hes[1], &ldh, &wr[1], &wi[1], z, &ldz, wt, &lwork, &info);
		if (info != 0) {
			if (info < 0) {
				Melder_throw (L"Programming error. Argument ", info, " in NUMlapack_dhseqr has illegal value.");
			}
		}
		lwork = wt[0];
		autoNUMvector<double> work (1, lwork);

		// Find eigenvalues.

		NUMlapack_dhseqr (&job, &compz, &n, &ilo, &ihi, &hes[1], &ldh, &wr[1], &wi[1], z, &ldz, &work[1], &lwork, &info);
		long nrootsfound = n;
		long ioffset = 0;
		if (info > 0) {
			// if INFO = i, NUMlapack_dhseqr failed to compute all of the eigenvalues. Elements i+1:n of
			// WR and WI contain those eigenvalues which have been successfully computed
			nrootsfound -= info;
			if (nrootsfound < 1) {
				Melder_throw ("No roots found.");
			}
			Melder_warning (L"Calculated only ", Melder_integer (nrootsfound), L" roots.");
			ioffset = info;
		} else if (info < 0) {
			Melder_throw (L"Programming error. Argument ", info, L" in NUMlapack_dhseqr has illegal value.");
		}

		autoRoots thee = Roots_create (nrootsfound);
		for (long i = 1; i <= nrootsfound; i++) {
			(thy v[i]).re = wr[ioffset + i];
			(thy v[i]).im = wi[ioffset + i];
		}
		Roots_and_Polynomial_polish (thee.peek(), me);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no roots can be calculated.");
	}
}

void Roots_sort (Roots me) {
	(void) me;
}

/*
	Precondition: complex roots occur in pairs (a,bi), (a,-bi) with b>0
*/
void Roots_and_Polynomial_polish (Roots me, Polynomial thee) {
	long i = my min, maxit = 80;
	while (i <= my max) {
		double im = my v[i].im, re = my v[i].re;
		if (im != 0) {
			Polynomial_polish_complexroot_nr (thee, & my v[i], maxit);
			if (i < my max && im == -my v[i + 1].im && re == my v[i + 1].re) {
				my v[i + 1].re = my v[i].re; my v[i + 1].im = -my v[i].im;
				i++;
			}
		} else {
			Polynomial_polish_realroot (thee, & (my v[i].re), maxit);
		}
		i++;
	}
}

Polynomial Roots_to_Polynomial (Roots me) {
	try {
		(void) me;
		throw MelderError();
	} catch (MelderError) {
		Melder_throw ("Not implemented yet");
	}
}

void Roots_setRoot (Roots me, long index, double re, double im) {
	if (index < my min || index > my max) {
		Melder_throw ("Index must be in interval [1, ", my max, "].");
	}
	my v[index].re = re;
	my v[index].im = im;
}

dcomplex Roots_evaluate_z (Roots me, dcomplex z) {
	dcomplex result = {1, 0};
	for (long i = my min; i <= my max; i++) {
		dcomplex t = dcomplex_sub (z, my v[i]);
		result = dcomplex_mul (result, t);
	}
	return result;
}

Spectrum Roots_to_Spectrum (Roots me, double nyquistFrequency, long numberOfFrequencies, double radius) {
	try {
		if (numberOfFrequencies < 2) {
			Melder_throw ("NumberOfFrequencies must be greater or equal 2.");
		}
		autoSpectrum thee = Spectrum_create (nyquistFrequency, numberOfFrequencies);

		double phi = NUMpi / (numberOfFrequencies - 1);
		dcomplex z;
		for (long i = 1; i <= numberOfFrequencies; i++) {
			z.re = radius * cos ( (i - 1) * phi);
			z.im = radius * sin ( (i - 1) * phi);
			dcomplex s = Roots_evaluate_z (me, z);
			thy z[1][i] = s.re; thy z[2][i] = s.im;
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Spectrum calculated.");
	}
}

long Roots_getNumberOfRoots (Roots me) {
	return my max;
}

dcomplex Roots_getRoot (Roots me, long index) {
	if (index < 1 || index > my max) {
		Melder_throw ("Root index out of range.");
	}
	return my v [index];
}

/* Can be speeded up by doing a FFT */
Spectrum Polynomial_to_Spectrum (Polynomial me, double nyquistFrequency, long numberOfFrequencies, double radius) {
	try {
		if (numberOfFrequencies < 2) {
			Melder_throw (L"NumberOfFrequencies must be greater or equal 2.");
		}
		autoSpectrum thee = Spectrum_create (nyquistFrequency, numberOfFrequencies);

		double phi = NUMpi / (numberOfFrequencies - 1);
		for (long i = 1; i <= numberOfFrequencies; i++) {
			double re, im;
			Polynomial_evaluate_z_cart (me, radius, (i - 1) * phi, &re, &im);
			thy z[1][i] = re; thy z[2][i] = im;
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Spectrum calculated.");
	}
}

/****** ChebyshevSeries ******************************************/

Thing_implement (ChebyshevSeries, FunctionTerms, 0);

/*
	p(x) = sum (k=1..numberOfCoefficients, c[k]*T[k](x')) - c[1] / 2;
	Numerical evaluation via Clenshaw's recurrence equation (NRC: 5.8.11)
	d[m+1] = d[m] = 0;
	d[j] = 2 x' d[j+1] - d[j+2] + c[j];
	p(x) = d[0] = x' d[1] - d[2] + c[0] / 2;
	x' = (2 * x - xmin - xmax) / (xmax - xmin)
*/
double structChebyshevSeries :: v_evaluate (double x) {
	if (x < xmin || x > xmax) {
		return NUMundefined;
	}

	double d1 = 0, d2 = 0;
	if (numberOfCoefficients > 1) {
		// Transform x from domain [xmin, xmax] to domain [-1, 1]

		x = (2 * x - xmin - xmax) / (xmax - xmin);
		double x2 = 2 * x;
		for (long i = numberOfCoefficients; i > 1; i--) {
			double tmp = d1;
			d1 = x2 * d1 - d2 + coefficients[i];
			d2 = tmp;
		}
	}
	return x * d1 - d2 + coefficients[1];
}

/*
	T[n](x) = 2*x*T[n-1] - T[n-2](x)  n >= 2
*/
void structChebyshevSeries :: v_evaluateTerms (double x, double *terms) {
	if (x < xmin || x > xmax) {
		for (long i = 1; i <= numberOfCoefficients; i++) {
			terms[i] = NUMundefined;
		}
		return;
	}
	terms[1] = 1;
	if (numberOfCoefficients > 1) {
		// Transform x from domain [xmin, xmax] to domain [-1, 1]

		terms[2] = x = (2 * x - xmin - xmax) / (xmax - xmin);

		for (long i = 3; i <= numberOfCoefficients; i++) {
			terms[i] = 2 * x * terms[i - 1] - terms[i - 2];
		}
	}
}

void structChebyshevSeries :: v_getExtrema (double x1, double x2,
        double *lxmin, double *ymin, double *lxmax, double *ymax) {
	try {
		autoPolynomial p = ChebyshevSeries_to_Polynomial (this);
		FunctionTerms_getExtrema (p.peek(), x1, x2, lxmin, ymin, lxmax, ymax);
	} catch (MelderError) {
		Melder_throw (this, "Extrema cannot be calculated");   // David, dat is fout: er moet een Melder_throw of een Melder_clearError volgen: Klopt
	}
}

ChebyshevSeries ChebyshevSeries_create (double lxmin, double lxmax, long numberOfPolynomials) {
	try {
		autoChebyshevSeries me = Thing_new (ChebyshevSeries);
		FunctionTerms_init (me.peek(), lxmin, lxmax, numberOfPolynomials);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("ChebyshevSeries not created.");
	}
}

ChebyshevSeries ChebyshevSeries_createFromString (double lxmin, double lxmax, const wchar_t *s) {
	try {
		autoChebyshevSeries me = Thing_new (ChebyshevSeries);
		FunctionTerms_initFromString (me.peek(), lxmin, lxmax, s, 0);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("ChebyshevSeries not created from string.");
	};
}

Polynomial ChebyshevSeries_to_Polynomial (ChebyshevSeries me) {
	try {
		double xmin = -1, xmax = 1;

		autoPolynomial thee = Polynomial_create (xmin, xmax, my numberOfCoefficients - 1);

		thy coefficients[1] = my coefficients[1] /* * p[1] */;
		if (my numberOfCoefficients == 1) {
			return thee.transfer();
		}

		thy coefficients[2] = my coefficients[2];
		if (my numberOfCoefficients > 2) {
			autoNUMvector<double> buf (1, 3 * my numberOfCoefficients);

			double *pn = buf.peek();
			double *pnm1 = pn   + my numberOfCoefficients;
			double *pnm2 = pnm1 + my numberOfCoefficients;

			// Start the recursion: T[1] = x; T[0] = 1;

			pnm1[2] = 1; pnm2[1] = 1;
			double a = 2, b = 0, c = -1;
			for (long n = 2; n <= my numberOfCoefficients - 1; n++) {
				double *t1 = pnm1, *t2 = pnm2;
				NUMpolynomial_recurrence (pn, n, a, b, c, pnm1, pnm2);
				if (my coefficients[n + 1] != 0) {
					for (long j = 1; j <= n + 1; j++) {
						thy coefficients[j] += my coefficients[n + 1] * pn[j];
					}
				}
				pnm1 = pn;
				pnm2 = t1;
				pn = t2;
			}
		}
		if (my xmin != xmin || my xmax != xmax) {
			thee.reset (Polynomial_scaleX (thee.peek(), my xmin, my xmax));
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, "; not converted to Polynomial.");
	};
}


void FunctionTerms_and_RealTier_fit (I, thou, int *freeze, double tol, int ic, Covariance *c) {
	iam (FunctionTerms); thouart (RealTier);
	try {
		long numberOfData = thy points -> size;
		long numberOfParameters = my numberOfCoefficients;
		long numberOfFreeParameters = numberOfParameters;

		if (numberOfData < 2) {
			Melder_throw ("Not enough data points.");
		}

		autoFunctionTerms frozen = Data_copy (me);
		autoNUMvector<double> terms (1, my numberOfCoefficients);
		autoNUMvector<double> p (1, numberOfParameters);
		autoNUMvector<double> y_residual (1, numberOfData);
		autoCovariance ac = 0;
		if (ic) {
			ac.reset (Covariance_create (numberOfParameters));
		}

		long k = 1;
		for (long j = 1; j <= my numberOfCoefficients; j++) {
			if (freeze && freeze[j]) {
				numberOfFreeParameters--;
			} else {
				p[k] = my coefficients[j]; k++;
				frozen -> coefficients[j] = 0;
			}
		}

		if (numberOfFreeParameters == 0) {
			Melder_throw ("No free parameters left.");
		}

		autoSVD svd = SVD_create (numberOfData, numberOfFreeParameters);

		double sigma = RealTier_getStandardDeviation_points (thee, my xmin, my xmax);
		if (sigma == NUMundefined) {
			Melder_throw ("Not enough data points in fit interval.");
		}

		for (long i = 1; i <= numberOfData; i++) {
			// Only 'residual variance' must be explained by the model
			// Evaluate only with the frozen parameters

			RealPoint point = (RealPoint) thy points -> item [i];
			double x = point -> number, y = point -> value, **u = svd -> u;
			double y_frozen = numberOfFreeParameters == numberOfParameters ? 0 :
			                  FunctionTerms_evaluate (frozen.peek(), x);

			y_residual[i] = (y - y_frozen) / sigma;

			// Data matrix

			FunctionTerms_evaluateTerms (me, x, terms.peek());
			k = 0;
			for (long j = 1; j <= my numberOfCoefficients; j++) {
				if (! freeze || ! freeze[j]) {
					k++; u[i][k] = terms[j] / sigma;
				}
			}
		}

		// SVD and evaluation of the singular values

		if (tol > 0) {
			SVD_setTolerance (svd.peek(), tol);
		}

		SVD_compute (svd.peek());
		SVD_solve (svd.peek(), y_residual.peek(), p.peek());

		// Put fitted values at correct position
		k = 1;
		for (long j = 1; j <= my numberOfCoefficients; j++) {
			if (! freeze || ! freeze[j]) {
				my coefficients[j] = p[k++];
			}
		}

		if (ic) {
			svdcvm (svd -> v, numberOfFreeParameters, numberOfParameters, freeze, svd -> d, ac -> data);
		}
		*c = ac.transfer();
	} catch (MelderError) {
		Melder_throw (me, " & ", thee, ": no fit.");
	}
}


Polynomial RealTier_to_Polynomial (I, long degree, double tol, int ic, Covariance *cvm) {
	iam (RealTier);
	try {
		autoPolynomial thee = Polynomial_create (my xmin, my xmax, degree);
		FunctionTerms_and_RealTier_fit (thee.peek(), me, 0, tol, ic, cvm);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Polynomial fitted.");
	}
}

LegendreSeries RealTier_to_LegendreSeries (I, long degree, double tol, int ic, Covariance *cvm) {
	iam (RealTier);
	try {
		autoLegendreSeries thee = LegendreSeries_create (degree, my xmin, my xmax);
		FunctionTerms_and_RealTier_fit (thee.peek(), me, 0, tol, ic, cvm);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no LegendreSeries fitted.");
	}
}

ChebyshevSeries RealTier_to_ChebyshevSeries (I, long degree, double tol, int ic, Covariance *cvm) {
	iam (RealTier);
	try {
		autoChebyshevSeries thee = ChebyshevSeries_create (degree, my xmin, my xmax);
		FunctionTerms_and_RealTier_fit (thee.peek(), me, 0, tol, ic, cvm);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ":no ChebyshevSeries fitted.");
	};
}

/******* Splines *************************************************/

/*
	Functions for calculating an mspline and an ispline. These functions should replace
	the functions in NUM2.c. Before we can do that we first have to adapt the spline-
	dependencies in MDS.c.

	Formally nKnots == order + numberOfInteriorKnots + order.
	We forget about the multiple knots at start and end.
	Our point-sequece xmin < interiorKont[1] < ... < interiorKnot[n] < xmax.
	nKnots is now numberOfinteriorKnots + 2.
*/
static double NUMmspline2 (double points[], long numberOfPoints, long order, long index, double x) {
	long numberOfSplines = numberOfPoints + order - 2;
	double m[Spline_MAXIMUM_DEGREE + 2];

	Melder_assert (numberOfPoints > 2 && order > 0 && index > 0);

	if (index > numberOfSplines) {
		return NUMundefined;
	}

	/*
		Find the range/interval where x is located.
		M-splines of order k have degree k-1.
		M-splines are zero outside interval [ knot[i], knot[i+order] ).
		First and last 'order' knots are equal, i.e.,
		knot[1] = ... = knot[order] && knot[nKnots-order+1] = ... knot[nKnots].
	*/

	long index_b = index - order + 1;
	index_b = MAX (index_b, 1);
	if (x < points[index_b]) {
		return 0;
	}

	long index_e = index_b + MIN (index, order);
	index_e = MIN (numberOfPoints, index_e);
	if (x > points[index_e]) {
		return 0;
	}

	// Calculate M[i](x|1,t) according to eq.2.

	for (long k = 1; k <= order; k++) {
		long k1 = index - order + k, k2 = k1 + 1;
		m[k] = 0;
		if (k1 > 0 && k2 <= numberOfPoints && x >= points[k1] && x < points[k2]) {
			m[k] = 1 / (points[k2] - points[k1]);
		}
	}

	// Iterate to get M[i](x|k,t)

	for (long k = 2; k <= order; k++) {
		for (long j = 1; j <= order - k + 1; j++) {
			long k1 = index - order + j, k2 = k1 + k;
			if (k2 > 1 && k1 < 1) {
				k1 = 1;
			} else if (k2 > numberOfPoints && k1 < numberOfPoints) {
				k2 = numberOfPoints;
			}
			if (k1 > 0 && k2 <= numberOfPoints) {
				double p1 = points[k1], p2 = points[k2];
				m[j] = k * ( (x - p1) * m[j] + (p2 - x) * m[j + 1]) /
				       ( (k - 1) * (p2 - p1));
			}
		}
	}
	return m[1];
}

static double NUMispline2 (double points[], long numberOfPoints, long order, long index, double x) {
	Melder_assert (numberOfPoints > 2 && order > 0 && index > 0);

	long index_b = index - order + 1;
	index_b = MAX (index_b, 1);

	if (x < points[index_b]) {
		return 0;
	}

	long index_e = index_b + MIN (index, order);
	index_e = MIN (numberOfPoints, index_e);

	if (x > points[index_e]) {
		return 1;
	}
	long j;
	for (j = index_e - 1; j >= index_b; j--) {
		if (x > points[j]) {
			break;
		}
	}

	// Equation 5 in Ramsay's article contains some errors!!!
	// 1. the interval selection must be 'j-k <= i <= j' instead of
	//	'j-k+1 <= i <= j'
	// 2. the summation index m starts at 'i+1' instead of 'i'

	double y = 0;
	for (long m = index + 1; m <= j + order; m++) {
		long km = m - order, kmp = km + order + 1;
		km = MAX (km, 1);
		kmp = MIN (kmp, numberOfPoints);
		y += (points[kmp] - points[km]) * NUMmspline2 (points, numberOfPoints, order + 1, m, x);
	}
	return y /= (order + 1);
}

Thing_implement (Spline, FunctionTerms, 0);

double structSpline :: v_evaluate (double x) {
	(void) x;
	return 0;
}

long structSpline :: v_getDegree () {
	return degree;
}

long structSpline :: v_getOrder () {
	return degree + 1;
}

/* Precondition: FunctionTerms part inited + degree */
static void Spline_initKnotsFromString (I, long degree, const wchar_t *interiorKnots) {
	iam (Spline);

	if (degree > Spline_MAXIMUM_DEGREE) {
		Melder_throw ("Degree must be <= 20.");
	}
	long numberOfInteriorKnots;
	autoNUMvector<double> numbers (NUMstring_to_numbers (interiorKnots, &numberOfInteriorKnots), 1);
	if (numberOfInteriorKnots > 0) {
		NUMsort_d (numberOfInteriorKnots, numbers.peek());
		if (numbers[1] <= my xmin || numbers[numberOfInteriorKnots] > my xmax) {
			Melder_throw ("Knots must be inside domain.");
		}
	}

	my degree = degree;
	long order = Spline_getOrder (me); /* depends on spline type !! */
	long n = numberOfInteriorKnots + order;

	if (my numberOfCoefficients != n) {
		Melder_throw ("NumberOfCoefficients must equal ", n, ".");
	}

	my numberOfKnots = numberOfInteriorKnots + 2;
	my knots = NUMvector<double> (1, my numberOfKnots);

	for (long i = 1; i <= numberOfInteriorKnots; i++) {
		my knots[i + 1] = numbers[i];
	}
	my knots[1] = my xmin;
	my knots[my numberOfKnots] = my xmax;
}

void Spline_init (I, double xmin, double xmax, long degree, long numberOfCoefficients, long numberOfKnots) {
	iam (Spline);

	if (degree > Spline_MAXIMUM_DEGREE) {
		Melder_throw ("Spline_init: degree must be <= 20.");
	}
	FunctionTerms_init (me, xmin, xmax, numberOfCoefficients);
	my knots = NUMvector<double> (1, numberOfKnots);
	my degree = degree;
	my numberOfKnots = numberOfKnots;
	my knots[1] = xmin;
	my knots[numberOfKnots] = xmax;
}

void Spline_drawKnots (I, Graphics g, double xmin, double xmax, double ymin, double ymax, int garnish) {
	iam (Spline);
	long order = Spline_getOrder (me);
	wchar_t ts[20] = L"";

	if (xmax <= xmin) {
		xmin = my xmin; xmax = my xmax;
	}

	if (xmax < my xmin || xmin > my xmax) {
		return;
	}

	if (ymax <= ymin) {
		double x1, x2;
		FunctionTerms_getExtrema (me, xmin, xmax, &x1, &ymin, &x2, &ymax);
	}

	Graphics_setWindow (g, xmin, xmax, ymin, ymax);

	if (my knots[1] >= xmin && my knots[1] <= xmax) {
		if (garnish) {
			if (order == 1) {
				swprintf (ts, 20, L"t__1_");
			} else if (order == 2) {
				swprintf (ts, 20, L"{t__1_, t__2_}");
			} else {
				swprintf (ts, 20, L"{t__1_..t__%ld_}", order);
			}
		}
		Graphics_markTop (g, my knots[1], 0, 1, 1, ts);
	}
	for (long i = 2; i <= my numberOfKnots - 1; i++) {
		if (my knots[i] >= xmin && my knots[i] <= xmax) {
			if (garnish) {
				swprintf (ts, 20, L"t__%ld_", i + order - 1);
			}
			Graphics_markTop (g, my knots[i], 0, 1, 1, ts);
		}
	}
	if (my knots[my numberOfKnots] >= xmin && my knots[my numberOfKnots] <= xmax) {
		if (garnish) {
			long numberOfKnots = my numberOfKnots + 2 * (order - 1);
			if (order == 1) {
				swprintf (ts, 20, L"t__%ld_", numberOfKnots);
			} else if (order == 2) {
				swprintf (ts, 20, L"{t__%d_, t__%ld_}", numberOfKnots - 1, numberOfKnots);
			} else {
				swprintf (ts, 20, L"{t__%d_..t__%ld_}", numberOfKnots - order + 1, numberOfKnots);
			}
		}
		Graphics_markTop (g, my knots[my numberOfKnots], 0, 1, 1, ts);
	}
}

long Spline_getOrder (I) {
	iam (Spline);
	return my v_getOrder ();
}

Spline Spline_scaleX (I, double xmin, double xmax) {
	try {
		iam (Spline);
		Melder_assert (xmin < xmax);

		autoSpline thee = Data_copy (me);

		thy xmin = xmin; thy xmax = xmax;

		// x = a x + b
		// Constraints:
		// my xmin = a xmin + b;    a = (my xmin - my xmax) / (xmin - xmax);
		// my xmax = a xmax + b;    b = my xmin - a * xmin

		double a = (xmin - xmax) / (my xmin - my xmax);
		double b = xmin - a * my xmin;
		for (long i = 1; i <= my numberOfKnots; i++) {
			thy knots[i] = a * my knots[i] + b;
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw ("Scaled Spline not created.");
	}
}

/********* MSplines ************************************************/

double structMSpline :: v_evaluate (double x) {
	if (x < xmin || x > xmax) {
		return 0;
	}

	double result = 0;
	for (long i = 1; i <= numberOfCoefficients; i++) {
		if (coefficients[i] != 0) result += coefficients[i] *
			                                    NUMmspline2 (knots, numberOfKnots, degree + 1, i, x);
	}
	return result;
}

void structMSpline :: v_evaluateTerms (double x, double *terms) {
	if (x < xmin || x > xmax) {
		return;
	}

	for (long i = 1; i <= numberOfCoefficients; i++) {
		terms[i] = NUMmspline2 (knots, numberOfKnots, degree + 1, i, x);
	}
}

Thing_implement (MSpline, Spline, 0);

MSpline MSpline_create (double xmin, double xmax, long degree, long numberOfInteriorKnots) {
	try {
		autoMSpline me = Thing_new (MSpline);
		long numberOfCoefficients = numberOfInteriorKnots + degree + 1;
		long numberOfKnots = numberOfCoefficients + degree + 1;
		Spline_init (me.peek(), xmin, xmax, degree, numberOfCoefficients, numberOfKnots);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("MSpline not created.");
	}
}

MSpline MSpline_createFromStrings (double xmin, double xmax, long degree, const wchar_t *coef, const wchar_t *interiorKnots) {
	try {
		if (degree > Spline_MAXIMUM_DEGREE) {
			Melder_throw ("Degree must be <= 20.");
		}
		autoMSpline me = Thing_new (MSpline);
		FunctionTerms_initFromString (me.peek(), xmin, xmax, coef, 1);
		Spline_initKnotsFromString (me.peek(), degree, interiorKnots);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("MSpline not created from strings.");
	}
}

/******** ISplines ************************************************/

double structISpline :: v_evaluate (double x) {
	if (x < xmin || x > xmax) {
		return 0;
	}

	double result = 0;
	for (long i = 1; i <= numberOfCoefficients; i++) {
		if (coefficients[i] != 0) result += coefficients[i] *
			                                    NUMispline2 (knots, numberOfKnots, degree, i, x);
	}
	return result;
}

void structISpline :: v_evaluateTerms (double x, double *terms) {
	for (long i = 1; i <= numberOfCoefficients; i++) {
		terms[i] = NUMispline2 (knots, numberOfKnots, degree, i, x);
	}
}

long structISpline :: v_getOrder () {
	return degree;
}

Thing_implement (ISpline, Spline, 0);

ISpline ISpline_create (double xmin, double xmax, long degree, long numberOfInteriorKnots) {
	try {
		autoISpline me = Thing_new (ISpline);
		long numberOfCoefficients = numberOfInteriorKnots + degree;
		long numberOfKnots = numberOfCoefficients + degree;
		Spline_init (me.peek(), xmin, xmax, degree, numberOfCoefficients, numberOfKnots);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("ISpline not created.");
	}
}

ISpline ISpline_createFromStrings (double xmin, double xmax, long degree, const wchar_t *coef, const wchar_t *interiorKnots) {
	try {
		if (degree > Spline_MAXIMUM_DEGREE) {
			Melder_throw ("Degree must be <= 20.");
		}
		autoISpline me = Thing_new (ISpline);
		FunctionTerms_initFromString (me.peek(), xmin, xmax, coef, 1);
		Spline_initKnotsFromString (me.peek(), degree, interiorKnots);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("ISpline not created from strings.");
	};
}

/*

#define RationalFunction_members Function_members \
	Polynomial num, denum;
#define RationalFunction_methods Function_methods
class_create (RationalFunction, Function)

RationalFunction RationalFunction_create (double xmin, double xmax,
	long degree_num, long degree_denum)
{
	RationalFunction me = new (RationalFunction);
	if (! me || ! (my num = Polynomial_create (xmin, xmax, degree_num)) ||
		!  (my denum = Polynomial_create (xmin, xmax, degree_denum))) forget (me);
	return me;
}

RationalFunction RationalFunction_createFromString (I, double xmin, double xmax,
	char *num, char *denum)
{
	RationalFunction me = new (RationalFunction); long i;

	if (! (my num = Polynomial_createFromString (xmin, xmax, num)) ||
		! (my denum = Polynomial_createFromString (xmin, xmax, denum))) forget (me);
	if (my denum -> v[1] != 1 && my denum -> v[1] != 0)
	{
		double q0 = my denum -> v[1];
		for (i=1; 1 <= my num ->numberOfCoefficients; i++) my num -> v[i] /= q0;
		for (i=1; 1 <= my denum ->numberOfCoefficients; i++) my denum -> v[i] /= q0;
	}
	return me;
}

// divide out common roots
RationalFunction RationalFunction_simplify (RationalFunction me)
{
	Roots num = NULL, denum = NULL; RationalFunction thee = NULL;
	if (! (num = Polynomial_to_Roots (my num)) ||
		! (denum = Polynomial_to_Roots (my denum))) goto end;

}

*/

#undef MAX
#undef MIN

/* end of file Polynomial.cpp */
