package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;

public class Formant extends Sampled {
	
	/**
		Function:
			return a new instance of Formant, or NULL if out of memory.
		Preconditions:
			nt >= 1;
			dt > 0.0;
			maxnFormants >= 1;
		Postconditions:
			my xmin = tmin;
			my xmax = tmax;
			my nx = nt;
			my dx = dt;
			my x1 = t1;
			my maximumNumberOfPairs == maxnFormants;
			my frames [1..nt]. intensity == 0.0;
			my frames [1..nt]. numberOfPairs == 0;
			my frames [1..nt]. formants [1..maxnFormants] = 0.0;
			my frames [1..nt]. bandwidths [1..maxnFormants] = 0.0;
	 */
	public static Formant create (double tmin, double tmax, long nt, double dt, double t1, int maxnFormants) {
		return Praat.INSTANCE.Formant_create(tmin, tmax, nt, dt, t1, maxnFormants);
	}
	
	public long getMinNumFormants () {
		return Praat.INSTANCE.Formant_getMinNumFormants(this);
	}
	
	public long getMaxNumFormants () {
		return Praat.INSTANCE.Formant_getMaxNumFormants(this);
	}

	public double getValueAtTime (int iformant, double time, int bark) {
		return Praat.INSTANCE.Formant_getValueAtTime(this, iformant, time, bark);
	}
	
	public double getBandwidthAtTime (int iformant, double time, int bark) {
		return Praat.INSTANCE.Formant_getBandwidthAtTime(this, iformant, time, bark);
	}

	public void getExtrema (int iformant, double tmin, double tmax, Pointer fmin, Pointer fmax) {
		Praat.INSTANCE.Formant_getExtrema(this, iformant, tmin, tmax, fmin, fmax);
	}
	
	public void getMinimumAndTime (int iformant, double tmin, double tmax, int bark, int interpolate,
		Pointer return_minimum, Pointer return_timeOfMinimum) {
		Praat.INSTANCE.Formant_getMinimumAndTime(this, iformant, tmin, tmax, bark, interpolate, return_minimum, return_timeOfMinimum);
	}
	
	public void getMaximumAndTime (int iformant, double tmin, double tmax, int bark, int interpolate,
		Pointer return_maximum, Pointer return_timeOfMaximum) {
		Praat.INSTANCE.Formant_getMaximumAndTime(this, iformant, tmin, tmax, bark, interpolate, return_maximum, return_timeOfMaximum);
	}
	
	public double getMinimum (int iformant, double tmin, double tmax, int bark, int interpolate) {
		return Praat.INSTANCE.Formant_getMinimum(this, iformant, tmin, tmax, bark, interpolate);
	}
	
	public double getMaximum (int iformant, double tmin, double tmax, int bark, int interpolate) {
		return Praat.INSTANCE.Formant_getMaximum(this, iformant, tmin, tmax, bark, interpolate);
	}
	
	public double getTimeOfMaximum (int iformant, double tmin, double tmax, int bark, int interpolate) {
		return Praat.INSTANCE.Formant_getTimeOfMaximum(this, iformant, tmin, tmax, bark, interpolate);
	}
	
	public double getTimeOfMinimum (int iformant, double tmin, double tmax, int bark, int interpolate) {
		return Praat.INSTANCE.Formant_getTimeOfMinimum(this, iformant, tmin, tmax, bark, interpolate);
	}

	public double getQuantile (int iformant, double quantile, double tmin, double tmax, int bark) {
		return Praat.INSTANCE.Formant_getQuantile(this, iformant, quantile, tmin, tmax, bark);
	}
	
	public double getQuantileOfBandwidth (int iformant, double quantile, double tmin, double tmax, int bark) {
		return Praat.INSTANCE.Formant_getQuantileOfBandwidth(this, iformant, quantile, tmin, tmax, bark);
	}
	
	public double getMean (int iformant, double tmin, double tmax, int bark) {
		return Praat.INSTANCE.Formant_getMean(this, iformant, tmin, tmax, bark);
	}
	
	public double getStandardDeviation (int iformant, double tmin, double tmax, int bark) {
		return Praat.INSTANCE.Formant_getStandardDeviation(this, iformant, tmin, tmax, bark);
	}

	public void sort () {
		Praat.INSTANCE.Formant_sort(this);
	}

	public Matrix to_Matrix (int iformant) {
		return Praat.INSTANCE.Formant_to_Matrix(this, iformant);
	}
	
	public Matrix to_Matrix_bandwidths (int iformant) {
		return Praat.INSTANCE.Formant_to_Matrix_bandwidths(this, iformant);
	}

	public Formant tracker (int numberOfTracks,
		double refF1, double refF2, double refF3, double refF4, double refF5,
		double dfCost,   /* Per kHz. */
		double bfCost, double octaveJumpCost) {
		return Praat.INSTANCE.Formant_tracker(this, numberOfTracks, refF1, refF2, refF3, refF4, refF5, dfCost, bfCost, octaveJumpCost);
	}

	public void list (boolean includeFrameNumbers,
		boolean includeTimes, int timeDecimals,
		boolean includeIntensity, int intensityDecimals,
		boolean includeNumberOfFormants, int frequencyDecimals,
		boolean includeBandwidths) {
		Praat.INSTANCE.Formant_list(this, includeFrameNumbers, includeTimes, timeDecimals, includeIntensity, intensityDecimals, includeNumberOfFormants, frequencyDecimals, includeBandwidths);
	}

}
