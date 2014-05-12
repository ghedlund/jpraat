/* Configuration_AffineTransform.cpp
 *
 * Copyright (C) 1993-2012 David Weenink
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
 djmw 20020315 GPL header
 */

#include "Configuration_AffineTransform.h"
#include "Configuration_and_Procrustes.h"
#include "Procrustes.h"
#include "SVD.h"

#undef your
#define your ((AffineTransform_Table) thy methods) ->

static void do_steps45 (double **w, double **t, double **c, long n, double *f) {
	// Step 4 || 10: If W'T has negative diagonal elements, multiply corresponding columns in T by -1.

	for (long i = 1; i <= n; i++) {
		double d = 0;
		for (long k = 1; k <= n; k++) {
			d += w[k][i] * t[k][i];
		}
		if (d < 0) {
			for (long k = 1; k <= n; k++) {
				t[k][i] = -t[k][i];
			}
		}
	}

	// Step 5 & 11: f = tr W'T (Diag (T'CT))^-1/2

	*f = 0;
	for (long i = 1; i <= n; i++) {
		double d = 0, tct = 0;
		for (long k = 1; k <= n; k++) {
			d += w[k][i] * t[k][i];
			for (long j = 1; j <= n; j++) {
				tct += t[k][i] * c[k][j] * t[j][i];
			}
		}
		if (tct > 0) {
			*f += d / sqrt (tct);
		}
	}
}

static void NUMmaximizeCongruence (double **b, double **a, long nr, long nc,
                                   double **t, long maximumNumberOfIterations, double tolerance) {
	long numberOfIterations = 0;
	Melder_assert (nr > 0 && nc > 0);
	Melder_assert (t);

	if (nc == 1) {
		t[1][1] = 1; return;
	}
	autoNUMmatrix<double> c (1, nc, 1, nc);
	autoNUMmatrix<double> w (1, nc, 1, nc);
	autoNUMmatrix<double> u (1, nc, 1, nc);
	autoNUMvector<double> evec (1, nc);
	autoSVD svd = SVD_create (nc, nc);

	// Steps 1 & 2: C = A'A and W = A'B

	double checkc = 0, checkw = 0;
	for (long i = 1; i <= nc; i++) {
		for (long j = 1; j <= nc; j++) {
			for (long k = 1; k <= nr; k++) {
				c[i][j] += a[k][i] * a[k][j];
				w[i][j] += a[k][i] * b[k][j];
			}
			checkc += c[i][j]; checkw += w[i][j];
		}
	}

	if (checkc == 0 || checkw == 0) {
		Melder_throw ("NUMmaximizeCongruence: we cannot rotate a zero matrix.");
	}

	// Scale W by (diag(B'B))^-1/2

	for (long j = 1; j <= nc; j++) {
		double scale = 0;
		for (long k = 1; k <= nr; k++) {
			scale += b[k][j] * b[k][j];
		}
		if (scale > 0) {
			scale = 1 / sqrt (scale);
		}
		for (long i = 1; i <= nc; i++) {
			w[i][j] *= scale;
		}
	}

	// Step 3: largest eigenvalue of C

	evec[1] = 1;
	double rho, f, f_old;
	NUMdominantEigenvector (c.peek(), nc, evec.peek(), &rho, 1.0e-6);

	do_steps45 (w.peek(), t, c.peek(), nc, &f);
	do {
		for (long j = 1; j <= nc; j++) {
			// Step 7.a

			double p = 0;
			for (long k = 1; k <= nc; k++) {
				for (long i = 1; i <= nc; i++) {
					p += t[k][j] * c[k][i] * t[i][j];
				}
			}

			// Step 7.b

			double q = 0;
			for (long k = 1; k <= nc; k++) {
				q += w[k][j] * t[k][j];
			}

			// Step 7.c

			if (q == 0) {
				for (long i = 1; i <= nc; i++) {
					u[i][j] = 0;
				}
			} else {
				double ww = 0;
				for (long k = 1; k <= nc; k++) {
					ww += w[k][j] * w[k][j];
				}
				for (long i = 1; i <= nc; i++) {
					double ct = 0;
					for (long k = 1; k <= nc; k++) {
						ct += c[i][k] * t[k][j];
					}
					u[i][j] = (q * (ct - rho * t[i][j]) / p - 2 * ww * t[i][j] / q - w[i][j]) / sqrt (p);
				}
			}
		}

		// Step 8

		SVD_svd_d (svd.peek(), u.peek());

		// Step 9

		for (long i = 1; i <= nc; i++) {
			for (long j = 1; j <= nc; j++) {
				t[i][j] = 0;
				for (long  k = 1; k <= nc; k++) {
					t[i][j] -= svd -> u[i][k] * svd -> v[j][k];
				}
			}
		}

		numberOfIterations++;
		f_old = f;

		// Steps 10 & 11 equal steps 4 & 5

		do_steps45 (w.peek(), t, c.peek(), nc, &f);

	} while (fabs (f_old - f) / f_old > tolerance && numberOfIterations < maximumNumberOfIterations);
}

AffineTransform Configurations_to_AffineTransform_congruence (Configuration me,
        Configuration thee, long maximumNumberOfIterations, double tolerance) {
	try {
		// Use Procrustes transform to obtain starting configuration.
		// (We only need the transformation matrix T.)
		autoProcrustes p = Configurations_to_Procrustes (me, thee, 0);
		NUMmaximizeCongruence (my data, thy data, my numberOfRows,
		                       p -> n, p -> r, maximumNumberOfIterations, tolerance);

		autoAffineTransform at = AffineTransform_create (p -> n);
		NUMmatrix_copyElements (p -> r, at -> r, 1, p -> n, 1, p -> n);
		return at.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no congruence transformation created.");
	}
}

Configuration Configuration_and_AffineTransform_to_Configuration (Configuration me, thou) {
	try {
		thouart (AffineTransform);

		if (my numberOfColumns != thy n) {
			Melder_throw ("Dimensions do not agree.");
		}
		autoConfiguration him = (Configuration) Data_copy (me);

		// Apply transformation YT

		thy v_transform (my data, my numberOfRows, his data);
		return him.transfer();
	} catch (MelderError) {
		Melder_throw ("Configuration not created.");
	}
}

/* End of file Configuration_AffineTransform.cpp */
