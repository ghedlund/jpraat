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
import ca.hedlund.jpraat.exceptions.PraatException;

public class Interpreter extends Thing {

	public Interpreter() {
	}
	
	public Interpreter(Pointer p) {
		super(p);
	}
	
	public static Interpreter create() throws PraatException {
		Interpreter retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Interpreter_create_wrapped(null, null);
			Praat.checkAndClearLastError();
		} catch (PraatException pe) {
			throw pe;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
}
