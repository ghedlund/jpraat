package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Declared;
import ca.hedlund.jpraat.binding.sys.MelderFile;

/**
 * 
 */
public class Sound extends Vector {
	
	public static final int LEVEL_MONO = 0;
	public static final int LEVEL_LEFT = 1;
	public static final int LEVEL_RIGHT = 2;

	public static Sound readFromPath(String path) {
		return readFromSoundFile(MelderFile.fromPath(path));
	}
	
	public static Sound readFromSoundFile(MelderFile file) {
		return Praat.INSTANCE.Sound_readFromSoundFile(file);
	}
	
	public static Sound create(long numberOfChannels, double xmin, double xmax, long nx, double dx, double x1) {
		return Praat.INSTANCE.Sound_create(numberOfChannels, xmin, xmax, nx, dx, x1);
	}
	
	public static Sound createSimple(long numberOfChannels, double duration, double samplingFrequency) {
		return Praat.INSTANCE.Sound_createSimple(numberOfChannels, duration, samplingFrequency);
	}
	
	public Sound convertToMono () {
		return Praat.INSTANCE.Sound_convertToMono(this);
	}

	public Sound convertToStereo () {
		return Praat.INSTANCE.Sound_convertToStereo(this);
	}
	
	public Sound  extractChannel (long ichannel) {
		return Praat.INSTANCE.Sound_extractChannel(this, ichannel);
	}
	
	public Sound upsample () {
		return Praat.INSTANCE.Sound_upsample(this);
	}

	public Sound resample (double samplingFrequency, long precision) {
		return Praat.INSTANCE.Sound_resample(this, samplingFrequency, precision);
	}
	
	public Sound autoCorrelate (kSounds_convolveScaling scaling, kSounds_convolveSignalOutsideTimeDomain signalOutsideTimeDomain) {
		return Praat.INSTANCE.Sound_autoCorrelate(this, scaling, signalOutsideTimeDomain);
	}

	public double getEnergyInAir () {
		return Praat.INSTANCE.Sound_getEnergyInAir(this);
	}
	
	public Sound extractPart (double t1, double t2, kSound_windowShape windowShape, double relativeWidth, boolean preserveTimes) {
		return Praat.INSTANCE.Sound_extractPart(this, t1, t2, windowShape, relativeWidth, preserveTimes);
	}

	@Declared("fon/Sound_to_Spectrogram.h")
	public Spectrogram toSpectrogram (double effectiveAnalysisWidth, double fmax,
			double minimumTimeStep1, double minimumFreqStep1, kSound_to_Spectrogram_windowShape windowShape,
			double maximumTimeOversampling, double maximumFreqOversampling) {
		return Praat.INSTANCE.Sound_to_Spectrogram(this, effectiveAnalysisWidth, fmax, minimumTimeStep1, minimumFreqStep1, 
				windowShape, maximumTimeOversampling, maximumFreqOversampling);
	}
	
	@Declared("fon/Sound_to_Pitch.h")
	/* Calls Sound_to_Pitch_ac with default arguments. */
	public Pitch to_Pitch (double timeStep,
			double minimumPitch, double maximumPitch) {
		return Praat.INSTANCE.Sound_to_Pitch(this, timeStep, minimumPitch, maximumPitch);
	}

	/* Calls Sound_to_Pitch_any with AC method. */
	@Declared("fon/Sound_to_Pitch.h")
	public Pitch to_Pitch_ac (double timeStep, double minimumPitch,
		double periodsPerWindow, int maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch) {
		return Praat.INSTANCE.Sound_to_Pitch_ac(this, timeStep, minimumPitch, periodsPerWindow, maxnCandidates, accurate, 
				silenceThreshold, voicingThreshold, octaveCost, octaveJumpCost, voicedUnvoicedCost, maximumPitch);
	}

	/* Calls Sound_to_Pitch_any with FCC method. */
	@Declared("fon/Sound_to_Pitch.h")
	public Pitch Sound_to_Pitch_cc (Sound me, double timeStep, double minimumPitch,
		double periodsPerWindow, int maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch) {
		return Praat.INSTANCE.Sound_to_Pitch_cc(this, timeStep, minimumPitch, periodsPerWindow, maxnCandidates, accurate,
				silenceThreshold, voicingThreshold, octaveCost, octaveJumpCost, voicedUnvoicedCost, maximumPitch);
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
	@Declared("fon/Sound_to_Pitch.h")
	public Pitch Sound_to_Pitch_any (Sound me,

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
		double maximumPitch)     /* (Hz) */
	{
		return Praat.INSTANCE.Sound_to_Pitch_any(this, dt, minimumPitch, periodsPerWindow, maxnCandidates, method, 
				silenceThreshold, voicingThreshold, octaveCost, octaveJumpCost, voicedUnvoicedCost, maximumPitch);
	}
	
	/*
		Which = 1: Burg.
		Which = 2: Split-Levinson
	*/
	public Formant to_Formant_any (double timeStep, int numberOfPoles, double maximumFrequency,
			double halfdt_window, int which, double preemphasisFrequency, double safetyMargin) {
		return Praat.INSTANCE.Sound_to_Formant_any(this, timeStep, numberOfPoles, maximumFrequency, halfdt_window, which, preemphasisFrequency, safetyMargin);
	}
	
	/* Throws away all formants below 50 Hz and above Nyquist minus 50 Hz. */
	public Formant to_Formant_burg (double timeStep, double maximumNumberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency) {
		return Praat.INSTANCE.Sound_to_Formant_burg(this, timeStep, maximumNumberOfFormants, maximumFormantFrequency, windowLength, preemphasisFrequency);
	}

	/* Same as previous, but keeps all formants. Good for resynthesis. */
	public Formant to_Formant_keepAll (double timeStep, double maximumNumberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency) {
		return Praat.INSTANCE.Sound_to_Formant_keepAll(this, timeStep, maximumNumberOfFormants, maximumFormantFrequency, windowLength, preemphasisFrequency);
	}

	public Formant to_Formant_willems (double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency) {
		return Praat.INSTANCE.Sound_to_Formant_willems(this, timeStep, numberOfFormants, maximumFormantFrequency, windowLength, preemphasisFrequency);
	}
	
	public Intensity to_Intensity (double minimumPitch, double timeStep, int subtractMean) {
		return Praat.INSTANCE.Sound_to_Intensity(this, minimumPitch, timeStep, subtractMean);
	}
	
}
