package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

@Declared("fon/Vector_enums.h")
public enum kVector_peakInterpolation implements NativeEnum {
	NONE(0, "none"),
	PARABOLIC(1, "parabolic"),
	CUBIC(2, "cubic"),
	SINC70(3, "sinc70"),
	SINC700(4, "sinc700");

	private int nativeValue;

	private String name;

	private kVector_peakInterpolation(int value, String name) {
		this.nativeValue = value;
		this.name = name;
	}

	@Override
	public int getNativeValue() {
		return this.nativeValue;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
