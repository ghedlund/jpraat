package ca.hedlund.jpraat.binding.stat;

import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.Data;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Table extends Data {

	public static Table createWithColumnNames (long numberOfRows, WString columnNames)
		throws PraatException {
		Table retVal = Praat.INSTANCE.Table_createWithColumnNames_wrapped (new NativeLong(numberOfRows), columnNames);
		Praat.checkLastError();
		return retVal;
	}
	
	public static Table createWithoutColumnNames (long numberOfRows, long numberOfColumns)
		throws PraatException {
		Table retVal = Praat.INSTANCE.Table_createWithoutColumnNames_wrapped (new NativeLong(numberOfRows), new NativeLong(numberOfColumns));
		Praat.checkLastError();
		return retVal;
	}
	
	public double getMean (long column) throws PraatException {
		double retVal = Praat.INSTANCE.Table_getMean_wrapped (this, new NativeLong(column));
		Praat.checkLastError();
		return retVal;
	}

	public void appendRow () throws PraatException {
		Praat.INSTANCE.Table_appendRow_wrapped (this);
		Praat.checkLastError();
	}
	
	public void appendColumn (WString label) throws PraatException {
		Praat.INSTANCE.Table_appendColumn_wrapped(this, label);
		Praat.checkLastError();
	}
	
	public void appendSumColumn ( long column1, long column2, WString label) 
		throws PraatException {
		Praat.INSTANCE.Table_appendSumColumn_wrapped (this, new NativeLong(column1), new NativeLong(column2), label);
		Praat.checkLastError();
	}
	
	public void appendDifferenceColumn ( long column1, long column2, WString label)
		throws PraatException {
		Praat.INSTANCE.Table_appendDifferenceColumn_wrapped(this, new NativeLong(column1), new NativeLong(column2), label);
		Praat.checkLastError();
	}
	
	public void appendProductColumn ( long column1, long column2, WString label)
		throws PraatException {
		Praat.INSTANCE.Table_appendProductColumn_wrapped (this, new NativeLong(column1), new NativeLong(column2), label);
		Praat.checkLastError();
	}
	
	public void appendQuotientColumn ( long column1, long column2, WString label) 
		throws PraatException {
		Praat.INSTANCE.Table_appendQuotientColumn_wrapped (this, new NativeLong(column1), new NativeLong(column2), label);
		Praat.checkLastError();
	}
	
	public void removeRow ( long row) throws PraatException {
		Praat.INSTANCE.Table_removeRow_wrapped (this, new NativeLong(row));
		Praat.checkLastError();
	}
	
	public void removeColumn ( long column) throws PraatException {
		Praat.INSTANCE.Table_removeColumn_wrapped (this, new NativeLong(column));
		Praat.checkLastError();
	}
	
	public void insertRow ( long row) throws PraatException {
		Praat.INSTANCE.Table_insertRow_wrapped (this, new NativeLong(row));
		Praat.checkLastError();
	}
	
	public void insertColumn ( long column, WString label ) throws PraatException {
		Praat.INSTANCE.Table_insertColumn_wrapped (this, new NativeLong(column), label);
		Praat.checkLastError();
	}
	
	public void setColumnLabel ( long column, WString label ) throws PraatException {
		Praat.INSTANCE.Table_setColumnLabel_wrapped (this, new NativeLong(column), label);
		Praat.checkLastError();
	}
	
	public long findColumnIndexFromColumnLabel ( WString label ) {
		return Praat.INSTANCE.Table_findColumnIndexFromColumnLabel(this, label).longValue();
	}
	
	public long getColumnIndexFromColumnLabel ( WString label ) throws PraatException {
		long retVal = Praat.INSTANCE.Table_getColumnIndexFromColumnLabel_wrapped (this, label).longValue();
		Praat.checkLastError();
		return retVal;
	}
	
	public long[] getColumnIndicesFromColumnLabelString ( WString label ) throws PraatException {
		long[] retVal = new long[0];
		
		final Pointer numberOfTokensPtr = new Memory(Native.getNativeSize(Long.TYPE));
		final Pointer ptrToLongArray  =
				Praat.INSTANCE.Table_getColumnIndicesFromColumnLabelString_wrapped(this, label, numberOfTokensPtr);
		Praat.checkLastError();
		
		final long numberOfTokens = numberOfTokensPtr.getLong(0);
		retVal = ptrToLongArray.getLongArray(0, (int)numberOfTokens);
		
		return retVal;
	}
	
	public long searchColumn ( long column, WString value) {
		return Praat.INSTANCE.Table_searchColumn(this, new NativeLong(column), value).longValue();
	}

	public void setStringValue (long row, long column, WString value) throws PraatException {
		Praat.INSTANCE.Table_setStringValue_wrapped (this, new NativeLong(row), new NativeLong(column), value);
		Praat.checkLastError();
	}
	
	public void setNumericValue (long row, long column, double value) throws PraatException {
		Praat.INSTANCE.Table_setNumericValue_wrapped(this, new NativeLong(row), new NativeLong(column), value);
		Praat.checkLastError();
	}
	
	public double getMaximum (Table me, long icol) throws PraatException {
		double retVal = Praat.INSTANCE.Table_getMaximum_wrapped (this, new NativeLong(icol));
		Praat.checkLastError();
		return retVal;
	}
	
	public double getMinimum (Table me, long icol) throws PraatException {
		double retVal = Praat.INSTANCE.Table_getMinimum_wrapped (this, new NativeLong(icol));
		Praat.checkLastError();
		return retVal;
	}
	
	public double getGroupMean (Table me, long column, long groupColumn, WString group)
		throws PraatException {
		double retVal = Praat.INSTANCE.Table_getGroupMean_wrapped (this, new NativeLong(column), new NativeLong(groupColumn), group);
		Praat.checkLastError();
		return retVal;
	}
	
	public double getStdev (Table me, long column) throws PraatException {
		double retVal = Praat.INSTANCE.Table_getStdev_wrapped (this, new NativeLong(column));
		Praat.checkLastError();
		return retVal;
	}
	
	public long drawRowFromDistribution (Table me, long column) throws PraatException {
		long retVal = Praat.INSTANCE.Table_drawRowFromDistribution_wrapped (this, new NativeLong(column)).longValue();
		Praat.checkLastError();
		return retVal;
	}
	
	public double getCorrelation_pearsonR (Table me, long column1, long column2, double significanceLevel,
			AtomicReference<Double> out_significance, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit)
		throws PraatException {
		final Pointer out_significancePtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer out_lowerLimitPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer out_upperLimitPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		double retVal = Praat.INSTANCE.Table_getCorrelation_pearsonR_wrapped (this, new NativeLong(column1), new NativeLong(column2), significanceLevel, 
				out_significancePtr, out_lowerLimitPtr, out_upperLimitPtr);
		Praat.checkLastError();
		
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
		
		double retVal = Praat.INSTANCE.Table_getCorrelation_kendallTau_wrapped(this, new NativeLong(column1), new NativeLong(column2), significanceLevel, 
				out_significancePtr, out_lowerLimitPtr, out_upperLimitPtr);
		Praat.checkLastError();
		
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
		
		double retVal = Praat.INSTANCE.Table_getMean_studentT_wrapped (this, new NativeLong(column), significanceLevel, 
				varPtr.getPointer(0), varPtr.getPointer(1), varPtr.getPointer(2), 
				varPtr.getPointer(3), varPtr.getPointer(4));
		Praat.checkLastError();
		
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
		
		double retVal = Praat.INSTANCE.Table_getDifference_studentT_wrapped (this, new NativeLong(column1), new NativeLong(column2), significanceLevel, 
				varPtr.getPointer(0), varPtr.getPointer(1), varPtr.getPointer(2), 
				varPtr.getPointer(3), varPtr.getPointer(4));
		Praat.checkLastError();
		
		out_tFromZero.set(varPtr.getDouble(0));
		out_numberOfDegreesOfFreedom.set(varPtr.getDouble(1));
		out_significanceFromZero.set(varPtr.getDouble(2));
		out_lowerLimit.set(varPtr.getDouble(3));
		out_upperLimit.set(varPtr.getDouble(4));
		
		return retVal;
	}
	
	public double getGroupMean_studentT (Table me, long column, long groupColumn, WString group1, double significanceLevel,
			AtomicReference<Double> out_tFromZero, AtomicReference<Double> out_numberOfDegreesOfFreedom, 
			AtomicReference<Double> out_significanceFromZero, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit)
		throws PraatException {
		final Pointer varPtr = new Memory(Native.getNativeSize(Double.TYPE) * 5);
		
		double retVal = Praat.INSTANCE.Table_getGroupMean_studentT_wrapped (this, new NativeLong(column), new NativeLong(groupColumn),
				group1, significanceLevel, 
				varPtr.getPointer(0), varPtr.getPointer(1), varPtr.getPointer(2), 
				varPtr.getPointer(3), varPtr.getPointer(4));
		Praat.checkLastError();
	
		out_tFromZero.set(varPtr.getDouble(0));
		out_numberOfDegreesOfFreedom.set(varPtr.getDouble(1));
		out_significanceFromZero.set(varPtr.getDouble(2));
		out_lowerLimit.set(varPtr.getDouble(3));
		out_upperLimit.set(varPtr.getDouble(4));
		
		return retVal;
	}
	
	public double getGroupDifference_studentT (Table me, long column, long groupColumn, WString group1, WString group2, double significanceLevel,
			AtomicReference<Double> out_tFromZero, AtomicReference<Double> out_numberOfDegreesOfFreedom, 
			AtomicReference<Double> out_significanceFromZero, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit) 
		throws PraatException {
		final Pointer varPtr = new Memory(Native.getNativeSize(Double.TYPE) * 5);
		
		double retVal = Praat.INSTANCE.Table_getGroupDifference_studentT_wrapped (this, new NativeLong(column), new NativeLong(groupColumn), group1, group2, significanceLevel,
				varPtr.getPointer(0), varPtr.getPointer(1), varPtr.getPointer(2), 
				varPtr.getPointer(3), varPtr.getPointer(4));
		Praat.checkLastError();
	
		out_tFromZero.set(varPtr.getDouble(0));
		out_numberOfDegreesOfFreedom.set(varPtr.getDouble(1));
		out_significanceFromZero.set(varPtr.getDouble(2));
		out_lowerLimit.set(varPtr.getDouble(3));
		out_upperLimit.set(varPtr.getDouble(4));
		
		return retVal;
	}
	
	public double getGroupDifference_wilcoxonRankSum (Table me, long column, long groupColumn, WString group1, WString group2,
			AtomicReference<Double> out_rankSum, AtomicReference<Double> out_significanceFromZero)
		throws PraatException {
		final Pointer rankSumPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer significanceFromZeroPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		double retVal = Praat.INSTANCE.Table_getGroupDifference_wilcoxonRankSum_wrapped (this, new NativeLong(column), new NativeLong(groupColumn), 
				group1, group2, rankSumPtr, significanceFromZeroPtr);
		Praat.checkLastError();
		
		out_rankSum.set(rankSumPtr.getDouble(0));
		out_significanceFromZero.set(significanceFromZeroPtr.getDouble(0));
		
		return retVal;
	}
	
	public double getVarianceRatio (Table me, long column1, long column2, double significanceLevel,
			AtomicReference<Double> out_significance, AtomicReference<Double> out_lowerLimit, AtomicReference<Double> out_upperLimit) 
		throws PraatException {
		final Pointer out_significancePtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer out_lowerLimitPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer out_upperLimitPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		double retVal = Praat.INSTANCE.Table_getVarianceRatio_wrapped (this, new NativeLong(column1), new NativeLong(column2), significanceLevel, 
				out_significancePtr, out_lowerLimitPtr, out_upperLimitPtr);
		Praat.checkLastError();
		
		out_significance.set(out_significancePtr.getDouble(0));
		out_lowerLimit.set(out_lowerLimitPtr.getDouble(0));
		out_upperLimit.set(out_upperLimitPtr.getDouble(0));
		
		return retVal;
	}
	
	public boolean getExtrema (Table me, long icol, AtomicReference<Double> minimum, AtomicReference<Double> maximum) 
		throws PraatException {
		final Pointer minPtr = new Memory(Native.getNativeSize(Double.TYPE));
		final Pointer maxPtr = new Memory(Native.getNativeSize(Double.TYPE));
		
		boolean retVal = Praat.INSTANCE.Table_getExtrema_wrapped (this, new NativeLong(icol), minPtr, maxPtr);
		Praat.checkLastError();
		
		minimum.set(minPtr.getDouble(0));
		maximum.set(maxPtr.getDouble(0));
		
		return retVal;
	}
	
	public void sortRows_Assert (Pointer columns, long numberOfColumns) 
		throws PraatException {
		Praat.INSTANCE.Table_sortRows_Assert_wrapped (this, columns, new NativeLong(numberOfColumns));
		Praat.checkLastError();
	}
	
	public void sortRows_string (WString columns_string) 
		throws PraatException {
		Praat.INSTANCE.Table_sortRows_string_wrapped (this, columns_string);
		Praat.checkLastError();
	}
	
	public void randomizeRows () throws PraatException {
		Praat.INSTANCE.Table_randomizeRows_wrapped(this);
		Praat.checkLastError();
	}
	
	public void reflectRows () throws PraatException {
		Praat.INSTANCE.Table_reflectRows_wrapped (this);
		Praat.checkLastError();
	}
	
	public void writeToTabSeparatedFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.Table_writeToTabSeparatedFile_wrapped (this, file);
		Praat.checkLastError();
	}
	
	public void writeToCommaSeparatedFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.Table_writeToCommaSeparatedFile_wrapped(this, file);
		Praat.checkLastError();
	}
	
	public static Table Table_readFromTableFile (MelderFile file) throws PraatException {
		Table retVal = Praat.INSTANCE.Table_readFromTableFile_wrapped (file);
		Praat.checkLastError();
		return retVal;
	}
	
	public static Table Table_readFromCharacterSeparatedTextFile (MelderFile file, char separator) 
		throws PraatException {
		Table retVal = Praat.INSTANCE.Table_readFromCharacterSeparatedTextFile_wrapped (file, separator);
		Praat.checkLastError();
		return retVal;
	}
	
	public Table extractRowsWhereColumn_number (long column, int which_Melder_NUMBER, double criterion)
		throws PraatException {
		Table retVal = Praat.INSTANCE.Table_extractRowsWhereColumn_number_wrapped (this, new NativeLong(column), which_Melder_NUMBER, criterion);
		Praat.checkLastError();
		return retVal;
	}
	
	public Table extractRowsWhereColumn_string (long column, int which_Melder_STRING, WString criterion) 
		throws PraatException {
		Table retVal = Praat.INSTANCE.Table_extractRowsWhereColumn_string_wrapped (this, new NativeLong(column), which_Melder_STRING, criterion);
		Praat.checkLastError();
		return retVal;
	}
	
	public Table collapseRows (WString factors_string, WString columnsToSum_string,
		WString columnsToAverage_string, WString columnsToMedianize_string,
		WString columnsToAverageLogarithmically_string, WString columnsToMedianizeLogarithmically_string)
		throws PraatException {
		Table retVal = Praat.INSTANCE.Table_collapseRows_wrapped(this, factors_string, columnsToSum_string, columnsToAverage_string,
				columnsToMedianize_string, columnsToAverageLogarithmically_string, columnsToMedianizeLogarithmically_string);
		Praat.checkLastError();
		return retVal;
	}
	
	public Table rowsToColumns (WString factors_string, long columnToTranspose, WString columnsToExpand_string)
		throws PraatException {
		Table retVal = Praat.INSTANCE.Table_rowsToColumns_wrapped (this, factors_string, new NativeLong(columnToTranspose), columnsToExpand_string);
		Praat.checkLastError();
		return retVal;
	}
	
	public Table transpose (Table me) throws PraatException {
		Table retVal = Praat.INSTANCE.Table_transpose_wrapped (this);
		Praat.checkLastError();
		return retVal;
	}

	public void checkSpecifiedRowNumberWithinRange (long rowNumber) throws PraatException {
		Praat.INSTANCE.Table_checkSpecifiedRowNumberWithinRange_wrapped (this, new NativeLong(rowNumber));
		Praat.checkLastError();
	}
	
	public void checkSpecifiedColumnNumberWithinRange (long columnNumber) throws PraatException {
		Praat.INSTANCE.Table_checkSpecifiedColumnNumberWithinRange_wrapped (this, new NativeLong(columnNumber));
		Praat.checkLastError();
	}
	
	public boolean isCellNumeric_ErrorFalse (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_isCellNumeric_ErrorFalse(this, new NativeLong(rowNumber), new NativeLong(columnNumber));
	}
	
	public boolean isColumnNumeric_ErrorFalse (long columnNumber) {
		return Praat.INSTANCE.Table_isColumnNumeric_ErrorFalse(this, new NativeLong(columnNumber));
	}
	
	public double getNrow () {
		return Praat.INSTANCE.Table_getNrow(this);
	}
	
	public double getNcol () {
		return Praat.INSTANCE.Table_getNcol(this);
	}
	
	public WString  getColStr (long columnNumber) {
		return Praat.INSTANCE.Table_getColStr(this, new NativeLong(columnNumber));
	}
	
	public double getMatrix (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_getMatrix(this, new NativeLong(rowNumber), new NativeLong(columnNumber));
	}
	
	public WString  getMatrixStr (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_getMatrixStr(this, new NativeLong(rowNumber), new NativeLong(columnNumber));
	}
	
	public double getColIndex  (WString columnLabel) {
		return Praat.INSTANCE.Table_getColIndex(this, columnLabel);
	}
	
}
