package ca.hedlund.jpraat.binding.stat;

import ca.hedlund.jpraat.binding.sys.Data;

public class TableOfReal extends Data {

	public static TableOfReal create(long numberOfRows, long numberOfColumns) {
		return new TableOfReal();
	}
	
}
