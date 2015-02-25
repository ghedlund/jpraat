package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

@Declared("fon/Sound_to_Spectrogram_enums.h")
public enum kSound_to_Spectrogram_method implements NativeEnum {
	FOURIER("Fourier");
	
	private String name;
	
	private kSound_to_Spectrogram_method(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public int getNativeValue() {
		return ordinal()+1;
	}

}
