package ca.hedlund.jpraat.binding.sys;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.exceptions.PraatException;

public class SimpleString extends Daata {
	
	public SimpleString() {
		super();
	}
	
	public SimpleString(Pointer p) {
		super(p);
	}
	
	public static SimpleString create (String str) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			SimpleString retVal = Praat.INSTANCE.SimpleString_create_wrapped(new Str32(str));
			Praat.checkAndClearLastError();
			return retVal;
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public String getString() {
		return Praat.INSTANCE.SimpleString_getString(this).toString();
	}
	
	public void setString(String str) {
		Praat.INSTANCE.SimpleString_setString(this, new Str32(str));
	}

}
