package ca.hedlund.jpraat.binding.stat;


import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.Daata;
import ca.hedlund.jpraat.exceptions.PraatException;

public class SimpleDouble extends Daata {
	
	public SimpleDouble() {
		super();
	}
	
	public SimpleDouble(Pointer p) {
		super(p);
	}
	
	public static SimpleDouble create (double number) throws PraatException {
		return Praat.INSTANCE.SimpleDouble_create_wrapped(number);
	}
	
	public double getNumber() {
		return Praat.INSTANCE.SimpleDouble_getNumber(this);
	}
	
	public void setNumber(double number) {
		Praat.INSTANCE.SimpleDouble_setNumber(this, number);
	}
	
}
