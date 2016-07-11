package ca.hedlund.jpraat.binding.sys;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Strings extends Daata {
	
	public Strings(Pointer p) {
		super(p);
	}
	
	public static Strings createAsFileList (String path) throws PraatException {
		Strings retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Strings_createAsFileList_wrapped(new Str32(path));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Strings createAsDirectoryList (String path) throws PraatException {
		Strings retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Strings_createAsDirectoryList_wrapped(new Str32(path));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Strings readFromRawTextFile (MelderFile file) throws PraatException {
		Strings retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Strings_readFromRawTextFile_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public void writeToRawTextFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Strings_writeToRawTextFile_wrapped(this, file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
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
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Strings_remove_wrapped(this,
					new NativeLong(position));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void replace (long position, String text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Strings_replace_wrapped(this, new NativeLong(
					position), new Str32(text));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insert (long position, String text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Strings_insert_wrapped(this,
					new NativeLong(position), new Str32(text));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

}
