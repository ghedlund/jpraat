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

import ca.hedlund.jpraat.annotations.*;
import ca.hedlund.jpraat.binding.jna.*;

@Declared("fon/Pitch_enums.h")
public enum kPitch_unit implements NativeEnum {
	HERTZ("Hertz"),
	HERTZ_LOGARITHMIC("Hertz (logarithmic)"),
	MEL("mel"),
	LOG_HERTZ("logHertz"),
	SEMITONES_1("semitones re 1 Hz"),
	SEMITONES_100("semitones re 100 Hz"),
	SEMITONES_200("semitones re 200 Hz"),
	SEMITONES_440("semitones re 440 Hz"),
	ERB("ERB");
	
	private String name;
	
	private kPitch_unit(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	public int getNativeValue() {
		return ordinal();
	}
	
	public static kPitch_unit fromString(String val) {
		kPitch_unit retVal = null;
		
		for(kPitch_unit v:values()) {
			if(v.getName().equalsIgnoreCase(val)) {
				retVal = v;
				break;
			}
		}
		
		return retVal;
	}

}
