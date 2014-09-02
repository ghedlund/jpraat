package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Declared;
import ca.hedlund.jpraat.binding.sys.MelderFile;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class Matrix extends SampledXY {
	
	public static Matrix create
	(double xmin, double xmax, long nx, double dx, double x1,
	 double ymin, double ymax, long ny, double dy, double y1) {
		return Praat.INSTANCE.Matrix_create(xmin, xmax, nx, dx, x1, ymin, ymax, ny, dy, y1);
	}

	public static Matrix createSimple (long numberOfRows, long numberOfColumns) {
		return Praat.INSTANCE.Matrix_createSimple(numberOfRows, numberOfColumns);
	}
	
	public long getWindowSamplesX (double xmin, double xmax, Pointer ixmin, Pointer ixmax) {
		return Praat.INSTANCE.Matrix_getWindowSamplesX(this, xmin, xmax, ixmin, ixmax);
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

	public long getWindowSamplesY (double ymin, double ymax, Pointer iymin, Pointer iymax) {
		return Praat.INSTANCE.Matrix_getWindowSamplesY(this, ymin, ymax, iymin, iymax);
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
			Pointer minimum, Pointer maximum) {
		return Praat.INSTANCE.Matrix_getWindowExtrema(this, ixmin, ixmax, iymin, iymax, minimum, maximum);
	}

	public static Matrix readFromRawTextFile (MelderFile file) {
		return Praat.INSTANCE.Matrix_readFromRawTextFile(file);
	}
	
	public static Matrix readAP (MelderFile file) {
		return Praat.INSTANCE.Matrix_readAP(file);
	}
	
	public void eigen (Matrix eigenvectors, Matrix eigenvalues) {
		Praat.INSTANCE.Matrix_eigen(this, eigenvectors, eigenvalues);
	}
	
	public Matrix power (long power) {
		return Praat.INSTANCE.Matrix_power(this, power);
	}
	
	public void scaleAbsoluteExtremum (double scale) {
		Praat.INSTANCE.Matrix_scaleAbsoluteExtremum(this, scale);
	}

	public void writeToMatrixTextFile (MelderFile file) {
		Praat.INSTANCE.Matrix_writeToMatrixTextFile(this, file);
	}
	
	public void writeToHeaderlessSpreadsheetFile (MelderFile file) {
		Praat.INSTANCE.Matrix_writeToHeaderlessSpreadsheetFile(this, file);
	}
	
}
