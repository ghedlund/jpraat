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

import ca.hedlund.jpraat.annotations.*;
import ca.hedlund.jpraat.binding.*;

public class SendPraat {

	@Declared("sys/sendpraat.c")
	public static String sendpraat (Object display, String programName, long timeOut,  String text) {
		return Praat.INSTANCE.sendpraat(display, programName, new NativeLong(timeOut), text);
	}
	
	@Declared("sys/sendpraat.c")
	public static WString sendpraatW (Object display, String programName, long timeOut, WString text) {
		return Praat.INSTANCE.sendpraatW(display, programName, new NativeLong(timeOut), text);
	}
	
}
