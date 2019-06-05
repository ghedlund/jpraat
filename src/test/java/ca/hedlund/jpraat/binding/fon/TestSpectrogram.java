/*
 * Copyright (C) 2012-2018 Gregory Hedlund
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.hedlund.jpraat.binding.fon;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.Thing;
import ca.hedlund.jpraat.exceptions.PraatException;
import junit.framework.Assert;

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
	
	@Before
	public void init() {
		Praat.INSTANCE.praat_lib_init();
	}
	
	/**
	 * Test loading a {@link Spectrogram} from a {@link LongSound}.
	 */
	@Test
	public void testSpectrogram() throws URISyntaxException, PraatException {
		final URL uri = 
				getClass().getResource(DEMO_SOUND);
		final File f = new File(uri.toURI());
		Assert.assertEquals(true, f.exists());
		
		try(final LongSound longSound = LongSound.open(MelderFile.fromPath(f.getAbsolutePath()))) {
			try(final Sound sound = longSound.extractPart(XMIN, XMAX, true)) {
				try(final Spectrogram spectrogram = 
						sound.to_Spectrogram(TIMESTEP, YMAX, DX, FREQSTEP, 
						kSound_to_Spectrogram_windowShape.GAUSSIAN, 8.0, 8.0)) {
					Assert.assertEquals(X1, spectrogram.getX1());
					Assert.assertEquals(NX, spectrogram.getNx());
					Assert.assertEquals(DY, spectrogram.getDy());
					Assert.assertEquals(NY, spectrogram.getNy());
					
					final MelderFile textFile = MelderFile.fromPath(System.getProperty("user.home") + "/Desktop/test.txt");
					spectrogram.writeToTextFile(textFile);
				}
			}
		} catch (Exception e) {
			throw new PraatException(e);
		}
	}
	
}
