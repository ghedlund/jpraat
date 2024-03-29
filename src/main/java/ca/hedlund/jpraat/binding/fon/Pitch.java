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

import com.sun.jna.*;

import ca.hedlund.jpraat.annotations.*;
import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;

@Header(value="fon/Pitch.h")
public final class Pitch extends Sampled {
	
	public Pitch() {
		super();
	}
	
	public Pitch(Pointer p) {
		super(p);
	}
	
	public static final int LEVEL_FREQUENCY = 1;
	public static final int LEVEL_STRENGTH = 2;

	public static final int STRENGTH_UNIT_min = 0;
	public static final int STRENGTH_UNIT_AUTOCORRELATION = 0;
	public static final int STRENGTH_UNIT_NOISE_HARMONICS_RATIO = 1;
	public static final int STRENGTH_UNIT_HARMONICS_NOISE_DB = 2;
	public static final int STRENGTH_UNIT_max = 2;

	public static final int NEAREST = 0;
	public static final int LINEAR =  1;

	/**
			Function:
				create an empty pitch contour (voiceless), or NULL if out of memory.
			Preconditions:
				tmax > tmin;
				nt >= 1;
				dt > 0.0;
				maxnCandidates >= 2;
			Postconditions:
				my xmin == tmin;
				my xmax == tmax;
				my nx == nt;
				my dx == dt;
				my x1 == t1;
				my ceiling == ceiling;
				my maxnCandidates == maxnCandidates;
				my frame [1..nt]. nCandidates == 1;
				my frame [1..nt]. candidate [1]. frequency == 0.0; // unvoiced
				my frame [1..nt]. candidate [1]. strength == 0.0; // aperiodic
				my frame [1..nt]. intensity == 0.0; // silent
	 */
	public static Pitch create (double tmin, double tmax, long nt, double dt, double t1,
			double ceiling, long maxnCandidates) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Pitch retVal = Praat.INSTANCE.Pitch_create_wrapped(tmin, tmax,
					new NativeIntptr_t(nt), dt, t1, ceiling, new NativeIntptr_t(maxnCandidates));
			Praat.checkAndClearLastError();
			return retVal;
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	/**
		Is the frame 'index' voiced?
		A frame is considered voiced if the frequency of its first candidate
		is greater than 0.0 but less than my ceiling.
		Precondition:
			index >= 1 && index <= my nx;
	 */
	public boolean isVoiced_i (long index) {
		return Praat.INSTANCE.Pitch_isVoiced_i(this, new NativeIntptr_t(index));
	}
	
	/**
		Are you voiced at time 't'?
		The answer is TRUE iff 't' lies within a voiced frame.
	 */
	public boolean isVoiced_t (double t) {
		return Praat.INSTANCE.Pitch_isVoiced_t(this, t);
	}
	
	public double getValueAtTime (double time, kPitch_unit unit, boolean interpolate) {
		return Praat.INSTANCE.Pitch_getValueAtTime(this, time, unit, (interpolate ? 1 : 0));
	}
	
	public double getStrengthAtTime (double time, kPitch_unit unit, boolean interpolate) {
		return Praat.INSTANCE.Pitch_getStrengthAtTime(this, time, unit, (interpolate ? 1 : 0));
	}

	public long countVoicedFrames () {
		return Praat.INSTANCE.Pitch_countVoicedFrames(this).longValue();
	}

	public double getMean (double tmin, double tmax, kPitch_unit unit) {
		return Praat.INSTANCE.Pitch_getMean(this, tmin, tmax, unit);
	}
	
	public double getMeanStrength (double tmin, double tmax, kPitch_unit unit) {
		return Praat.INSTANCE.Pitch_getMeanStrength(this, tmin, tmax, unit);
	}
	
	public double getQuantile (double tmin, double tmax, double quantile, kPitch_unit unit) {
		return Praat.INSTANCE.Pitch_getQuantile(this, tmin, tmax, quantile, unit);
	}
	
	public double getStandardDeviation (double tmin, double tmax, kPitch_unit unit) {
		return Praat.INSTANCE.Pitch_getStandardDeviation(this, tmin, tmax, unit);
	}
	
	public void getMaximumAndTime (double tmin, double tmax, kPitch_unit unit, boolean interpolate,
		AtomicReference<Double> return_maximum, AtomicReference<Double> return_timeOfMaximum) {
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer timePtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Pitch_getMaximumAndTime(this, tmin, tmax, unit, (interpolate ? 1 : 0),
				maxPtr, timePtr);
	
		return_maximum.set(maxPtr.getDouble(0));
		return_timeOfMaximum.set(timePtr.getDouble(0));
	}
	
	public double getMaximum (double tmin, double tmax, kPitch_unit unit, boolean interpolate) {
		return Praat.INSTANCE.Pitch_getMaximum(this, tmin, tmax, unit, (interpolate ? 1 : 0));
	}
	
	public double getTimeOfMaximum (double tmin, double tmax, kPitch_unit unit, boolean interpolate) {
		return Praat.INSTANCE.Pitch_getTimeOfMaximum(this, tmin, tmax, unit, (interpolate ? 1 : 0));
	}
	
	public void getMinimumAndTime (double tmin, double tmax, kPitch_unit unit, boolean interpolate,
			AtomicReference<Double> return_minimum, AtomicReference<Double> return_timeOfMinimum) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer timePtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Pitch_getMinimumAndTime(this, tmin, tmax, unit, (interpolate ? 1 : 0),
				minPtr, timePtr);
	
		return_minimum.set(minPtr.getDouble(0));
		return_timeOfMinimum.set(timePtr.getDouble(0));
	}
	
	public double getMinimum (double tmin, double tmax, kPitch_unit unit, boolean interpolate) {
		return Praat.INSTANCE.Pitch_getMinimum(this, tmin, tmax, unit, (interpolate ? 1 : 0));
	}
	
	public double getTimeOfMinimum (double tmin, double tmax, kPitch_unit unit, boolean interpolate) {
		return Praat.INSTANCE.Pitch_getTimeOfMinimum(this, tmin, tmax, unit, (interpolate ? 1 : 0));
	}
	
	/**
		Returns the largest number of candidates actually attested in a frame.
	 */
	public long getMaxnCandidates () {
		return Praat.INSTANCE.Pitch_getMaxnCandidates(this).longValue();
	}

	/**
		Postcondition:
			my ceiling = ceiling;
	 */
	public void setCeiling (double ceiling) {
		Praat.INSTANCE.Pitch_setCeiling(this, ceiling);
	}
	
	public void Pitch_pathFinder (double silenceThreshold, double voicingThreshold,
			double octaveCost, double octaveJumpCost, double voicedUnvoicedCost,
			double ceiling, int pullFormants) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Pitch_pathFinder_wrapped(this, silenceThreshold,
					voicingThreshold, octaveCost, octaveJumpCost,
					voicedUnvoicedCost, ceiling, pullFormants);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	/**
	 *  give information about frames that are different in me and thee. 
	 */
	public void difference (Pitch thee) {
		Praat.INSTANCE.Pitch_difference(this, thee);
	}

	public long getMeanAbsSlope_hertz (Pointer slope) {
		return Praat.INSTANCE.Pitch_getMeanAbsSlope_hertz(this, slope).longValue();
	}
	
	public long getMeanAbsSlope_mel (Pointer slope) {
		return Praat.INSTANCE.Pitch_getMeanAbsSlope_mel(this, slope).longValue();
	}
	
	public long getMeanAbsSlope_semitones (Pointer slope) {
		return Praat.INSTANCE.Pitch_getMeanAbsSlope_semitones(this, slope).longValue();
	}
	
	public long getMeanAbsSlope_erb (Pointer slope) {
		return Praat.INSTANCE.Pitch_getMeanAbsSlope_erb(this, slope).longValue();
	}

	/**
	   The value returned is the number of voiced frames (nVoiced);
	   this signals if the values are valid:
	   'value', 'minimum', 'maximum', and 'mean' are valid if nVoiced >= 1;
	   'variance' and 'slope' are valid if nVoiced >= 2.
	   Invalid variables are always set to 0.0.
	   'minimum', 'maximum', 'mean', and 'variance' may be NULL.
	 */
	public long getMeanAbsSlope_noOctave (Pointer slope) {
		return Praat.INSTANCE.Pitch_getMeanAbsSlope_noOctave(this, slope).longValue();
	}
	
	/**
	   Add octave jumps so that every pitch step,
	   including those across unvoiced frames,
	   does not exceed 1/2 octave.
	   Postcondition:
	      result -> ceiling = my ceiling * 2;
	 */
	public Pitch killOctaveJumps () throws PraatException {
		Pitch retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Pitch_killOctaveJumps_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	/* Interpolate the pitch values of unvoiced frames. */
	/* No extrapolation beyond first and last voiced frames. */
	public Pitch interpolate ()  throws PraatException {
		Pitch retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Pitch_interpolate_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public Pitch subtractLinearFit (kPitch_unit unit) throws PraatException {
		Pitch retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Pitch_subtractLinearFit_wrapped(this,
					unit);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	/* Smoothing by convolution with Gaussian curve.
	   Time domain: exp (- (pi t bandWidth) ^ 2)
	      down to 8.5 % for t = +- 0.5/bandWidth
	   Frequency domain: exp (- (f / bandWidth) ^ 2)
	      down to 1/e for f = +- bandWidth
	   Example:
	      if bandWidth = 10 Hz,
	      then the Gaussian curve has a 8.5% duration of 0.1 s,
	      and a 1/e low-pass point of 10 Hz.
	   Algorithm:
	      Interpolation of pitch at internal unvoiced parts.
	      Zeroth-degree extrapolation at edges (duration triples).
	      FFT; multiply by Gaussian; inverse FFT.
	      Cut back to normal duration.
	      Undo interpolation.
	 */
	public Pitch smooth (double bandWidth) throws PraatException {
		Pitch retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Pitch_smooth_wrapped(this, bandWidth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	/*
		Instead of the currently chosen candidate,
		choose the candidate with another ("target") frequency, determined by 'step'.
		E.g., for an upward octave jump, 'step' is 2.
		Only consider frequencies between (1 - precision) * targetFrequency
		and (1 - precision) * targetFrequency.
		Take the candidate nearest to targetFrequency,
		as long as that candidate is in between 0 and my ceiling.
	 */
	public void step (double step, double precision, double tmin, double tmax) {
		Praat.INSTANCE.Pitch_step(this, step, precision, tmin, tmax);
	}
	
	public PointProcess to_PointProcess() throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Pitch_to_PointProcess_wrapped (this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess to_PointProcess_cc ( Sound sound ) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_Pitch_to_PointProcess_cc_wrapped ( sound, this );
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public PointProcess to_PointProcess_peaks ( Sound sound, int includeMaxima, int includeMinima ) throws PraatException {
		PointProcess retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sound_Pitch_to_PointProcess_peaks_wrapped ( sound, this, includeMaxima, includeMinima );
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	@Override
	public MelderQuantity getDomainQuantity () {
		return Praat.INSTANCE.Pitch_domainQuantity(this);
	}
	
	@Override
	public int getMinimumUnit (long ilevel) {
		return Praat.INSTANCE.Pitch_getMinimumUnit(this, new NativeIntptr_t(ilevel));
	}
	
	@Override
	public int getMaximumUnit (long ilevel) {
		return Praat.INSTANCE.Pitch_getMaximumUnit(this, new NativeIntptr_t(ilevel));
	}

	public String getUnitText (long ilevel, int unit, int flags) {
		Str32 txt32 = Praat.INSTANCE.Pitch_getUnitText(this, new NativeIntptr_t(ilevel), unit, new NativeUint32(flags));
		return (txt32 == null ? null : txt32.toString());
	}
	
	@Override
	public boolean isUnitLogarithmic (long ilevel, int unit) {
		return Praat.INSTANCE.Pitch_isUnitLogarithmic(this, new NativeIntptr_t(ilevel), unit);
	}
	
	@Override
	public double convertStandardToSpecialUnit (double value, long ilevel, int unit) {
		return Praat.INSTANCE.Pitch_convertStandardToSpecialUnit(this, value, new NativeIntptr_t(ilevel), unit);
	}
	
	@Override
	public double convertSpecialToStandardUnit (double value, long ilevel, int unit) {
		return Praat.INSTANCE.Pitch_convertSpecialToStandardUnit(this, value, new NativeIntptr_t(ilevel), unit);
	}
	
	@Override
	public double getValueAtSample (long isamp, long ilevel, int unit) {
		return Praat.INSTANCE.Pitch_getValueAtSample(this, new NativeIntptr_t(isamp), new NativeIntptr_t(ilevel), unit);
	}
	
	public void formula(String formula, Interpreter interpreter)
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Pitch_formula_wrapped(this, new Str32(formula), interpreter);
		} catch (PraatException pe) {
			throw pe;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
}
