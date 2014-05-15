package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Header;
import ca.hedlund.jpraat.binding.sys.MelderFile;

/**
 * 
 */
@Header("fon/Sound.h")
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

	@Header("fon/Sound_to_Spectrogram.h")
	public Spectrogram toSpectrogram (double effectiveAnalysisWidth, double fmax,
			double minimumTimeStep1, double minimumFreqStep1, kSound_to_Spectrogram_windowShape windowShape,
			double maximumTimeOversampling, double maximumFreqOversampling) {
		return Praat.INSTANCE.Sound_to_Spectrogram(this, effectiveAnalysisWidth, fmax, minimumTimeStep1, minimumFreqStep1, 
				windowShape, maximumTimeOversampling, maximumFreqOversampling);
	}
	
}
