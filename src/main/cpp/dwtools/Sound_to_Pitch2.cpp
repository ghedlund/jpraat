/* Sound_to_Pitch2.c
 *
 * Copyright (C) 1993-2011 David Weenink
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
 djmw 20021106 Latest modification
 djmw 20041124 Changed call to Sound_to_Spectrum.
 djmw 20070103 Sound interface changes
*/

#include "Sound_to_Pitch2.h"
#include "Pitch_extensions.h"
#include "Sound_and_Spectrum.h"
#include "Sound_to_SPINET.h"
#include "SPINET_to_Pitch.h"
#include "NUM2.h"

static int spec_enhance_SHS (double a[], long n) {
	if (n < 2) {
		return 0;
	}
	autoNUMvector<long> posmax (1, (n + 1) / 2);
	long nmax = 0;
	if (a[1] > a[2]) {
		posmax[++nmax] = 1;
	}
	for (long i = 2; i <= n - 1; i++) if (a[i] > a[i - 1] && a[i] >= a[i + 1]) {
			posmax[++nmax] = i;
		}
	if (a[n] > a[n - 1]) {
		posmax[++nmax] = n;
	}
	if (nmax == 1) {
		for (long j = 1; j <= posmax[1] - 3; j++) {
			a[j] = 0;
		}
		for (long j = posmax[1] + 3; j <= n; j++) {
			a[j] = 0;
		}
	} else {
		for (long i = 2; i <= nmax; i++) {
			for (long j = posmax[i - 1] + 3; j <= posmax[i] - 3; j++) {
				a[j] = 0;
			}
		}
	}
	return 1;
}

static void spec_smoooth_SHS (double a[], long n) {
	double ai, aim1 = 0;
	for (long i = 1; i <= n - 1; i++) {
		ai = a[i]; a[i] = (aim1 + 2 * ai + a[i + 1]) / 4; aim1 = ai;
	}
}

Pitch Sound_to_Pitch_shs (Sound me, double timeStep, double minimumPitch,
                          double maximumFrequency, double ceiling, long maxnSubharmonics, long maxnCandidates,
                          double compressionFactor, long nPointsPerOctave) {
	try {
		double firstTime, newSamplingFrequency = 2 * maximumFrequency;
		double windowDuration = 2 / minimumPitch, halfWindow = windowDuration / 2;
		double atans = nPointsPerOctave * NUMlog2 (65.0 / 50.0) - 1;
		// Number of speech samples in the downsampled signal in each frame:
		// 100 for windowDuration == 0.04 and newSamplingFrequency == 2500
		long nx = floor (windowDuration * newSamplingFrequency + 0.5);

		// The minimum number of points for the fft is 256.
		long nfft = 1;
		while ( (nfft *= 2) < nx || nfft <= 128) {
			;
		}
		long nfft2 = nfft / 2 + 1;
		double frameDuration = nfft / newSamplingFrequency;
		double df = newSamplingFrequency / nfft;

		// The number of points on the octave scale

		double fminl2 = NUMlog2 (minimumPitch), fmaxl2 = NUMlog2 (maximumFrequency);
		long nFrequencyPoints = (fmaxl2 - fminl2) * nPointsPerOctave;
		double dfl2 = (fmaxl2 - fminl2) / (nFrequencyPoints - 1);

		autoSound sound = Sound_resample (me, newSamplingFrequency, 50);
		long numberOfFrames;
		Sampled_shortTermAnalysis (sound.peek(), windowDuration, timeStep, &numberOfFrames, &firstTime);
		autoSound frame = Sound_createSimple (1, frameDuration, newSamplingFrequency);
		autoSound hamming = Sound_createHamming (nx / newSamplingFrequency, newSamplingFrequency);
		autoPitch thee = Pitch_create (my xmin, my xmax, numberOfFrames, timeStep, firstTime,
		                               ceiling, maxnCandidates);
		autoNUMvector<double> cc (1, numberOfFrames);
		autoNUMvector<double> specAmp (1, nfft2);
		autoNUMvector<double> fl2 (1, nfft2);
		autoNUMvector<double> yv2 (1, nfft2);
		autoNUMvector<double> arctg (1, nFrequencyPoints);
		autoNUMvector<double> al2 (1, nFrequencyPoints);

		Melder_assert (frame->nx >= nx);
		Melder_assert (hamming->nx == nx);

		// Compute the absolute value of the globally largest amplitude w.r.t. the global mean.

		double globalMean, globalPeak;
		Sound_localMean (sound.peek(), sound -> xmin, sound -> xmax, &globalMean);
		Sound_localPeak (sound.peek(), sound -> xmin, sound -> xmax, globalMean, &globalPeak);

		/*
			For the cubic spline interpolation we need the frequencies on an octave
			scale, i.e., a log2 scale. All frequencies must be DIFFERENT, otherwise
			the cubic spline interpolation will give corrupt results.
			Because log2(f==0) is not defined, we use the heuristic: f[2]-f[1] == f[3]-f[2].
		*/

		for (long i = 2; i <= nfft2; i++) {
			fl2[i] = NUMlog2 ( (i - 1) * df);
		}
		fl2[1] = 2 * fl2[2] - fl2[3];

		// Calculate frequencies regularly spaced on a log2-scale and
		// the frequency weighting function.

		for (long i = 1; i <= nFrequencyPoints; i++) {
			arctg[i] = 0.5 + atan (3 * (i - atans) / nPointsPerOctave) / NUMpi;
		}

		// Perform the analysis on all frames.

		for (long i = 1; i <= numberOfFrames; i++) {
			Pitch_Frame pitchFrame = &thy frame[i];
			double hm = 1, f0, pitch_strength, localMean, localPeak;
			double tmid = Sampled_indexToX (thee.peek(), i); /* The center of this frame */
			long nx_tmp = frame -> nx;

			// Copy a frame from the sound, apply a hamming window. Get local 'intensity'


			frame -> nx = nx; /*begin vies */
			Sound_into_Sound (sound.peek(), frame.peek(), tmid - halfWindow);
			Sounds_multiply (frame.peek(), hamming.peek());
			Sound_localMean (sound.peek(), tmid - 3 * halfWindow, tmid + 3 * halfWindow, &localMean);
			Sound_localPeak (sound.peek(), tmid - halfWindow, tmid + halfWindow, localMean, &localPeak);
			pitchFrame -> intensity = localPeak > globalPeak ? 1 : localPeak / globalPeak;
			frame -> nx = nx_tmp; /* einde vies */

			// Get the Fourier spectrum.

			autoSpectrum spec = Sound_to_Spectrum (frame.peek(), 1);
			Melder_assert (spec->nx == nfft2);

			// From complex spectrum to amplitude spectrum.

			for (long j = 1; j <= nfft2; j++) {
				double rs = spec -> z[1][j], is = spec -> z[2][j];
				specAmp[j] = sqrt (rs * rs + is * is);
			}

			// Enhance the peaks in the spectrum.

			spec_enhance_SHS (specAmp.peek(), nfft2);

			// Smooth the enhanced spectrum.

			spec_smoooth_SHS (specAmp.peek(), nfft2);

			// Go to a logarithmic scale and perform cubic spline interpolation to get
			// spectral values for the increased number of frequency points.

			NUMspline (fl2.peek(), specAmp.peek(), nfft2, 1e30, 1e30, yv2.peek());
			for (long j = 1; j <= nFrequencyPoints; j++) {
				double f = fminl2 + (j - 1) * dfl2;
				NUMsplint (fl2.peek(), specAmp.peek(), yv2.peek(), nfft2, f, &al2[j]);
			}

			// Multiply by frequency selectivity of the auditory system.

			for (long j = 1; j <= nFrequencyPoints; j++) al2[j] = al2[j] > 0 ?
				        al2[j] * arctg[j] : 0;

			// The subharmonic summation. Shift spectra in octaves and sum.

			Pitch_Frame_init (pitchFrame, maxnCandidates);
			autoNUMvector<double> sumspec (1, nFrequencyPoints);
			pitchFrame -> nCandidates = 0; /* !!!!! */

			for (long m = 1; m <= maxnSubharmonics + 1; m++) {
				long kb = 1 + floor (nPointsPerOctave * NUMlog2 (m));
				for (long k = kb; k <= nFrequencyPoints; k++) {
					sumspec[k - kb + 1] += al2[k] * hm;
				}
				hm *= compressionFactor;
			}

			// First register the voiceless candidate (always present).

			Pitch_Frame_addPitch (pitchFrame, 0, 0, maxnCandidates);

			/*
				Get the best local estimates for the pitch as the maxima of the
				subharmonic sum spectrum by parabolic interpolation on three points:
				The formula for a parabole with a maximum is:
					y(x) = a - b (x - c)^2 with a, b, c >= 0
				The three points are (-x, y1), (0, y2) and (x, y3).
				The solution for a (the maximum) and c (the position) is:
				a = (2 y1 (4 y2 + y3) - y1^2 - (y3 - 4 y2)^2)/( 8 (y1 - 2 y2 + y3)
				c = dx (y1 - y3) / (2 (y1 - 2 y2 + y3))
				(b = (2 y2 - y1 - y3) / (2 dx^2) )
			*/

			for (long k = 2; k <= nFrequencyPoints - 1; k++) {
				double y1 = sumspec[k - 1], y2 = sumspec[k], y3 = sumspec[k + 1];
				if (y2 > y1 && y2 >= y3) {
					double denum = y1 - 2 * y2 + y3, tmp = y3 - 4 * y2;
					double x =  dfl2 * (y1 - y3) / (2 * denum);
					double f = pow (2, fminl2 + (k - 1) * dfl2 + x);
					double strength = (2 * y1 * (4 * y2 + y3) - y1 * y1 - tmp * tmp) / (8 * denum);
					Pitch_Frame_addPitch (pitchFrame, f, strength, maxnCandidates);
				}
			}

			/*
				Check whether f0 corresponds to an actual periodicity T = 1 / f0:
				correlate two signal periods of duration T, one starting at the
				middle of the interval and one starting T seconds before.
				If there is periodicity the correlation coefficient should be high.

				However, some sounds do not show any regularity, or very low
				frequency and regularity, and nevertheless have a definite
				pitch, e.g. Shepard sounds.
			*/

			Pitch_Frame_getPitch (pitchFrame, &f0, &pitch_strength);
			if (f0 > 0) {
				cc[i] = Sound_correlateParts (sound.peek(), tmid - 1.0 / f0, tmid, 1.0 / f0);
			}
		}

		// Base V/UV decision on correlation coefficients.
		// Resize the pitch strengths w.r.t. the cc.

		double vuvCriterium = 0.52;
		for (long i = 1; i <= numberOfFrames; i++) {
			Pitch_Frame_resizeStrengths (& thy frame[i], cc[i], vuvCriterium);
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Pitch (shs) created.");
	}
}

Pitch Sound_to_Pitch_SPINET (Sound me, double timeStep, double windowDuration,
                             double minimumFrequencyHz, double maximumFrequencyHz, long nFilters,
                             double ceiling, int maxnCandidates) {
	try {
		autoSPINET him = Sound_to_SPINET (me, timeStep, windowDuration, minimumFrequencyHz,
		                                  maximumFrequencyHz, nFilters, 0.4, 0.6);
		autoPitch thee = SPINET_to_Pitch (him.peek(), 0.15, ceiling, maxnCandidates);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no Pitch (SPINET) created.");
	}
}

/* End of file Sound_to_Pitch2.cpp */
