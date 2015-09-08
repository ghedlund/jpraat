package ca.hedlund.jpraat.binding.sys;

import com.sun.jna.PointerType;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.exceptions.PraatException;

@Declared("sys/melder.h")
public class MelderFile extends PointerType {
	
	/**
	 * Create a new MelderFile from the given path
	 * 
	 * @param path
	 * @return file
	 */
	public static MelderFile fromPath(String path) throws PraatException {
		return fromPath(new Str32(path));
	}
	
	public static MelderFile fromPath(Str32 path) throws PraatException {
		final MelderFile retVal = Praat.INSTANCE.MelderFile_new();
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Melder_pathToFile_wrapped(path, retVal);
		Praat.checkAndClearLastError(); 
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public MelderFile open() throws PraatException {
		MelderFile retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.MelderFile_open_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public MelderFile create() throws PraatException {
		MelderFile retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.MelderFile_create_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public long length () {
		return Praat.INSTANCE.MelderFile_length(this).longValue();
	}
	
	public Str32 path() {
		return Praat.INSTANCE.Melder_fileToPath(this);
	}
	
}
