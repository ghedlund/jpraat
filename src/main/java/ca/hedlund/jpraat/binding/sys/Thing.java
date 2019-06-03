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

import java.lang.ref.WeakReference;
import java.lang.ref.Cleaner;
import java.lang.ref.Cleaner.Cleanable;
import java.lang.ref.PhantomReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.NativeIntptr_t;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Thing extends PointerType implements AutoCloseable {
	
	private final static Logger LOGGER = Logger.getLogger(Thing.class.getName());
	
	static final Cleaner cleaner = Cleaner.create();
	
	static class ThingCleaner implements Runnable {

		private Class<? extends Thing> thingClazz;
		
		private Pointer thingPtr;
		
		private ThingCleaner(Thing th) {
			this.thingClazz = th.getClass();
			this.thingPtr = th.getPointer();
		}
		
		public void run() {
			if(thingPtr != Pointer.NULL) {
				LOGGER.log(Level.FINE, "Forgetting " + thingClazz.getName() + " (" + thingPtr.toString() + ")" );
				PointerType pt;
				try {
					pt = thingClazz.newInstance();
					pt.setPointer(thingPtr);
					Praat.INSTANCE._Thing_forget_wrapped(pt);
				} catch (InstantiationException | IllegalAccessException e) {
					LOGGER.severe(e.getLocalizedMessage());;
				}
			}
		}
		
	}

	private Cleanable cleanable;
	
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
	public Object fromNative(Object nativeValue, FromNativeContext context) {
		// Always pass along null pointer values
        if (nativeValue == null) {
            return null;
        }
        try {
            Thing pt = getClass().newInstance();
            pt.setPointer((Pointer)nativeValue);
            
            pt.cleanable = cleaner.register(pt, new ThingCleaner(this.getClass().cast(pt)));
            return pt;
        }
        catch (InstantiationException e) {
            throw new IllegalArgumentException("Can't instantiate " + getClass());
        }
        catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Not allowed to instantiate " + getClass());
        }
	}

	@Override
	public void close() throws Exception {
		if(cleanable != null) {
			cleanable.clean();
		}
	}
	
}
