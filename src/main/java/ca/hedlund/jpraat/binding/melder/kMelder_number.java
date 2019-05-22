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
