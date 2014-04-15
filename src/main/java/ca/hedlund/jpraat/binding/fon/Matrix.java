package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Header;
import ca.hedlund.jpraat.binding.sys.MelderFile;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

@Header("fon/Matrix.h")
public class Matrix extends SampledXY {
	
	public static Matrix create
	(double xmin, double xmax, long nx, double dx, double x1,
	 double ymin, double ymax, long ny, double dy, double y1) {
		return Praat.INSTANCE.Matrix_create(xmin, xmax, nx, dx, x1, ymin, ymax, ny, dy, y1);
	}

	public Matrix createSimple (long numberOfRows, long numberOfColumns) {
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
	
//	public double rowToY (double row);   /* Return my y1 + (row - 1) * my dy. */
//	
//	public double xToColumn (double x);   /* Return (x - xmin) / my dx + 1. */
//	
//	public long xToLowColumn (double x);   /* Return floor (xToColumn (me, x)). */
//	
//	public long xToHighColumn (double x);   /* Return ceil (xToColumn (me, x)). */
//	
//	public long xToNearestColumn (double x);   /* Return floor (xToColumn (me, x) + 0.5). */
//	
//	public double yToRow (double y);   /* Return (y - ymin) / my dy + 1. */
//	
//	public long yToLowRow (double y);   /* Return floor (yToRow (me, y)). */
//	
//	public long yToHighRow (double x);   /* Return ceil (yToRow (me, y)). */
//	
//	public long yToNearestRow (double y);   /* Return floor (yToRow (me, y) + 0.5). */
//	
//	public long getWindowSamplesY (double ymin, double ymax, Pointer iymin, Pointer iymax);
//	
//	public long getWindowExtrema (long ixmin, long ixmax, long iymin, long iymax,
//		Pointer minimum, Pointer maximum);
//	
//	//public void formula (WString expression, Interpreter interpreter, Matrix target);
//	//
//	//public void formula_part (double xmin, double xmax, double ymin, double ymax,
//	//	WString expression, Interpreter interpreter, Matrix target);
//	
//	public Matrix readFromRawTextFile (MelderFile file);
//	public Matrix readAP (MelderFile file);
//	//public Matrix appendRows (Matrix thee, ClassInfo klas);
//	
//	public void eigen (Pointer eigenvectors, Pointer eigenvalues);
//	public Matrix power (long power);
//	
//	void scaleAbsoluteExtremum (double scale);
//	
//	//Matrix Table_to_Matrix (Table me);
//	public void writeToMatrixTextFile (MelderFile file);
//	public void writeToHeaderlessSpreadsheetFile (MelderFile file);
//	
//	//Matrix TableOfReal_to_Matrix ();
//	//TableOfReal to_TableOfReal ();
	
}
