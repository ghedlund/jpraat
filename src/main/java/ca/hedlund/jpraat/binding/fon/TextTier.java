package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.WString;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

public class TextTier extends Function {

	public static TextTier create (double tmin, double tmax) throws PraatException {
		TextTier retVal = Praat.INSTANCE.TextTier_create_wrapped(tmin, tmax);
		Praat.checkLastError();
		return retVal;
	}
	
	public void addPoint (double time, String mark) throws PraatException {
		addPoint(time, new WString(mark));
	}
	
	public void addPoint (double time, WString mark) throws PraatException {
		Praat.INSTANCE.TextTier_addPoint_wrapped(this, time, mark);
		Praat.checkLastError();
	}
	
}
