package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Sampled extends Function {
	
	public Sampled() {
		super();
	}
	
	public Sampled(Pointer p) {
		super(p);
	}
	
	public double getXMin() {
		return Praat.INSTANCE.Sampled_getXMin(this);
	}
	
	public double getXMax() {
		return Praat.INSTANCE.Sampled_getXMax(this);
	}
	
	public long getNx() {
		return Praat.INSTANCE.Sampled_getNx(this).longValue();
	}

	public double getDx() {
		return Praat.INSTANCE.Sampled_getDx(this);
	}

	public double getX1() {
		return Praat.INSTANCE.Sampled_getX1(this);
	}
	
	public double indexToX (long i) {
		return Praat.INSTANCE.Sampled_indexToX(this, new NativeLong(i));
	}

	public double xToIndex (double x) {
		return Praat.INSTANCE.Sampled_xToIndex(this, x);
	}

	public long xToLowIndex (double x) {
		return Praat.INSTANCE.Sampled_xToLowIndex(this, x).longValue();
	}

	public long xToHighIndex (double x) {
		return Praat.INSTANCE.Sampled_xToHighIndex(this, x).longValue();
	}

	public long xToNearestIndex (double x) {
		return Praat.INSTANCE.Sampled_xToNearestIndex(this, x).longValue();
	}

	public long getWindowSamples (double xmin, double xmax, 
			AtomicReference<Long> ixmin, AtomicReference<Long> ixmax) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		long retVal = Praat.INSTANCE.Sampled_getWindowSamples(this, xmin, xmax, 
				minPtr, maxPtr).longValue();
		
		ixmin.set(minPtr.getLong(0));
		ixmax.set(maxPtr.getLong(0));
		
		return retVal;
	}

	public void shortTermAnalysis (double windowDuration, double timeStep,
			AtomicReference<Long> numberOfFrames, AtomicReference<Double> firstTime) throws PraatException {
		final Pointer numPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer timePtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Sampled_shortTermAnalysis_wrapped(this, windowDuration, timeStep, 
				numPtr, timePtr);
		Praat.checkLastError();
		
		numberOfFrames.set(numPtr.getLong(0));
		firstTime.set(timePtr.getDouble(0));
	}
	
	public double getValueAtSample (long isamp, long ilevel, int unit) {
		return Praat.INSTANCE.Sampled_getValueAtSample(this, new NativeLong(isamp), new NativeLong(ilevel), unit);
	}
	
	public double getValueAtX (double x, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getValueAtX(this, x, new NativeLong(ilevel), unit, interpolate);
	}
	
	public long countDefinedSamples (long ilevel, int unit) {
		return Praat.INSTANCE.Sampled_countDefinedSamples(this, new NativeLong(ilevel), unit).longValue();
	}
	
	public double[] getSortedValues (long ilevel, int unit) {
		final Pointer numPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		final Pointer ret = Praat.INSTANCE.Sampled_getSortedValues(this, new NativeLong(ilevel), unit, numPtr);
		
		double[] retVal = ret.getDoubleArray(0, (int)numPtr.getLong(0));
		return retVal;
	}

	public double getQuantile
		(double xmin, double xmax, double quantile, long ilevel, int unit) throws PraatException {
		double retVal = Praat.INSTANCE.Sampled_getQuantile_wrapped (this, xmin, xmax, quantile, new NativeLong(ilevel), unit);
		Praat.checkLastError();
		return retVal;
	}
	
	public double getMean
		(double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMean(this, xmin, xmax, new NativeLong(ilevel), unit, interpolate);
	}
	
	public double getMean_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMean_standardUnit(this, xmin, xmax, 
				new NativeLong(ilevel), averagingUnit, interpolate);
	}
	
	public double getIntegral
		(double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getIntegral(this, xmin, xmax, new NativeLong(ilevel), unit, interpolate);
	}
	
	public double getIntegral_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, int interpolate) {
		return Praat.INSTANCE.Sampled_getIntegral_standardUnit(this, xmin, xmax, 
				new NativeLong(ilevel), averagingUnit, interpolate);
	}
	
	public double getStandardDeviation
		(double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getStandardDeviation(this, xmin, xmax, new NativeLong(ilevel), unit, interpolate);
	}
	
	public double getStandardDeviation_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, int interpolate) {
		return Praat.INSTANCE.Sampled_getStandardDeviation_standardUnit(this, xmin, xmax, 
				new NativeLong(ilevel), averagingUnit, interpolate);
	}

	public void getMinimumAndX (double xmin, double xmax, long ilevel, int unit, int interpolate,
		AtomicReference<Double> return_minimum, AtomicReference<Double> return_xOfMinimum) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Sampled_getMinimumAndX(this, xmin, xmax, new NativeLong(ilevel), unit, 
				interpolate, minPtr, xPtr);
		
		return_minimum.set(minPtr.getDouble(0));
		return_xOfMinimum.set(xPtr.getDouble(0));
	}
	
	
	public double getMinimum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMinimum(this, xmin, xmax, new NativeLong(ilevel), unit, interpolate);
	}
	
	public double getXOfMinimum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getXOfMinimum(this, xmin, xmax, new NativeLong(ilevel), unit, interpolate);
	}
	
	public void getMaximumAndX (double xmin, double xmax, long ilevel, int unit, int interpolate,
			AtomicReference<Double> return_maximum, AtomicReference<Double> return_xOfMaximum) {
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Sampled_getMaximumAndX(this, xmin, xmax, new NativeLong(ilevel), unit,
				interpolate, maxPtr, xPtr);
		
		return_maximum.set(maxPtr.getDouble(0));
		return_xOfMaximum.set(xPtr.getDouble(0));
	}
	
	public double getMaximum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMaximum(this, xmin, xmax, new NativeLong(ilevel), unit, interpolate);
	}
	
	public double getXOfMaximum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getXOfMaximum(this, xmin, xmax, new NativeLong(ilevel), unit, interpolate);
	}

}
