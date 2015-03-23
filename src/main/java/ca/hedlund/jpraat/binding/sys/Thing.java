package ca.hedlund.jpraat.binding.sys;

import java.util.logging.Level;
import java.util.logging.Logger;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.WString;

public abstract class Thing extends PointerType {
	
	private final static Logger LOGGER = Logger.getLogger(Thing.class.getName());
	
	/*
	 * Should we 'forget' (delete object and all resources) the object
	 * when the JVM finalizes this object.  By defualt, this is 'true'.
	 * 
	 * An example of a case where this is not desiriable is when creating
	 * temporary java references to data in memory which needs to be retained.
	 */
	private boolean forgetOnFinalize = true;
	
	protected Thing() {
		super();
	}
	
	protected Thing(Pointer p) {
		super(p);
	}

	public static Object newFromClassName (WString className) throws PraatException {
		Object retVal = Praat.INSTANCE.Thing_newFromClassName_wrapped (className);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public boolean isForgetOnFinalize() {
		return this.forgetOnFinalize;
	}
	
	public void setForgetOnFinalize(boolean forgetOnFinalize) {
		this.forgetOnFinalize = forgetOnFinalize;
	}

	@Override
	public void finalize() {
		if(isForgetOnFinalize() && getPointer() != Pointer.NULL) {
			try {
				forget();
			} catch (PraatException e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	public void forget() throws PraatException {
		Praat.INSTANCE._Thing_forget_wrapped(this);
		Praat.checkAndClearLastError();
	}
	
	public WString className () {
		return Praat.INSTANCE.Thing_className(this);
	}
	
	public void info () {
		Praat.INSTANCE.Thing_info(this);
	}
	
	public void infoWithId (long id) {
		Praat.INSTANCE.Thing_infoWithId(this, new NativeLong(id));
	}
	
	
	/* Return a pointer to your internal name (which can be NULL). */
	public WString getName () {
		return Praat.INSTANCE.Thing_getName(this);
	}
	
	public void setName (String name) {
		setName(new WString(name));
	}
	
	public void setName (WString name) {
		Praat.INSTANCE.Thing_setName(this, name);
	}

	public void swap (Thing thee) {
		Praat.INSTANCE.Thing_swap(this, thee);
	}
	
	public ClassInfo getClassInfo() {
		Pointer p = Praat.getNativeLibrary().getGlobalVariableAddress("class" + getClass().getSimpleName());
		return new ClassInfo(p);
	}
	
}
