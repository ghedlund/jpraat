package ca.hedlund.jpraat.binding.sys;


import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

/**
 * Holder class for class info objects needed for some functions.
 */
public class ClassInfo extends PointerType {
	
	public ClassInfo() {
		super();
	}
	
	public ClassInfo(Pointer p) {
		super(p);
	}
	

}
