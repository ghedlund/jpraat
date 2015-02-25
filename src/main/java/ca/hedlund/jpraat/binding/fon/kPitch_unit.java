package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.jna.NativeEnum;

@Declared("fon/Pitch_enums.h")
public enum kPitch_unit implements NativeEnum {
	HERTZ("Hertz"),
	HERTZ_LOGARITHMIC("Hertz (logarithmic)"),
	MEL("mel"),
	LOG_HERTZ("logHertz"),
	SEMITONES_1("semitones re 1 Hz"),
	SEMITONES_100("semitones re 100 Hz"),
	SEMITONES_200("semitones re 200 Hz"),
	SEMITONES_440("semitones re 440 Hz"),
	ERB("ERB");
	
	private String name;
	
	private kPitch_unit(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	public int getNativeValue() {
		return ordinal();
	}
	
	public static kPitch_unit fromString(String val) {
		kPitch_unit retVal = null;
		
		for(kPitch_unit v:values()) {
			if(v.getName().equalsIgnoreCase(val)) {
				retVal = v;
				break;
			}
		}
		
		return retVal;
	}

}
