package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;

import com.sun.jna.NativeLong;
import com.sun.jna.WString;

public class SendPraat {

	@Declared("sys/sendpraat.c")
	public static String sendpraat (Object display, String programName, long timeOut,  String text) {
		return Praat.INSTANCE.sendpraat(display, programName, new NativeLong(timeOut), text);
	}
	
	@Declared("sys/sendpraat.c")
	public static WString sendpraatW (Object display, String programName, long timeOut, WString text) {
		return Praat.INSTANCE.sendpraatW(display, programName, new NativeLong(timeOut), text);
	}
	
}
