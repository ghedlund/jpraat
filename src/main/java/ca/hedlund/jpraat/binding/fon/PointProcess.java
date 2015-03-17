package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.annotations.NativeType;
import ca.hedlund.jpraat.annotations.Wrapped;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public class PointProcess extends Function {
	
	public PointProcess() {
		super();
	}
	
	public PointProcess(Pointer p) {
		super(p);
	}
	
	public static PointProcess create (double startingTime, double finishingTime, long initialMaxnt)
			throws PraatException {
		PointProcess retVal = 
				Praat.INSTANCE.PointProcess_create_wrapped(startingTime, finishingTime, new NativeLong(initialMaxnt));
		Praat.checkLastError();
		return retVal;
	}

	public static PointProcess createPoissonProcess (double startingTime, double finishingTime, double density) 
			throws PraatException {
		PointProcess retVal = 
				Praat.INSTANCE.PointProcess_createPoissonProcess_wrapped(startingTime, finishingTime, density);
		Praat.checkLastError();
		return retVal;
	}
	
	public void init (double startingTime, double finishingTime, long initialMaxnt) {
		Praat.INSTANCE.PointProcess_init(this, startingTime, finishingTime, new NativeLong(initialMaxnt));
	}
	
	public long getLowIndex (double t) {
		return Praat.INSTANCE.PointProcess_getLowIndex(this, t).longValue();
	}
	
	public long getHighIndex (double t) {
		return Praat.INSTANCE.PointProcess_getHighIndex(this, t).longValue();
	}
	
	public long getNearestIndex (double t) {
		return Praat.INSTANCE.PointProcess_getNearestIndex(this, t).longValue();
	}
	
	public long getWindowPoints (double tmin, double tmax, 
			AtomicReference<Long> imin, AtomicReference<Long> imax) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		long retVal = Praat.INSTANCE.PointProcess_getWindowPoints(this, tmin, tmax, minPtr, maxPtr).longValue();
		
		imin.set(minPtr.getLong(0));
		imax.set(maxPtr.getLong(0));
		
		return retVal;
	}

	public void addPoint (double t) throws PraatException {
		Praat.INSTANCE.PointProcess_addPoint_wrapped(this, t);
		Praat.checkLastError();
	}
	
	public long findPoint (double t) {
		return Praat.INSTANCE.PointProcess_findPoint(this, t).longValue();
	}
	
	public void removePoint (long index) {
		Praat.INSTANCE.PointProcess_removePoint(this, new NativeLong(index));
	}
	
	public void removePointNear (double t) {
		Praat.INSTANCE.PointProcess_removePointNear(this, t);
	}
	
	public void removePoints (long first, long last) {
		Praat.INSTANCE.PointProcess_removePoints(this, new NativeLong(first), new NativeLong(last));
	}
	
	public void removePointsBetween (double fromTime, double toTime) {
		Praat.INSTANCE.PointProcess_removePointsBetween(this, fromTime, toTime);
	}

	public double getInterval (double t) {
		return Praat.INSTANCE.PointProcess_getInterval(this, t);
	}
	
	public static PointProcess union(PointProcess p1, PointProcess p2) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.PointProcesses_union_wrapped(p1, p2);
		Praat.checkLastError();
		return retVal;
	}
	
	public PointProcess union (PointProcess thee) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.PointProcesses_union_wrapped(this, thee);
		Praat.checkLastError();
		return retVal;
	}
	
	public static PointProcess intersection (PointProcess p1, PointProcess p2) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.PointProcesses_intersection_wrapped(p1, p2);
		Praat.checkLastError();
		return retVal;
	}
	
	public PointProcess intersection (PointProcess thee) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.PointProcesses_intersection_wrapped(this, thee);
		Praat.checkLastError();
		return retVal;
	}
	
	public static PointProcess difference (PointProcess p1, PointProcess p2)
		throws PraatException {
		PointProcess retVal = Praat.INSTANCE.PointProcesses_difference_wrapped(p1, p2);
		Praat.checkLastError();
		return retVal;
	}
	
	public PointProcess difference (PointProcess thee) throws PraatException {
		PointProcess retVal = Praat.INSTANCE.PointProcesses_difference_wrapped(this, thee);
		Praat.checkLastError();
		return retVal;
	}
	
	public void fill (double tmin, double tmax, double period) 
		throws PraatException {
		Praat.INSTANCE.PointProcess_fill_wrapped(this, tmin, tmax, period);
		Praat.checkLastError();
	}
	
	public void voice (double period, double maxT) 
		throws PraatException {
		Praat.INSTANCE.PointProcess_voice_wrapped(this, period, maxT);
		Praat.checkLastError();
	}
	
	public long getNumberOfPeriods (double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor) {
		return Praat.INSTANCE.PointProcess_getNumberOfPeriods(this, tmin, tmax, minimumPeriod,
				maximumPeriod, maximumPeriodFactor).longValue();
	}
	
	public double getMeanPeriod (double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor) {
		return Praat.INSTANCE.PointProcess_getMeanPeriod(this, tmin, tmax, minimumPeriod, maximumPeriod, maximumPeriodFactor);
	}

	public double PointProcess_getStdevPeriod (double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor) {
		return Praat.INSTANCE.PointProcess_getStdevPeriod(this, tmin, tmax, minimumPeriod, maximumPeriod, maximumPeriodFactor);
	}
	
}
