package ca.hedlund.jpraat.binding.sys;

import java.util.Arrays;
import java.util.List;

import ca.hedlund.jpraat.binding.Praat;

import com.sun.jna.Structure;

public class PraatVersion extends Structure {

	public String versionStr;
	public int version;
	public int year;
	public String month;
	public int day;

	@Override
	protected List getFieldOrder() {
		return Arrays.asList(new String[]{
				"versionStr", 
				"version",
				"year",
				"month",
				"day"
		});
	}
	
	public static PraatVersion getVersion() {
		return Praat.INSTANCE.praat_version();
	}

}
