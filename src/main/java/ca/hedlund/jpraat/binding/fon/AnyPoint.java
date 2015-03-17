package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.stat.SimpleDouble;

public class AnyPoint extends SimpleDouble {
	
	public AnyPoint() {
		super();
	}	
	
	public AnyPoint(Pointer p) {
		super(p);
	}
	
	

}
