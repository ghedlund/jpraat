package ca.hedlund.jpraat.binding.stat;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.annotations.Wrapped;
import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.Data;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.Strings;
import ca.hedlund.jpraat.exceptions.PraatException;

public class TableOfReal extends Data {
	
	public TableOfReal() {
		super();
	}
	
	public TableOfReal(Pointer p) {
		super(p);
	}

	public static TableOfReal create (long numberOfRows, long numberOfColumns) throws PraatException {
		TableOfReal retVal = Praat.INSTANCE.TableOfReal_create_wrapped(new NativeLong(numberOfRows), new NativeLong(numberOfColumns));
		Praat.checkLastError();
		return retVal;
	}
	
	public void removeRow (long irow) throws PraatException {
		Praat.INSTANCE.TableOfReal_removeRow_wrapped(this, new NativeLong(irow));
		Praat.checkLastError();
	}
	
	public void removeColumn (long icol) throws PraatException {
		Praat.INSTANCE.TableOfReal_removeColumn_wrapped(this, new NativeLong(icol));
		Praat.checkLastError();
	}
	
	public void insertRow (long irow) throws PraatException {
		Praat.INSTANCE.TableOfReal_insertRow_wrapped(this, new NativeLong(irow));
		Praat.checkLastError();
	}
	
	public void insertColumn (long icol) throws PraatException {
		Praat.INSTANCE.TableOfReal_insertColumn_wrapped(this, new NativeLong(icol));
		Praat.checkLastError();
	}
	
	public void setRowLabel (long irow, WString label) throws PraatException {
		Praat.INSTANCE.TableOfReal_setRowLabel_wrapped(this, new NativeLong(irow), label);
		Praat.checkLastError();
	}
	
	public void setColumnLabel (long icol, WString label) throws PraatException {
		
	}
	
	public long rowLabelToIndex (WString label) throws PraatException {
		long retVal = Praat.INSTANCE.TableOfReal_rowLabelToIndex_wrapped(this, label).longValue();
		Praat.checkLastError();
		return retVal;
	}
	
	public long columnLabelToIndex (WString label) throws PraatException {
		long retVal = Praat.INSTANCE.TableOfReal_columnLabelToIndex_wrapped(this, label).longValue();
		Praat.checkLastError();
		return retVal;
	}
	
	public double getColumnMean (long icol) throws PraatException {
		double retVal = Praat.INSTANCE.TableOfReal_getColumnMean_wrapped(this, new NativeLong(icol));
		Praat.checkLastError();
		return retVal;
	}
	
	public double getColumnStdev (long icol) throws PraatException {
		double retVal = Praat.INSTANCE.TableOfReal_getColumnStdev_wrapped(this, new NativeLong(icol));
		Praat.checkLastError();
		return retVal;
	}
	
	public Table to_Table (WString labelOfFirstColumn) throws PraatException {
		Table retVal = Praat.INSTANCE.TableOfReal_to_Table_wrapped(this, labelOfFirstColumn);
		Praat.checkLastError();
		return retVal;
	}
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void sortByLabel (long column1, long column2) throws PraatException {
		Praat.INSTANCE.TableOfReal_sortByLabel_wrapped(this, new NativeLong(column1), new NativeLong(column2));
		Praat.checkLastError();
	}
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void sortByColumn (long column1, long column2) throws PraatException {
		Praat.INSTANCE.TableOfReal_sortByColumn_wrapped(this, new NativeLong(column1), new NativeLong(column2));
		Praat.checkLastError();
	}

	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void writeToHeaderlessSpreadsheetFile (MelderFile file) throws PraatException {
		Praat.INSTANCE.TableOfReal_writeToHeaderlessSpreadsheetFile_wrapped(this, file);
		Praat.checkLastError();
	}
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public TableOfReal readFromHeaderlessSpreadsheetFile (MelderFile file) throws PraatException {
		TableOfReal retVal = Praat.INSTANCE.TableOfReal_readFromHeaderlessSpreadsheetFile_wrapped(file);
		Praat.checkLastError();
		return retVal;
	}

	@Declared("stat/TableOfReal.h")
	@Wrapped
	public TableOfReal extractRowRanges (WString ranges) throws PraatException {
		TableOfReal retVal = Praat.INSTANCE.TableOfReal_extractRowRanges_wrapped(this, ranges);
		Praat.checkLastError();
		return retVal;
	}
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public TableOfReal extractColumnRanges (WString ranges) throws PraatException {
		TableOfReal retVal = Praat.INSTANCE.TableOfReal_extractColumnRanges_wrapped(this, ranges);
		Praat.checkLastError();
		return retVal;
	}

	@Declared("stat/TableOfReal.h")
	@Wrapped
	public TableOfReal extractRowsWhereColumn (long icol, int which_Melder_NUMBER, double criterion) throws PraatException {
		TableOfReal retVal = Praat.INSTANCE.TableOfReal_extractRowsWhereColumn_wrapped(this, new NativeLong(icol), which_Melder_NUMBER, criterion);
		Praat.checkLastError();
		return retVal;
	}
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public TableOfReal extractColumnsWhereRow (long icol, int which_Melder_NUMBER, double criterion) throws PraatException {
		TableOfReal retVal = Praat.INSTANCE.TableOfReal_extractColumnsWhereRow_wrapped(this, new NativeLong(icol), which_Melder_NUMBER, criterion);
		Praat.checkLastError();
		return retVal;
	}

	@Declared("stat/TableOfReal.h")
	@Wrapped
	public TableOfReal extractRowsWhereLabel (int which_Melder_STRING, WString criterion) throws PraatException {
		TableOfReal retVal = Praat.INSTANCE.TableOfReal_extractRowsWhereLabel_wrapped(this, which_Melder_STRING, criterion);
		Praat.checkLastError();
		return retVal;
	}
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public TableOfReal extractColumnsWhereLabel (int which_Melder_STRING, WString criterion) throws PraatException {
		TableOfReal retVal = Praat.INSTANCE.TableOfReal_extractRowsWhereLabel_wrapped(this, which_Melder_STRING, criterion);
		Praat.checkLastError();
		return retVal;
	}

	@Declared("stat/TableOfReal.h")
	@Wrapped
	Strings extractRowLabelsAsStrings () throws PraatException {
		Strings retVal = Praat.INSTANCE.TableOfReal_extractRowLabelsAsStrings_wrapped(this);
		Praat.checkLastError();
		return retVal;
	}
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	Strings extractColumnLabelsAsStrings () throws PraatException {
		Strings retVal = Praat.INSTANCE.TableOfReal_extractColumnLabelsAsStrings_wrapped(this);
		Praat.checkLastError();
		return retVal;
	}
	
}
