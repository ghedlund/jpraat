package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.PointerType;
import com.sun.jna.WString;

@Declared("sys/melder.h")
public class MelderFile extends PointerType {
	
	/**
	 * Create a new MelderFile from the given path
	 * 
	 * @param path
	 * @return file
	 */
	public static MelderFile fromPath(String path) throws PraatException {
		return fromPath(new WString(path));
	}
	
	public static MelderFile fromPath(WString path) throws PraatException {
		final MelderFile retVal = Praat.INSTANCE.MelderFile_new();
		Praat.INSTANCE.Melder_pathToFile_wrapped(path, retVal);
		Praat.checkLastError(); 
		return retVal;
	}
	
	public MelderFile open() throws PraatException {
		MelderFile retVal = Praat.INSTANCE.MelderFile_open_wrapped (this);
		Praat.checkLastError();
		return retVal;
	}
	
	public MelderFile create() throws PraatException {
		MelderFile retVal = Praat.INSTANCE.MelderFile_create_wrapped(this);
		Praat.checkLastError();
		return retVal;
	}
	
	public long length () {
		return Praat.INSTANCE.MelderFile_length(this);
	}
	
	public WString path() {
		return Praat.INSTANCE.Melder_fileToPath(this);
	}
	
}
