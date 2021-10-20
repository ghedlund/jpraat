package ca.hedlund.jpraat.binding.melder;

import com.sun.jna.Structure;

public class MelderFraction extends Structure {

	public double numerator = 0.0;

	public double denominator = 0.0;

	public MelderFraction() {
		super();
	}

	public static class ByReference extends MelderFraction implements Structure.ByReference {};

	public static class ByValue extends MelderFraction implements  Structure.ByValue {};

}
