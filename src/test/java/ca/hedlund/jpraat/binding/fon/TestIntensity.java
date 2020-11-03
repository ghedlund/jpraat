/*
 * Copyright (C) 2012-2020 Gregory Hedlund
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

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.concurrent.atomic.*;

import org.junit.*;
import org.junit.Test;
import org.junit.runner.*;
import org.junit.runners.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;
import junit.framework.Assert;

@RunWith(JUnit4.class)
public class TestIntensity {
	
	private final static String DEMO_SOUND = "DemoVideo.wav";
	
	private final static double XMIN = 7.737;
	private final static double XMAX = 10.478;
	
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
		
		try(final LongSound longSound = LongSound.open(MelderFile.fromPath(f.getAbsolutePath()))) {
			try(final Sound sound = longSound.extractPart(XMIN, XMAX, true)) {
				try(final Intensity intensity = sound.to_Intensity(50.0, 0.0, true)) {
					final AtomicReference<Long> ixminRef = new AtomicReference<Long>();
					final AtomicReference<Long> ixmaxRef = new AtomicReference<Long>();
					
					intensity.getWindowSamples(XMIN, XMAX, ixminRef, ixmaxRef);
					
					final int ixmin = ixminRef.get().intValue();
					final int ixmax = ixmaxRef.get().intValue();
					final NumberFormat format = NumberFormat.getNumberInstance();
					format.setMaximumFractionDigits(6);
					
					final PrintWriter out = 
							new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"));
					final char qc = '\"';
					final char sc = ',';
					
					final StringBuilder sb = new StringBuilder();
					sb.append(qc).append("Time(s)").append(qc).append(sc);
					sb.append(qc).append("Intensity(dB)").append(qc);
					out.println(sb.toString());
					sb.setLength(0);
					
					for(int i = ixmin; i < ixmax; i++) {
						final double time = intensity.indexToX(i);
						final double val = intensity.getValueAtSample(i, 1, Intensity.UNITS_DB);
						
						sb.append(qc).append(format.format(time)).append(qc).append(sc);
						sb.append(qc).append(format.format(val)).append(qc);
						out.println(sb.toString());
						sb.setLength(0);
					}
					
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
