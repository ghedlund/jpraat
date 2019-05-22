/*
 * Copyright (C) 2012-2018 Gregory Hedlund
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.NativeIntptr_t;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.binding.melder.kMelder_string;
import ca.hedlund.jpraat.binding.stat.Table;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.MelderQuantity;
import ca.hedlund.jpraat.exceptions.PraatException;

public final class TextGrid extends Function {
	
	public TextGrid() {
		super();
	}
	
	public TextGrid(Pointer p) {
		super(p);
	}
	
	/**
	Merge two textGrids.
	The new domain will be:
	[min(grid1->xmin, grid2->xmin), max(grid1->xmax, grid2->xmax)].
	This implies that for the resulting TextGrid each interval tier will have
	one or two extra intervals if the domains of the two TextGrids are not equal,
	*/
	public static TextGrid merge (TextGrid tg1, TextGrid tg2)
		throws PraatException {
		TextGrid retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextGrids_merge_wrapped(tg1, tg2);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
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
		TextGrid retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextGrid_create_wrapped(tmin,
					tmax, new Str32(tierNames), new Str32(pointTiers));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	/**
	Extend the begin-time (delta_time<0) or end-time (delta_time>0).
	For Point-tiers only the domain will be extended.
	Interval tiers will have a new (empty) interval at the start or the end.
	*/
	public void extendTime(double delta_time, int position) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_extendTime_wrapped(this, delta_time, position);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public long countLabels (long itier, String text) throws PraatException {
		long retVal = 0L;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextGrid_countLabels_wrapped(this,
					new NativeIntptr_t(itier), new Str32(text)).longValue();
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getStartingPoints (long itier, kMelder_string which, String criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getStartingPoints_wrapped(this, new NativeIntptr_t(
							itier), which, (criterion != null ? new Str32(criterion) : null));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getEndPoints (long itier, kMelder_string which, String criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getEndPoints_wrapped(this, new NativeIntptr_t(itier),
							which, (criterion != null ? new Str32(criterion) : null));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getCentrePoints (long itier, kMelder_string which, String criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getCentrePoints_wrapped(this, new NativeIntptr_t(
							itier), which, (criterion != null ? new Str32(criterion) : null));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getPoints (long itier, kMelder_string which, String criterion)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getPoints_wrapped(this, new NativeIntptr_t(itier),
							which, (criterion != null ? new Str32(criterion) : null));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getPoints_preceded (long tierNumber,
		kMelder_string which, String criterion,
		kMelder_string precededBy, String criterion_precededBy)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getPoints_preceded_wrapped(this, new NativeIntptr_t(
							tierNumber), which, (criterion != null ? new Str32(criterion) : null),
							precededBy,
							(criterion_precededBy != null ? new Str32(criterion_precededBy) : null));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess getPoints_followed (long tierNumber,
		kMelder_string which, String criterion,
		kMelder_string followedBy, String criterion_followedBy)throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TextGrid_getPoints_followed_wrapped(this, new NativeIntptr_t(
							tierNumber), which, (criterion != null ? new Str32(criterion) : null),
							followedBy,
							(criterion_followedBy != null ? new Str32(criterion_followedBy) : null));
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
							new NativeIntptr_t(tierNumber));
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
							new NativeIntptr_t(tierNumber));
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
							new NativeIntptr_t(tierNumber));
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
			Praat.INSTANCE.TextGrid_addTier_copy_wrapped(this, tier);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void removeTier (long tierNumber) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_removeTier_wrapped(this, new NativeIntptr_t(tierNumber));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
//	public static TextGrid merge (java.util.Collection<TextGrid> textGrids) throws PraatException {
//		Collection col = Collection.create(Praat.getClassInfo(TextGrid.class), textGrids.size());
//		for(TextGrid tg:textGrids) {
//			col.addItem(tg);
//		}
//		
//		TextGrid retVal = null;
//		try {
//			Praat.wrapperLock.lock();
//			retVal = Praat.INSTANCE.TextGrid_merge_wrapped(col);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		return retVal;
//	}
	
	public TextGrid extractPart (double tmin, double tmax, boolean preserveTimes) throws PraatException {
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

	public void convertToBackslashTrigraphs () throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_convertToBackslashTrigraphs_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void convertToUnicode () throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_convertToUnicode_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void changeLabels(long tier, long from, long to, 
			String search, String replace, boolean use_regexp, 
			AtomicReference<Long> nmatches, AtomicReference<Long> nstringmatches) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			
			Pointer nmatchesPtr = new Memory(Native.getNativeSize(Long.class));
			Pointer nstringmatchesPtr = new Memory(Native.getNativeSize(Long.class));
			
			Praat.INSTANCE.TextGrid_changeLabels_wrapped(this, new NativeIntptr_t(tier), new NativeIntptr_t(from), new NativeIntptr_t(to), 
					new Str32(search), new Str32(replace), use_regexp, nmatchesPtr, nstringmatchesPtr);
			Praat.checkAndClearLastError();
			
			nmatches.set(nmatchesPtr.getLong(0));
			nstringmatches.set(nstringmatchesPtr.getLong(0));
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insertBoundary (long itier, double t) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_insertBoundary_wrapped(this, new NativeIntptr_t(itier), t);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void removeBoundaryAtTime (long itier, double t) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE
					.TextGrid_removeBoundaryAtTime_wrapped(this, new NativeIntptr_t(itier), t);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void setIntervalText (long itier, long iinterval, String text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_setIntervalText_wrapped(this, new NativeIntptr_t(itier),
					new NativeIntptr_t(iinterval), new Str32(text));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insertPoint (long itier, double t, String mark) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_insertPoint_wrapped(this, new NativeIntptr_t(itier), t, new Str32(mark));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public void setPointText (long itier, long ipoint, String text) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_setPointText_wrapped(this, new NativeIntptr_t(itier),
					new NativeIntptr_t(ipoint), new Str32(text));
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
		Function tier = Praat.INSTANCE.TextGrid_tier(this, new NativeIntptr_t(i));
		tier.setForgetOnFinalize(false);
		return tier;
	}
	
	public void setTierName(long itier, String newName) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_setTierName_wrapped(this, new NativeIntptr_t(itier), new Str32(newName));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void removePoints (long tierNumber, kMelder_string which, String criterion) {
		Praat.INSTANCE.TextGrid_removePoints(this, new NativeIntptr_t(tierNumber), which, 
				(criterion != null ? new Str32(criterion) : null));
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

	/**
	 * Set the end time to a larger value.
	 * If mark is NULL, only times are changed
	 * If mark != NULL mark the previous start/end time
	 *    For a TextTier this involves adding a point with the marker
	 *    For an IntervalTier this involves adding a new interval
	 */
	public void setLaterEndTime(double xmax, String imark, String pmark) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_setLaterEndTime_wrapped(this, xmax, 
					(imark == null ? null : new Str32(imark)),
					(pmark == null ? null : new Str32(pmark)));
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
	public void setEarlierStartTime(double xmin, String imark, String pmark) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrid_setEarlierStartTime_wrapped(this, xmin,
					(imark == null ? null : new Str32(imark)),
					(pmark == null ? null : new Str32(pmark)));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void append_inplace(TextGrid thee, boolean preserveTimes) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextGrids_append_inplace_wrapped(this, thee, preserveTimes);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

//	public static TextGrid TextGrids_to_TextGrid_appendContinuous (java.util.Collection<TextGrid> me, boolean preserveTimes)
//		throws PraatException {
//		TextGrid retVal = null;
//		
//		Collection col = Collection.create(Praat.getClassInfo(TextGrid.class), me.size());
//		for(TextGrid tg:me) col.addItem(tg);
//		
//		try {
//			Praat.wrapperLock.lock();
//			Praat.INSTANCE.TextGrids_to_TextGrid_appendContinuous_wrapped(col, preserveTimes);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		
//		return retVal;
//	}

}
