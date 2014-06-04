package ca.hedlund.jpraat;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.hedlund.jpraat.binding.fon.LongSound;
import ca.hedlund.jpraat.binding.fon.Sound;
import ca.hedlund.jpraat.binding.fon.Spectrogram;
import ca.hedlund.jpraat.binding.fon.kSound_to_Spectrogram_windowShape;
import ca.hedlund.jpraat.binding.sys.MelderFile;

@RunWith(JUnit4.class)
public class TestSpectrogram {

	private final static String DEMO_SOUND = "DemoVideo.wav";
	
	/*
	 * Expected values for tests
	 */
	private final static double XMIN = 7.737;
	private final static double XMAX = 10.478;
	private final static double TIMESTEP = 0.005;
	private final static double FREQSTEP = 20.0;
	private final static double DX = 0.002;
	private final static double X1 = 7.7425;
	private final static int NX = 1366;
	private final static double YMAX = 5000.0;
	private final static double DY = 31.25;
	private final static int NY = 160;
	
	/**
	 * Test loading a {@link Spectrogram} from a {@link LongSound}.
	 */
	@Test
	public void testSpectrogram() {
		final String path = 
				getClass().getResource(DEMO_SOUND).getFile();
		final LongSound longSound = LongSound.open(MelderFile.fromPath(path));
		final Sound sound = longSound.extractPart(XMIN, XMAX, 1);
		final Spectrogram spectrogram = 
				sound.toSpectrogram(TIMESTEP, YMAX, DX, FREQSTEP, 
				kSound_to_Spectrogram_windowShape.GAUSSIAN, 8.0, 8.0);
		
		
		Assert.assertEquals(X1, spectrogram.getX1());
		Assert.assertEquals(NX, spectrogram.getNx());
		Assert.assertEquals(DY, spectrogram.getDy());
		Assert.assertEquals(NY, spectrogram.getNy());
		
		final MelderFile textFile = MelderFile.fromPath(System.getProperty("user.home") + "/Desktop/test.txt");
		spectrogram.writeToTextFile(textFile);
	}
	
}
