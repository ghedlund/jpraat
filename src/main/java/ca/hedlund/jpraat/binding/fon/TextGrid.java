package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Custom;
import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.annotations.Wrapped;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.stat.Table;
import ca.hedlund.jpraat.binding.sys.Collection;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.MelderQuantity;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

public class TextGrid extends Function {
	
	public TextGrid() {
		super();
	}
	
	public TextGrid(Pointer p) {
		super(p);
	}
	
	public static TextGrid createWithoutTiers (double tmin, double tmax) 
		throws PraatException {
		TextGrid retVal = Praat.INSTANCE.TextGrid_createWithoutTiers_wrapped(tmin, tmax);
		Praat.checkAndClearLastError();
		return retVal;
	}

	public static TextGrid create (double tmin, double tmax, String tierNames, String pointTiers)
		throws PraatException {
		return create(tmin, tmax, new WString(tierNames), new WString(pointTiers));
	}
	
	public static TextGrid create (double tmin, double tmax, WString tierNames, WString pointTiers)
		throws PraatException {
		TextGrid retVal = Praat.INSTANCE.TextGrid_create_wrapped(tmin, tmax, tierNames, pointTiers);
		Praat.checkAndClearLastError();
		return retVal;
	}

	public long countLables (long itier, String text) throws PraatException {
		return countLabels(itier, new WString(text));
	}
	
	public long countLabels (long itier, WString text) throws PraatException {
		long retVal = Praat.INSTANCE.TextGrid_countLabels_wrapped(this, new NativeLong(itier), text).longValue();
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getStartingPoints (long itier, int which_Melder_STRING, String criterion)throws PraatException {
		return getStartingPoints(itier, which_Melder_STRING, new WString(criterion));
	}
	
	public PointProcess getStartingPoints (long itier, int which_Melder_STRING, WString criterion)throws PraatException {
		PointProcess retVal = Praat.INSTANCE.TextGrid_getStartingPoints_wrapped(this, new NativeLong(itier), which_Melder_STRING, criterion);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getEndPoints (long itier, int which_Melder_STRING, String criterion)throws PraatException {
		return getEndPoints(itier, which_Melder_STRING, new WString(criterion));
	}
	
	public PointProcess getEndPoints (long itier, int which_Melder_STRING, WString criterion)throws PraatException {
		PointProcess retVal = Praat.INSTANCE.TextGrid_getEndPoints_wrapped(this, new NativeLong(itier), which_Melder_STRING, criterion);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getCentrePoints (long itier, int which_Melder_STRING, String criterion)throws PraatException {
		PointProcess retVal = Praat.INSTANCE.TextGrid_getCentrePoints_wrapped(this, new NativeLong(itier), 
				which_Melder_STRING, new WString(criterion));
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getCentrePoints (long itier, int which_Melder_STRING, WString criterion)throws PraatException {
		PointProcess retVal = Praat.INSTANCE.TextGrid_getCentrePoints_wrapped(this, new NativeLong(itier), which_Melder_STRING, criterion);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getPoints (long itier, int which_Melder_STRING, String criterion)throws PraatException {
		return getPoints(itier, which_Melder_STRING, new WString(criterion));
	}
	
	public PointProcess getPoints (long itier, int which_Melder_STRING, WString criterion)throws PraatException {
		PointProcess retVal = Praat.INSTANCE.TextGrid_getPoints_wrapped(this, new NativeLong(itier), which_Melder_STRING, criterion);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getPoints_preceded (long tierNumber,
			int which_Melder_STRING, String criterion,
			int which_Melder_STRING_precededBy, String criterion_precededBy)throws PraatException {
		return getPoints_preceded(tierNumber, which_Melder_STRING, new WString(criterion), which_Melder_STRING_precededBy, new WString(criterion_precededBy));
	}
	
	public PointProcess getPoints_preceded (long tierNumber,
		int which_Melder_STRING, WString criterion,
		int which_Melder_STRING_precededBy, WString criterion_precededBy)throws PraatException {
		PointProcess retVal = Praat.INSTANCE.TextGrid_getPoints_preceded_wrapped(this, new NativeLong(tierNumber), which_Melder_STRING, criterion, which_Melder_STRING_precededBy, criterion_precededBy);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public PointProcess getPoints_followed (long tierNumber,
			int which_Melder_STRING, String criterion,
			int which_Melder_STRING_followedBy, String criterion_followedBy)throws PraatException {
		return getPoints_followed(tierNumber, which_Melder_STRING, new WString(criterion), which_Melder_STRING_followedBy, new WString(criterion_followedBy));
	}
	
	public PointProcess getPoints_followed (long tierNumber,
		int which_Melder_STRING, WString criterion,
		int which_Melder_STRING_followedBy, WString criterion_followedBy)throws PraatException {
		PointProcess retVal = Praat.INSTANCE.TextGrid_getPoints_followed_wrapped(this, new NativeLong(tierNumber), which_Melder_STRING, criterion, which_Melder_STRING_followedBy, criterion_followedBy);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public Function checkSpecifiedTierNumberWithinRange (long tierNumber) throws PraatException {
		Function retVal = Praat.INSTANCE.TextGrid_checkSpecifiedTierNumberWithinRange_wrapped(this, new NativeLong(tierNumber));
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public IntervalTier checkSpecifiedTierIsIntervalTier (long tierNumber) throws PraatException {
		IntervalTier retVal = Praat.INSTANCE.TextGrid_checkSpecifiedTierIsIntervalTier_wrapped(this, new NativeLong(tierNumber));
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public TextTier checkSpecifiedTierIsPointTier (long tierNumber) throws PraatException {
		TextTier retVal = Praat.INSTANCE.TextGrid_checkSpecifiedTierIsPointTier_wrapped(this, new NativeLong(tierNumber));
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public void addTier (Function tier) throws PraatException {
		Praat.INSTANCE.TextGrid_addTier_wrapped(this, tier);
		Praat.checkAndClearLastError();
	}
	
	public static TextGrid merge (java.util.Collection<TextGrid> textGrids) throws PraatException {
		Collection col = Collection.create(Praat.getClassInfo(TextGrid.class), textGrids.size());
		for(TextGrid tg:textGrids) {
			col.addItem(tg);
			Praat.checkAndClearLastError();
		}
		
		TextGrid retVal = Praat.INSTANCE.TextGrid_merge_wrapped(col);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public TextGrid extractPart (double tmin, double tmax, int preserveTimes) throws PraatException {
		TextGrid retVal = Praat.INSTANCE.TextGrid_extractPart_wrapped(this, tmin, tmax, preserveTimes);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public long  maximumLabelLength () {
		return Praat.INSTANCE.TextGrid_maximumLabelLength(this).longValue();
	}

	public void genericize () throws PraatException {
		Praat.INSTANCE.TextGrid_genericize_wrapped(this);
		Praat.checkAndClearLastError();
	}
	
	public void nativize () throws PraatException {
		Praat.INSTANCE.TextGrid_nativize_wrapped(this);
		Praat.checkAndClearLastError();
	}
	
	public void insertBoundary (int itier, double t) throws PraatException {
		Praat.INSTANCE.TextGrid_insertBoundary_wrapped(this, itier, t);
		Praat.checkAndClearLastError();
	}
	
	public void removeBoundaryAtTime (int itier, double t) throws PraatException {
		Praat.INSTANCE.TextGrid_removeBoundaryAtTime_wrapped(this, itier, t);
		Praat.checkAndClearLastError();
	}
	
	public void setIntervalText (int itier, long iinterval, String text) throws PraatException {
		setIntervalText(itier, iinterval, new WString(text));
	}
	
	public void setIntervalText (int itier, long iinterval, WString text) throws PraatException {
		Praat.INSTANCE.TextGrid_setIntervalText_wrapped(this, itier, new NativeLong(iinterval), text);
		Praat.checkAndClearLastError();
	}
	
	public void insertPoint (int itier, double t, String mark) throws PraatException {
		insertPoint(itier, t, new WString(mark));
		Praat.checkAndClearLastError();
	}

	public void insertPoint (int itier, double t, WString mark) throws PraatException {
		Praat.INSTANCE.TextGrid_insertPoint_wrapped(this, itier, t, mark);
		Praat.checkAndClearLastError();
	}

	public void setPointText (int itier, long ipoint, String text) throws PraatException {
		setPointText(itier, ipoint, new WString(text));
	}

	public void setPointText (int itier, long ipoint, WString text) throws PraatException {
		Praat.INSTANCE.TextGrid_setPointText_wrapped(this, itier, new NativeLong(ipoint), text);
		Praat.checkAndClearLastError();
	}
	
	public void writeToChronologicalTextFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.TextGrid_writeToChronologicalTextFile_wrapped(this, file);
		Praat.checkAndClearLastError();
	}
	
	public static TextGrid readFromChronologicalTextFile (MelderFile file) throws PraatException {
		TextGrid retVal = Praat.INSTANCE.TextGrid_readFromChronologicalTextFile_wrapped(file);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public static TextGrid readFromCgnSyntaxFile (MelderFile file) throws PraatException {
		TextGrid retVal = Praat.INSTANCE.TextGrid_readFromCgnSyntaxFile_wrapped(file);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public Table downto_Table (boolean includeLineNumbers, int timeDecimals, boolean includeTierNames, boolean includeEmptyIntervals)
		throws PraatException {
		Table retVal = Praat.INSTANCE.TextGrid_downto_Table_wrapped(this, includeLineNumbers, timeDecimals, includeTierNames, includeEmptyIntervals);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public long numberOfTiers () {
		return Praat.INSTANCE.TextGrid_numberOfTiers(this).longValue();
	}
	
	public Function tier (long i) {
		return Praat.INSTANCE.TextGrid_tier(this, new NativeLong(i));
	}
	
	public void removePoints (long tierNumber, int which_Melder_STRING, WString criterion) {
		Praat.INSTANCE.TextGrid_removePoints(this, new NativeLong(tierNumber), which_Melder_STRING, criterion);
	}
	
	public void repair () {
		Praat.INSTANCE.TextGrid_repair(this);
	}
	
	public MelderQuantity domainQuantity () {
		return Praat.INSTANCE.TextGrid_domainQuantity(this);
	}
	
	public void shiftX (double xfrom, double xto) {
		Praat.INSTANCE.TextGrid_shiftX(this, xfrom, xto);
	}
	
	public void scaleX (double xminfrom, double xmaxfrom, double xminto, double xmaxto) {
		Praat.INSTANCE.TextGrid_scaleX(this, xminfrom, xmaxfrom, xminto, xmaxto);
	}

}
