package ca.hedlund.jpraat.binding.sys;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

@Declared("sys/Data.h")
public class Data extends Thing {
	
	public Data() {
		super();
	}
	
	public Data(Pointer p) {
		super(p);
	}
	
	public static <T extends Thing> T readFromTextFile (Class<T> type, MelderFile file) throws PraatException {
		Pointer p = Praat.INSTANCE.Data_readFromTextFile_wrapped(file);
		Praat.checkLastError();
		
		T retVal = null;
		try {
			final Constructor<T> constructor = type.getConstructor(Pointer.class);
			retVal = constructor.newInstance(p);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | 
					IllegalArgumentException | InvocationTargetException e) {
			throw new PraatException(e);
		}
	
		return retVal;
	}

	public static <T extends Thing> T readFromBinaryFile (Class<T> type, MelderFile file) throws PraatException {
		Pointer p = Praat.INSTANCE.Data_readFromBinaryFile_wrapped(file);
		Praat.checkLastError();
		
		T retVal = null;
		try {
			final Constructor<T> constructor = type.getConstructor(Pointer.class);
			retVal = constructor.newInstance(p);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | 
					IllegalArgumentException | InvocationTargetException e) {
			throw new PraatException(e);
		}
	
		return retVal;
	}

	public static <T extends Thing> T readFromFile (Class<T> type, MelderFile file) throws PraatException {
		Pointer p = Praat.INSTANCE.Data_readFromFile_wrapped (file);
		Praat.checkLastError();
		
		T retVal = null;
		try {
			final Constructor<T> constructor = type.getConstructor(Pointer.class);
			retVal = constructor.newInstance(p);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | 
					IllegalArgumentException | InvocationTargetException e) {
			throw new PraatException(e);
		}
	
		return retVal;
	}
	
	public boolean equal (Data data2) {
		return Praat.INSTANCE.Data_equal(this, data2);
	}
	
	public boolean canWriteAsEncoding (int outputEncoding) {
		return Praat.INSTANCE.Data_canWriteAsEncoding(this, outputEncoding);
	}

	public boolean canWriteText () {
		return Praat.INSTANCE.Data_canReadText(this);
	}

	public MelderFile createTextFile (MelderFile file, boolean verbose) throws PraatException {
		MelderFile retVal = Praat.INSTANCE.Data_createTextFile_wrapped(this, file, verbose);
		Praat.checkLastError();
		return retVal;
	}

	public void writeText (MelderFile openFile) throws PraatException {
		Praat.INSTANCE.Data_writeText_wrapped(this, openFile);
		Praat.checkLastError();
	}

	public void writeToTextFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.Data_writeToTextFile_wrapped (this, file);
		Praat.checkLastError();
	}
	
	public void Data_writeToShortTextFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.Data_writeToShortTextFile_wrapped(this, file);
		Praat.checkLastError();
	}

	public boolean canWriteBinary () {
		return Praat.INSTANCE.Data_canWriteBinary(this);
	}

	public void writeToBinaryFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.Data_writeToBinaryFile_wrapped (this, file);
		Praat.checkLastError();
	}

	public boolean canWriteLisp () {
		return Praat.INSTANCE.Data_canWriteLisp(this);
	}

	public void writeLispToConsole () {
		Praat.INSTANCE.Data_writeLispToConsole(this);
	}
	
	public boolean canReadText () {
		return Praat.INSTANCE.Data_canReadText(this);
	}

	public boolean canReadBinary () {
		return Praat.INSTANCE.Data_canReadBinary(this);
	}
	
}
