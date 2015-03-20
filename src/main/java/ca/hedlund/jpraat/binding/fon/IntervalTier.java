package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import ca.hedlund.jpraat.annotations.Custom;
import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.annotations.Wrapped;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.MelderQuantity;
import ca.hedlund.jpraat.exceptions.PraatException;

public class IntervalTier extends Function {
	
	public IntervalTier() {
		super();
	}
	
	public IntervalTier(Pointer p) {
		super(p);
	}
	
	public static IntervalTier create (double tmin, double tmax) throws PraatException {
		IntervalTier retVal = Praat.INSTANCE.IntervalTier_create_wrapped(tmin, tmax);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public static IntervalTier readFromXwaves (MelderFile file) throws PraatException {
		IntervalTier retVal = Praat.INSTANCE.IntervalTier_readFromXwaves_wrapped(file);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public void writeToXwaves (MelderFile file) throws PraatException {
		Praat.INSTANCE.IntervalTier_writeToXwaves_wrapped(this, file);
		Praat.checkAndClearLastError();
	}
	
	public long timeToLowIndex (double t) {
		return Praat.INSTANCE.IntervalTier_timeToLowIndex(this, t).longValue();
	}
	
	@Deprecated
	public long timeToIndex (double t) {
		return Praat.INSTANCE.IntervalTier_timeToIndex(this, t).longValue();
	}

	public long timeToHighIndex (double t) {
		return Praat.INSTANCE.IntervalTier_timeToHighIndex(this, t).longValue();
	}
	
	public long hasTime (double t) {
		return Praat.INSTANCE.IntervalTier_hasTime(this, t).longValue();
	}
	
	public long hasBoundary (double t) {
		return Praat.INSTANCE.IntervalTier_hasBoundary(this, t).longValue();
	}
	
	public PointProcess getStartingPoints (WString text) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.IntervalTier_getStartingPoints_wrapped(this, text);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getEndPoints (WString text) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.IntervalTier_getEndPoints_wrapped(this, text);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getCentrePoints (WString text) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.IntervalTier_getCentrePoints_wrapped(this, text);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess PointProcess_startToCentre (PointProcess point, double phase) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.IntervalTier_PointProcess_startToCentre_wrapped(this, point, phase);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess PointProcess_endToCentre (PointProcess point, double phase)
		throws PraatException {
		PointProcess retVal = Praat.INSTANCE.IntervalTier_PointProcess_endToCentre_wrapped(this, point, phase);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public void removeLeftBoundary (long iinterval) throws PraatException {
		Praat.INSTANCE.IntervalTier_removeLeftBoundary_wrapped(this, new NativeLong(iinterval));
		Praat.checkAndClearLastError();
	}

	public long maximumLabelLength () {
		return Praat.INSTANCE.IntervalTier_maximumLabelLength(this).longValue();
	}
	
	public void removeText () {
		Praat.INSTANCE.IntervalTier_removeText(this);
	}

	public long numberOfIntervals () {
		return Praat.INSTANCE.IntervalTier_numberOfIntervals(this).longValue();
	}
	
	public TextInterval interval (long i) {
		return Praat.INSTANCE.IntervalTier_interval(this, new NativeLong(i));
	}
	
	public MelderQuantity domainQuantity () {
		return Praat.INSTANCE.IntervalTier_domainQuantity(this);
	}
	
	public void shiftX (double xfrom, double xto) {
		Praat.INSTANCE.IntervalTier_shiftX(this, xfrom, xto);
	}
	
	public void scaleX (double xminfrom, double xmaxfrom, double xminto, double xmaxto) {
		Praat.INSTANCE.IntervalTier_scaleX(this, xminfrom, xmaxfrom, xminto, xmaxto);
	}
	
	public void addInterval (double tmin, double tmax, String label) {
		addInterval(tmin, tmax, new WString(label));
	}

	/**
	 * Unsafe add interval.  This method <b>does not</b> ensure proper ordering
	 * of intervals.
	 * 
	 * @param me
	 * @param tmin
	 * @param tmax
	 * @param label
	 */
	public void addInterval (double tmin, double tmax, WString label) {
		Praat.INSTANCE.IntervalTier_addInterval(this, tmin, tmax, label);
	}
	
	public void removeInterval (long iinterval) {
		Praat.INSTANCE.IntervalTier_removeInterval(this, new NativeLong(iinterval));
	}

}
