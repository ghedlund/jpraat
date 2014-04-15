package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Header;

import com.sun.jna.PointerType;
import com.sun.jna.WString;

@Header("sys/melder.h")
public class MelderFile extends PointerType {
	
	/**
	 * Create a new MelderFile from the given path
	 * 
	 * @param path
	 * @return file
	 */
	public static MelderFile fromPath(String path) {
		return fromPath(new WString(path));
	}
	
	public static MelderFile fromPath(WString path) {
		final MelderFile retVal = Praat.INSTANCE.MelderFile_new();
		Praat.INSTANCE.Melder_pathToFile(path, retVal);
		return retVal;
	}
	
	public MelderFile open() {
		return Praat.INSTANCE.MelderFile_open(this);
	}
	
	public long length () {
		return Praat.INSTANCE.MelderFile_length(this);
	}
	
	public WString path() {
		return Praat.INSTANCE.Melder_fileToPath(this);
	}
	
}
