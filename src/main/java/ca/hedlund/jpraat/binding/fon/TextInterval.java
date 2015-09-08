package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.exceptions.PraatException;

public class TextInterval extends Function {
	
	public TextInterval() {
		super();
	}
	
	public TextInterval(Pointer p) {
		super(p);
	}

	public static TextInterval create (double tmin, double tmax, String text) throws PraatException {
		return create(tmin, tmax, new Str32(text));
	}
	
	public static TextInterval create (double tmin, double tmax, Str32 text) throws PraatException {
		TextInterval retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextInterval_create_wrapped(
					tmin, tmax, text);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void setText(String text) throws PraatException {
		setText(new Str32(text));
	}

	public void setText(Str32 text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextInterval_setText_wrapped(this, text);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public String getText() {
		return (getTextW() != null ? getTextW().toString() : "");
	}
	
	public Str32 getTextW() {
		return Praat.INSTANCE.TextInterval_getText(this);
	}
	
	public long labelLength () {
		return Praat.INSTANCE.TextInterval_labelLength(this).longValue();
	}

	public void removeText () {
		Praat.INSTANCE.TextInterval_removeText(this);
	}
	
	@Override
	public String toString() {
		return getText();
	}

}
