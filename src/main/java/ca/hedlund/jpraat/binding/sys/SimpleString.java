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

import com.sun.jna.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.exceptions.*;

public class SimpleString extends Daata {
	
	public SimpleString() {
		super();
	}
	
	public SimpleString(Pointer p) {
		super(p);
	}
	
	public static SimpleString create (String str) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			SimpleString retVal = Praat.INSTANCE.SimpleString_create_wrapped(new Str32(str));
			Praat.checkAndClearLastError();
			return retVal;
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public String getString() {
		return Praat.INSTANCE.SimpleString_getString(this).toString();
	}
	
	public void setString(String str) {
		Praat.INSTANCE.SimpleString_setString(this, new Str32(str));
	}

}
