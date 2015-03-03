package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

/**
 * kSound_convolve_scaling
 */
@Declared("fon/Sound_enums.h")
public enum kSounds_convolve_scaling implements NativeEnum {
	INTEGRAL("integral"),
	SUM("sum"),
	NORMALIZE("normalize"),
	PEAK_099("peak 0.99");

	private String name;
	
	private kSounds_convolve_scaling(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNativeValue() {
		return ordinal()+1;
	}
}
