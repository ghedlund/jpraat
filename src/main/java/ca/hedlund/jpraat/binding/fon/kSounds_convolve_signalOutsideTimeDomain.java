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

/**
 * kSound_convolve_signalOutsideTimeDomain
 */
@Declared("fon/Sound_enums.h")
public enum kSounds_convolve_signalOutsideTimeDomain implements NativeEnum {
	ZERO("zero"),
	SIMILAR("similar");

	private String name;
	
	private kSounds_convolve_signalOutsideTimeDomain(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNativeValue() {
		return ordinal()+1;
	}

}
