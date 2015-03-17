package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;

/**
 * Bindings for Praat Vector type
 */
public class Vector extends Matrix {
	
	public Vector() {
		super();
	}
	
	public Vector(Pointer p) {
		super(p);
	}
	
	public static final int CHANNEL_AVERAGE = 0;
	public static final int CHANNEL_1 = 1;
	public static final int CHANNEL_2 = 2;
	public static final int VALUE_INTERPOLATION_NEAREST = 0;
	public static final int VALUE_INTERPOLATION_LINEAR = 1;
	public static final int VALUE_INTERPOLATION_CUBIC = 2;
	public static final int VALUE_INTERPOLATION_SINC70 = 3;
	public static final int VALUE_INTERPOLATION_SINC700 = 4;
	
	public double getValueAtX (double x, long channel, int interpolation) {
		checkInterpolation(interpolation);
		return Praat.INSTANCE.Vector_getValueAtX(this, x, new NativeLong(channel), interpolation);
	}
	
	public void getMinimumAndX (double xmin, double xmax, long channel, int interpolation,
			 AtomicReference<Double> return_minimum, AtomicReference<Double> return_xOfMinimum) {
		checkInterpolation(interpolation);
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Vector_getMinimumAndX(this, xmin, xmax, new NativeLong(channel), interpolation,
				minPtr, xPtr);
		
		return_minimum.set(minPtr.getDouble(0));
		return_xOfMinimum.set(xPtr.getDouble(0));
	}
	
	public void getMinimumAndXAndChannel (double xmin, double xmax, int interpolation,
			AtomicReference<Double> return_minimum, AtomicReference<Double> return_xOfMinimum, AtomicReference<Long> return_channelOfMinimum) {
		checkInterpolation(interpolation);
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer chPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		Praat.INSTANCE.Vector_getMinimumAndXAndChannel(this, xmin, xmax, interpolation,
				minPtr, xPtr, chPtr);
		
		return_minimum.set(minPtr.getDouble(0));
		return_xOfMinimum.set(xPtr.getDouble(0));
		return_channelOfMinimum.set(chPtr.getLong(0));
	}
	
	public void getMaximumAndX (double xmin, double xmax, long channel, int interpolation,
			AtomicReference<Double> return_maximum, AtomicReference<Double> return_xOfMaximum) {
		checkInterpolation(interpolation);
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Vector_getMaximumAndX(this, xmin, xmax, new NativeLong(channel), interpolation, 
				maxPtr, xPtr);
		
		return_maximum.set(maxPtr.getDouble(0));
		return_xOfMaximum.set(xPtr.getDouble(0));
	}

	public void getMaximumAndXAndChannel (double xmin, double xmax, int interpolation,
			AtomicReference<Double> return_maximum, AtomicReference<Double> return_xOfMaximum, AtomicReference<Long> return_channelOfMaximum) {
		checkInterpolation(interpolation);
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer chPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		Praat.INSTANCE.Vector_getMaximumAndXAndChannel(this, xmin, xmax, interpolation, 
				maxPtr, xPtr, chPtr);
		
		return_maximum.set(maxPtr.getDouble(0));
		return_xOfMaximum.set(xPtr.getDouble(0));
		return_channelOfMaximum.set(chPtr.getLong(0));
	}
	
	public double getMinimum (double xmin, double xmax, int interpolation) {
		checkInterpolation(interpolation);
		return Praat.INSTANCE.Vector_getMinimum(this, xmin, xmax, interpolation);
	}
	
	public double getAbsoluteExtremum (double xmin, double xmax, int interpolation) {
		checkInterpolation(interpolation);
		return Praat.INSTANCE.Vector_getAbsoluteExtremum(this, xmin, xmax, interpolation);
	}
	
	public double getXOfMinimum ( double xmin, double xmax, int interpolation) {
		checkInterpolation(interpolation);
		return Praat.INSTANCE.Vector_getXOfMinimum(this, xmin, xmax, interpolation);
	}
	
	public double getXOfMaximum (double xmin, double xmax, int interpolation) {
		checkInterpolation(interpolation);
		return Praat.INSTANCE.Vector_getXOfMaximum(this, xmin, xmax, interpolation);
	}
	
	public long getChannelOfMinimum (double xmin, double xmax, int interpolation) {
		checkInterpolation(interpolation);
		return Praat.INSTANCE.Vector_getChannelOfMinimum(this, xmin, xmax, interpolation).longValue();
	}
	
	public long getChannelOfMaximum (double xmin, double xmax, int interpolation) {
		checkInterpolation(interpolation);
		return Praat.INSTANCE.Vector_getChannelOfMaximum(this, xmin, xmax, interpolation).longValue();
	}

	public double getMean (double xmin, double xmax, long channel) {
		return Praat.INSTANCE.Vector_getMean(this, xmin, xmax, new NativeLong(channel));
	}
	
	public double getStandardDeviation (double xmin, double xmax, long channel) {
		return Praat.INSTANCE.Vector_getStandardDeviation(this, xmin, xmax, new NativeLong(channel));
	}

	public void addScalar (double scalar) {
		Praat.INSTANCE.Vector_addScalar(this, scalar);
	}
	
	public void subtractMean () {
		Praat.INSTANCE.Vector_subtractMean(this);
	}
	
	public void multiplyByScalar (double scalar) {
		Praat.INSTANCE.Vector_multiplyByScalar(this, scalar);
	}
	
	public void scale (double scale) {
		Praat.INSTANCE.Vector_scale(this, scale);
	}
	
	private void checkInterpolation(int interpolation) {
		switch(interpolation) {
		case VALUE_INTERPOLATION_NEAREST:
		case VALUE_INTERPOLATION_LINEAR:
		case VALUE_INTERPOLATION_CUBIC:
		case VALUE_INTERPOLATION_SINC70:
		case VALUE_INTERPOLATION_SINC700:
			break;
		default:
			throw new IllegalArgumentException("Unknown interpolation: " + interpolation);
		}
	}
	
}
