package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.Praat;

/**
 * Utility class for obtaining the praat prefs folder.
 */
public class PraatDir {
	
	public static String getPath() {
		return Praat.INSTANCE.praat_dir().toString();
	}

}
