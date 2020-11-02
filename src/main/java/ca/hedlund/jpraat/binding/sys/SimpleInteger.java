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

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.NativeIntptr_t;
import ca.hedlund.jpraat.exceptions.PraatException;

public class SimpleInteger extends Daata {
	
	public SimpleInteger() {
		super();
	}
	
	public SimpleInteger(Pointer p) {
		super(p);
	}
	
	public static SimpleInteger create (long number) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			SimpleInteger retVal = Praat.INSTANCE.SimpleInteger_create_wrapped(new NativeIntptr_t(number));
			Praat.checkAndClearLastError();
			return retVal;
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public long getNumber() {
		return Praat.INSTANCE.SimpleInteger_getNumber(this).longValue();
	}
	
	public void setNumber(long number) {
		Praat.INSTANCE.SimpleInteger_setNumber (this, new NativeIntptr_t(number));
	}
	
}
