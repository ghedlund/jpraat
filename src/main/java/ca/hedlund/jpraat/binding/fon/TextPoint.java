package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.exceptions.PraatException;

public class TextPoint extends AnyPoint {
	
	public TextPoint() {
		super();
	}
	
	public TextPoint(Pointer p) {
		super(p);
	}
	
	public static TextPoint create(double time, String mark) throws PraatException {
		TextPoint retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextPoint_create_wrapped(time,
					new Str32(mark));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void setText(String text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextPoint_setText_wrapped(this, new Str32(text));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public String getText() {
		Str32 txt32 = Praat.INSTANCE.TextPoint_getText(this);
		return (txt32 == null ? null : txt32.toString());
	}
	
	public long labelLength () {
		return Praat.INSTANCE.TextPoint_labelLength(this).longValue();
	}

	public void removeText () {
		Praat.INSTANCE.TextPoint_removeText(this);
	}

	@Override
	public String toString() {
		return getText();
	}

}
