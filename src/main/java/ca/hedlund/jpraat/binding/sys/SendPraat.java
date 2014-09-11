package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Declared;

import com.sun.jna.WString;

public class SendPraat {

	@Declared("sys/sendpraat.c")
	public static String sendpraat (Object display, String programName, long timeOut,  String text) {
		return Praat.INSTANCE.sendpraat(display, programName, timeOut, text);
	}
	
	@Declared("sys/sendpraat.c")
	public static WString sendpraatW (Object display, String programName, long timeOut, WString text) {
		return Praat.INSTANCE.sendpraatW(display, programName, timeOut, text);
	}
	
}
