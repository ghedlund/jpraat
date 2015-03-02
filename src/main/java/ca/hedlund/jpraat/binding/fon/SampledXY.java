package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.NativeLong;

import ca.hedlund.jpraat.binding.Praat;

public class SampledXY extends Sampled {
	
	public double getYMin() {
		return Praat.INSTANCE.SampledXY_getYMin(this);
	}	
	
	public double getYMax() {
		return Praat.INSTANCE.SampledXY_getYMax(this);
	}
	
	public long getNy() {
		return Praat.INSTANCE.SampledXY_getNy(this).longValue();
	}
	
	public double getDy() {
		return Praat.INSTANCE.SampledXY_GetDy(this);
	}
	
	public double getY1() {
		return Praat.INSTANCE.SampledXY_getY1(this);
	}
	
	public double indexToY (long   index) {
		return Praat.INSTANCE.SampledXY_indexToY(this, new NativeLong(index));
	}
	
	public double yToIndex (double y) {
		return Praat.INSTANCE.SampledXY_yToIndex(this, y);
	}
	
	public long yToLowIndex     (double y) {
		return Praat.INSTANCE.SampledXY_yToLowIndex(this, y).longValue();
	}
	
	public long yToHighIndex    (double y) {
		return Praat.INSTANCE.SampledXY_yToHighIndex(this, y).longValue();
	}
	
	public long yToNearestIndex (double y) {
		return Praat.INSTANCE.SampledXY_yToNearestIndex(this, y).longValue();
	}
	
}
