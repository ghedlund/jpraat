package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Header;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

@Header("sys/Data.h")
public class Data extends PointerType {
	
	public static Data readFromTextFile (MelderFile file) {
		return Praat.INSTANCE.Data_readFromTextFile(file);
	}

	public static Data readFromBinaryFile (MelderFile file) {
		return Praat.INSTANCE.Data_readFromBinaryFile(file);
	}

	public static Data readFromFile (MelderFile file) {
		return Praat.INSTANCE.Data_readFromFile(file);
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

	public MelderFile createTextFile (MelderFile file, boolean verbose) {
		return Praat.INSTANCE.Data_createTextFile(this, file, verbose);
	}

	public void writeText (MelderFile openFile) {
		Praat.INSTANCE.Data_writeText(this, openFile);
	}

	public void writeToTextFile (MelderFile file) {
		Praat.INSTANCE.Data_writeToTextFile(this, file);
	}
	
	public void Data_writeToShortTextFile (MelderFile file) {
		Praat.INSTANCE.Data_writeToShortTextFile(this, file);
	}

	public boolean canWriteBinary () {
		return Praat.INSTANCE.Data_canWriteBinary(this);
	}

	public void writeToBinaryFile (MelderFile file) {
		Praat.INSTANCE.Data_writeToBinaryFile(this, file);
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
