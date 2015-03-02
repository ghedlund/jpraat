package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.NativeLong;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;

/**
 * 
 */
public class Sound extends Vector {
	
	public static final int LEVEL_MONO = 0;
	public static final int LEVEL_LEFT = 1;
	public static final int LEVEL_RIGHT = 2;

	public static Sound readFromPath(String path) throws PraatException {
		return readFromSoundFile(MelderFile.fromPath(path));
	}
	
	public static Sound readFromSoundFile(MelderFile file) throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_readFromSoundFile_wrapped (file);
		Praat.checkLastError();
		return retVal;
	}
	
	public static Sound create(long numberOfChannels, double xmin, double xmax, long nx, double dx, double x1)
		throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_create_wrapped (new NativeLong(numberOfChannels), xmin, xmax, new NativeLong(nx), dx, x1);
		Praat.checkLastError();
		return retVal;
	}
	
	public static Sound createSimple(long numberOfChannels, double duration, double samplingFrequency) throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_createSimple_wrapped(new NativeLong(numberOfChannels), duration, samplingFrequency);
		Praat.checkLastError();
		return retVal;
	}
	
	public Sound convertToMono () throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_convertToMono_wrapped(this);
		Praat.checkLastError();
		return retVal;
	}

	public Sound convertToStereo () throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_convertToStereo_wrapped (this);
		Praat.checkLastError();
		return retVal;
	}
	
	public Sound  extractChannel (long ichannel) throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_extractChannel_wrapped (this, new NativeLong(ichannel));
		Praat.checkLastError();
		return retVal;
	}
	
	public Sound upsample () throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_upsample_wrapped(this);
		Praat.checkLastError();
		return retVal;
	}

	public Sound resample (double samplingFrequency, long precision) throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_resample_wrapped (this, samplingFrequency, new NativeLong(precision));
		Praat.checkLastError();
		return retVal;
	}
	
	public Sound autoCorrelate (kSounds_convolveScaling scaling, kSounds_convolveSignalOutsideTimeDomain signalOutsideTimeDomain)
		throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_autoCorrelate_wrapped (this, scaling, signalOutsideTimeDomain);
		Praat.checkLastError();
		return retVal;
	}

	public double getEnergyInAir () {
		return Praat.INSTANCE.Sound_getEnergyInAir(this);
	}
	
	public Sound extractPart (double t1, double t2, kSound_windowShape windowShape, double relativeWidth, boolean preserveTimes)
		throws PraatException {
		Sound retVal = Praat.INSTANCE.Sound_extractPart_wrapped (this, t1, t2, windowShape, relativeWidth, preserveTimes);
		Praat.checkLastError();
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
	public Spectrogram toSpectrogram (double effectiveAnalysisWidth, double fmax,
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
		
		Spectrogram retVal = Praat.INSTANCE.Sound_to_Spectrogram_wrapped(this, effectiveAnalysisWidth, fmax, minimumTimeStep1, minimumFreqStep1, 
				windowShape, maximumTimeOversampling, maximumFreqOversampling);
		Praat.checkLastError();
		return retVal;
	}
	
	public Pitch to_Pitch (double timeStep,
			double minimumPitch, double maximumPitch) throws PraatException {
		Pitch retVal = Praat.INSTANCE.Sound_to_Pitch_wrapped(this, timeStep, minimumPitch, maximumPitch);
		Praat.checkLastError();
		return retVal;
	}

	/* Calls Sound_to_Pitch_any with AC method. */
	public Pitch to_Pitch_ac (double timeStep, double minimumPitch,
		double periodsPerWindow, int maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch) throws PraatException {
		checkMaxnCandidates(maxnCandidates);
		checkMinPitchPeriodsPerWindow(minimumPitch, periodsPerWindow);
		
		Pitch retVal = Praat.INSTANCE.Sound_to_Pitch_ac_wrapped (this, timeStep, minimumPitch, periodsPerWindow, maxnCandidates, accurate, 
				silenceThreshold, voicingThreshold, octaveCost, octaveJumpCost, voicedUnvoicedCost, maximumPitch);
		Praat.checkLastError();
		return retVal;
	}

	/* Calls Sound_to_Pitch_any with FCC method. */
	public Pitch to_Pitch_cc (double timeStep, double minimumPitch,
		double periodsPerWindow, int maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch) throws PraatException {
		checkMaxnCandidates(maxnCandidates);
		checkMinPitchPeriodsPerWindow(minimumPitch, periodsPerWindow);
		Pitch retVal = Praat.INSTANCE.Sound_to_Pitch_cc_wrapped(this, timeStep, minimumPitch, periodsPerWindow, maxnCandidates, accurate,
				silenceThreshold, voicingThreshold, octaveCost, octaveJumpCost, voicedUnvoicedCost, maximumPitch);
		Praat.checkLastError();
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
		double maximumPitch)    throws PraatException  /* (Hz) */
	{
		checkMaxnCandidates(maxnCandidates);
		checkMinPitchPeriodsPerWindow(minimumPitch, periodsPerWindow);
		Pitch retVal = Praat.INSTANCE.Sound_to_Pitch_any_wrapped (this, dt, minimumPitch, periodsPerWindow, maxnCandidates, method, 
				silenceThreshold, voicingThreshold, octaveCost, octaveJumpCost, voicedUnvoicedCost, maximumPitch);
		Praat.checkLastError();
		return retVal;
	}
	
	private void checkMaxnCandidates(int maxnCandidates) {
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
	public Formant to_Formant_any (double timeStep, int numberOfPoles, double maximumFrequency,
			double halfdt_window, int which, double preemphasisFrequency, double safetyMargin) throws PraatException {
		checkFormantWindow(numberOfPoles, halfdt_window);
		Formant retVal =
				Praat.INSTANCE.Sound_to_Formant_any_wrapped(this, timeStep, numberOfPoles, maximumFrequency, halfdt_window, which, preemphasisFrequency, safetyMargin);
		Praat.checkLastError();
		return retVal;
	}
	
	/* Throws away all formants below 50 Hz and above Nyquist minus 50 Hz. */
	public Formant to_Formant_burg (double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency) 
		throws PraatException {
		checkFormantWindow((int)(2*numberOfFormants), windowLength);
		Formant retVal = 
				Praat.INSTANCE.Sound_to_Formant_burg_wrapped(this, timeStep, numberOfFormants, maximumFormantFrequency, windowLength, preemphasisFrequency);
		Praat.checkLastError();
		return retVal;
	}

	/* Same as previous, but keeps all formants. Good for resynthesis. */
	public Formant to_Formant_keepAll (double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency) 
		throws PraatException {
		checkFormantWindow((int)(2*numberOfFormants), windowLength);
		Formant retVal = Praat.INSTANCE.Sound_to_Formant_keepAll_wrapped(this, timeStep, numberOfFormants, maximumFormantFrequency, windowLength, preemphasisFrequency);
		Praat.checkLastError();
		return retVal;
	}

	public Formant to_Formant_willems (double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency)
		throws PraatException {
		checkFormantWindow((int)(2*numberOfFormants), windowLength);
		Formant retVal =
				Praat.INSTANCE.Sound_to_Formant_willems_wrapped(this, timeStep, numberOfFormants, maximumFormantFrequency, windowLength, preemphasisFrequency);
		Praat.checkLastError();
		return retVal;
	}
	
	private void checkFormantWindow(int numberOfPoles, double windowLength) {
		double dt_window = 2.0 * windowLength;
		long nsamp_window = (long)Math.floor(dt_window/getDx());
		
		if(nsamp_window < numberOfPoles + 1) {
			throw new IllegalArgumentException("Window too short.");
		}
	}
	
	public Intensity to_Intensity (double minimumPitch, double timeStep, int subtractMean) {

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
		
		return Praat.INSTANCE.Sound_to_Intensity(this, minimumPitch, timeStep, subtractMean);
	}
	
}
