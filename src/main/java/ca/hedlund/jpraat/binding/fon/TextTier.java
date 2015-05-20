package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.MelderQuantity;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

public class TextTier extends Function {
	
	public TextTier() {
		super();
	}

	public TextTier(Pointer p) {
		super(p);
	}
	
	public static TextTier create (double tmin, double tmax) throws PraatException {
		TextTier retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextTier_create_wrapped(tmin, tmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void addPoint (double time, String mark) throws PraatException {
		addPoint(time, new WString(mark));
	}
	
	public void addPoint (double time, WString mark) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextTier_addPoint_wrapped(this, time, mark);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public static TextTier readFromXwaves (MelderFile file) 
		throws PraatException {
		TextTier retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextTier_readFromXwaves_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getPoints(String text) throws PraatException {
		return getPoints(new WString(text));
	}
	
	public PointProcess getPoints (WString text) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextTier_getPoints_wrapped(
					this, text);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void removePoint (long ipoint) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextTier_removePoint_wrapped(this, new NativeLong(
					ipoint));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public long maximumLabelLength () {
		return Praat.INSTANCE.TextTier_maximumLabelLength(this).longValue();
	}
	
	public void removeText () {
		Praat.INSTANCE.TextTier_removeText(this);
	}

	public long numberOfPoints () {
		return Praat.INSTANCE.TextTier_numberOfPoints(this).longValue();
	}
	
	public TextPoint point (long i) {
		TextPoint retVal = Praat.INSTANCE.TextTier_point(this, new NativeLong(i));
		retVal.setForgetOnFinalize(false);
		return retVal;
	}
	
	public void removePoints (int which_Melder_STRING, String criterion) {
		removePoints(which_Melder_STRING, new WString(criterion));
	}
	
	public void removePoints (int which_Melder_STRING, WString criterion) {
		Praat.INSTANCE.TextTier_removePoints(this, which_Melder_STRING, criterion);
	}
	
	public MelderQuantity domainQuantity () {
		return Praat.INSTANCE.TextTier_domainQuantity(this);
	}
	
	public void shiftX (double xfrom, double xto) {
		Praat.INSTANCE.TextTier_shiftX(this, xfrom, xto);
	}
	
	public void scaleX (TextTier me, double xminfrom, double xmaxfrom, double xminto, double xmaxto) {
		Praat.INSTANCE.TextTier_scaleX(this, xminfrom, xmaxfrom, xminto, xmaxto);
	}

}
