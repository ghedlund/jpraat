package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.jna.Declared;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

@Declared("fon/Sound_to_Spectrogram_enums.h")
public enum kSound_to_Spectrogram_windowShape implements NativeEnum {
	SQUARE("square (rectangular)"),
	HAMMING("Hamming (raised sine-squared)"),
	BARTLETT("Bartlett (triangular)"),
	WELCH("Welch (parabolic)"),
	HANNING("Hanning (sine-squared)"),
	GAUSSIAN("Gaussian");

	private String name;
	
	private kSound_to_Spectrogram_windowShape(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNativeValue() {
		return ordinal();
	}

}
