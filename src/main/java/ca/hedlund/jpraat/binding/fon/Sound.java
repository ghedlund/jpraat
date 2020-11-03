/*
 * Copyright (C) 2005-2020 Gregory Hedlund
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.*;

import ca.hedlund.jpraat.annotations.*;
import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;

/**
 * 
 */
public final class Sound extends Vector {
	
	public Sound() {
		super();
	}
	
	public Sound(Pointer p) {
		super(p);
	}
	
	public static final int LEVEL_MONO = 0;
	public static final int LEVEL_LEFT = 1;
	public static final int LEVEL_RIGHT = 2;
	
	public static final int TONE_COMPLEX_SINE = 0;
	public static final int TONE_COMPLEX_COSINE = 1;
	
	/* Audio encodings. */
	public static final int Melder_LINEAR_8_SIGNED = 1;
	public static final int Melder_LINEAR_8_UNSIGNED = 2;
	public static final int Melder_LINEAR_16_BIG_ENDIAN = 3;
	public static final int Melder_LINEAR_16_LITTLE_ENDIAN = 4;
	public static final int Melder_LINEAR_24_BIG_ENDIAN = 5;
	public static final int Melder_LINEAR_24_LITTLE_ENDIAN = 6;
	public static final int Melder_LINEAR_32_BIG_ENDIAN = 7;
	public static final int Melder_LINEAR_32_LITTLE_ENDIAN = 8;

	public static Sound readFromPath(String path) throws PraatException {
		return readFromSoundFile(MelderFile.fromPath(path));
	}
	
	public static Sound readFromSoundFile(MelderFile file) throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_readFromSoundFile_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Sound createAsPureTone (long numberOfChannels, double startingTime, double endTime,
			double sampleRate, double frequency, double amplitude, double fadeInDuration, double fadeOutDuration)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_createAsPureTone_wrapped(new NativeIntptr_t(numberOfChannels), startingTime,
					endTime, sampleRate, frequency, amplitude, fadeInDuration, fadeOutDuration);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
		
	public static Sound createAsToneComplex (double startingTime, double endTime,
			double sampleRate, int phase, double frequencyStep,
			double firstFrequency, double ceiling, long numberOfComponents) 
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_createAsToneComplex_wrapped(startingTime, endTime,
					sampleRate, phase, frequencyStep, firstFrequency, ceiling, numberOfComponents);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Sound create(long numberOfChannels, double xmin, double xmax, long nx, double dx, double x1)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_create_wrapped(new NativeIntptr_t(numberOfChannels), 
					xmin, xmax, new NativeIntptr_t(nx), dx, x1);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Sound createSimple(long numberOfChannels, double duration, double samplingFrequency) throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_createSimple_wrapped(
					new NativeIntptr_t(numberOfChannels), duration,
					samplingFrequency);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Sound convertToMono () throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_convertToMono_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public Sound convertToStereo () throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_convertToStereo_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Sound extractChannel (long ichannel) throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_extractChannel_wrapped(this,
					new NativeIntptr_t(ichannel));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
//	public static Sound combineToStereo(Sound left, Sound right)
//		throws PraatException {
//		Collection sounds = Collection.create(left.getClassInfo(), 2);
//		sounds.addItem(left);
//		sounds.addItem(right);
//		Sound retVal = null;
//		try {
//			Praat.wrapperLock.lock();
//			retVal = Praat.INSTANCE.Sounds_combineToStereo_wrapped(sounds);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		return retVal;
//	}
	
	public static Sound convolve (Sound me, Sound thee, 
			kSounds_convolve_scaling scaling, kSounds_convolve_signalOutsideTimeDomain signalOutsideTimeDomain)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sounds_convolve_wrapped(me, thee, scaling, signalOutsideTimeDomain);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Sound append(Sound me, double silenceDuration, Sound thee) 
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sounds_append_wrapped(me, silenceDuration, thee);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Sound upsample () throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_upsample_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public Sound resample (double samplingFrequency, long precision) throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_resample_wrapped(this,
					samplingFrequency, new NativeIntptr_t(precision));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Sound autoCorrelate (kSounds_convolve_scaling scaling, kSounds_convolve_signalOutsideTimeDomain signalOutsideTimeDomain)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_autoCorrelate_wrapped(this,
					scaling, signalOutsideTimeDomain);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Sound crossCorrelate (Sound me, Sound thee,
			kSounds_convolve_scaling scaling, kSounds_convolve_signalOutsideTimeDomain signalOutsideTimeDomain)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sounds_crossCorrelate_wrapped(me, thee, scaling, signalOutsideTimeDomain);
			Praat.checkAndClearLastError();
		} catch(PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Sound crossCorrelate_short (Sound me, Sound thee, double tmin, double tmax, int normalize) 
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sounds_crossCorrelate_short_wrapped(me, thee, tmin, tmax, normalize);
			Praat.checkAndClearLastError();
		} catch(PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public double getEnergyInAir () {
		return Praat.INSTANCE.Sound_getEnergyInAir(this);
	}
	
	public double getRootMeanSquare ( double xmin, double xmax) {
		return Praat.INSTANCE.Sound_getRootMeanSquare(this, xmin, xmax);
	}
	
	public double getEnergy ( double xmin, double xmax) {
		return Praat.INSTANCE.Sound_getEnergy(this, xmin, xmax);
	}
	
	public double getPower ( double xmin, double xmax) {
		return Praat.INSTANCE.Sound_getPower(this, xmin, xmax);
	}
	
	public double getPowerInAir () {
		return Praat.INSTANCE.Sound_getPowerInAir(this);
	}
	
	public double getIntensity_dB () {
		return Praat.INSTANCE.Sound_getIntensity_dB(this);
	}
	
	public Sound extractPart (double t1, double t2, kSound_windowShape windowShape, double relativeWidth, boolean preserveTimes)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_extractPart_wrapped(this, t1,
					t2, windowShape, relativeWidth, preserveTimes);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Sound extractPartForOverlap (double t1, double t2, double overlap)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_extractPartForOverlap_wrapped(this, t1, t2, overlap);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getNearestZeroCrossing (double position, long ichannel) {
		return Praat.INSTANCE.Sound_getNearestZeroCrossing(this, position, new NativeIntptr_t(ichannel));
	}
	
	public void setZero (double tmin, double tmax, boolean roundTimesToNearestZeroCrossing) {
		Praat.INSTANCE.Sound_setZero(this, tmin, tmax, roundTimesToNearestZeroCrossing);
	}
	
//	public static Sound concatenate_e (Collection me, double overlapTime) 
//		throws PraatException {
//		Sound retVal = null;
//		try {
//			Praat.wrapperLock.lock();
//			retVal = Praat.INSTANCE.Sounds_concatenate_e_wrapped(me, overlapTime);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		return retVal;
//	}
	
	public void multiplyByWindow (kSound_windowShape windowShape) {
		Praat.INSTANCE.Sound_multiplyByWindow(this, windowShape);
	}
	
	public void scaleIntensity (double newAverageIntensity) {
		Praat.INSTANCE.Sound_scaleIntensity(this, newAverageIntensity);
	}
	
	public void overrideSamplingFrequency (double newSamplingFrequency) {
		Praat.INSTANCE.Sound_overrideSamplingFrequency(this, newSamplingFrequency);
	}
	
	public void reverse (double tmin, double tmax) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Sound_reverse_wrapped(this, tmin, tmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void filterWithFormants (double tmin, double tmax,
			int numberOfFormants, double formant [], double bandwidth []) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Sound_filterWithFormants_wrapped(this, tmin, tmax, numberOfFormants, formant, bandwidth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public Sound filter_oneFormant (double frequency, double bandwidth)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_filter_oneFormant_wrapped(this, frequency, bandwidth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void filterWithOneFormantInplace_wrapped (double frequency, double bandwidth)
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Sound_filterWithOneFormantInplace_wrapped(this, frequency, bandwidth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public Sound filter_preemphasis (double frequency)
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_filter_preemphasis_wrapped(this, frequency);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	@Declared("fon/Sound.h")
	@Wrapped
	public Sound filter_deemphasis (double frequency) 
		throws PraatException {
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_filter_deemphasis_wrapped(this, frequency);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	/**
	 * Create a new spectrogram object from this sound.
	 * 
	 * 
	 * 
	 * @param effectiveAnalysisWidth
	 * @param fmax
	 * @param minimumTimeStep1
	 * @param minimumFreqStep1
	 * @param windowShape
	 * @param maximumTimeOversampling
	 * @param maximumFreqOversampling
	 * @return
	 * 
	 * @throws IllegalArgumentException
	 */
	public Spectrogram to_Spectrogram (double effectiveAnalysisWidth, double fmax,
			double minimumTimeStep1, double minimumFreqStep1, kSound_to_Spectrogram_windowShape windowShape,
			double maximumTimeOversampling, double maximumFreqOversampling) throws PraatException {
		
		// do some checks so here so we don't throw MelderErrors and crash the JVM
		// from praat:fon/Sound_and_Spectrogram.cpp#Sound_to_Spectrogram
		double physicalAnalysisWidth =
				windowShape == kSound_to_Spectrogram_windowShape.GAUSSIAN ? 2 * effectiveAnalysisWidth : effectiveAnalysisWidth;
		double duration = getDx() * getNx();
		
		long nsamp_window = (long)Math.floor(physicalAnalysisWidth / getDx());
		if(nsamp_window < 1) 
			throw new IllegalArgumentException("Your analysis window is too short: less than two samples.");
		if(physicalAnalysisWidth > duration) {
			throw new IllegalArgumentException("Your sound is too short.");
		}
		
		Spectrogram retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Spectrogram_wrapped(
					this, effectiveAnalysisWidth, fmax, minimumTimeStep1,
					minimumFreqStep1, windowShape, maximumTimeOversampling,
					maximumFreqOversampling);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Pitch to_Pitch (double timeStep,
			double minimumPitch, double maximumPitch) throws PraatException {
		Pitch retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Pitch_wrapped(this,
					timeStep, minimumPitch, maximumPitch);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	/* Calls Sound_to_Pitch_any with AC method. */
	public Pitch to_Pitch_ac (double timeStep, double minimumPitch,
		double periodsPerWindow, long maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch) throws PraatException {
		checkMaxnCandidates(maxnCandidates);
		checkMinPitchPeriodsPerWindow(minimumPitch, periodsPerWindow);
		
		Pitch retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Pitch_ac_wrapped(this,
					timeStep, minimumPitch, periodsPerWindow, new NativeIntptr_t(maxnCandidates),
					accurate, silenceThreshold, voicingThreshold, octaveCost,
					octaveJumpCost, voicedUnvoicedCost, maximumPitch);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	/* Calls Sound_to_Pitch_any with FCC method. */
	public Pitch to_Pitch_cc (double timeStep, double minimumPitch,
		double periodsPerWindow, long maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch) throws PraatException {
		checkMaxnCandidates(maxnCandidates);
		checkMinPitchPeriodsPerWindow(minimumPitch, periodsPerWindow);
		Pitch retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Pitch_cc_wrapped(this,
					timeStep, minimumPitch, periodsPerWindow, new NativeIntptr_t(maxnCandidates),
					accurate, silenceThreshold, voicingThreshold, octaveCost,
					octaveJumpCost, voicedUnvoicedCost, maximumPitch);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	/*
		Function:
			acoustic periodicity analysis.
		Preconditions:
			minimumPitch > 0.0;
			maxnCandidates >= 2;
		Return value:
			the resulting pitch contour, or NULL in case of failure.
		Failures:
			Out of memory.
			Minimum frequency too low.
			Maximum frequency should not be greater than the Sound's Nyquist frequency.
		Description for method 0 or 1:
			There is a Hanning window (method == 0) or Gaussian window (method == 1)
			over the analysis window, in order to avoid phase effects.
			Zeroes are appended to the analysis window to avoid edge effects in the FFT.
			An FFT is done on the window, giving a complex spectrum.
			This complex spectrum is squared, thus giving the power spectrum.
			The power spectrum is FFTed back, thus giving the autocorrelation of
			the windowed frame. This autocorrelation is expressed relative to the power,
			which is the autocorrelation for lag 0. The autocorrelation is divided by
			the normalized autocorrelation of the window, in order to bring
			all maxima of the autocorrelation of a periodic signal to the same height.
		General description:
			The maxima are found by sinc interpolation.
			The pitch values (frequencies) of the highest 'maxnCandidates' maxima
			are saved in 'result', together with their strengths (relative correlations).
			A Viterbi algorithm is used to find a smooth path through the candidates,
			using the last six arguments of this function.

			The 'maximumPitch' argument has no influence on the search for candidates.
			It is directly copied into the Pitch object as a hint for considering
			pitches above a certain value "voiceless".
 	*/
	public Pitch to_Pitch_any (Sound me,

		double dt,                 /* time step (seconds); 0.0 = automatic = periodsPerWindow / minimumPitch / 4 */
		double minimumPitch,       /* (Hz) */
		double periodsPerWindow,   /* ac3 for pitch analysis, 6 or 4.5 for HNR, 1 for FCC */
		int maxnCandidates,        /* maximum number of candidates per frame */
		int method,                /* 0 or 1 = AC, 2 or 3 = FCC, 0 or 2 = fast, 1 or 3 = accurate */

		double silenceThreshold,   /* relative to purely periodic; default 0.03 */
		double voicingThreshold,   /* relative to purely periodic; default 0.45 */
		double octaveCost,         /* favours higher pitches; default 0.01 */
		double octaveJumpCost,     /* default 0.35 */
		double voicedUnvoicedCost, /* default 0.14 */
		double maximumPitch)    throws PraatException  /* (Hz) */
	{
		checkMaxnCandidates(maxnCandidates);
		checkMinPitchPeriodsPerWindow(minimumPitch, periodsPerWindow);
		Pitch retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Pitch_any_wrapped(this, dt,
					minimumPitch, periodsPerWindow, maxnCandidates, method,
					silenceThreshold, voicingThreshold, octaveCost,
					octaveJumpCost, voicedUnvoicedCost, maximumPitch);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	private void checkMaxnCandidates(long maxnCandidates) {
		if(maxnCandidates < 2)
			throw new IllegalArgumentException("Max candidates must be >= 2");
	}
	
	private void checkMinPitchPeriodsPerWindow(double minPitch, double periodsPerWindow) {
		double duration = getNx() * getDx();
		if(minPitch < periodsPerWindow / duration) {
			final String msg = 
					"To analyse this sound minimum pitch must be no less than "
					+ periodsPerWindow/duration + " Hz.";
			throw new IllegalArgumentException(msg);
		}
		
		double dt_window = periodsPerWindow / minPitch;
		long nsamp_window = (long)Math.floor(dt_window / getDx());
		long halfnsamp_window = nsamp_window / 2 - 1;
		if(halfnsamp_window < 2) {
			throw new IllegalArgumentException("Analysis window too short.");
		}
	}

	/*
		Which = 1: Burg.
		Which = 2: Split-Levinson
	*/
	public Formant to_Formant_any (double timeStep, long numberOfPoles, double maximumFrequency,
			double halfdt_window, int which, double preemphasisFrequency, double safetyMargin) throws PraatException {
		checkFormantWindow(numberOfPoles, halfdt_window);
		Formant retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Formant_any_wrapped(this,
					timeStep, new NativeIntptr_t(numberOfPoles), maximumFrequency, halfdt_window,
					which, preemphasisFrequency, safetyMargin);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	/* Throws away all formants below 50 Hz and above Nyquist minus 50 Hz. */
	public Formant to_Formant_burg (double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency) 
		throws PraatException {
		checkFormantWindow((int)(2*numberOfFormants), windowLength);
		Formant retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Formant_burg_wrapped(this,
					timeStep, numberOfFormants, maximumFormantFrequency,
					windowLength, preemphasisFrequency);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	/* Same as previous, but keeps all formants. Good for resynthesis. */
	public Formant to_Formant_keepAll (double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency) 
		throws PraatException {
		checkFormantWindow((int)(2*numberOfFormants), windowLength);
		Formant retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Formant_keepAll_wrapped(
					this, timeStep, numberOfFormants, maximumFormantFrequency,
					windowLength, preemphasisFrequency);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public Formant to_Formant_willems (double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency)
		throws PraatException {
		checkFormantWindow((int)(2*numberOfFormants), windowLength);
		Formant retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Formant_willems_wrapped(
					this, timeStep, numberOfFormants, maximumFormantFrequency,
					windowLength, preemphasisFrequency);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	private void checkFormantWindow(long numberOfPoles, double windowLength) {
		double dt_window = 2.0 * windowLength;
		long nsamp_window = (long)Math.floor(dt_window/getDx());
		
		if(nsamp_window < numberOfPoles + 1) {
			throw new IllegalArgumentException("Window too short.");
		}
	}
	
	public Ltas to_Ltas (double bandwidth) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Ltas_wrapped(this, bandwidth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Ltas to_Ltas_pitchCorrected(double minimumPitch, double maximumPitch,
		double maximumFrequency, double bandWidth,
		double shortestPeriod, double longestPeriod, double maximumPeriodFactor) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Ltas_pitchCorrected_wrapped(this, minimumPitch,
					maximumPitch, maximumFrequency, bandWidth, shortestPeriod, longestPeriod, maximumPeriodFactor);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	
	public Intensity to_Intensity (double minimumPitch, double timeStep, boolean subtractMean) 
		throws PraatException {

		if(Double.isNaN(minimumPitch) || minimumPitch <= 0.0) {
			throw new IllegalArgumentException("Minimum pitch must be > 0.0");
		}
		
		if(Double.isNaN(timeStep) || timeStep < 0.0) {
			throw new IllegalArgumentException("Time step must be >= 0.0");
		}
		
		double duration = getDx() * getNx();
		double minDuration = 6.4 / minimumPitch;
		if(duration < minDuration) {
			throw new IllegalArgumentException("The duration of the sound in an intensity analysis should be "
					+ "at least 6.4 divided by the minimum pitch.  i.e., at least " + minDuration + "s.");
		}
		
		Intensity retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Intensity_wrapped(this, minimumPitch, timeStep, subtractMean);
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}

	public Spectrum to_Spectrum (boolean fast) throws PraatException {
		Spectrum retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_to_Spectrum_wrapped(this, fast);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
		
	public Sound filter_passHannBand (double fmin, double fmax, double smooth) throws PraatException {
		@SuppressWarnings("resource")
		Sound retVal = this;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_filter_passHannBand_wrapped(this, fmin, fmax, smooth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
	public Sound filter_stopHannBand (double fmin, double fmax, double smooth) throws PraatException {
		@SuppressWarnings("resource")
		Sound retVal = this;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_filter_stopHannBand_wrapped(this, fmin, fmax, smooth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
	public void saveAsAudioFile(MelderFile file, int audioFileType, int numberOfBitsPerSamplePoint) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Sound_saveAsAudioFile_wrapped(this, file, audioFileType, numberOfBitsPerSamplePoint);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void saveAsRawSoundFile (MelderFile file, int encoding) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Sound_saveAsRawSoundFile_wrapped(this, file, encoding);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
}
