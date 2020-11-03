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
package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.exceptions.*;

public class TextPoint extends AnyPoint {
	
	public TextPoint() {
		super();
	}
	
	public TextPoint(Pointer p) {
		super(p);
	}
	
	public static TextPoint create(double time, String mark) throws PraatException {
		TextPoint retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextPoint_create_wrapped(time,
					new Str32(mark));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void setText(String text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextPoint_setText_wrapped(this, new Str32(text));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public String getText() {
		Str32 txt32 = Praat.INSTANCE.TextPoint_getText(this);
		return (txt32 == null ? null : txt32.toString());
	}
	
	public long labelLength () {
		return Praat.INSTANCE.TextPoint_labelLength(this).longValue();
	}

	public void removeText () {
		Praat.INSTANCE.TextPoint_removeText(this);
	}

	@Override
	public String toString() {
		return getText();
	}

}
