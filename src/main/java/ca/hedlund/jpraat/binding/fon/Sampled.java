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

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.exceptions.*;

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
		return Praat.INSTANCE.Sampled_indexToX(this, new NativeIntptr_t(i));
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
		
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Sampled_shortTermAnalysis_wrapped(this, windowDuration, timeStep, 
					numPtr, timePtr);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		numberOfFrames.set(numPtr.getLong(0));
		firstTime.set(timePtr.getDouble(0));
	}
	
	public double getValueAtSample (long isamp, long ilevel, int unit) {
		return Praat.INSTANCE.Sampled_getValueAtSample(this, new NativeIntptr_t(isamp), new NativeIntptr_t(ilevel), unit);
	}
	
	public double getValueAtX (double x, long ilevel, int unit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getValueAtX(this, x, new NativeIntptr_t(ilevel), unit, (interpolate ? 1 : 0));
	}
	
	public long countDefinedSamples (long ilevel, int unit) {
		return Praat.INSTANCE.Sampled_countDefinedSamples(this, new NativeIntptr_t(ilevel), unit).longValue();
	}
	
	public double[] getSortedValues (long ilevel, int unit) {
		final Pointer numPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		final Pointer ret = Praat.INSTANCE.Sampled_getSortedValues(this, new NativeIntptr_t(ilevel), unit, numPtr);
		
		double[] retVal = ret.getDoubleArray(0, (int)numPtr.getLong(0));
		return retVal;
	}

	public double getQuantile
		(double xmin, double xmax, double quantile, long ilevel, int unit) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Sampled_getQuantile_wrapped(this,
					xmin, xmax, quantile, new NativeIntptr_t(ilevel), unit);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getMean
		(double xmin, double xmax, long ilevel, int unit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getMean(this, xmin, xmax, new NativeIntptr_t(ilevel), unit, (interpolate ? 1 : 0));
	}
	
	public double getMean_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getMean_standardUnit(this, xmin, xmax, 
				new NativeIntptr_t(ilevel), averagingUnit, (interpolate ? 1 : 0));
	}
	
	public double getIntegral
		(double xmin, double xmax, long ilevel, int unit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getIntegral(this, xmin, xmax, new NativeIntptr_t(ilevel), unit, (interpolate ? 1 : 0));
	}
	
	public double getIntegral_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getIntegral_standardUnit(this, xmin, xmax, 
				new NativeIntptr_t(ilevel), averagingUnit, (interpolate ? 1 : 0));
	}
	
	public double getStandardDeviation
		(double xmin, double xmax, long ilevel, int unit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getStandardDeviation(this, xmin, xmax, new NativeIntptr_t(ilevel), unit, (interpolate ? 1 : 0));
	}
	
	public double getStandardDeviation_standardUnit
		(double xmin, double xmax, long ilevel, int averagingUnit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getStandardDeviation_standardUnit(this, xmin, xmax, 
				new NativeIntptr_t(ilevel), averagingUnit, (interpolate ? 1 : 0));
	}

	public void getMinimumAndX (double xmin, double xmax, long ilevel, int unit, boolean interpolate,
		AtomicReference<Double> return_minimum, AtomicReference<Double> return_xOfMinimum) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Sampled_getMinimumAndX(this, xmin, xmax, new NativeIntptr_t(ilevel), unit, 
				(interpolate ? 1 : 0), minPtr, xPtr);
		
		return_minimum.set(minPtr.getDouble(0));
		return_xOfMinimum.set(xPtr.getDouble(0));
	}
	
	
	public double getMinimum (double xmin, double xmax, long ilevel, int unit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getMinimum(this, xmin, xmax, new NativeIntptr_t(ilevel), unit, (interpolate ? 1 : 0));
	}
	
	public double getXOfMinimum (double xmin, double xmax, long ilevel, int unit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getXOfMinimum(this, xmin, xmax, new NativeIntptr_t(ilevel), unit, (interpolate ? 1 : 0));
	}
	
	public void getMaximumAndX (double xmin, double xmax, long ilevel, int unit, boolean interpolate,
			AtomicReference<Double> return_maximum, AtomicReference<Double> return_xOfMaximum) {
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Sampled_getMaximumAndX(this, xmin, xmax, new NativeIntptr_t(ilevel), unit,
				(interpolate ? 1 : 0), maxPtr, xPtr);
		
		return_maximum.set(maxPtr.getDouble(0));
		return_xOfMaximum.set(xPtr.getDouble(0));
	}
	
	public double getMaximum (double xmin, double xmax, long ilevel, int unit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getMaximum(this, xmin, xmax, new NativeIntptr_t(ilevel), unit, (interpolate ? 1 : 0));
	}
	
	public double getXOfMaximum (double xmin, double xmax, long ilevel, int unit, boolean interpolate) {
		return Praat.INSTANCE.Sampled_getXOfMaximum(this, xmin, xmax, new NativeIntptr_t(ilevel), unit, (interpolate ? 1 : 0));
	}

}
