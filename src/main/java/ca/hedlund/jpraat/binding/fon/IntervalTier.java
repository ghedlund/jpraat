package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
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
		IntervalTier retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.IntervalTier_create_wrapped(tmin,
					tmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static IntervalTier readFromXwaves (MelderFile file) throws PraatException {
		IntervalTier retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.IntervalTier_readFromXwaves_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void writeToXwaves (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTier_writeToXwaves_wrapped(this, file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
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
	
	public PointProcess getStartingPoints (String text) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.IntervalTier_getStartingPoints_wrapped(this, new Str32(text));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getEndPoints (String text) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.IntervalTier_getEndPoints_wrapped(
					this, new Str32(text));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getCentrePoints (String text) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.IntervalTier_getCentrePoints_wrapped(this, new Str32(text));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess PointProcess_startToCentre (PointProcess point, double phase) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.IntervalTier_PointProcess_startToCentre_wrapped(this, point,
							phase);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess PointProcess_endToCentre (PointProcess point, double phase)
		throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.IntervalTier_PointProcess_endToCentre_wrapped(this, point,
							phase);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void removeLeftBoundary (long iinterval) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTier_removeLeftBoundary_wrapped(this,
					new NativeLong(iinterval));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
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
		TextInterval retVal = Praat.INSTANCE.IntervalTier_interval(this, new NativeLong(i));
		retVal.setForgetOnFinalize(false);
		return retVal;
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
	
	/**
	 * Unsafe add interval.  This method <b>does not</b> ensure proper ordering
	 * of intervals.
	 * 
	 * @param me
	 * @param tmin
	 * @param tmax
	 * @param label
	 */
	public void addInterval (double tmin, double tmax, String label) {
		Praat.INSTANCE.IntervalTier_addInterval(this, tmin, tmax, new Str32(label));
	}
	
	public void removeInterval (long iinterval) {
		Praat.INSTANCE.IntervalTier_removeInterval(this, new NativeLong(iinterval));
	}
	
	public void removeBoundariesBetweenIdenticallyLabeledIntervals (String label) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTier_removeBoundariesBetweenIdenticallyLabeledIntervals_wrapped(this, new Str32(label));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public void cutIntervalsOnLabelMatch (String label) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTier_cutIntervalsOnLabelMatch_wrapped(this, new Str32(label));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public void cutIntervals_minimumDuration (String label, double minimumDuration) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTier_cutIntervals_minimumDuration_wrapped(this, new Str32(label), minimumDuration);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	/**
	 * Set the end time to a larger value.
	 * If mark is NULL, only times are changed
	 * If mark != NULL mark the previous start/end time
	 *    For a TextTier this involves adding a point with the marker
	 *    For an IntervalTier this involves adding a new interval
	 */
	public void setLaterEndTime(double xmax, String mark) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTier_setLaterEndTime_wrapped(this, xmax, 
					(mark == null ? null : new Str32(mark)));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	/**
	 * Set the start time to a smaller value.
	 * If mark is NULL, only times are changed
	 * If mark != NULL mark the previous start/end time
	 *    For a TextTier this involves adding a point with the marker
	 *    For an IntervalTier this involves adding a new interval
	 */
	public void setEarlierStartTime(double xmin, String mark) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTier_setEarlierStartTime_wrapped(this, xmin,
					(mark == null ? null : new Str32(mark)));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void moveBoundary(long interval, boolean atStart, double newTime) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTier_moveBoundary_wrapped(this, new NativeLong(interval), atStart, newTime);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void append_inline(IntervalTier thee, boolean preserveTimes) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.IntervalTiers_append_inline_wrapped(this, thee, preserveTimes);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}


}
