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
package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

/**
 * kSound_windowShape
 */
@Declared("fon/Sound_enums.h")
public enum kSound_windowShape implements NativeEnum {
	RECTANGULAR("rectangular"),
	TRIANGULAR("triangular"),
	PARABOLIC("parabolic"),
	HANNING("Hanning"),
	HAMMING("Hamming"),
	GAUSSIAN_1("Gaussian1"),
	GAUSSIAN_2("Gaussian2"),
	GAUSSIAN_3("Gaussian3"),
	GAUSSIAN_4("Gaussian4"),
	GAUSSIAN_5("Gaussian5"),
	KAISER_1("Kaiser1"),
	KAISER_2("Kaiser2");
	
	private String  name;
	
	private kSound_windowShape(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public int getNativeValue() {
		return ordinal();
	}

}
