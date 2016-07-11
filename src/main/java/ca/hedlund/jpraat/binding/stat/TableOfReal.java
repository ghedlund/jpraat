package ca.hedlund.jpraat.binding.stat;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.binding.sys.Daata;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.Strings;
import ca.hedlund.jpraat.exceptions.PraatException;

public class TableOfReal extends Daata {
	
	public TableOfReal() {
		super();
	}
	
	public TableOfReal(Pointer p) {
		super(p);
	}

	public static TableOfReal create (long numberOfRows, long numberOfColumns) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TableOfReal_create_wrapped(
					new NativeLong(numberOfRows), new NativeLong(
							numberOfColumns));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void removeRow (long irow) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TableOfReal_removeRow_wrapped(this, new NativeLong(
					irow));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void removeColumn (long icol) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TableOfReal_removeColumn_wrapped(this,
					new NativeLong(icol));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insertRow (long irow) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TableOfReal_insertRow_wrapped(this, new NativeLong(
					irow));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void insertColumn (long icol) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TableOfReal_insertColumn_wrapped(this,
					new NativeLong(icol));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void setRowLabel (long irow, String label) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TableOfReal_setRowLabel_wrapped(this,
					new NativeLong(irow), new Str32(label));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void setColumnLabel (long icol, String label) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE
					.TableOfReal_setColumnLabel_wrapped(this, new NativeLong(icol), new Str32(label));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public long rowLabelToIndex (String label) throws PraatException {
		long retVal = 0L;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TableOfReal_rowLabelToIndex_wrapped(
					this, new Str32(label)).longValue();
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public long columnLabelToIndex (String label) throws PraatException {
		long retVal = 0L;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_columnLabelToIndex_wrapped(this, new Str32(label))
					.longValue();
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getColumnMean (long icol) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TableOfReal_getColumnMean_wrapped(
					this, new NativeLong(icol));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getColumnStdev (long icol) throws PraatException {
		double retVal = 0.0;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TableOfReal_getColumnStdev_wrapped(
					this, new NativeLong(icol));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Table to_Table (String labelOfFirstColumn) throws PraatException {
		Table retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TableOfReal_to_Table_wrapped(this,
					new Str32(labelOfFirstColumn));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public void sortByLabel (long column1, long column2) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TableOfReal_sortByLabel_wrapped(this,
					new NativeLong(column1), new NativeLong(column2));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public void sortByColumn (long column1, long column2) throws PraatException {
		try {
			Praat.wrapperLock.lock();
			Praat.INSTANCE.TableOfReal_sortByColumn_wrapped(this,
					new NativeLong(column1), new NativeLong(column2));
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
			Praat.INSTANCE
					.TableOfReal_writeToHeaderlessSpreadsheetFile_wrapped(this,
							file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
	}
	
	public TableOfReal readFromHeaderlessSpreadsheetFile (MelderFile file) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_readFromHeaderlessSpreadsheetFile_wrapped(file);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public TableOfReal extractRowRanges (String ranges) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowRanges_wrapped(this, new Str32(ranges));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TableOfReal extractColumnRanges (String ranges) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractColumnRanges_wrapped(this, new Str32(ranges));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public TableOfReal extractRowsWhereColumn (long icol, int which_Melder_NUMBER, double criterion) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowsWhereColumn_wrapped(this,
							new NativeLong(icol), which_Melder_NUMBER,
							criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TableOfReal extractColumnsWhereRow (long icol, int which_Melder_NUMBER, double criterion) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractColumnsWhereRow_wrapped(this,
							new NativeLong(icol), which_Melder_NUMBER,
							criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public TableOfReal extractRowsWhereLabel (int which_Melder_STRING, String criterion) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowsWhereLabel_wrapped(this,
							which_Melder_STRING, (criterion == null ? null : new Str32(criterion)));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TableOfReal extractColumnsWhereLabel (int which_Melder_STRING, String criterion) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowsWhereLabel_wrapped(this,
							which_Melder_STRING, (criterion == null ? null : new Str32(criterion)));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	Strings extractRowLabelsAsStrings () throws PraatException {
		Strings retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowLabelsAsStrings_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	Strings extractColumnLabelsAsStrings () throws PraatException {
		Strings retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.TableOfReal_extractColumnLabelsAsStrings_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
}
