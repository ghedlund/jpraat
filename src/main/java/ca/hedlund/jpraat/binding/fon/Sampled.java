package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Declared;

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

	public long getWindowSamples (double xmin, double xmax, Pointer ixmin, Pointer ixmax) {
		return Praat.INSTANCE.Sampled_getWindowSamples(this, xmin, xmax, ixmin, ixmax);
	}

	public void shortTermAnalysis (double windowDuration, double timeStep,
			Pointer numberOfFrames, Pointer firstTime) {
		Praat.INSTANCE.Sampled_shortTermAnalysis(this, windowDuration, timeStep, numberOfFrames, firstTime);
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
	
	public Pointer getSortedValues (long ilevel, int unit, Pointer numberOfValues) {
		return Praat.INSTANCE.Sampled_getSortedValues(this, ilevel, unit, numberOfValues);
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
		Pointer return_minimum, Pointer return_xOfMinimum) {
		Praat.INSTANCE.Sampled_getMinimumAndX(this, xmin, xmax, ilevel, unit, 
				interpolate, return_minimum, return_xOfMinimum);
	}
	
	
	public double getMinimum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMinimum(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public double getXOfMinimum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getXOfMinimum(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public void getMaximumAndX (double xmin, double xmax, long ilevel, int unit, int interpolate,
		Pointer return_maximum, Pointer return_xOfMaximum) {
		Praat.INSTANCE.Sampled_getMaximumAndX(this, xmin, xmax, ilevel, unit,
				interpolate, return_maximum, return_xOfMaximum);
	}
	
	public double getMaximum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getMaximum(this, xmin, xmax, ilevel, unit, interpolate);
	}
	
	public double getXOfMaximum (double xmin, double xmax, long ilevel, int unit, int interpolate) {
		return Praat.INSTANCE.Sampled_getXOfMaximum(this, xmin, xmax, ilevel, unit, interpolate);
	}

}
