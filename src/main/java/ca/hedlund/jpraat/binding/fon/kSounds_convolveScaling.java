package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.jna.Header;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

/**
 * kSound_convolve_scaling
 */
@Header("fon/Sound_enums.h")
public enum kSounds_convolveScaling implements NativeEnum {
	INTEGRAL("integral"),
	SUM("sum"),
	NORMALIZE("normalize"),
	PEAK_099("peak 0.99");

	private String name;
	
	private kSounds_convolveScaling(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNativeValue() {
		return ordinal()+1;
	}

}
