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
import ca.hedlund.jpraat.exceptions.PraatException;

public final class PointProcess extends Function {
	
	public PointProcess() {
		super();
	}
	
	public PointProcess(Pointer p) {
		super(p);
	}
	
	public static PointProcess create (double startingTime, double finishingTime, long initialMaxnt)
			throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.PointProcess_create_wrapped(
					startingTime, finishingTime, new NativeIntptr_t(initialMaxnt));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public static PointProcess createPoissonProcess (double startingTime, double finishingTime, double density) 
			throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.PointProcess_createPoissonProcess_wrapped(startingTime,
							finishingTime, density);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void init (double startingTime, double finishingTime, long initialMaxnt) {
		Praat.INSTANCE.PointProcess_init(this, startingTime, finishingTime, new NativeLong(initialMaxnt));
	}
	
	public long getLowIndex (double t) {
		return Praat.INSTANCE.PointProcess_getLowIndex(this, t).longValue();
	}
	
	public long getHighIndex (double t) {
		return Praat.INSTANCE.PointProcess_getHighIndex(this, t).longValue();
	}
	
	public long getNearestIndex (double t) {
		return Praat.INSTANCE.PointProcess_getNearestIndex(this, t).longValue();
	}
	
	public long getWindowPoints (double tmin, double tmax, 
			AtomicReference<Long> imin, AtomicReference<Long> imax) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		long retVal = Praat.INSTANCE.PointProcess_getWindowPoints(this, tmin, tmax, minPtr, maxPtr).longValue();
		
		imin.set(minPtr.getLong(0));
		imax.set(maxPtr.getLong(0));
		
		return retVal;
	}

	public void addPoint (double t) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.PointProcess_addPoint_wrapped(this, t);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public long findPoint (double t) {
		return Praat.INSTANCE.PointProcess_findPoint(this, t).longValue();
	}
	
	public void removePoint (long index) {
		Praat.INSTANCE.PointProcess_removePoint(this, new NativeIntptr_t(index));
	}
	
	public void removePointNear (double t) {
		Praat.INSTANCE.PointProcess_removePointNear(this, t);
	}
	
	public void removePoints (long first, long last) {
		Praat.INSTANCE.PointProcess_removePoints(this, new NativeIntptr_t(first), new NativeIntptr_t(last));
	}
	
	public void removePointsBetween (double fromTime, double toTime) {
		Praat.INSTANCE.PointProcess_removePointsBetween(this, fromTime, toTime);
	}

	public double getInterval (double t) {
		return Praat.INSTANCE.PointProcess_getInterval(this, t);
	}
	
	public static PointProcess union(PointProcess p1, PointProcess p2) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.PointProcesses_union_wrapped(p1,
					p2);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess union (PointProcess thee) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.PointProcesses_union_wrapped(this,
					thee);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static PointProcess intersection (PointProcess p1, PointProcess p2) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.PointProcesses_intersection_wrapped(p1, p2);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess intersection (PointProcess thee) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.PointProcesses_intersection_wrapped(this, thee);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static PointProcess difference (PointProcess p1, PointProcess p2)
		throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.PointProcesses_difference_wrapped(
					p1, p2);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess difference (PointProcess thee) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.PointProcesses_difference_wrapped(
					this, thee);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void fill (double tmin, double tmax, double period) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.PointProcess_fill_wrapped(this, tmin, tmax, period);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void voice (double period, double maxT) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.PointProcess_voice_wrapped(this, period, maxT);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public long getNumberOfPeriods (double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor) {
		return Praat.INSTANCE.PointProcess_getNumberOfPeriods(this, tmin, tmax, minimumPeriod,
				maximumPeriod, maximumPeriodFactor).longValue();
	}
	
	public double getMeanPeriod (double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor) {
		return Praat.INSTANCE.PointProcess_getMeanPeriod(this, tmin, tmax, minimumPeriod, maximumPeriod, maximumPeriodFactor);
	}

	public double PointProcess_getStdevPeriod (double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor) {
		return Praat.INSTANCE.PointProcess_getStdevPeriod(this, tmin, tmax, minimumPeriod, maximumPeriod, maximumPeriodFactor);
	}
	
}
