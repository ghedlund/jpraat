package ca.hedlund.jpraat.binding.stat;


import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.Data;

public class SimpleDouble extends Data {
	
	public SimpleDouble() {
		super();
	}
	
	public SimpleDouble(Pointer p) {
		super(p);
	}
	
	public static SimpleDouble create (double number) {
		return Praat.INSTANCE.SimpleDouble_create(number);
	}
	
	public double getNumber() {
		return Praat.INSTANCE.SimpleDouble_getNumber(this);
	}
	
	public void setNumber(double number) {
		Praat.INSTANCE.SimpleDouble_setNumber(this, number);
	}
	
}
