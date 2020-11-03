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
import ca.hedlund.jpraat.binding.stat.*;
import ca.hedlund.jpraat.exceptions.*;

public final class Spectrum extends Matrix {
	
	public static Spectrum create(double fmax, long nf) throws PraatException {
		Spectrum retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrum_create_wrapped(fmax, new NativeIntptr_t(nf));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
	@Override
	public double getValueAtSample(long isamp, long which, int units) {
		return Praat.INSTANCE.Spectrum_getValueAtSample(this, new NativeIntptr_t(isamp), new NativeIntptr_t(which), units);
	}
	
	public int getPowerDensityRange (AtomicReference<Double> minimum, AtomicReference<Double> maximum) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		int retVal = Praat.INSTANCE.Spectrum_getPowerDensityRange(this, minPtr, maxPtr);
		
		minimum.set(minPtr.getDouble(0));
		maximum.set(maxPtr.getDouble(0));
		
		return retVal;
	}
	
	public double getBandDensity (double fmin, double fmax) {
		return Praat.INSTANCE.Spectrum_getBandDensity(this, fmin, fmax);
	}
	
	public double getBandEnergy (double fmin, double fmax) {
		return Praat.INSTANCE.Spectrum_getBandEnergy(this, fmin, fmax);
	}
	
	public double getBandDensityDifference (double lowBandMin, double lowBandMax, double highBandMin, double HighBandMax) {
		return Praat.INSTANCE.Spectrum_getBandDensityDifference(this, lowBandMin, lowBandMax, highBandMin, HighBandMax);
	}
	
	public double getBandEnergyDifference (double lowBandMin, double lowBandMax, double highBandMin, double highBandMax) {
		return Praat.INSTANCE.Spectrum_getBandEnergyDifference(this, lowBandMin, lowBandMax, highBandMin, highBandMax);
	}
	
	public double getCentreOfGravity (double power) {
		return Praat.INSTANCE.Spectrum_getCentreOfGravity(this, power);
	}
	
	public double getCentralMoment (double moment, double power) {
		return Praat.INSTANCE.Spectrum_getCentralMoment(this, moment, power);
	}
	
	public double getStandardDeviation (double power) {
		return Praat.INSTANCE.Spectrum_getStandardDeviation(this, power);
	}
	
	public double getSkewness (double power) {
		return Praat.INSTANCE.Spectrum_getSkewness(this, power);
	}
	
	public double getKurtosis (double power) {
		return Praat.INSTANCE.Spectrum_getKurtosis(this, power);
	}

	public Table downto_Table (boolean includeBinNumbers, boolean includeFrequency,
			boolean includeRealPart, boolean includeImaginaryPart, boolean includeEnergyDensity, boolean includePowerDensity) throws PraatException {
		Table retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrum_downto_Table_wrapped(this, includeBinNumbers, 
					includeFrequency, includeRealPart, includeImaginaryPart, includeEnergyDensity, includePowerDensity);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}

	public Matrix to_Matrix () throws PraatException {
		Matrix retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrum_to_Matrix_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}

	public Spectrum cepstralSmoothing (double bandWidth) throws PraatException {
		Spectrum retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Spectrum_cepstralSmoothing_wrapped(this, bandWidth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}

	public void passHannBand (double fmin, double fmax, double smooth) {
		Praat.INSTANCE.Spectrum_passHannBand(this, fmin, fmax, smooth);
	}
	
	public void  stopHannBand (double fmin, double fmax, double smooth) {
		Praat.INSTANCE.Spectrum_stopHannBand(this, fmin, fmax, smooth);
	}

//	public void Spectrum_getNearestMaximum (double frequency, 
//			AtomicReference<Double> frequencyOfMaximum, AtomicReference<Double> heightOfMaximum) throws PraatException {
//		final Pointer freqPtr = new Memory(Native.getNativeSize(Double.TYPE));
//		final Pointer heightPtr = new Memory(Native.getNativeSize(Double.TYPE));
//		
//		try {
//			Praat.wrapperLock.lock();
//			Praat.INSTANCE.Spectrum_getNearestMaximum_wrapped(this, frequency, freqPtr, heightPtr);
//			Praat.checkAndClearLastError();
//			
//			frequencyOfMaximum.set(freqPtr.getDouble(0));
//			heightOfMaximum.set(heightPtr.getDouble(0));
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//	}
	
	public Sound to_Sound () throws PraatException {
		Sound retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrum_to_Sound_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e; 
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
	public Spectrogram to_Spectrogram () throws PraatException {
		Spectrogram retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrum_to_Spectrogram_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e; 
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
	public Ltas to_Ltas (double bandwidth) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrum_to_Ltas_wrapped(this, bandwidth);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public Ltas to_Ltas_1to1 () throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrum_to_Ltas_1to1_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
}
