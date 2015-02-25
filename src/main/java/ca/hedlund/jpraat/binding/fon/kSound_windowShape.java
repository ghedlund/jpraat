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
