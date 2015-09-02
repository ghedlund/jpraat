package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class Matrix extends SampledXY {
	
	public Matrix( ){
		super();
	}
	
	public Matrix(Pointer p) {
		super(p);
	}
	
	public static Matrix create
	(double xmin, double xmax, long nx, double dx, double x1,
	 double ymin, double ymax, long ny, double dy, double y1) throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_create_wrapped(xmin, xmax,
					new NativeLong(nx), dx, x1, ymin, ymax, new NativeLong(ny), dy,
					y1);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public static Matrix createSimple (long numberOfRows, long numberOfColumns) throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_createSimple_wrapped(
					new NativeLong(numberOfRows), new NativeLong(numberOfColumns));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public long getWindowSamplesX (double xmin, double xmax, 
			AtomicReference<Long> ixmin, AtomicReference<Long> ixmax) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		long retVal = Praat.INSTANCE.Matrix_getWindowSamplesX(this, xmin, xmax, 
				minPtr, maxPtr).longValue();
		
		ixmin.set(minPtr.getLong(0));
		ixmax.set(maxPtr.getLong(0));
		
		return retVal;
	}
	
	public double getValueAtXY (double x, double y) {
		return Praat.INSTANCE.Matrix_getValueAtXY(this, x, y);
	}
	
	public double getSum () {
		return Praat.INSTANCE.Matrix_getSum(this);
	}
	
	public double getNorm () {
		return Praat.INSTANCE.Matrix_getNorm(this);
	}
	
	public double columnToX (double column) {
		/* Return my x1 + (column - 1) * my dx.	 */
		return Praat.INSTANCE.Matrix_columnToX(this, column);
	}
	
	/* Return my y1 + (row - 1) * my dy. */
	public double rowToY (double row) {
		return Praat.INSTANCE.Matrix_rowToY(this, row);
	}

	/* Return (x - xmin) / my dx + 1. */
	public double xToColumn (double x) {
		return Praat.INSTANCE.Matrix_xToColumn(this, x);
	}

	/* Return floor (xToColumn (me, x)). */
	public long xToLowColumn (double x) {
		return Praat.INSTANCE.Matrix_xToLowColumn(this, x).longValue();
	}

	/* Return ceil (xToColumn (me, x)). */
	public long xToHighColumn (double x) {
		return Praat.INSTANCE.Matrix_xToHighColumn(this, x).longValue();
	}

	/* Return floor (xToColumn (me, x) + 0.5). */
	public long xToNearestColumn (double x) {
		return Praat.INSTANCE.Matrix_xToNearestColumn(this, x).longValue();
	}

	/* Return (y - ymin) / my dy + 1. */
	public double yToRow (double y) {
		return Praat.INSTANCE.Matrix_yToRow(this, y);
	}

	/* Return floor (yToRow (me, y)). */
	public long yToLowRow (double y) {
		return Praat.INSTANCE.Matrix_yToLowRow(this, y).longValue();
	}

	/* Return ceil (yToRow (me, y)). */
	public long yToHighRow (double x) {
		return Praat.INSTANCE.Matrix_yToHighRow(this, x).longValue();
	}

	/* Return floor (yToRow (me, y) + 0.5). */
	public long yToNearestRow (double y) {
		return Praat.INSTANCE.Matrix_yToNearestRow(this, y).longValue();
	}

	public long getWindowSamplesY (double ymin, double ymax, 
			AtomicReference<Long> iymin, AtomicReference<Long> iymax) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		long retVal = Praat.INSTANCE.Matrix_getWindowSamplesY(this, ymin, ymax, 
				minPtr, maxPtr).longValue();
		
		iymin.set(minPtr.getLong(0));
		iymax.set(maxPtr.getLong(0));
		
		return retVal;
	}
	
	/**
			Function:
				compute the minimum and maximum values of my z over all samples inside [ixmin, ixmax] * [iymin, iymax].
			Arguments:
				if ixmin = 0, start at first column; if ixmax = 0, end at last column (same for iymin and iymax).
			Return value:
				the number of samples inside the window.
			Postconditions:
				if result == 0, *minimum and *maximum are not changed;
	 */
	public long getWindowExtrema (long ixmin, long ixmax, long iymin, long iymax,
			AtomicReference<Double> minimum, AtomicReference<Double> maximum) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		long retVal = Praat.INSTANCE.Matrix_getWindowExtrema(this, new NativeLong(ixmin), new NativeLong(ixmax),
				new NativeLong(iymin),  new NativeLong(iymax), 
				minPtr, maxPtr).longValue();
		
		minimum.set(minPtr.getDouble(0));
		maximum.set(maxPtr.getDouble(0));
		
		return retVal;
	}

	public static Matrix readFromRawTextFile (MelderFile file) throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_readFromRawTextFile_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Matrix readAP (MelderFile file) throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_readAP_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void eigen (Matrix eigenvectors, Matrix eigenvalues) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Matrix_eigen_wrapped(this, eigenvectors, eigenvalues);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public Matrix power (long power) throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_power_wrapped(this,
					new NativeLong(power));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void scaleAbsoluteExtremum (double scale) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Matrix_scaleAbsoluteExtremum_wrapped(this, scale);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}

	public void writeToMatrixTextFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Matrix_writeToMatrixTextFile_wrapped(this, file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void writeToHeaderlessSpreadsheetFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Matrix_writeToHeaderlessSpreadsheetFile_wrapped(this,
					file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public Spectrum to_Spectrum() throws PraatException {
		Spectrum retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_to_Spectrum_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
	public Ltas to_Ltas() throws PraatException {
		Ltas retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_to_Ltas_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
}
