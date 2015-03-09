package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.WString;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

public class TextPoint extends AnyPoint {
	
	public static TextPoint create(double time, String mark) throws PraatException {
		return create(time, new WString(mark));
	}
	
	public static TextPoint create(double time, WString mark) throws PraatException {
		TextPoint retVal = Praat.INSTANCE.TextPoint_create_wrapped(time, mark);
		Praat.checkLastError();
		return retVal;
	}
	
	public void setText(String text) throws PraatException {
		setText(new WString(text));
	}
	
	public void setText (WString text) throws PraatException {
		Praat.INSTANCE.TextPoint_setText_wrapped(this, text);
		Praat.checkLastError();
	}

}
