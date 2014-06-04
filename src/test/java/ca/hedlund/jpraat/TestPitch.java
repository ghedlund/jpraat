package ca.hedlund.jpraat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.hedlund.jpraat.binding.fon.LongSound;
import ca.hedlund.jpraat.binding.fon.Pitch;
import ca.hedlund.jpraat.binding.fon.Sound;
import ca.hedlund.jpraat.binding.sys.MelderFile;

@RunWith(JUnit4.class)
public class TestPitch {

private final static String DEMO_SOUND = "DemoVideo.wav";
	
	/*
	 * Expected values for tests
	 */
	private final static double XMIN = 7.737;
	private final static double XMAX = 10.478;
	private final static double TIMESTEP = 0.0;
	private final static double PITCHCEIL = 600.0;
	private final static double PITCHFLOOR = 75.0;
	
	@Test
	public void testPitch() {
		final String path = 
				getClass().getResource(DEMO_SOUND).getFile();
		final LongSound longSound = LongSound.open(MelderFile.fromPath(path));
		final Sound sound = longSound.extractPart(XMIN, XMAX, 1);
		final Pitch pitch = sound.to_Pitch(0.005, PITCHFLOOR, PITCHCEIL);
		
//		System.out.println(pitch.countVoicedFrames());
	}
	
}
