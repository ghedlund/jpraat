/*
 * Copyright (C) 2012-2018 Gregory Hedlund
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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Thing extends PointerType {
	
	private final static Logger LOGGER = Logger.getLogger(Thing.class.getName());
	
	/*
	 * Should we 'forget' (delete object and all resources) the object
	 * when the JVM finalizes this object.  By defualt, this is 'true'.
	 * 
	 * An example of a case where this is not desiriable is when creating
	 * temporary java references to data in memory which needs to be retained.
	 */
	private boolean forgetOnFinalize = true;
	
	public Thing() {
		super();
	}
	
	public Thing(Pointer p) {
		super(p);
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
	
	public boolean isForgetOnFinalize() {
		return this.forgetOnFinalize;
	}
	
	public void setForgetOnFinalize(boolean forgetOnFinalize) {
		this.forgetOnFinalize = forgetOnFinalize;
	}

	@Override
	public void finalize() {
		if(isForgetOnFinalize() && getPointer() != Pointer.NULL) {
			try {
//				LOGGER.log(Level.INFO, "Forgetting object of type " + getClass().getSimpleName() + " with native pointer " + 
//						Long.toHexString(Pointer.nativeValue(getPointer())));
				forget();
			} catch (PraatException e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
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
	
	public void infoWithId (long id) {
		Praat.INSTANCE.Thing_infoWithId(this, new NativeLong(id));
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
	
}
