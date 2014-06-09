package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Declared;

public class SampledXY extends Sampled {
	
	public double getYMin() {
		return Praat.INSTANCE.SampledXY_getYMin(this);
	}	
	
	public double getYMax() {
		return Praat.INSTANCE.SampledXY_getYMax(this);
	}
	
	public long getNy() {
		return Praat.INSTANCE.SampledXY_getNy(this);
	}
	
	public double getDy() {
		return Praat.INSTANCE.SampledXY_GetDy(this);
	}
	
	public double getY1() {
		return Praat.INSTANCE.SampledXY_getY1(this);
	}

	public double columnToX (double column) {
		/* Return my x1 + (column - 1) * my dx.	 */
		return Praat.INSTANCE.Matrix_columnToX(this, column);
	}

	public double rowToY (double row) {
		/* Return my y1 + (row - 1) * my dy. */
		return Praat.INSTANCE.Matrix_rowToY(this, row);
	}

	public double xToColumn (double x) {
		/* Return (x - xmin) / my dx + 1. */
		return Praat.INSTANCE.Matrix_xToColumn(this, x);
	}

	public long xToLowColumn (double x) {
		/* Return floor (xToColumn (me, x)). */
		return Praat.INSTANCE.Matrix_xToLowColumn(this, x);
	}

	public long xToHighColumn (double x) {
		/* Return ceil (xToColumn (me, x)). */
		return Praat.INSTANCE.Matrix_xToHighColumn(this, x);
	}

	public long xToNearestColumn (double x) {
		/* Return floor (xToColumn (me, x) + 0.5). */
		return Praat.INSTANCE.Matrix_xToNearestColumn(this, x);
	}

	public double yToRow (double y) {
		/* Return (y - ymin) / my dy + 1. */
		return Praat.INSTANCE.Matrix_yToRow(this, y);
	}

	public long yToLowRow (double y) {
		/* Return floor (yToRow (me, y)). */
		return Praat.INSTANCE.Matrix_yToLowRow(this, y);
	}

	public long yToHighRow (double x) {
		/* Return ceil (yToRow (me, y)). */
		return Praat.INSTANCE.Matrix_yToHighRow(this, x);
	}

	public long yToNearestRow (double y) {
		/* Return floor (yToRow (me, y) + 0.5). */
		return Praat.INSTANCE.Matrix_yToNearestRow(this, y);
	}

	public long getWindowSamplesX (double xmin, double xmax, Pointer ixmin, Pointer ixmax) {
		return Praat.INSTANCE.Matrix_getWindowSamplesX(this, xmin, xmax, ixmin, ixmax);
	}
	
	public long getWindowSamplesY (double ymin, double ymax, Pointer iymin, Pointer iymax) {
		return Praat.INSTANCE.Matrix_getWindowSamplesY(this, ymin, ymax, iymin, iymax);
	}
	
}
