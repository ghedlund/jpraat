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

import org.junit.*;
import org.junit.Test;
import org.junit.runner.*;
import org.junit.runners.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.stat.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;
import junit.framework.Assert;

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
		Praat.initLibrary();
	}

	@Test
	public void testFormant() throws URISyntaxException, PraatException {
		final URL uri = 
				getClass().getResource(DEMO_SOUND);
		final File f = new File(uri.toURI());
		Assert.assertEquals(true, f.exists());
				
		try(final LongSound longSound = LongSound.open(MelderFile.fromPath(f.getAbsolutePath()))) {			
			try(final Sound sound = longSound.extractPart(XMIN, XMAX, true)) {
				try(final Formant formant = sound.to_Formant_burg(TIMESTEP, MAXFORMANTS, MAXFREQ, WINDOWLENGTH, PREEMPHASIS)) {
					try(final Table formantTable = formant.downto_Table(false, true, 3, true, 6, true, 6, true)) {
						// do formant table listing
						final StringBuilder sb = new StringBuilder();
						
						for(int col = 1; col < formantTable.getNcol(); col++) {
							if(col > 1) sb.append(',');
							sb.append('\"');
							sb.append(formantTable.getColStr(col));
							sb.append('\"');
						}
						System.out.println(sb.toString());
						
						for(int row = 1; row <= formantTable.getNrow(); row++) {
							sb.setLength(0);
							for(int col = 1; col < formantTable.getNcol(); col++) {
								if(col > 1) sb.append(',');
								sb.append('\"');
								sb.append(formantTable.getNumericValue(row, col));
								sb.append('\"');
							}
							System.out.println(sb.toString());
						}
					}
				}
			}
		} catch(Exception e) {
			throw new PraatException(e);
		}
	}
	
}
