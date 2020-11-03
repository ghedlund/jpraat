/*
 * Copyright (C) 2012-2020 Gregory Hedlund
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

import com.sun.jna.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.exceptions.*;

/**
 * 
 */
public class Thing extends PointerType implements AutoCloseable {
	
	public Thing() {
		super();
	}
	
	public Thing(Pointer p) {
		super(p);
	}
	
	@Override
	public void setPointer(Pointer p) {
		super.setPointer(p);
	}

	public static Object newFromClassName (String className) throws PraatException {
		Object retVal = null;
		try {
			final Pointer formatVersion = new Memory(Native.getNativeSize(Integer.TYPE));
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Thing_newFromClassName_wrapped(new Str32(className), formatVersion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}

	public void forget() throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE._Thing_forget_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public String className () {
		return Praat.INSTANCE.Thing_className(this).toString();
	}
	
	public void info () {
		Praat.INSTANCE.Thing_info(this);
	}
	
	public void infoWithIdAndFile (long id, MelderFile file) {
		Praat.INSTANCE.Thing_infoWithIdAndFile(this, new NativeIntptr_t(id), file);
	}
	
	public String getName() {
		return Praat.INSTANCE.Thing_getName(this).toString();
	}
	
	public void setName(String name) {
		Praat.INSTANCE.Thing_setName(this, (name == null ? null : new Str32(name)));
	}
	
	public void swap (Thing thee) {
		Praat.INSTANCE.Thing_swap(this, thee);
	}
	
	public ClassInfo getClassInfo() {
		Pointer p = Praat.getNativeLibrary().getGlobalVariableAddress("class" + getClass().getSimpleName());
		return new ClassInfo(p);
	}
	
	@Override
	public void close() throws Exception {
		if(this.getPointer() != Pointer.NULL) {
			this.forget();
			this.setPointer(Pointer.NULL);
		}
	}
	
}
