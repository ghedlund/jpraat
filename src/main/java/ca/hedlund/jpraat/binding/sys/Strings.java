package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.NativeLong;
import com.sun.jna.WString;

public class Strings extends Data {
	
	public static Strings createAsFileList (WString path) throws PraatException {
		Strings retVal = Praat.INSTANCE.Strings_createAsFileList_wrapped(path);
		Praat.checkLastError();
		return retVal;
	}
	
	public static Strings createAsDirectoryList (WString path) throws PraatException {
		Strings retVal = Praat.INSTANCE.Strings_createAsDirectoryList_wrapped(path);
		Praat.checkLastError();
		return retVal;
	}
	
	public static Strings readFromRawTextFile (MelderFile file) throws PraatException {
		Strings retVal = Praat.INSTANCE.Strings_readFromRawTextFile_wrapped(file);
		Praat.checkLastError();
		return retVal;
	}

	public void writeToRawTextFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.Strings_writeToRawTextFile_wrapped(this, file);
		Praat.checkLastError();
	}

	public void randomize () {
		Praat.INSTANCE.Strings_randomize(this);
	}

	public void genericize () {
		Praat.INSTANCE.Strings_genericize(this);
	}

	public void nativize () {
		Praat.INSTANCE.Strings_nativize(this);
	}

	public void sort () {
		Praat.INSTANCE.Strings_sort(this);
	}
	
	public void remove (long position) throws PraatException {
		Praat.INSTANCE.Strings_remove_wrapped(this, new NativeLong(position));
		Praat.checkLastError();
	}
	
	public void replace (long position, WString text) throws PraatException {
		Praat.INSTANCE.Strings_replace_wrapped(this, new NativeLong(position), text);
		Praat.checkLastError();
	}
	
	public void insert (long position, WString text) throws PraatException {
		Praat.INSTANCE.Strings_insert_wrapped(this, new NativeLong(position), text);
		Praat.checkLastError();
	}

}
