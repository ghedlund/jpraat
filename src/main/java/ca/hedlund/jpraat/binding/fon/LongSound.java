package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;

public class LongSound extends Sampled {

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
	public Sound extractPart (double tmin, double tmax, int preserveTimes) throws PraatException {
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
	
	public void getWindowExtrema (double tmin, double tmax, int channel, 
			AtomicReference<Double> minimum, AtomicReference<Double> maximum) throws PraatException {
		final Pointer pmin = new Memory(Native.getNativeSize(Double.class)*2);
		final Pointer pmax = pmin.getPointer(1);
		
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.LongSound_getWindowExtrema_wrapped(this, tmin, tmax,
					channel, pmin, pmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		minimum.set(pmin.getDouble(0));
		minimum.set(pmax.getDouble(0));
	}
	
}
