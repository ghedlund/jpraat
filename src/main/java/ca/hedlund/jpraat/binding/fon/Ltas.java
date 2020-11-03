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

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;

public final class Ltas extends Vector {
	
	public static Ltas create (long nx, double dx) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Ltas_create_wrapped(new NativeIntptr_t(nx), dx);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	@Override
	public MelderQuantity domainQuantity() {
		return Praat.INSTANCE.Ltas_domainQuantity(this);
	}
	
	@Override
	public double convertSpecialToStandardUnit(double value, long ilevel, int unit) {
		return Praat.INSTANCE.Ltas_convertSpecialToStandardUnit(this, value, new NativeIntptr_t(ilevel), unit);
	}
	
	@Override
	public double convertStandardToSpecialUnit(double value, long ilevel, int unit) {
		return Praat.INSTANCE.Ltas_convertStandardToSpecialUnit(this, value, new NativeIntptr_t(ilevel), unit);
	}
	
	public double Ltas_getSlope (double f1min, double f1max, double f2min, double f2max, int averagingUnits) {
		return Praat.INSTANCE.Ltas_getSlope(this, f1min, f1max, f2min, f2max, averagingUnits);
	}
	
	public double Ltas_getLocalPeakHeight (double environmentMin, 
			double environmentMax, double peakMin, double peakMax, int averagingUnits) {
		return Praat.INSTANCE.Ltas_getLocalPeakHeight(this, environmentMin, environmentMax, peakMin, peakMax, averagingUnits);
	}
	
//	public static Ltas merge(java.util.Collection<Ltas> ltases) throws PraatException {
//		Collection col = Collection.create(Praat.getClassInfo(Ltas.class), ltases.size());
//		for(Ltas ltas:ltases) col.addItem(ltas);
//		
//		Ltas retVal = null;
//		try {
//			Praat.wrapperLock.lock();
//			retVal = Praat.INSTANCE.Ltases_merge_wrapped(col);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		return retVal;
//	}
//	
//	public static Ltas average(java.util.Collection<Ltas> ltases) throws PraatException {
//		Collection col = Collection.create(Praat.getClassInfo(Ltas.class), ltases.size());
//		for(Ltas ltas:ltases) col.addItem(ltas);
//		
//		Ltas retVal = null;
//		try {
//			Praat.wrapperLock.lock();
//			retVal = Praat.INSTANCE.Ltases_average_wrapped(col);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		return retVal;
//	}
	
	public Ltas computeTrendLine(double fmin, double fmax) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Ltas_computeTrendLine_wrapped(this, fmin, fmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Ltas subtractTrendLine(double fmin, double fmax) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Ltas_subtractTrendLine_wrapped(this, fmin, fmax);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Ltas PointProcess_Sound_to_Ltas(PointProcess pulses, Sound sound,
		double maximumFrequency, double bandWidth,
		double shortestPeriod, double longestPeriod, double maximumPeriodFactor) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.PointProcess_Sound_to_Ltas_wrapped(pulses, sound, 
					maximumFrequency, bandWidth, shortestPeriod, longestPeriod, maximumPeriodFactor);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Ltas PointProcess_Sound_to_Ltas_harmonics(PointProcess pulses, Sound sound,
		long maximumHarmonic,
		double shortestPeriod, double longestPeriod, double maximumPeriodFactor) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.PointProcess_Sound_to_Ltas_harmonics_wrapped(pulses, sound,
					new NativeIntptr_t(maximumHarmonic), shortestPeriod, longestPeriod, maximumPeriodFactor);
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
			retVal = Praat.INSTANCE.Ltas_to_Matrix_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

}
