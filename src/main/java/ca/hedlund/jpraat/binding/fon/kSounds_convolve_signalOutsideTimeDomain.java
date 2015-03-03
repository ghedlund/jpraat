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
