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
package ca.hedlund.jpraat.binding.stat;

import com.sun.jna.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.binding.melder.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;

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
					new NativeIntptr_t(numberOfRows), new NativeIntptr_t(
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
			Praat.INSTANCE.TableOfReal_removeRow_wrapped(this, new NativeIntptr_t(
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
					new NativeIntptr_t(icol));
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
			Praat.INSTANCE.TableOfReal_insertRow_wrapped(this, new NativeIntptr_t(
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
					new NativeIntptr_t(icol));
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
					new NativeIntptr_t(irow), new Str32(label));
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
					.TableOfReal_setColumnLabel_wrapped(this, new NativeIntptr_t(icol), new Str32(label));
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
					this, new NativeIntptr_t(icol));
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
					this, new NativeIntptr_t(icol));
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
					new NativeIntptr_t(column1), new NativeIntptr_t(column2));
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
					new NativeIntptr_t(column1), new NativeIntptr_t(column2));
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

	public TableOfReal extractRowsWhereColumn (long icol, kMelder_number which, double criterion) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowsWhereColumn_wrapped(this,
							new NativeIntptr_t(icol), which,
							criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TableOfReal extractColumnsWhereRow (long icol, kMelder_number which, double criterion) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractColumnsWhereRow_wrapped(this,
							new NativeIntptr_t(icol), which,
							criterion);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public TableOfReal extractRowsWhereLabel (kMelder_string which, String criterion) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowsWhereLabel_wrapped(this,
							which, (criterion == null ? null : new Str32(criterion)));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TableOfReal extractColumnsWhereLabel (kMelder_string which, String criterion) throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowsWhereLabel_wrapped(this,
							which, (criterion == null ? null : new Str32(criterion)));
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TableOfReal extractRowsWhere (TableOfReal me, Str32 condition, Interpreter interpreter)
		throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractRowsWhere_wrapped(this, condition, interpreter);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public TableOfReal extractColumnsWhere (TableOfReal me, Str32 condition, Interpreter interpreter) 
		throws PraatException {
		TableOfReal retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.TableOfReal_extractColumnsWhere_wrapped(this, condition, interpreter);
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
