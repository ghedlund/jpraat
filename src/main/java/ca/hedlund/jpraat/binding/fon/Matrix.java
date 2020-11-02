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

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.annotations.Wrapped;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.NativeIntptr_t;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.binding.sys.Interpreter;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;

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
					new NativeIntptr_t(nx), dx, x1, ymin, ymax, new NativeIntptr_t(ny), dy,
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
					new NativeIntptr_t(numberOfRows), new NativeIntptr_t(numberOfColumns));
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
		
		long retVal = Praat.INSTANCE.Matrix_getWindowExtrema(this, new NativeIntptr_t(ixmin), new NativeIntptr_t(ixmax),
				new NativeIntptr_t(iymin),  new NativeIntptr_t(iymax), 
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
	
//	public void eigen (Matrix eigenvectors, Matrix eigenvalues) throws PraatException {
//		try {
//			Praat.wrapperLock.lock();
//			Praat.INSTANCE.Matrix_eigen_wrapped(this, eigenvectors, eigenvalues);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//	}
	
	public Matrix power (long power) throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_power_wrapped(this,
					new NativeIntptr_t(power));
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
	
	public Sound to_Sound() throws PraatException {
		Sound retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_to_Sound_wrapped (this);
			Praat.checkAndClearLastError();
		} catch(PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
	}
	
	public Sound to_Sound_mono(long row) throws PraatException {
		Sound retVal = null;
		
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Matrix_to_Sound_mono_wrapped (this, new NativeIntptr_t(row));
			Praat.checkAndClearLastError();
		} catch(PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		return retVal;
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
	
	public void formula(String expression, Interpreter interpreter, Matrix target) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Matrix_formula_wrapped(this, new Str32(expression), interpreter, target);
			Praat.checkAndClearLastError();
		} catch (PraatException pe) {
			throw pe;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void formula_part(double xmin, double xmax, double ymin, double ymax,
			String expression, Interpreter interpreter, Matrix target) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Matrix_formula_part_wrapped(this, xmin, xmax, ymin, ymax, new Str32(expression), interpreter, target);
			Praat.checkAndClearLastError();
		} catch (PraatException pe) {
			throw pe;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
}
