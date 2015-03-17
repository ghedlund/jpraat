package ca.hedlund.jpraat.binding.sys;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Collection extends Data {
	
	public Collection() {
		super();
	}
	
	public Collection(Pointer p) {
		super(p);
	}
	
	public static Collection create (ClassInfo itemClass, long initialCapacity) {
		return Praat.INSTANCE.Collection_create(itemClass, new NativeLong(initialCapacity));
	}

	public void addItem (Thing item) throws PraatException {
		Praat.INSTANCE.Collection_addItem_wrapped(this, item);
		Praat.checkLastError();
	}

}
