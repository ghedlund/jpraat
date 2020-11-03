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

import ca.hedlund.jpraat.annotations.*;
import ca.hedlund.jpraat.binding.jna.*;

@Header("melder/melder_enums.h")
public enum kMelder_string implements NativeEnum {
	EQUAL_TO("is equal to"),
    NOT_EQUAL_TO("is not equal to"),
    CONTAINS("contains"),
    DOES_NOT_CONTAIN("does not contain"),
    STARTS_WITH("starts with"),
    DOES_NOT_START_WITH("does not start with"),
    ENDS_WITH("ends with"),
    DOES_NOT_END_WITH("does not end with"),
    CONTAINS_WORD("contains a word equal to"),
    DOES_NOT_CONTAIN_WORD("does not contain a word equal to"),
    CONTAINS_WORD_STARTING_WITH("contains a word starting with"),
    DOES_NOT_CONTAIN_WORD_STARTING_WITH("does not contain a word starting with"),
    CONTAINS_WORD_ENDING_WITH("contains a word ending with"),
    DOES_NOT_CONTAIN_WORD_ENDING_WITH("does not contain a word ending with"),
    CONTAINS_INK("contains ink equal to"),
    DOES_NOT_CONTAIN_INK("does not contain ink equal to"),
    CONTAINS_INK_STARTING_WITH("contains ink starting with"),
    DOES_NOT_CONTAIN_INK_STARTING_WITH("does not contain ink starting with"),
    CONTAINS_INK_ENDING_WITH("contains ink ending with"),
    DOES_NOT_CONTAIN_INK_ENDING_WITH("does not contain ink ending with"),
    MATCH_REGEXP("matches (regex)");

	private String text;
	
	private kMelder_string(String text) {
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
