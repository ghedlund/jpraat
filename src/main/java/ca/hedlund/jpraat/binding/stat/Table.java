package ca.hedlund.jpraat.binding.stat;

import com.sun.jna.Pointer;
import com.sun.jna.WString;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.Data;
import ca.hedlund.jpraat.binding.sys.MelderFile;

public class Table extends Data {

	public static Table createWithColumnNames (long numberOfRows, WString columnNames) {
		return Praat.INSTANCE.Table_createWithColumnNames(numberOfRows, columnNames);
	}
	
	public static Table createWithoutColumnNames (long numberOfRows, long numberOfColumns) {
		return Praat.INSTANCE.Table_createWithoutColumnNames(numberOfRows, numberOfColumns);
	}
	
	public double getMean (long column) {
		return Praat.INSTANCE.Table_getMean(this, column);
	}

	public void appendRow () {
		Praat.INSTANCE.Table_appendRow(this);
	}
	
	public void appendColumn (WString label) {
		Praat.INSTANCE.Table_appendColumn(this, label);
	}
	
	public void appendSumColumn ( long column1, long column2, WString label) {
		Praat.INSTANCE.Table_appendSumColumn(this, column1, column2, label);
	}
	
	public void appendDifferenceColumn ( long column1, long column2, WString label) {
		Praat.INSTANCE.Table_appendDifferenceColumn(this, column1, column2, label);
	}
	
	public void appendProductColumn ( long column1, long column2, WString label) {
		Praat.INSTANCE.Table_appendProductColumn(this, column1, column2, label);
	}
	
	public void appendQuotientColumn ( long column1, long column2, WString label) {
		Praat.INSTANCE.Table_appendQuotientColumn(this, column1, column2, label);
	}
	
	public void removeRow ( long row) {
		Praat.INSTANCE.Table_removeRow(this, row);
	}
	
	public void removeColumn ( long column) {
		Praat.INSTANCE.Table_removeColumn(this, column);
	}
	
	public void insertRow ( long row) {
		Praat.INSTANCE.Table_insertRow(this, row);
	}
	
	public void insertColumn ( long column, WString label) {
		Praat.INSTANCE.Table_insertColumn(this, column, label);
	}
	
	public void setColumnLabel ( long column, WString label) {
		Praat.INSTANCE.Table_setColumnLabel(this, column, label);
	}
	
	public long findColumnIndexFromColumnLabel ( WString label) {
		return Praat.INSTANCE.Table_findColumnIndexFromColumnLabel(this, label);
	}
	
	public long getColumnIndexFromColumnLabel ( WString label) {
		return Praat.INSTANCE.Table_getColumnIndexFromColumnLabel(this, label);
	}
	
	public Pointer getColumnIndicesFromColumnLabelString ( WString label, Pointer numberOfTokens) {
		return Praat.INSTANCE.Table_getColumnIndicesFromColumnLabelString(this, label, numberOfTokens);
	}
	
	public long searchColumn ( long column, WString value) {
		return Praat.INSTANCE.Table_searchColumn(this, column, value);
	}
	
	public WString  getStringValue_Assert (long row, long column) {
		return Praat.INSTANCE.Table_getStringValue_Assert(this, row, column);
	}
	
	public double getNumericValue_Assert (long row, long column) {
		return Praat.INSTANCE.Table_getNumericValue_Assert(this, row, column);
	}

	public void setStringValue (long row, long column, WString value) {
		Praat.INSTANCE.Table_setStringValue(this, row, column, value);
	}
	
	public void setNumericValue (long row, long column, double value) {
		Praat.INSTANCE.Table_setNumericValue(this, row, column, value);
	}

	public void numericize_Assert (long columnNumber) {
		Praat.INSTANCE.Table_numericize_Assert(this, columnNumber);
	}
	
	public double getMaximum (Table me, long icol) {
		return Praat.INSTANCE.Table_getMaximum(this, icol);
	}
	
	public double getMinimum (Table me, long icol) {
		return Praat.INSTANCE.Table_getMinimum(this, icol);
	}
	
	public double getGroupMean (Table me, long column, long groupColumn, WString group) {
		return Praat.INSTANCE.Table_getGroupMean(this, column, groupColumn, group);
	}
	
	public double getStdev (Table me, long column) {
		return Praat.INSTANCE.Table_getStdev(this, column);
	}
	
	public long drawRowFromDistribution (Table me, long column) {
		return Praat.INSTANCE.Table_drawRowFromDistribution(this, column);
	}
	
	public double getCorrelation_pearsonR (Table me, long column1, long column2, double significanceLevel,
		Pointer out_significance, Pointer out_lowerLimit, Pointer out_upperLimit) {
		return Praat.INSTANCE.Table_getCorrelation_pearsonR(this, column1, column2, significanceLevel, 
				out_significance, out_lowerLimit, out_upperLimit);
	}
	
	public double getCorrelation_kendallTau (Table me, long column1, long column2, double significanceLevel,
		Pointer out_significance, Pointer out_lowerLimit, Pointer out_upperLimit) {
		return Praat.INSTANCE.Table_getCorrelation_kendallTau(this, column1, column2, significanceLevel, 
				out_significance, out_lowerLimit, out_upperLimit);
	}
	
	public double getMean_studentT (Table me, long column, double significanceLevel,
		Pointer out_tFromZero, Pointer out_numberOfDegreesOfFreedom, Pointer out_significanceFromZero, Pointer out_lowerLimit, Pointer out_upperLimit) {
		return Praat.INSTANCE.Table_getMean_studentT(this, column, significanceLevel, out_tFromZero, 
				out_numberOfDegreesOfFreedom, out_significanceFromZero, out_lowerLimit, out_upperLimit);	
	}
	
	public double getDifference_studentT (Table me, long column1, long column2, double significanceLevel,
		Pointer out_t, Pointer out_numberOfDegreesOfFreedom, Pointer out_significance, Pointer out_lowerLimit, Pointer out_upperLimit) {
		return Praat.INSTANCE.Table_getDifference_studentT(this, column1, column2, significanceLevel, out_t, 
				out_numberOfDegreesOfFreedom, out_significance, out_lowerLimit, out_upperLimit);
	}
	
	public double getGroupMean_studentT (Table me, long column, long groupColumn, WString group1, double significanceLevel,
		Pointer out_tFromZero, Pointer out_numberOfDegreesOfFreedom, Pointer out_significanceFromZero, Pointer out_lowerLimit, Pointer out_upperLimit) {
		return Praat.INSTANCE.Table_getGroupMean_studentT(this, column, groupColumn, group1, significanceLevel, 
				out_tFromZero, out_numberOfDegreesOfFreedom, out_significanceFromZero, out_lowerLimit, out_upperLimit);
	}
	
	public double getGroupDifference_studentT (Table me, long column, long groupColumn, WString group1, WString group2, double significanceLevel,
		Pointer out_tFromZero, Pointer out_numberOfDegreesOfFreedom, Pointer out_significanceFromZero, Pointer out_lowerLimit, Pointer out_upperLimit) {
		return Praat.INSTANCE.Table_getGroupDifference_studentT(this, column, groupColumn, group1, group2, significanceLevel,
				out_tFromZero, out_numberOfDegreesOfFreedom, out_significanceFromZero, out_lowerLimit, out_upperLimit);
	}
	
	public double getGroupDifference_wilcoxonRankSum (Table me, long column, long groupColumn, WString group1, WString group2,
		Pointer out_rankSum, Pointer out_significanceFromZero) {
		return Praat.INSTANCE.Table_getGroupDifference_wilcoxonRankSum(this, column, groupColumn, 
				group1, group2, out_rankSum, out_significanceFromZero);
	}
	
	public double getVarianceRatio (Table me, long column1, long column2, double significanceLevel,
		Pointer out_significance, Pointer out_lowerLimit, Pointer out_upperLimit) {
		return Praat.INSTANCE.Table_getVarianceRatio(this, column1, column2, significanceLevel, 
				out_significance, out_lowerLimit, out_upperLimit);
	}
	
	public boolean getExtrema (Table me, long icol, Pointer minimum, Pointer maximum) {
		return Praat.INSTANCE.Table_getExtrema(this, icol, minimum, maximum);
	}
	
	public void sortRows_Assert (Pointer columns, long numberOfColumns) {
		Praat.INSTANCE.Table_sortRows_Assert(this, columns, numberOfColumns);
	}
	
	public void sortRows_string (WString columns_string) {
		Praat.INSTANCE.Table_sortRows_string(this, columns_string);
	}
	
	public void randomizeRows () {
		Praat.INSTANCE.Table_randomizeRows(this);
	}
	
	public void reflectRows () {
		Praat.INSTANCE.Table_reflectRows(this);
	}
	
	public void writeToTabSeparatedFile (MelderFile file) {
		Praat.INSTANCE.Table_writeToTabSeparatedFile(this, file);
	}
	
	public void writeToCommaSeparatedFile (MelderFile file) {
		Praat.INSTANCE.Table_writeToCommaSeparatedFile(this, file);
	}
	
	public static Table Table_readFromTableFile (MelderFile file) {
		return Praat.INSTANCE.Table_readFromTableFile(file);
	}
	
	public static Table Table_readFromCharacterSeparatedTextFile (MelderFile file, char separator) {
		return Praat.INSTANCE.Table_readFromCharacterSeparatedTextFile(file, separator);
	}
	
	public Table extractRowsWhereColumn_number (long column, int which_Melder_NUMBER, double criterion) {
		return Praat.INSTANCE.Table_extractRowsWhereColumn_number(this, column, which_Melder_NUMBER, criterion);
	}
	
	public Table extractRowsWhereColumn_string (long column, int which_Melder_STRING, WString criterion) {
		return Praat.INSTANCE.Table_extractRowsWhereColumn_string(this, column, which_Melder_STRING, criterion);
	}
	
	public Table collapseRows (WString factors_string, WString columnsToSum_string,
		WString columnsToAverage_string, WString columnsToMedianize_string,
		WString columnsToAverageLogarithmically_string, WString columnsToMedianizeLogarithmically_string) {
		return Praat.INSTANCE.Table_collapseRows(this, factors_string, columnsToSum_string, columnsToAverage_string,
				columnsToMedianize_string, columnsToAverageLogarithmically_string, columnsToMedianizeLogarithmically_string);
	}
	
	public Table rowsToColumns (WString factors_string, long columnToTranspose, WString columnsToExpand_string) {
		return Praat.INSTANCE.Table_rowsToColumns(this, factors_string, columnToTranspose, columnsToExpand_string);
	}
	
	public Table transpose (Table me){
		return Praat.INSTANCE.Table_transpose(this);
	}

	public void checkSpecifiedRowNumberWithinRange (long rowNumber) {
		Praat.INSTANCE.Table_checkSpecifiedRowNumberWithinRange(this, rowNumber);
	}
	
	public void checkSpecifiedColumnNumberWithinRange (long columnNumber) {
		Praat.INSTANCE.Table_checkSpecifiedColumnNumberWithinRange(this, columnNumber);
	}
	
	public boolean isCellNumeric_ErrorFalse (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_isCellNumeric_ErrorFalse(this, rowNumber, columnNumber);
	}
	
	public boolean isColumnNumeric_ErrorFalse (long columnNumber) {
		return Praat.INSTANCE.Table_isColumnNumeric_ErrorFalse(this, columnNumber);
	}
	
	public double getNrow () {
		return Praat.INSTANCE.Table_getNrow(this);
	}
	
	public double getNcol () {
		return Praat.INSTANCE.Table_getNcol(this);
	}
	
	public WString  getColStr (long columnNumber) {
		return Praat.INSTANCE.Table_getColStr(this, columnNumber);
	}
	
	public double getMatrix (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_getMatrix(this, rowNumber, columnNumber);
	}
	
	public WString  getMatrixStr (long rowNumber, long columnNumber) {
		return Praat.INSTANCE.Table_getMatrixStr(this, rowNumber, columnNumber);
	}
	
	public double getColIndex  (WString columnLabel) {
		return Praat.INSTANCE.Table_getColIndex(this, columnLabel);
	}
	
}
