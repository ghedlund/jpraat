/*
 * Copyright (C) 2005-2020 Gregory Hedlund
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

import com.sun.jna.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.exceptions.*;

public final class Intensity extends Vector {
	
	public Intensity() {
		super();
	}
	
	public Intensity(Pointer p) {
		super(p);
	}
	
	public static final int UNITS_ENERGY = 1;
	public static final int UNITS_SONES = 2;
	public static final int UNITS_DB = 3;

	public static final int AVERAGING_MEDIAN = 0;
	public static final int AVERAGING_ENERGY = 1;
	public static final int AVERAGING_SONES = 2;
	public static final int AVERAGING_DB = 3;
	
	public static Intensity create (double tmin, double tmax, long nt, double dt, double t1) throws PraatException {
		Intensity retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Intensity_create_wrapped(tmin, tmax,
					new NativeIntptr_t(nt), dt, t1);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Matrix Intensity_to_Matrix (Intensity me) throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Intensity_to_Matrix_wrapped(me);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Intensity Matrix_to_Intensity (Matrix me) throws PraatException {
		Intensity retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_to_Intensity_wrapped(me);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Matrix to_Matrix() throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Intensity_to_Matrix(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public double getQuantile (double tmin, double tmax, double quantile) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Intensity_getQuantile_wrapped(this,
					tmin, tmax, quantile);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getAverage (double tmin, double tmax, int averagingMethod) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Intensity_getAverage_wrapped(this, tmin,
					tmax, averagingMethod);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
}
