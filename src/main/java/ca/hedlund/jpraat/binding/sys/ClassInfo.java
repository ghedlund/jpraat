package ca.hedlund.jpraat.binding.sys;


import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.WString;

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
