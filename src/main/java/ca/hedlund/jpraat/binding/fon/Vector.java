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
	
	public double getValueAtX (double x, long channel, kVector_valueInterpolation interpolation) {
		return Praat.INSTANCE.Vector_getValueAtX(this, x, new NativeIntptr_t(channel), interpolation);
	}
	
	public void getMinimumAndX (double xmin, double xmax, long channel, kVector_peakInterpolation interpolation,
			 AtomicReference<Double> return_minimum, AtomicReference<Double> return_xOfMinimum) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Vector_getMinimumAndX(this, xmin, xmax, new NativeIntptr_t(channel), interpolation,
				minPtr, xPtr);
		
		return_minimum.set(minPtr.getDouble(0));
		return_xOfMinimum.set(xPtr.getDouble(0));
	}
	
	public void getMinimumAndXAndChannel (double xmin, double xmax, kVector_peakInterpolation interpolation,
			AtomicReference<Double> return_minimum, AtomicReference<Double> return_xOfMinimum, AtomicReference<Long> return_channelOfMinimum) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer chPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		Praat.INSTANCE.Vector_getMinimumAndXAndChannel(this, xmin, xmax, interpolation,
				minPtr, xPtr, chPtr);
		
		return_minimum.set(minPtr.getDouble(0));
		return_xOfMinimum.set(xPtr.getDouble(0));
		return_channelOfMinimum.set(chPtr.getLong(0));
	}
	
	public void getMaximumAndX (double xmin, double xmax, long channel, kVector_peakInterpolation interpolation,
			AtomicReference<Double> return_maximum, AtomicReference<Double> return_xOfMaximum) {
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		Praat.INSTANCE.Vector_getMaximumAndX(this, xmin, xmax, new NativeIntptr_t(channel), interpolation, 
				maxPtr, xPtr);
		
		return_maximum.set(maxPtr.getDouble(0));
		return_xOfMaximum.set(xPtr.getDouble(0));
	}

	public void getMaximumAndXAndChannel (double xmin, double xmax, kVector_peakInterpolation interpolation,
			AtomicReference<Double> return_maximum, AtomicReference<Double> return_xOfMaximum, AtomicReference<Long> return_channelOfMaximum) {
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer xPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer chPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		Praat.INSTANCE.Vector_getMaximumAndXAndChannel(this, xmin, xmax, interpolation, 
				maxPtr, xPtr, chPtr);
		
		return_maximum.set(maxPtr.getDouble(0));
		return_xOfMaximum.set(xPtr.getDouble(0));
		return_channelOfMaximum.set(chPtr.getLong(0));
	}
	
	public double getMinimum (double xmin, double xmax, kVector_peakInterpolation interpolation) {
		return Praat.INSTANCE.Vector_getMinimum(this, xmin, xmax, interpolation);
	}
	
	public double getAbsoluteExtremum (double xmin, double xmax, kVector_peakInterpolation interpolation) {
		return Praat.INSTANCE.Vector_getAbsoluteExtremum(this, xmin, xmax, interpolation);
	}
	
	public double getXOfMinimum ( double xmin, double xmax, kVector_peakInterpolation interpolation) {
		return Praat.INSTANCE.Vector_getXOfMinimum(this, xmin, xmax, interpolation);
	}
	
	public double getXOfMaximum (double xmin, double xmax, kVector_peakInterpolation interpolation) {
		return Praat.INSTANCE.Vector_getXOfMaximum(this, xmin, xmax, interpolation);
	}
	
	public long getChannelOfMinimum (double xmin, double xmax, kVector_peakInterpolation interpolation) {
		return Praat.INSTANCE.Vector_getChannelOfMinimum(this, xmin, xmax, interpolation).longValue();
	}
	
	public long getChannelOfMaximum (double xmin, double xmax, kVector_peakInterpolation interpolation) {
		return Praat.INSTANCE.Vector_getChannelOfMaximum(this, xmin, xmax, interpolation).longValue();
	}

	public double getMean (double xmin, double xmax, long channel) {
		return Praat.INSTANCE.Vector_getMean(this, xmin, xmax, new NativeIntptr_t(channel));
	}
	
	public double getStandardDeviation (double xmin, double xmax, long channel) {
		return Praat.INSTANCE.Vector_getStandardDeviation(this, xmin, xmax, new NativeIntptr_t(channel));
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
	
}
