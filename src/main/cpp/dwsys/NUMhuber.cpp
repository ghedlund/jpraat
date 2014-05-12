/* NUMhuber.c
*
* Copyright (C) 1994-2008 David Weenink
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
 djmw 20030814 First version
 djmw 20080122 float -> double
*/

#include "NUM2.h"

void NUMmad (double *x, long n, double *location, int wantlocation, double *mad, double *work) {
	double *tmp = work;

	*mad = NUMundefined;
	if (n < 1) {
		Melder_throw ("The dimension must be at least 1");
	}
	if (n == 1) {
		*location = x[1];
		return;
	}
	autoNUMvector<double> atmp;
	if (work == 0)  {
		atmp.reset (1, n);
		tmp = atmp.peek();
	}

	for (long i = 1; i <= n; i++) {
		tmp[i] = x[i];
	}

	if (wantlocation) {
		NUMsort_d (n, tmp);
		*location = NUMquantile (n, tmp, 0.5);
	}

	for (long i = 1; i <= n; i++) {
		tmp[i] = fabs (tmp[i] - *location);
	}

	NUMsort_d (n, tmp);
	*mad = 1.4826 * NUMquantile (n, tmp, 0.5);
}

static double NUMgauss (double x) {
	return NUM1_sqrt2pi * exp (- 0.5 * x * x);
}

void NUMstatistics_huber (double *x, long n, double *location, int wantlocation,
                          double *scale, int wantscale, double k, double tol, double *work) {
	double *tmp = work;
	double theta = 2 * NUMgaussP (k) - 1;
	double beta = theta + k * k * (1 - theta) - 2 * k * NUMgauss (k);
	long n1 = n;

	autoNUMvector<double> atmp;
	if (work == 0)  {
		atmp.reset (1, n);
		tmp = atmp.peek();
	}
	double mad;
	NUMmad (x, n, location, wantlocation, & mad, tmp);
	if (wantscale) {
		*scale = mad;
	}
	if (*scale == 0) {
		Melder_throw ("Scale is zero.");
	}

	double mu0, mu1 = *location;
	double s0, s1 = *scale;

	if (wantlocation) {
		n1 = n - 1;
	}

	do {
		mu0 = mu1;
		s0 = s1;

		double low  = mu0 - k * s0;
		double high = mu0 + k * s0;

		for (long i = 1; i <= n; i++) {
			if (x[i] < low) {
				tmp[i] = low;
			} else if (x[i] > high) {
				tmp[i] = high;
			} else {
				tmp[i] =  x[i];
			}
		}
		if (wantlocation) {
			mu1 = 0;
			for (long i = 1; i <= n; i++) {
				mu1 += tmp[i];
			}
			mu1 /= n;
		}
		if (wantscale) {
			s1 = 0;
			for (long i = 1; i <= n; i++) {
				double dx = tmp[i] - mu1;
				s1 += dx * dx;
			}
			s1 = sqrt (s1 / (n1 * beta));
		}
	} while (fabs (mu0 - mu1) > tol * s0 || fabs (s0 - s1) > tol * s0); //TODO fabs (mu0 - mu1) > tol * s0 ??

	if (wantlocation) {
		*location = mu1;
	}
	if (wantscale) {
		*scale = s1;
	}
}
