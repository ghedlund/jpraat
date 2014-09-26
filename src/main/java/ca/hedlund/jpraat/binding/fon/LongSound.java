package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.MelderFile;

public class LongSound extends Sampled {
	
	public static LongSound open (MelderFile fs) {
		return Praat.INSTANCE.LongSound_open(fs);
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
	public Sound extractPart (double tmin, double tmax, int preserveTimes) {
		final AtomicReference<Long> iminRef = new AtomicReference<Long>();
		final AtomicReference<Long> imaxRef = new AtomicReference<Long>();
		
		long n = getWindowSamples(tmin, tmax, iminRef, imaxRef);
		if(n < 1) {
			throw new IllegalArgumentException("Window is outside of data range");
		}
		
		return Praat.INSTANCE.LongSound_extractPart(this, tmin, tmax, preserveTimes);
	}
	
	public boolean haveWindow (double tmin, double tmax) {
		return Praat.INSTANCE.LongSound_haveWindow(this, tmin, tmax);
	}
	
	public void getWindowExtrema (double tmin, double tmax, int channel, 
			AtomicReference<Double> minimum, AtomicReference<Double> maximum) {
		final Pointer pmin = new Memory(Native.getNativeSize(Double.class)*2);
		final Pointer pmax = pmin.getPointer(1);
		
		Praat.INSTANCE.LongSound_getWindowExtrema(this, tmin, tmax, channel, pmin, pmax);
		
		minimum.set(pmin.getDouble(0));
		minimum.set(pmax.getDouble(0));
	}
	
}
