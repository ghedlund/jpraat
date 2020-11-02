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
		final MelderFile retVal = Praat.INSTANCE.MelderFile_new();
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Melder_pathToFile_wrapped(new Str32(path), retVal);
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
	
	public String path() {
		Str32 txt32 = Praat.INSTANCE.Melder_fileToPath(this);
		return (txt32 == null ? null : txt32.toString());
	}
	
}
