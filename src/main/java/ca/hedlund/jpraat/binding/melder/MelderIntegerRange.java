package ca.hedlund.jpraat.binding.melder;

import com.sun.jna.Structure;

public class MelderIntegerRange extends Structure {

	public int first, last = 0;

	public class ByValue extends MelderIntegerRange implements Structure.ByValue {};

	public class ByReference extends MelderIntegerRange implements Structure.ByReference {};

}
