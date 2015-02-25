package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;

public class Sampled extends Function {
	
	public double getXMin() {
		return Praat.INSTANCE.Sampled_getXMin(this);
	}
	
	public double getXMax() {
		return Praat.INSTANCE.Sampled_getXMax(this);
	}
	
	public long getNx() {
		return Praat.INSTANCE.Sampled_getNx(this);
	}

	public double getDx() {
		return Praat.INSTANCE.Sampled_getDx(this);
	}

	public double getX1() {
		return Praat.INSTANCE.Sampled_getX1(this);
	}
	
	public double indexToX (long i) {
		return Praat.INSTANCE.Sampled_indexToX(this, i);
	}

	public double xToIndex (double x) {
		return Praat.INSTANCE.Sampled_xToIndex(this, x);
	}

	public long xToLowIndex (double x) {
		return Praat.INSTANCE.Sampled_xToLowIndex(this, x);
	}

	public long xToHighIndex (double x) {
		return Praat.INSTANCE.Sampled_xToHighIndex(this, x);
	}

	public long xToNearestIndex (double x) {
		return Praat.INSTANCE.Sampled_xToNearestIndex(this, x);
	}

	public long getWindowSamples (double xmin, double xmax, 
			AtomicReference<Long> ixmin, AtomicReference<Long> ixmax) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		long retVal = Praat.INSTANCE.Sampled_getWindowSamples(this, xmin, xmax, 
				minPtr, maxPtr);
		
		ixmin.set(minPtr.getLong(0));
		ixmax.set(maxPtr.getLong(0));
		
		return retVal;
	}

	public void shortTermAnalysis (double windowDuration, double timeStep,
			AtomicReference<Long> numberOfFrames, AtomicReference<Double> firstTime) {
		final Pointer numPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer timePtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Sampled_shortTermAnalysis(this, windowDuration, timeStep, 
				numPtr, timePtr);
		
		numberOfFrames.set(numPtr.getLong(0));
		firstTime.set(timePtr.getDouble(0));
	}
	
	public double getValueAtSample (long isamp, long ilevel, int unit) {
		return Praat.INSTANCE.Sampled_getValueAtSample(this, isamp, ilevel, unit);
	}
	
	public double getValueAtX (double x, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getValueAtX(this, x, ilevel, unit, interpolate);
	}
	
	public long countDefinedSamples (long ilevel, int unit) {
		return Praat.INSTANCE.Sampled_countDefinedSamples(this, ilevel, unit);
	}
	
	public double[] getSortedValues (long ilevel, int unit) {
		final Pointer numPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		final Pointer ret = Praat.INSTANCE.Sampled_getSortedValues(this, ilevel, unit, numPtr);
		
		double[] retVal = ret.getDoubleArray(0, (int)numPtr.getLong(0));
		return retVal;
	}

	public double getQuantile
		(double xmin, double xmax, double quantile, long ilevel, int unit) {
		return Praat.INSTANCE.Sampled_getQuantile(this, xmin, xmax, quantile, ilevel, unit);
	}
	
	public double getMean
		(double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMean(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public double getMean_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMean_standardUnit(this, xmin, xmax, 
				ilevel, averagingUnit, interpolate);
	}
	
	public double getIntegral
		(double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getIntegral(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public double getIntegral_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, int interpolate) {
		return Praat.INSTANCE.Sampled_getIntegral_standardUnit(this, xmin, xmax, 
				ilevel, averagingUnit, interpolate);
	}
	
	public double getStandardDeviation
		(double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getStandardDeviation(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public double getStandardDeviation_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, int interpolate) {
		return Praat.INSTANCE.Sampled_getStandardDeviation_standardUnit(this, xmin, xmax, 
				ilevel, averagingUnit, interpolate);
	}

	public void getMinimumAndX (double xmin, double xmax, long ilevel, int unit, int interpolate,
		AtomicReference<Double> return_minimum, AtomicReference<Double> return_xOfMinimum) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Sampled_getMinimumAndX(this, xmin, xmax, ilevel, unit, 
				interpolate, minPtr, xPtr);
		
		return_minimum.set(minPtr.getDouble(0));
		return_xOfMinimum.set(xPtr.getDouble(0));
	}
	
	
	public double getMinimum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMinimum(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public double getXOfMinimum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getXOfMinimum(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public void getMaximumAndX (double xmin, double xmax, long ilevel, int unit, int interpolate,
			AtomicReference<Double> return_maximum, AtomicReference<Double> return_xOfMaximum) {
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Sampled_getMaximumAndX(this, xmin, xmax, ilevel, unit,
				interpolate, maxPtr, xPtr);
		
		return_maximum.set(maxPtr.getDouble(0));
		return_xOfMaximum.set(xPtr.getDouble(0));
	}
	
	public double getMaximum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMaximum(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public double getXOfMaximum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getXOfMaximum(this, xmin, xmax, ilevel, unit, interpolate);
	}

}
