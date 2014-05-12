/* Sound_and_Cepstrum.cpp
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
 djmw 20020516 GPL header
 djmw 20020529 changed NUMrealft to NUM...RealFastFourierTransform_f
 djmw 20041124 Changed call to Sound_to_Spectrum.
*/

#include "Sound_and_Cepstrum.h"
#include "Sound_and_Spectrum.h"
#include "Cepstrum_and_Spectrum.h"
#include "NUM2.h"

/*
	Algorithm:
	J.B. Bednar & T.L. Watt (1985), Calculating the Complex Cepstrum
	without Phase Unwrapping or Integration, IEEE Trans. on ASSP 33,
	1014-1017 (Does not work yet).
*/
Cepstrum Sound_to_Cepstrum_bw (Sound me) {
	try {
		long nfft = 2;
		while (nfft < my nx) {
			nfft *= 2;
		}

		double qmax = (my xmax - my xmin) * nfft / my nx;
		autoCepstrum thee = Cepstrum_create (qmax, nfft);

		autoNUMvector<double> x (1, nfft);
		autoNUMvector<double> nx (1, nfft);

		for (long i = 1; i <= my nx; i++) {
			x[i] = my z[1][i];
			nx[i] = (i - 1) * x[i];
		}

		// Step 1: Fourier transform x(n) -> X(f)
		// and n*x(n) -> NX(f)

		NUMforwardRealFastFourierTransform (x.peek() , nfft);
		NUMforwardRealFastFourierTransform (nx.peek(), nfft);

		// Step 2: Multiply {X^*(f) * NX(f)} / |X(f)|^2
		// Compute Avg (ln |X(f)|) as Avg (ln |X(f)|^2) / 2.
		// Treat i=1 separately: x[1] * nx[1] / |x[1]|^2

		double lnxa = 0;
		if (x[1] != 0) {
			lnxa = 2 * log (fabs (x[1]));
			x[1] = nx[1] / x[1];
		}
		if (x[2] != 0) {
			lnxa = 2 * log (fabs (x[2]));
			x[2] = nx[2] / x[2];
		}

		for (long i = 3; i < nfft; i += 2) {
			double xr = x[i], nxr = nx[i];
			double xi = x[i + 1], nxi = nx[i + 1];
			double xa = xr * xr + xi * xi;
			if (xa > 0) {
				x[i]   = (xr * nxr + xi * nxi) / xa;
				x[i + 1] = (xr * nxi - xi * nxr) / xa;
				lnxa += log (xa);
			} else {
				x[i] = x[i + 1] = 0;
			}
		}

		lnxa /= 2 * nfft / 2;

		// Step 4: Inverse transform of complex array x
		//	results in: n * xhat (n)

		NUMreverseRealFastFourierTransform (x.peek(), nfft);

		// Step 5: Inverse fft-correction factor: 1/nfftd2
		// Divide n * xhat (n) by n

		for (long i = 2; i <= my nx; i++) {
			thy z[1][i] = x[i] / ( (i - 1) * nfft);
		}

		// Step 6: xhat[0] = Avg (ln |X(f)|)

		thy z[1][1] = lnxa;
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Cepstrum created.");
	}
}

/* Zijn nog niet elkaars inverse!!!!*/

Cepstrum Sound_to_Cepstrum (Sound me) {
	try {
		autoSpectrum spectrum = Sound_to_Spectrum (me, TRUE);
		autoCepstrum thee = Spectrum_to_Cepstrum (spectrum.peek());
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Cepstrum calculated.");
	}
}

Sound Cepstrum_to_Sound (Cepstrum me) {
	try {
		autoSpectrum sx = Cepstrum_to_Spectrum (me);
		autoSound thee = Spectrum_to_Sound (sx.peek());
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Sound calculated.");
	}
}

/* End of file Sound_and_Cepstrum.cpp  */
