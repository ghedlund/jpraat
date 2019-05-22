package ca.hedlund.jpraat.binding.melder;

import ca.hedlund.jpraat.annotations.Header;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

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
