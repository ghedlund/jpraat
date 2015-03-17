package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

public class TextInterval extends Function {
	
	public TextInterval() {
		super();
	}
	
	public TextInterval(Pointer p) {
		super(p);
	}
	
	public static TextInterval create (double tmin, double tmax, String text) throws PraatException {
		return create(tmin, tmax, new WString(text));
	}
	
	public static TextInterval create (double tmin, double tmax, WString text) throws PraatException {
		TextInterval retVal = Praat.INSTANCE.TextInterval_create_wrapped(tmin, tmax, text);
		Praat.checkLastError();
		return retVal;
	}
	
	public void setText(String text) throws PraatException {
		setText(new WString(text));
	}

	public void setText(WString text) throws PraatException {
		Praat.INSTANCE.TextInterval_setText_wrapped(this, text);
		Praat.checkLastError();
	}
	
	public long labelLength () {
		return Praat.INSTANCE.TextInterval_labelLength(this).longValue();
	}

	public void removeText () {
		Praat.INSTANCE.TextInterval_removeText(this);
	}

}
