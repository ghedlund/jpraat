package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Declared;

/**
 * Bindings for Praat Vector type
 */
public class Vector extends Matrix {
	
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
		return Praat.INSTANCE.Vector_getValueAtX(this, x, channel, interpolation);
	}
	
	public void getMinimumAndX (double xmin, double xmax, long channel, int interpolation,
			 Pointer return_minimum, Pointer return_xOfMinimum) {
		checkInterpolation(interpolation);
		Praat.INSTANCE.Vector_getMinimumAndX(this, xmin, xmax, channel, interpolation,
				return_minimum, return_xOfMinimum);
	}
	
	public void getMinimumAndXAndChannel (double xmin, double xmax, int interpolation,
			Pointer return_minimum, Pointer return_xOfMinimum, Pointer return_channelOfMinimum) {
		checkInterpolation(interpolation);
		Praat.INSTANCE.Vector_getMinimumAndXAndChannel(this, xmin, xmax, interpolation,
				return_minimum, return_xOfMinimum, return_channelOfMinimum);
	}
	
	public void getMaximumAndX (double xmin, double xmax, long channel, int interpolation,
			Pointer return_maximum, Pointer return_xOfMaximum) {
		checkInterpolation(interpolation);
		Praat.INSTANCE.Vector_getMaximumAndX(this, xmin, xmax, channel, interpolation, 
				return_maximum, return_xOfMaximum);
	}

	public void getMaximumAndXAndChannel (double xmin, double xmax, int interpolation,
			Pointer return_maximum, Pointer return_xOfMaximum, Pointer return_channelOfMaximum) {
		checkInterpolation(interpolation);
		Praat.INSTANCE.Vector_getMaximumAndXAndChannel(this, xmin, xmax, interpolation, 
				return_maximum, return_xOfMaximum, return_channelOfMaximum);
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
		return Praat.INSTANCE.Vector_getChannelOfMinimum(this, xmin, xmax, interpolation);
	}
	
	public long getChannelOfMaximum (double xmin, double xmax, int interpolation) {
		checkInterpolation(interpolation);
		return Praat.INSTANCE.Vector_getChannelOfMaximum(this, xmin, xmax, interpolation);
	}

	public double getMean (double xmin, double xmax, long channel) {
		return Praat.INSTANCE.Vector_getMean(this, xmin, xmax, channel);
	}
	
	public double getStandardDeviation (double xmin, double xmax, long channel) {
		return Praat.INSTANCE.Vector_getStandardDeviation(this, xmin, xmax, channel);
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
