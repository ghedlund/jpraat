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
package ca.hedlund.jpraat.binding.melder;

import ca.hedlund.jpraat.annotations.Header;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

@Header("melder/melder_enums.h")
public enum kMelder_number implements NativeEnum {
	EQUAL_TO("equal to"),
    NOT_EQUAL_TO("not equal to"),
    LESS_THAN("less than"),
    LESS_THAN_OR_EQUAL_TO("less than or equal to"),
    GREATER_THAN("greater than"),
    GREATER_THAN_OR_EQUAL_TO("greater than or equal to");
	
	private String text;
	
	private kMelder_number(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

	@Override
	public int getNativeValue() {
		return ordinal()+1;
	}
	
}
