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

import ca.hedlund.jpraat.binding.jna.NativeIntptr_t;
import com.sun.jna.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;

public final class LongSound extends Sampled {

	public LongSound() {
		super();
	}
	
	public LongSound(Pointer p) {
		super(p);
	}
	
	public static LongSound open (MelderFile fs) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			LongSound retVal = Praat.INSTANCE.LongSound_open_wrapped(fs);
			Praat.checkAndClearLastError();
			return retVal;
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	/**
	 * Extract a portion of this audio stream.  This function will throw
	 * an {@link IllegalArgumentException} if the given window is outside
	 * of the data range.
	 * 
	 * @param tmin
	 * @param tmax
	 * @param preserveTimes
	 * @return
	 * 
	 * @throws IllegalArgumentException if (tmin, tmax) is outside of 
	 * 	this sound's range
	 */
	public Sound extractPart (double tmin, double tmax, boolean preserveTimes) throws PraatException {
		final AtomicReference<Long> iminRef = new AtomicReference<Long>();
		final AtomicReference<Long> imaxRef = new AtomicReference<Long>();
		
		long n = getWindowSamples(tmin, tmax, iminRef, imaxRef);
		if(n < 1) {
			throw new PraatException("Window is outside of data range");
		}
		
		Sound retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.LongSound_extractPart_wrapped(this, tmin,
					tmax, preserveTimes);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
	public boolean haveWindow (double tmin, double tmax) throws PraatException {
		boolean retVal = false;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.LongSound_haveWindow_wrapped(this,
					tmin, tmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void getWindowExtrema (double tmin, double tmax, long channel,
			AtomicReference<Double> minimum, AtomicReference<Double> maximum) throws PraatException {
		final Pointer pmin = new Memory(Native.getNativeSize(Double.class)*2);
		final Pointer pmax = pmin.getPointer(1);
		
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.LongSound_getWindowExtrema_wrapped(this, tmin, tmax,
					new NativeIntptr_t(channel), pmin, pmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		minimum.set(pmin.getDouble(0));
		minimum.set(pmax.getDouble(0));
	}
	
	public void savePartAsAudioFile(int audioFileType, double tmin,
			double tmax, MelderFile file, int numberOfBitsPerSamplePoint)
		throws PraatException {
		
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.LongSound_savePartAsAudioFile_wrapped(this, audioFileType, tmin, tmax, file, numberOfBitsPerSamplePoint);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public void saveChannelAsAudioFile(int audioFileType, long channel,
			MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.LongSound_saveChannelAsAudioFile_wrapped(this, audioFileType, new NativeIntptr_t(channel), file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
}
