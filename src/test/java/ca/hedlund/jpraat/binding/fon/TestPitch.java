package ca.hedlund.jpraat.binding.fon;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.fon.LongSound;
import ca.hedlund.jpraat.binding.fon.Pitch;
import ca.hedlund.jpraat.binding.fon.Sound;
import ca.hedlund.jpraat.binding.fon.kPitch_unit;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;

@RunWith(JUnit4.class)
public class TestPitch {

	private final static String DEMO_SOUND = "DemoVideo.wav";
	
	private final static double XMIN = 7.737;
	private final static double XMAX = 10.478;
	private final static double TIMESTEP = 0.0; // automatic
	private final static double PITCHFLOOR = 75.0;
	private final static double PITCHCEIL = 600.0;
	
	@Before
	public void init() {
		Praat.INSTANCE.praat_lib_init();
	}
	
	@Test
	public void testPitch() throws URISyntaxException, PraatException {
		final URL uri = 
				getClass().getResource(DEMO_SOUND);
		final File f = new File(uri.toURI());
		Assert.assertEquals(true, f.exists());
		
		final MelderFile melderFile = MelderFile.fromPath(f.getAbsolutePath());
		final LongSound longSound = LongSound.open(melderFile);
		final Sound sound = longSound.extractPart(XMIN, XMAX, 1);
		final Pitch pitch = sound.to_Pitch(TIMESTEP, PITCHFLOOR, PITCHCEIL);
		
		final AtomicReference<Long> ixminRef = new AtomicReference<Long>();
		final AtomicReference<Long> ixmaxRef = new AtomicReference<Long>();
		pitch.getWindowSamples(XMIN, XMAX, ixminRef, ixmaxRef);
		
		final StringBuilder sb = new StringBuilder();
		sb.append("\"Time\"");
		
		for(kPitch_unit unit:kPitch_unit.values()) {
			sb.append('\t');
			sb.append('\"');
			sb.append(unit.getName());
			sb.append('\"');
		}
		System.out.println(sb.toString());
		sb.setLength(0);
		
		final long ixmin = ixminRef.get();
		final long ixmax = ixmaxRef.get();
		for(long ix = ixmin; ix <= ixmax; ix++) {
			final double time = (XMIN + (ix * pitch.getDx()));
			sb.append('\"');
			sb.append(time);
			sb.append('\"');
			
			for(kPitch_unit unit:kPitch_unit.values()) {
				double value = pitch.getValueAtSample(ix, Pitch.LEVEL_FREQUENCY, unit.getNativeValue());
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
