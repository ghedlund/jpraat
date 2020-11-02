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
package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

@Declared("fon/Formant_enums.h")
public enum kFormant_unit implements NativeEnum {
	HERTZ("hertz"),
	BARK("bark");

	String name;
	
	private kFormant_unit(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public int getNativeValue() {
		return ordinal();
	}

	public static kFormant_unit fromString(String val) {
		kFormant_unit retVal = null;
		
		for(kFormant_unit v:values()) {
			if(v.getName().equalsIgnoreCase(val)) {
				retVal = v;
				break;
			}
		}
		
		return retVal;
	}
	
}
