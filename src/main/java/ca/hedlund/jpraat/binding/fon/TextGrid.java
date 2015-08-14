package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Custom;
import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.annotations.Wrapped;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.binding.stat.Table;
import ca.hedlund.jpraat.binding.sys.Collection;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.MelderQuantity;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public class TextGrid extends Function {
	
	public TextGrid() {
		super();
	}
	
	public TextGrid(Pointer p) {
		super(p);
	}
	
	public static TextGrid createWithoutTiers (double tmin, double tmax) 
		throws PraatException {
		TextGrid retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_createWithoutTiers_wrapped(tmin, tmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public static TextGrid create (double tmin, double tmax, String tierNames, String pointTiers)
		throws PraatException {
		return create(tmin, tmax, new Str32(tierNames), new Str32(pointTiers));
	}
	
	public static TextGrid create (double tmin, double tmax, Str32 tierNames, Str32 pointTiers)
		throws PraatException {
		TextGrid retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextGrid_create_wrapped(tmin,
					tmax, tierNames, pointTiers);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public long countLables (long itier, String text) throws PraatException {
		return countLabels(itier, new Str32(text));
	}
	
	public long countLabels (long itier, Str32 text) throws PraatException {
		long retVal = 0L;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextGrid_countLabels_wrapped(this,
					new NativeLong(itier), text).longValue();
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getStartingPoints (long itier, int which_Melder_STRING, String criterion)throws PraatException {
		return getStartingPoints(itier, which_Melder_STRING, new Str32(criterion));
	}
	
	public PointProcess getStartingPoints (long itier, int which_Melder_STRING, Str32 criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getStartingPoints_wrapped(this, new NativeLong(
							itier), which_Melder_STRING, criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getEndPoints (long itier, int which_Melder_STRING, String criterion)throws PraatException {
		return getEndPoints(itier, which_Melder_STRING, new Str32(criterion));
	}
	
	public PointProcess getEndPoints (long itier, int which_Melder_STRING, Str32 criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getEndPoints_wrapped(this, new NativeLong(itier),
							which_Melder_STRING, criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getCentrePoints (long itier, int which_Melder_STRING, String criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getCentrePoints_wrapped(this, new NativeLong(
							itier), which_Melder_STRING, new Str32(criterion));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getCentrePoints (long itier, int which_Melder_STRING, Str32 criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getCentrePoints_wrapped(this, new NativeLong(
							itier), which_Melder_STRING, criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getPoints (long itier, int which_Melder_STRING, String criterion)throws PraatException {
		return getPoints(itier, which_Melder_STRING, new Str32(criterion));
	}
	
	public PointProcess getPoints (long itier, int which_Melder_STRING, Str32 criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getPoints_wrapped(this, new NativeLong(itier),
							which_Melder_STRING, criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getPoints_preceded (long tierNumber,
			int which_Melder_STRING, String criterion,
			int which_Melder_STRING_precededBy, String criterion_precededBy)throws PraatException {
		return getPoints_preceded(tierNumber, which_Melder_STRING, new Str32(criterion), which_Melder_STRING_precededBy, new Str32(criterion_precededBy));
	}
	
	public PointProcess getPoints_preceded (long tierNumber,
		int which_Melder_STRING, Str32 criterion,
		int which_Melder_STRING_precededBy, Str32 criterion_precededBy)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getPoints_preceded_wrapped(this, new NativeLong(
							tierNumber), which_Melder_STRING, criterion,
							which_Melder_STRING_precededBy,
							criterion_precededBy);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getPoints_followed (long tierNumber,
			int which_Melder_STRING, String criterion,
			int which_Melder_STRING_followedBy, String criterion_followedBy)throws PraatException {
		return getPoints_followed(tierNumber, which_Melder_STRING, new Str32(criterion), which_Melder_STRING_followedBy, new Str32(criterion_followedBy));
	}
	
	public PointProcess getPoints_followed (long tierNumber,
		int which_Melder_STRING, Str32 criterion,
		int which_Melder_STRING_followedBy, Str32 criterion_followedBy)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getPoints_followed_wrapped(this, new NativeLong(
							tierNumber), which_Melder_STRING, criterion,
							which_Melder_STRING_followedBy,
							criterion_followedBy);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Function checkSpecifiedTierNumberWithinRange (long tierNumber) throws PraatException {
		Function retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_checkSpecifiedTierNumberWithinRange_wrapped(this,
							new NativeLong(tierNumber));
			Praat.checkAndClearLastError();
			retVal.setForgetOnFinalize(false);
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public IntervalTier checkSpecifiedTierIsIntervalTier (long tierNumber) throws PraatException {
		IntervalTier retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_checkSpecifiedTierIsIntervalTier_wrapped(this,
							new NativeLong(tierNumber));
			Praat.checkAndClearLastError();
			retVal.setForgetOnFinalize(false);
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TextTier checkSpecifiedTierIsPointTier (long tierNumber) throws PraatException {
		TextTier retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_checkSpecifiedTierIsPointTier_wrapped(this,
							new NativeLong(tierNumber));
			Praat.checkAndClearLastError();
			retVal.setForgetOnFinalize(false);
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void addTier (Function tier) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_addTier_wrapped(this, tier);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public static TextGrid merge (java.util.Collection<TextGrid> textGrids) throws PraatException {
		Collection col = Collection.create(Praat.getClassInfo(TextGrid.class), textGrids.size());
		for(TextGrid tg:textGrids) {
			col.addItem(tg);
		}
		
		TextGrid retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextGrid_merge_wrapped(col);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TextGrid extractPart (double tmin, double tmax, int preserveTimes) throws PraatException {
		TextGrid retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextGrid_extractPart_wrapped(this,
					tmin, tmax, preserveTimes);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public long  maximumLabelLength () {
		return Praat.INSTANCE.TextGrid_maximumLabelLength(this).longValue();
	}

	public void genericize () throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_genericize_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void nativize () throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_nativize_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insertBoundary (int itier, double t) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_insertBoundary_wrapped(this, itier, t);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void removeBoundaryAtTime (int itier, double t) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE
					.TextGrid_removeBoundaryAtTime_wrapped(this, itier, t);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void setIntervalText (int itier, long iinterval, String text) throws PraatException {
		setIntervalText(itier, iinterval, new Str32(text));
	}
	
	public void setIntervalText (int itier, long iinterval, Str32 text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_setIntervalText_wrapped(this, itier,
					new NativeLong(iinterval), text);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insertPoint (int itier, double t, String mark) throws PraatException {
		insertPoint(itier, t, new Str32(mark));
	}

	public void insertPoint (int itier, double t, Str32 mark) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_insertPoint_wrapped(this, itier, t, mark);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public void setPointText (int itier, long ipoint, String text) throws PraatException {
		setPointText(itier, ipoint, new Str32(text));
	}

	public void setPointText (int itier, long ipoint, Str32 text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_setPointText_wrapped(this, itier,
					new NativeLong(ipoint), text);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void writeToChronologicalTextFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_writeToChronologicalTextFile_wrapped(this,
					file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public static TextGrid readFromChronologicalTextFile (MelderFile file) throws PraatException {
		TextGrid retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_readFromChronologicalTextFile_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static TextGrid readFromCgnSyntaxFile (MelderFile file) throws PraatException {
		TextGrid retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_readFromCgnSyntaxFile_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Table downto_Table (boolean includeLineNumbers, int timeDecimals, boolean includeTierNames, boolean includeEmptyIntervals)
		throws PraatException {
		Table retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextGrid_downto_Table_wrapped(this,
					includeLineNumbers, timeDecimals, includeTierNames,
					includeEmptyIntervals);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public long numberOfTiers () {
		return Praat.INSTANCE.TextGrid_numberOfTiers(this).longValue();
	}
	
	public Function tier (long i) {
		Function tier = Praat.INSTANCE.TextGrid_tier(this, new NativeLong(i));
		tier.setForgetOnFinalize(false);
		return tier;
	}
	
	public void removePoints (long tierNumber, int which_Melder_STRING, Str32 criterion) {
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
