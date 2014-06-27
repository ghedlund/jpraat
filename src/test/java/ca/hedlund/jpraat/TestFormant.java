package ca.hedlund.jpraat;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.fon.Formant;
import ca.hedlund.jpraat.binding.fon.LongSound;
import ca.hedlund.jpraat.binding.fon.Sound;
import ca.hedlund.jpraat.binding.sys.MelderFile;

@RunWith(JUnit4.class)
public class TestFormant {
	
	private final static String DEMO_SOUND = "DemoVideo.wav";
	
	private final static double XMIN = 7.737;
	private final static double XMAX = 10.478;
	private final static double TIMESTEP = 0.0; // automatic
	private final static double MAXFREQ = 5000.0;
	private final static double MAXFORMANTS = 5;
	private final static double WINDOWLENGTH = 0.2;
	private final static double PREEMPHASIS = 50.0;
	
	@Before
	public void init() {
		Praat.INSTANCE.praat_lib_init();
	}

	@Test
	public void testFormant() throws URISyntaxException {
		final URL uri = 
				getClass().getResource(DEMO_SOUND);
		final File f = new File(uri.toURI());
		Assert.assertEquals(true, f.exists());
		
		final LongSound longSound = LongSound.open(MelderFile.fromPath(f.getAbsolutePath()));
		final Sound sound = longSound.extractPart(XMIN, XMAX, 1);
		final Formant formant = sound.to_Formant_burg(TIMESTEP, MAXFORMANTS, MAXFREQ, WINDOWLENGTH, PREEMPHASIS);
		
		// print a table of means
		final StringBuilder sb = new StringBuilder();
		sb.append('\"');
		sb.append("Time");
		sb.append('\"');
		
		for(int i = 0; i <= MAXFORMANTS; i++) {
			sb.append('\t');
			sb.append('\"');
			sb.append("f");
			sb.append(i);
			sb.append('\"');
		}
		System.out.println(sb.toString());
		sb.setLength(0);
		
		for(double x = XMIN; x <= XMAX; x += formant.getDx()) {
			sb.append('\"');
			sb.append(x);
			sb.append('\"');
			
			for(int i = 1; i <= MAXFORMANTS; i++) {
				final double value = formant.getValueAtTime(i, x, 1);
				sb.append('\t');
				sb.append('\"');
				sb.append(value);
				sb.append('\"');
			}
			System.out.println(sb.toString());
			sb.setLength(0);
		}
	}
	
}
