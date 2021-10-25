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
package ca.hedlund.jpraat.binding.stat;

import java.util.concurrent.atomic.*;

import com.sun.jna.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.binding.melder.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;

public class Table extends Daata {
	
	public Table() {
		super();
	}
	
	public Table(Pointer p) {
		super(p);
	}

	public static Table createWithoutColumnNames (long numberOfRows, long numberOfColumns)
		throws PraatException {
		Table retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_createWithoutColumnNames_wrapped(new NativeIntptr_t(
							numberOfRows), new NativeIntptr_t(numberOfColumns));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getMean (long column) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_getMean_wrapped(this,
					new NativeIntptr_t(column));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public void appendRow () throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_appendRow_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void appendColumn (Str32 label) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_appendColumn_wrapped(this, label);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void appendSumColumn ( long column1, long column2, Str32 label) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_appendSumColumn_wrapped(this, new NativeIntptr_t(
					column1), new NativeIntptr_t(column2), label);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void appendDifferenceColumn ( long column1, long column2, Str32 label)
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_appendDifferenceColumn_wrapped(this,
					new NativeIntptr_t(column1), new NativeIntptr_t(column2), label);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void appendProductColumn ( long column1, long column2, Str32 label)
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_appendProductColumn_wrapped(this,
					new NativeIntptr_t(column1), new NativeIntptr_t(column2), label);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void appendQuotientColumn ( long column1, long column2, Str32 label) 
		throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_appendQuotientColumn_wrapped(this,
					new NativeIntptr_t(column1), new NativeIntptr_t(column2), label);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void removeRow ( long row) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_removeRow_wrapped(this, new NativeIntptr_t(row));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void removeColumn ( long column) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_removeColumn_wrapped(this, new NativeIntptr_t(
					column));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insertRow ( long row) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_insertRow_wrapped(this, new NativeIntptr_t(row));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insertColumn ( long column, Str32 label ) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_insertColumn_wrapped(this, new NativeIntptr_t(
					column), label);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void setColumnLabel ( long column, Str32 label ) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_setColumnLabel_wrapped(this, new NativeIntptr_t(
					column), label);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public long findColumnIndexFromColumnLabel ( Str32 label ) {
		return Praat.INSTANCE.Table_findColumnIndexFromColumnLabel(this, label).longValue();
	}
	
	public long getColumnIndexFromColumnLabel ( Str32 label ) throws PraatException {
		long retVal = 0L;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_getColumnIndexFromColumnLabel_wrapped(this, label)
					.longValue();
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
//	public long[] getColumnIndicesFromColumnLabelString ( Str32 label ) throws PraatException {
//		long[] retVal = new long[0];
//		
//		final Pointer numberOfTokensPtr = new Memory(Native.getNativeSize(Long.TYPE));
//		try {
//			Praat.wrapperLock.lock();
//			final Pointer ptrToLongArray = Praat.INSTANCE
//					.Table_getColumnIndicesFromColumnLabelString_wrapped(this,
//							label, numberOfTokensPtr);
//			Praat.checkAndClearLastError();
//			final long numberOfTokens = numberOfTokensPtr.getLong(0);
//			retVal = ptrToLongArray.getLongArray(0, (int)numberOfTokens);
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		
//		return retVal;
//	}
	
	public long searchColumn ( long column, Str32 value) {
		return Praat.INSTANCE.Table_searchColumn(this, new NativeIntptr_t(column), value).longValue();
	}
	
	public Str32 getStringValue(long row, long column) throws PraatException {
		Str32 retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_getStringValue_Assert_wrapped(this, new NativeIntptr_t(
							row), new NativeIntptr_t(column));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getNumericValue(long row, long column) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_getNumericValue_Assert_wrapped(this, new NativeIntptr_t(
							row), new NativeIntptr_t(column));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public void setStringValue (long row, long column, Str32 value) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_setStringValue_wrapped(this, new NativeIntptr_t(
					row), new NativeIntptr_t(column), value);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void setNumericValue (long row, long column, double value) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_setNumericValue_wrapped(this, new NativeIntptr_t(
					row), new NativeIntptr_t(column), value);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public double getMaximum (Table me, long icol) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_getMaximum_wrapped(this,
					new NativeIntptr_t(icol));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getMinimum (Table me, long icol) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_getMinimum_wrapped(this,
					new NativeIntptr_t(icol));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getGroupMean (Table me, long column, long groupColumn, Str32 group)
		throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_getGroupMean_wrapped(this,
					new NativeIntptr_t(column), new NativeIntptr_t(groupColumn), group);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getStdev (Table me, long column) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_getStdev_wrapped(this,
					new NativeIntptr_t(column));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public long drawRowFromDistribution (Table me, long column) throws PraatException {
		long retVal = 0L;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_drawRowFromDistribution_wrapped(
					this, new NativeIntptr_t(column)).longValue();
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getCorrelation_pearsonR (Table me, long column1, long column2, double significanceLevel,
			AtomicReference<Double> out_significance, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit)
		throws PraatException {
		final Pointer out_significancePtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer out_lowerLimitPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer out_upperLimitPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_getCorrelation_pearsonR_wrapped(this,
							new NativeIntptr_t(column1), new NativeIntptr_t(column2),
							significanceLevel, out_significancePtr,
							out_lowerLimitPtr, out_upperLimitPtr);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		out_significance.set(out_significancePtr.getDouble(0));
		out_lowerLimit.set(out_lowerLimitPtr.getDouble(0));
		out_upperLimit.set(out_upperLimitPtr.getDouble(0));
		
		return retVal;
	}
	
	public double getCorrelation_kendallTau (Table me, long column1, long column2, double significanceLevel,
			AtomicReference<Double> out_significance, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit) 
		throws PraatException {
		final Pointer out_significancePtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer out_lowerLimitPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer out_upperLimitPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_getCorrelation_kendallTau_wrapped(this,
							new NativeIntptr_t(column1), new NativeIntptr_t(column2),
							significanceLevel, out_significancePtr,
							out_lowerLimitPtr, out_upperLimitPtr);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		out_significance.set(out_significancePtr.getDouble(0));
		out_lowerLimit.set(out_lowerLimitPtr.getDouble(0));
		out_upperLimit.set(out_upperLimitPtr.getDouble(0));
		
		return retVal;
	}
	
	public double getMean_studentT (Table me, long column, double significanceLevel,
			AtomicReference<Double> out_tFromZero, AtomicReference<Double> out_numberOfDegreesOfFreedom, 
			AtomicReference<Double> out_significanceFromZero, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit)
		throws PraatException {
		final Pointer varPtr = new Memory(Native.getNativeSize(Double.TYPE) * 5);
		
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_getMean_studentT_wrapped(this,
					new NativeIntptr_t(column), significanceLevel,
					varPtr.getPointer(0), varPtr.getPointer(1),
					varPtr.getPointer(2), varPtr.getPointer(3),
					varPtr.getPointer(4));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		out_tFromZero.set(varPtr.getDouble(0));
		out_numberOfDegreesOfFreedom.set(varPtr.getDouble(1));
		out_significanceFromZero.set(varPtr.getDouble(2));
		out_lowerLimit.set(varPtr.getDouble(3));
		out_upperLimit.set(varPtr.getDouble(4));
		
		return retVal;
	}
	
	public double getDifference_studentT (Table me, long column1, long column2, double significanceLevel,
			AtomicReference<Double> out_tFromZero, AtomicReference<Double> out_numberOfDegreesOfFreedom, 
			AtomicReference<Double> out_significanceFromZero, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit)
		throws PraatException {
		final Pointer varPtr = new Memory(Native.getNativeSize(Double.TYPE) * 5);
		
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_getDifference_studentT_wrapped(this, new NativeIntptr_t(
							column1), new NativeIntptr_t(column2),
							significanceLevel, varPtr.getPointer(0), varPtr
									.getPointer(1), varPtr.getPointer(2),
							varPtr.getPointer(3), varPtr.getPointer(4));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		out_tFromZero.set(varPtr.getDouble(0));
		out_numberOfDegreesOfFreedom.set(varPtr.getDouble(1));
		out_significanceFromZero.set(varPtr.getDouble(2));
		out_lowerLimit.set(varPtr.getDouble(3));
		out_upperLimit.set(varPtr.getDouble(4));
		
		return retVal;
	}
	
	public double getGroupMean_studentT (Table me, long column, long groupColumn, Str32 group1, double significanceLevel,
			AtomicReference<Double> out_tFromZero, AtomicReference<Double> out_numberOfDegreesOfFreedom, 
			AtomicReference<Double> out_significanceFromZero, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit)
		throws PraatException {
		final Pointer varPtr = new Memory(Native.getNativeSize(Double.TYPE) * 5);
		
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_getGroupMean_studentT_wrapped(
					this, new NativeIntptr_t(column), new NativeIntptr_t(groupColumn),
					group1, significanceLevel, varPtr.getPointer(0),
					varPtr.getPointer(1), varPtr.getPointer(2),
					varPtr.getPointer(3), varPtr.getPointer(4));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	
		out_tFromZero.set(varPtr.getDouble(0));
		out_numberOfDegreesOfFreedom.set(varPtr.getDouble(1));
		out_significanceFromZero.set(varPtr.getDouble(2));
		out_lowerLimit.set(varPtr.getDouble(3));
		out_upperLimit.set(varPtr.getDouble(4));
		
		return retVal;
	}
	
	public double getGroupDifference_studentT (Table me, long column, long groupColumn, Str32 group1, Str32 group2, double significanceLevel,
			AtomicReference<Double> out_tFromZero, AtomicReference<Double> out_numberOfDegreesOfFreedom, 
			AtomicReference<Double> out_significanceFromZero, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit) 
		throws PraatException {
		final Pointer varPtr = new Memory(Native.getNativeSize(Double.TYPE) * 5);
		
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_getGroupDifference_studentT_wrapped(this,
							new NativeIntptr_t(column),
							new NativeIntptr_t(groupColumn), group1, group2,
							significanceLevel, varPtr.getPointer(0),
							varPtr.getPointer(1), varPtr.getPointer(2),
							varPtr.getPointer(3), varPtr.getPointer(4));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	
		out_tFromZero.set(varPtr.getDouble(0));
		out_numberOfDegreesOfFreedom.set(varPtr.getDouble(1));
		out_significanceFromZero.set(varPtr.getDouble(2));
		out_lowerLimit.set(varPtr.getDouble(3));
		out_upperLimit.set(varPtr.getDouble(4));
		
		return retVal;
	}
	
	public double getGroupDifference_wilcoxonRankSum (Table me, long column, long groupColumn, Str32 group1, Str32 group2,
			AtomicReference<Double> out_rankSum, AtomicReference<Double> out_significanceFromZero)
		throws PraatException {
		final Pointer rankSumPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer significanceFromZeroPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_getGroupDifference_wilcoxonRankSum_wrapped(this,
							new NativeIntptr_t(column),
							new NativeIntptr_t(groupColumn), group1, group2,
							rankSumPtr, significanceFromZeroPtr);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		out_rankSum.set(rankSumPtr.getDouble(0));
		out_significanceFromZero.set(significanceFromZeroPtr.getDouble(0));
		
		return retVal;
	}
	
	public boolean getExtrema (Table me, long icol, AtomicReference<Double> minimum, AtomicReference<Double> maximum) 
		throws PraatException {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		boolean retVal = false;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_getExtrema_wrapped(this,
					new NativeIntptr_t(icol), minPtr, maxPtr);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		
		minimum.set(minPtr.getDouble(0));
		maximum.set(maxPtr.getDouble(0));
		
		return retVal;
	}
	
//	public void sortRows_Assert (Pointer columns, long numberOfColumns) 
//		throws PraatException {
//		try {
//			Praat.wrapperLock.lock();
//			Praat.INSTANCE.Table_sortRows_Assert_wrapped(this, columns,
//					new NativeIntptr_t(numberOfColumns));
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//	}
	
//	public void sortRows_string (Str32 columns_string)
//		throws PraatException {
//		try {
//			Praat.wrapperLock.lock();
//			Praat.INSTANCE.Table_sortRows_string_wrapped(this, columns_string);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//	}
	
	public void randomizeRows () throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_randomizeRows_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void reflectRows () throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_reflectRows_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void writeToTabSeparatedFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_writeToTabSeparatedFile_wrapped(this, file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void writeToCommaSeparatedFile (MelderFile file) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_writeToCommaSeparatedFile_wrapped(this, file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public static Table Table_readFromTableFile (MelderFile file) throws PraatException {
		Table retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_readFromTableFile_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public static Table Table_readFromCharacterSeparatedTextFile (MelderFile file, char separator, boolean interpretQuotes) 
		throws PraatException {
		Table retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_readFromCharacterSeparatedTextFile_wrapped(file,
							separator, (interpretQuotes ? 1 : 0));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Table extractRowsWhereColumn_number (long column, kMelder_number which, double criterion)
		throws PraatException {
		Table retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_extractRowsWhereColumn_number_wrapped(this,
							new NativeIntptr_t(column), which,
							criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Table extractRowsWhereColumn_string (long column, kMelder_string which, Str32 criterion) 
		throws PraatException {
		Table retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Table_extractRowsWhereColumn_string_wrapped(this,
							new NativeIntptr_t(column), which,
							criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
//	public Table collapseRows (Str32 factors_string, Str32 columnsToSum_string,
//		Str32 columnsToAverage_string, Str32 columnsToMedianize_string,
//		Str32 columnsToAverageLogarithmically_string, Str32 columnsToMedianizeLogarithmically_string)
//		throws PraatException {
//		Table retVal = null;
//		try {
//			Praat.wrapperLock.lock();
//			retVal = Praat.INSTANCE.Table_collapseRows_wrapped(this,
//					factors_string, columnsToSum_string,
//					columnsToAverage_string, columnsToMedianize_string,
//					columnsToAverageLogarithmically_string,
//					columnsToMedianizeLogarithmically_string);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		return retVal;
//	}
//
//	public Table rowsToColumns (Str32 factors_string, long columnToTranspose, Str32 columnsToExpand_string)
//		throws PraatException {
//		Table retVal = null;
//		try {
//			Praat.wrapperLock.lock();
//			retVal = Praat.INSTANCE.Table_rowsToColumns_wrapped(this,
//					factors_string, new NativeIntptr_t(columnToTranspose),
//					columnsToExpand_string);
//			Praat.checkAndClearLastError();
//		} catch (PraatException e) {
//			throw e;
//		} finally {
//			Praat.wrapperLock.unlock();
//		}
//		return retVal;
//	}
	
	public Table transpose (Table me) throws PraatException {
		Table retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_transpose_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public void checkSpecifiedRowNumberWithinRange (long rowNumber) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_checkSpecifiedRowNumberWithinRange_wrapped(
					this, new NativeIntptr_t(rowNumber));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void checkSpecifiedColumnNumberWithinRange (long columnNumber) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.Table_checkSpecifiedColumnNumberWithinRange_wrapped(
					this, new NativeIntptr_t(columnNumber));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public boolean isCellNumeric_ErrorFalse (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_isCellNumeric_ErrorFalse(this, new NativeIntptr_t(rowNumber), new NativeIntptr_t(columnNumber));
	}
	
	public boolean isColumnNumeric_ErrorFalse (long columnNumber) {
		return Praat.INSTANCE.Table_isColumnNumeric_ErrorFalse(this, new NativeIntptr_t(columnNumber));
	}
	
	public double getNrow () {
		return Praat.INSTANCE.Table_getNrow(this);
	}
	
	public double getNcol () {
		return Praat.INSTANCE.Table_getNcol(this);
	}
	
	public Str32  getColStr (long columnNumber) {
		return Praat.INSTANCE.Table_getColStr(this, new NativeIntptr_t(columnNumber));
	}
	
	public double getMatrix (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_getMatrix(this, new NativeIntptr_t(rowNumber), new NativeIntptr_t(columnNumber));
	}
	
	public Str32  getMatrixStr (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_getMatrixStr(this, new NativeIntptr_t(rowNumber), new NativeIntptr_t(columnNumber));
	}
	
	public double getColIndex  (Str32 columnLabel) {
		return Praat.INSTANCE.Table_getColIndex(this, columnLabel);
	}
	
	public String messageColumn(long column) {
		return Praat.INSTANCE.Table_messageColumn(this, new NativeIntptr_t(column)).toString();
	}
	
	public TableOfReal to_TableOfReal (long labelColumn) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Table_to_TableOfReal_wrapped(
					this, new NativeIntptr_t(labelColumn));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
}
