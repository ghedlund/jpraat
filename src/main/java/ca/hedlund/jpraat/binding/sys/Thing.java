package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.Praat;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.WString;

public class Thing extends PointerType {

	public static Object newFromClassName (WString className) {
		return Praat.INSTANCE.Thing_newFromClassName(className);
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
		Praat.INSTANCE.Thing_infoWithId(this, id);
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
	
}