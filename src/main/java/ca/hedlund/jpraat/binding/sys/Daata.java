/*
 * Copyright (C) 2005-2020 Gregory Hedlund
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.hedlund.jpraat.binding.sys;

import java.lang.reflect.*;

import com.sun.jna.*;

import ca.hedlund.jpraat.annotations.*;
import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.exceptions.*;

@Declared("sys/Daata.h")
public class Daata extends Thing {
	
	public Daata() {
		super();
	}
	
	public Daata(Pointer p) {
		super(p);
	}
	
	public static <T extends Thing> T readFromTextFile (Class<T> type, MelderFile file) throws PraatException {
		Pointer p = Praat.INSTANCE.Data_readFromTextFile_wrapped(file);
		Praat.checkAndClearLastError();
		
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
		Praat.checkAndClearLastError();
		
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
		Praat.checkAndClearLastError();
		
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
	
	public boolean equal (Daata data2) {
		return Praat.INSTANCE.Data_equal(this, data2);
	}
	
	public boolean canWriteAsEncoding (int outputEncoding) {
		return Praat.INSTANCE.Data_canWriteAsEncoding(this, outputEncoding);
	}

	public boolean canWriteText () {
		return Praat.INSTANCE.Data_canReadText(this);
	}

	public MelderFile createTextFile (MelderFile file, boolean verbose) throws PraatException {
		MelderFile retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Data_createTextFile_wrapped(
					this, file, verbose);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public void writeText (MelderFile openFile) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Data_writeText_wrapped(this, openFile);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public void writeToTextFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Data_writeToTextFile_wrapped(this, file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void Data_writeToShortTextFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Data_writeToShortTextFile_wrapped(this, file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public boolean canWriteBinary () {
		return Praat.INSTANCE.Data_canWriteBinary(this);
	}

	public void writeToBinaryFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Data_writeToBinaryFile_wrapped(this, file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
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
