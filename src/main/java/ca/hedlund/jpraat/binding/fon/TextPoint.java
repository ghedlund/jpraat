package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

public class TextPoint extends AnyPoint {
	
	public TextPoint() {
		super();
	}
	
	public TextPoint(Pointer p) {
		super(p);
	}
	
	public static TextPoint create(double time, String mark) throws PraatException {
		return create(time, new WString(mark));
	}
	
	public static TextPoint create(double time, WString mark) throws PraatException {
		TextPoint retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextPoint_create_wrapped(time,
					mark);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void setText(String text) throws PraatException {
		setText(new WString(text));
	}
	
	public void setText (WString text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextPoint_setText_wrapped(this, text);
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
	
	public WString getTextW() {
		return Praat.INSTANCE.TextPoint_getText(this);
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
