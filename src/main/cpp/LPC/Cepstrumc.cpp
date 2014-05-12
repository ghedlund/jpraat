/* Cepstrumc.c
 *
 * Copyright (C) 1994-2011 David Weenink
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
 djmw 20020812 GPL header
 djmw 20061218 Changed info to Melder_writeLine<x> format.
 djmw 20071017 oo_CAN_WRITE_AS_ENCODING.h
 djmw 20080122 Version 1: float -> double
 djmw 20110304 Thing_new
*/

#include "Cepstrumc.h"
#include "DTW.h"

#include "oo_DESTROY.h"
#include "Cepstrumc_def.h"
#include "oo_COPY.h"
#include "Cepstrumc_def.h"
#include "oo_EQUAL.h"
#include "Cepstrumc_def.h"
#include "oo_CAN_WRITE_AS_ENCODING.h"
#include "Cepstrumc_def.h"
#include "oo_WRITE_TEXT.h"
#include "Cepstrumc_def.h"
#include "oo_WRITE_BINARY.h"
#include "Cepstrumc_def.h"
#include "oo_READ_TEXT.h"
#include "Cepstrumc_def.h"
#include "oo_READ_BINARY.h"
#include "Cepstrumc_def.h"
#include "oo_DESCRIPTION.h"
#include "Cepstrumc_def.h"

Thing_implement (Cepstrumc, Sampled, 1);

void structCepstrumc :: v_info () {
	structData :: v_info ();
	MelderInfo_writeLine (L"  Start time: ", Melder_double (xmin));
	MelderInfo_writeLine (L"  End time: ", Melder_double (xmax));
	MelderInfo_writeLine (L"  Number of frames: ", Melder_integer (nx));
	MelderInfo_writeLine (L"  Time step: ", Melder_double (dx));
	MelderInfo_writeLine (L"  First frame at: ", Melder_double (x1));
	MelderInfo_writeLine (L"  Number of coefficients: ", Melder_integer (maxnCoefficients));
}

void Cepstrumc_Frame_init (Cepstrumc_Frame me, int nCoefficients) {
	my c = NUMvector<double> (0, nCoefficients);
	my nCoefficients = nCoefficients;
}

void Cepstrumc_init (Cepstrumc me, double tmin, double tmax, long nt, double dt, double t1,
                     int nCoefficients, double samplingFrequency) {
	my samplingFrequency = samplingFrequency;
	my maxnCoefficients = nCoefficients;
	Sampled_init (me, tmin, tmax, nt, dt, t1);
	my frame = NUMvector<structCepstrumc_Frame> (1, nt);
}

Cepstrumc Cepstrumc_create (double tmin, double tmax, long nt, double dt, double t1,
                            int nCoefficients, double samplingFrequency) {
	try {
		autoCepstrumc me = Thing_new (Cepstrumc);
		Cepstrumc_init (me.peek(), tmin, tmax, nt, dt, t1, nCoefficients, samplingFrequency);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("Cepstrum not created.");
	}
}

static void regression (Cepstrumc me, long frame, double r[], long nr) {
	long nc = 1e6; double sumsq = 0;
	for (long i = 0; i <= my maxnCoefficients; i++) {
		r[i] = 0;
	}
	if (frame <= nr / 2 || frame >= my nx - nr / 2) {
		return;
	}
	for (long j = -nr / 2; j <= nr / 2; j++) {
		Cepstrumc_Frame f = & my frame[frame + j];
		if (f->nCoefficients < nc) {
			nc = f->nCoefficients;
		}
		sumsq += j * j;
	}
	for (long i = 0; i <= nc; i++) {
		for (long j = -nr / 2; j <= nr / 2; j++) {
			Cepstrumc_Frame f = & my frame[frame + j];
			r[i] += f->c[i] * j / sumsq / my dx;
		}
	}
}

DTW Cepstrumc_to_DTW (Cepstrumc me, Cepstrumc thee, double wc, double wle,
                      double wr, double wer, double dtr, int matchStart, int matchEnd, int constraint) {
	try {
		long nr = dtr / my dx;

		if (my maxnCoefficients != thy maxnCoefficients) {
			Melder_throw ("Cepstrumc orders must be equal.");
		}
		if (wr != 0 && nr < 2) {
			Melder_throw ("Time window for regression coefficients too small.");
		}
		if (nr % 2 == 0) {
			nr++;
		}
		if (wr != 0) {
			Melder_casual ("Number of frames used for regression coefficients %ld", nr);
		}
		autoDTW him = DTW_create (my xmin, my xmax, my nx, my dx, my x1, thy xmin, thy xmax, thy nx, thy dx, thy x1);
		autoNUMvector<double> ri (0L, my maxnCoefficients);
		autoNUMvector<double> rj (0L, my maxnCoefficients);

		// Calculate distance matrix

		autoMelderProgress progress (L"");
		for (long i = 1; i <= my nx; i++) {
			Cepstrumc_Frame fi = & my frame[i];
			regression (me, i, ri.peek(), nr);
			for (long j = 1; j <= thy nx; j++) {
				Cepstrumc_Frame fj = & thy frame[j];
				double d, dist = 0, distr = 0;
				if (wc != 0) { /* cepstral distance */
					for (long k = 1; k <= fj -> nCoefficients; k++) {
						d = fi -> c[k] - fj -> c[k];
						dist += d * d;
					}
					dist *= wc;
				}
				// log energy distance
				d = fi -> c[0] - fj -> c[0];
				dist += wle * d * d;
				if (wr != 0) { // regression distance
					regression (thee, j, rj.peek(), nr);
					for (long k = 1; k <= fj -> nCoefficients; k++) {
						d = ri[k] - rj[k];
						distr += d * d;
					}
					dist += wr * distr;
				}
				if (wer != 0) { // regression on c[0]: log(energy)
					if (wr == 0) {
						regression (thee, j, rj.peek(), nr);
					}
					d = ri[0] - rj[0];
					dist += wer * d * d;
				}
				dist /= wc + wle + wr + wer;
				his z[i][j] = sqrt (dist); // prototype along y-direction
			}
			Melder_progress ( (double) i / my nx, L"Calculate distances: frame ",
			                   Melder_integer (i), L" from ", Melder_integer (my nx), L".");
		}
		DTW_findPath (him.peek(), matchStart, matchEnd, constraint);
		return him.transfer();
	} catch (MelderError) {
		Melder_throw ("DTW not created.");
	}
}

Matrix Cepstrumc_to_Matrix (Cepstrumc me) {
	try {
		autoMatrix thee = Matrix_create (my xmin, my xmax, my nx, my dx, my x1,
		                                 0, my maxnCoefficients, my maxnCoefficients + 1, 1, 0);

		for (long i = 1; i <= my nx; i++) {
			Cepstrumc_Frame him = & my frame[i];
			for (long j = 1; j <= his nCoefficients + 1; j++) {
				thy z[j][i] = his c[j - 1];
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Matrix created.");
	}
}

/* End of file Cepstrumc.cpp */
