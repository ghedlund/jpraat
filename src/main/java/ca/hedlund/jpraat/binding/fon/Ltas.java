package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.NativeLong;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.Collection;
import ca.hedlund.jpraat.binding.sys.MelderQuantity;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Ltas extends Vector {
	
	public static Ltas create (long nx, double dx) throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Ltas_create_wrapped(new NativeLong(nx), dx);
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
		return Praat.INSTANCE.Ltas_convertSpecialToStandardUnit(this, value, new NativeLong(ilevel), unit);
	}
	
	@Override
	public double convertStandardToSpecialUnit(double value, long ilevel, int unit) {
		return Praat.INSTANCE.Ltas_convertStandardToSpecialUnit(this, value, new NativeLong(ilevel), unit);
	}
	
	public double Ltas_getSlope (double f1min, double f1max, double f2min, double f2max, int averagingUnits) {
		return Praat.INSTANCE.Ltas_getSlope(this, f1min, f1max, f2min, f2max, averagingUnits);
	}
	
	public double Ltas_getLocalPeakHeight (double environmentMin, 
			double environmentMax, double peakMin, double peakMax, int averagingUnits) {
		return Praat.INSTANCE.Ltas_getLocalPeakHeight(this, environmentMin, environmentMax, peakMin, peakMax, averagingUnits);
	}
	
	public static Ltas merge(java.util.Collection<Ltas> ltases) throws PraatException {
		Collection col = Collection.create(Praat.getClassInfo(Ltas.class), ltases.size());
		for(Ltas ltas:ltases) col.addItem(ltas);
		
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Ltases_merge_wrapped(col);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Ltas average(java.util.Collection<Ltas> ltases) throws PraatException {
		Collection col = Collection.create(Praat.getClassInfo(Ltas.class), ltases.size());
		for(Ltas ltas:ltases) col.addItem(ltas);
		
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Ltases_average_wrapped(col);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
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
					maximumHarmonic, shortestPeriod, longestPeriod, maximumPeriodFactor);
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
