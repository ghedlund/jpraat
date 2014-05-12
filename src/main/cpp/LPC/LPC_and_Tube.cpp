/* LPC_and_Tube.cpp
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
 djmw 20020612 GPL header
 djmw 20041020 struct Tube_Frame -> struct structTube_Frame; struct LPC_Frame -> struct structLPC_Frame;
 	struct Formant_Frame->struct structFormant_Frame
 djmw 20051005 Always make a VocalTract with length 0.01 m when wakita_length==NUMundefined.
*/

#include "LPC_and_Tube.h"
#include "LPC_and_Formant.h"
#include "LPC_to_Spectrum.h"
#include "SpectrumTier.h"
#include "VocalTract_to_Spectrum.h"
#include "NUM2.h"

// IEEE: Programs fo digital signal processing section 4.3 LPTRN

void LPC_Frame_into_Tube_Frame_rc (LPC_Frame me, Tube_Frame thee) {
	long p = my nCoefficients;
	Melder_assert (p <= thy nSegments); //TODO

	autoNUMvector<double> b (1, p);
	autoNUMvector<double> a (1, p);

	for (long i = 1; i <= p; i++) {
		a[i] = my a[i];
	}

	double *rc = thy c;
	for (long m = p; m > 0; m--) {
		rc[m] = a[m];
		if (fabs (rc[m]) > 1) {
			Melder_throw ("Relection coefficient [", Melder_integer(m), "] larger 1.");    // TODO kan er geen Tube worden gemaakt?
		}
		for (long i = 1; i < m; i++) {
			b[i] = a[i];
		}
		for (long i = 1; i < m; i++) {
			a[i] = (b[i] - rc[m] * b[m - i]) / (1.0 - rc[m] * rc[m]);
		}
	}
}

void LPC_Frame_into_Tube_Frame_area (LPC_Frame me, Tube_Frame thee) {
	struct structTube_Frame rc_struct = { 0 };
	Tube_Frame rc = & rc_struct;
	Tube_Frame_init (rc, my nCoefficients, thy length);
	LPC_Frame_into_Tube_Frame_rc (me, rc);
	Tube_Frames_rc_into_area (rc, thee);
	rc -> destroy ();
}

double VocalTract_and_LPC_Frame_getMatchingLength (VocalTract me, LPC_Frame thee, double glottalDamping, bool radiationDamping, bool internalDamping) {
	try {
		// match the average distance between the first two formants in the VocaTract and the LPC spectrum
		long numberOfFrequencies = 1000;
		double maximumFrequency = 5000;
		autoSpectrum vts = VocalTract_to_Spectrum (me, numberOfFrequencies, maximumFrequency, glottalDamping, radiationDamping, internalDamping);
		double samplingFrequency =  1000.0 * my nx;
		autoSpectrum lps = Spectrum_create (0.5 * samplingFrequency, numberOfFrequencies);
		LPC_Frame_into_Spectrum (thee, lps.peek(), 0, 50);
		autoSpectrumTier vtst = Spectrum_to_SpectrumTier_peaks (vts.peek());
		autoSpectrumTier lpst = Spectrum_to_SpectrumTier_peaks (lps.peek());
		RealPoint point = vtst -> f_peekPoint (1); double vt_f1 = point -> number;
		point = vtst -> f_peekPoint (2); double vt_f2 = point -> number;
		point = lpst -> f_peekPoint (1); double lp_f1 = point -> number;
		point = lpst -> f_peekPoint (2); double lp_f2 = point -> number;
		double df1 = lp_f1 - vt_f1, df2 =  lp_f2 - vt_f2, df = 0.5 * (df1 + df2);
		double dl = - df / lp_f2;
		return my dx * my nx * (1 + dl);
	} catch (MelderError) {
		Melder_throw (L"Length could not be determined from VocalTract and LPC_Frame.");
	}
}

double LPC_Frame_getVTL_wakita (LPC_Frame me, double samplingPeriod, double refLength) {
	struct structLPC_Frame lpc_struct;
	LPC_Frame lpc = & lpc_struct;
	struct structFormant_Frame f_struct;
	Formant_Frame f = & f_struct;
	struct structTube_Frame rc_struct, af_struct;
	Tube_Frame rc = & rc_struct, af = & af_struct;
	try {
		long m = my nCoefficients;
		double length, dlength = 0.001, wakita_length = NUMundefined;
		double varMin = 1e38;

		memset (& lpc_struct, 0, sizeof (lpc_struct));
		memset (& f_struct, 0, sizeof (f_struct));
		memset (& rc_struct, 0, sizeof (rc_struct));
		memset (& af_struct, 0, sizeof (af_struct));


		LPC_Frame_init (lpc, m);
		Tube_Frame_init (rc, m, refLength);
		Tube_Frame_init (af, m, refLength);

		// Step 2

		LPC_Frame_into_Formant_Frame (me, f, samplingPeriod, 0);

		// LPC_Frame_into_Formant_Frame performs the Formant_Frame_init !!

		if (f -> nFormants < 1) {
			Melder_throw ("Not enough formants.");
		}

		double *area = af -> c;
		double lmin = length = 0.10;
		double plength = refLength;
		while (length <= 0.25) {
			// Step 3

			double fscale = plength / length;
			for (long i = 1; i <= f -> nFormants; i++) {
				f -> formant[i].frequency *= fscale;
				f -> formant[i].bandwidth *= fscale;
			}

			/*
			20000125: Bovenstaande schaling van f1/b1 kan ook gedaan worden door
			MGfb_to_a (f, b, nf, samplingFrequency*length/refLength, a1)
			De berekening is extreem gevoelig voor de samplefrequentie: een zelfde
			stel f,b waardes geven andere lengtes afhankelijk van Fs. Ook het
			weglaten van een hogere formant heeft consekwenties.
			De refLength zou eigenlijk vast moeten liggen op
			refLength=c*aantalFormanten/Fs waarbij c=340 m/s (geluidssnelheid).
			Bij Fs=10000 zou aantalFormanten=5 zijn en refLength -> 0.17 m
			*/

			// step 4

			Formant_Frame_into_LPC_Frame (f, lpc, samplingPeriod);

			// step 5

			rc -> length = length;
			LPC_Frame_into_Tube_Frame_rc (lpc, rc);

			// step 6.1

			Tube_Frames_rc_into_area (rc, af);

			// step 6.2 Log(areas)

			double logSum = 0;
			for (long i = 1; i <= af -> nSegments; i++) {
				area[i] = log (area[i]);
				logSum += area[i];
			}

			// step 6.3 and 7
			double var = 0;
			for (long i = 1; i <= af -> nSegments; i++) {
				double delta = area[i] - logSum / af -> nSegments;
				var += delta * delta;
			}

			if (var < varMin) {
				lmin = length; varMin = var;
			}
			plength = length;
			length += dlength;
		}

		wakita_length = lmin;
		f -> destroy ();
		lpc -> destroy ();
		rc -> destroy ();
		af -> destroy ();
		return wakita_length;
	} catch (MelderError) {
		f -> destroy ();
		lpc -> destroy ();
		rc -> destroy ();
		af -> destroy ();
		return NUMundefined;
	}
}

int Tube_Frame_into_LPC_Frame_area (Tube_Frame me, LPC_Frame thee) {
	(void) me;
	(void) thee;
	return 0;
}

int Tube_Frame_into_LPC_Frame_rc (Tube_Frame me, LPC_Frame thee) {
	(void) me;
	(void) thee;
	return 0;
}

void VocalTract_setLength (VocalTract me, double newLength) {
	my xmax = newLength;
	my dx = newLength / my nx;
	my x1 = 0.5 * my dx;
}

VocalTract LPC_to_VocalTract (LPC me, double time, double glottalDamping, bool radiationDamping, bool internalDamping) {
	try {
		long iframe = Sampled_xToIndex (me, time);
		if (iframe < 1) {
			iframe = 1;
		}
		if (iframe > my nx) {
			iframe = my nx;
		}
		LPC_Frame lpc = & my d_frames[iframe];
		autoVocalTract thee = LPC_Frame_to_VocalTract (lpc, 0.17);
		double length = VocalTract_and_LPC_Frame_getMatchingLength (thee.peek(), lpc, glottalDamping, radiationDamping, internalDamping);
		VocalTract_setLength (thee.peek(), length);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no VocalTract created.");
	}
}

VocalTract LPC_Frame_to_VocalTract2 (LPC_Frame me, double length) {
	struct structTube_Frame area_struct = { 0 };
	Tube_Frame area = & area_struct;
	try {
		long m = my nCoefficients;

		Tube_Frame_init (area, m, length);
		LPC_Frame_into_Tube_Frame_area (me, area);

		autoVocalTract thee = VocalTract_create (m, area -> length / m);

		// area[lips..glottis] (m^2) to VocalTract[glottis..lips] (m^2)

		for (long i = 1; i <= m; i++) {
			thy z[1][i] = area -> c[m + 1 - i];
		}
		area -> destroy ();
		return thee.transfer();
	} catch (MelderError) {
		area -> destroy ();
		Melder_throw ("No VocalTract created from LPC_Frame.");
	}
}

VocalTract LPC_Frame_to_VocalTract (LPC_Frame me, double length) {
	try {
		long m = my nCoefficients;
		autoNUMvector<double> area (1, m + 1);
		NUMlpc_lpc_to_area (my a, m, area.peek());
		autoVocalTract thee = VocalTract_create (m, length / m);

		// area[lips..glottis] (m^2) to VocalTract[glottis..lips] (m^2)

		for (long i = 1; i <= m; i++) {
			thy z[1][i] = area[m + 1 - i];
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw ("No VocalTract created from LPC_Frame.");
	}
}

VocalTract LPC_to_VocalTract (LPC me, double time, double length) {
	try {
		long iframe = Sampled_xToIndex (me, time);
		if (iframe < 1) {
			iframe = 1;
		}
		if (iframe > my nx) {
			iframe = my nx;
		}
		LPC_Frame lpc = & my d_frames[iframe];
		autoVocalTract thee = LPC_Frame_to_VocalTract (lpc, length);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no VocalTract created.");
	}
}


/* End of file LPC_and_Tube.cpp */
