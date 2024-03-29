/*
 * Copyright (C) 2012-2020 Gregory Hedlund
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

import java.util.concurrent.atomic.*;

import com.sun.jna.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.binding.melder.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;

public final class TextTier extends Function {
	
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
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextTier_addPoint_wrapped(this, time, new Str32(mark));
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
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TextTier_getPoints_wrapped(
					this, (text == null ? null : new Str32(text)));
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
			Praat.INSTANCE.TextTier_removePoint_wrapped(this, new NativeIntptr_t(
					ipoint));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void changeLabels (long from, long to, 
			String search, String replace, boolean use_regexp, 
			AtomicReference<Long> nmatches, AtomicReference<Long> nstringmatches) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			
			Pointer nmatchesPtr = new Memory(Native.getNativeSize(Long.class));
			Pointer nstringmatchesPtr = new Memory(Native.getNativeSize(Long.class));
			
			Praat.INSTANCE.TextTier_changeLabels_wrapped(this, new NativeIntptr_t(from), new NativeIntptr_t(to), 
					new Str32(search), new Str32(replace), (use_regexp ? 1 : 0), nmatchesPtr, nstringmatchesPtr);
			Praat.checkAndClearLastError();
			
			nmatches.set(nmatchesPtr.getLong(0));
			nstringmatches.set(nstringmatchesPtr.getLong(0));
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
		TextPoint retVal = Praat.INSTANCE.TextTier_point(this, new NativeIntptr_t(i));
		return retVal;
	}
	
	public void removePoints (kMelder_string which, String criterion) {
		Praat.INSTANCE.TextTier_removePoints(this, which, (criterion == null ? null : new Str32(criterion)));
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
			Praat.INSTANCE.TextTier_setLaterEndTime_wrapped(this, xmax, 
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
			Praat.INSTANCE.TextTier_setEarlierStartTime_wrapped(this, xmin,
					(mark == null ? null : new Str32(mark)));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public void append_inplace(TextTier thee, boolean preserveTimes) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TextTiers_append_inplace_wrapped(this, thee, (preserveTimes ? 1 : 0));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

}
