package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.Praat;

import com.sun.jna.WString;

public class SendPraat {

	/* sendpraat.c */
	public static String sendpraat (Object display, String programName, long timeOut,  String text) {
		return Praat.INSTANCE.sendpraat(display, programName, timeOut, text);
	}
	
	public static WString sendpraatW (Object display, String programName, long timeOut, WString text) {
		return Praat.INSTANCE.sendpraatW(display, programName, timeOut, text);
	}
	
}
