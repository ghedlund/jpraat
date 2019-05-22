package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

@Declared("fon/Formant_enums.h")
public enum kFormant_unit implements NativeEnum {
	HERTZ("hertz"),
	BARK("bark");

	String name;
	
	private kFormant_unit(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public int getNativeValue() {
		return ordinal();
	}

	public static kFormant_unit fromString(String val) {
		kFormant_unit retVal = null;
		
		for(kFormant_unit v:values()) {
			if(v.getName().equalsIgnoreCase(val)) {
				retVal = v;
				break;
			}
		}
		
		return retVal;
	}
	
}
