package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Header;
import ca.hedlund.jpraat.binding.sys.MelderFile;

@Header("fon/LongSound.h")
public class LongSound extends Sampled {
	
	public static LongSound open (MelderFile fs) {
		return Praat.INSTANCE.LongSound_open(fs);
	}

	public Sound extractPart (double tmin, double tmax, int preserveTimes) {
		return Praat.INSTANCE.LongSound_extractPart(this, tmin, tmax, preserveTimes);
	}

}
