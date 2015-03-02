package ca.hedlund.jpraat.binding.fon;

import java.util.concurrent.atomic.AtomicReference;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class Matrix extends SampledXY {
	
	public static Matrix create
	(double xmin, double xmax, long nx, double dx, double x1,
	 double ymin, double ymax, long ny, double dy, double y1) throws PraatException {
		Matrix retVal = Praat.INSTANCE.Matrix_create_wrapped (xmin, xmax, nx, dx, x1, ymin, ymax, ny, dy, y1);
		Praat.checkLastError();
		return retVal;
	}

	public static Matrix createSimple (long numberOfRows, long numberOfColumns) throws PraatException {
		Matrix retVal = Praat.INSTANCE.Matrix_createSimple_wrapped (numberOfRows, numberOfColumns);
		Praat.checkLastError();
		return retVal;
	}
	
	public long getWindowSamplesX (double xmin, double xmax, 
			AtomicReference<Long> ixmin, AtomicReference<Long> ixmax) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		long retVal = Praat.INSTANCE.Matrix_getWindowSamplesX(this, xmin, xmax, 
				minPtr, maxPtr);
		
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
		return Praat.INSTANCE.Matrix_xToLowColumn(this, x);
	}

	/* Return ceil (xToColumn (me, x)). */
	public long xToHighColumn (double x) {
		return Praat.INSTANCE.Matrix_xToHighColumn(this, x);
	}

	/* Return floor (xToColumn (me, x) + 0.5). */
	public long xToNearestColumn (double x) {
		return Praat.INSTANCE.Matrix_xToNearestColumn(this, x);
	}

	/* Return (y - ymin) / my dy + 1. */
	public double yToRow (double y) {
		return Praat.INSTANCE.Matrix_yToRow(this, y);
	}

	/* Return floor (yToRow (me, y)). */
	public long yToLowRow (double y) {
		return Praat.INSTANCE.Matrix_yToLowRow(this, y);
	}

	/* Return ceil (yToRow (me, y)). */
	public long yToHighRow (double x) {
		return Praat.INSTANCE.Matrix_yToHighRow(this, x);
	}

	/* Return floor (yToRow (me, y) + 0.5). */
	public long yToNearestRow (double y) {
		return Praat.INSTANCE.Matrix_yToNearestRow(this, y);
	}

	public long getWindowSamplesY (double ymin, double ymax, 
			AtomicReference<Long> iymin, AtomicReference<Long> iymax) {
		final Pointer minPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Long.TYPE));
		
		long retVal = Praat.INSTANCE.Matrix_getWindowSamplesY(this, ymin, ymax, 
				minPtr, maxPtr);
		
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
		
		long retVal = Praat.INSTANCE.Matrix_getWindowExtrema(this, ixmin, ixmax, iymin, iymax, 
				minPtr, maxPtr);
		
		minimum.set(minPtr.getDouble(0));
		maximum.set(maxPtr.getDouble(0));
		
		return retVal;
	}

	public static Matrix readFromRawTextFile (MelderFile file) throws PraatException {
		Matrix retVal = Praat.INSTANCE.Matrix_readFromRawTextFile_wrapped (file);
		Praat.checkLastError();
		return retVal;
	}
	
	public static Matrix readAP (MelderFile file) throws PraatException {
		Matrix retVal = Praat.INSTANCE.Matrix_readAP_wrapped (file);
		Praat.checkLastError();
		return retVal;
	}
	
	public void eigen (Matrix eigenvectors, Matrix eigenvalues) throws PraatException {
		Praat.INSTANCE.Matrix_eigen_wrapped(this, eigenvectors, eigenvalues);
		Praat.checkLastError();
	}
	
	public Matrix power (long power) throws PraatException {
		Matrix retVal = Praat.INSTANCE.Matrix_power_wrapped (this, power);
		Praat.checkLastError();
		return retVal;
	}
	
	public void scaleAbsoluteExtremum (double scale) throws PraatException {
		Praat.INSTANCE.Matrix_scaleAbsoluteExtremum_wrapped (this, scale);
		Praat.checkLastError();
	}

	public void writeToMatrixTextFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.Matrix_writeToMatrixTextFile_wrapped (this, file);
		Praat.checkLastError();
	}
	
	public void writeToHeaderlessSpreadsheetFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.Matrix_writeToHeaderlessSpreadsheetFile_wrapped (this, file);
		Praat.checkLastError();
	}
	
}
