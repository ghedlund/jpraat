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
import ca.hedlund.jpraat.exceptions.*;

public class SimpleDouble extends Daata {
	
	public SimpleDouble() {
		super();
	}
	
	public SimpleDouble(Pointer p) {
		super(p);
	}
	
	public static SimpleDouble create (double number) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			SimpleDouble retVal = Praat.INSTANCE.SimpleDouble_create_wrapped(number);
			Praat.checkAndClearLastError();
			return retVal;
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public double getNumber() {
		return Praat.INSTANCE.SimpleDouble_getNumber(this);
	}
	
	public void setNumber(double number) {
		Praat.INSTANCE.SimpleDouble_setNumber(this, number);
	}
	
}
