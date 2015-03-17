package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.WString;

public abstract class Thing extends PointerType {
	
	protected Thing() {
		super();
	}
	
	protected Thing(Pointer p) {
		super(p);
	}

	public static Object newFromClassName (WString className) throws PraatException {
		Object retVal = Praat.INSTANCE.Thing_newFromClassName_wrapped (className);
		Praat.checkLastError();
		return retVal;
	}

	@Override
	public void finalize() {
		if(getPointer() != Pointer.NULL) {
			forget();
		}
	}
	
	public void forget() {
		Praat.INSTANCE._Thing_forget(this);
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
