package ca.hedlund.jpraat.binding.sys;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Interpreter extends Thing {

	public Interpreter() {
	}
	
	public Interpreter(Pointer p) {
		super(p);
	}
	
	public static Interpreter create() throws PraatException {
		Interpreter retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Interpreter_create_wrapped(null, null);
			Praat.checkAndClearLastError();
		} catch (PraatException pe) {
			throw pe;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
}
