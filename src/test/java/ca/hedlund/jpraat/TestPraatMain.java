package ca.hedlund.jpraat;

import ca.hedlund.jpraat.binding.Praat;

public class TestPraatMain {
	
	public static void main(String[] args) {
		Praat.INSTANCE.praat_main(args.length, args);
	}

}
