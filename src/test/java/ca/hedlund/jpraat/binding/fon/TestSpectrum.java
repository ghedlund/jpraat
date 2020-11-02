/*
 * Copyright (C) 2005-2020 Gregory Hedlund
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
import ca.hedlund.jpraat.binding.stat.Table;
import ca.hedlund.jpraat.binding.sys.Interpreter;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;
import junit.framework.Assert;

@RunWith(JUnit4.class)
public class TestSpectrum {
	
	private final static String DEMO_SOUND = "DemoVideo.wav";
	
	private final static double XMIN = 7.737;
	private final static double XMAX = 10.478;
	
	@Before
	public void init() {
		Praat.INSTANCE.praat_lib_init();
	}

	@Test
	public void testSpectrum() throws URISyntaxException, PraatException {
		final URL uri = 
				getClass().getResource(DEMO_SOUND);
		final File f = new File(uri.toURI());
		Assert.assertEquals(true, f.exists());
		
		try(final LongSound longSound = LongSound.open(MelderFile.fromPath(f.getAbsolutePath()))) {
			try(final Sound sound = longSound.extractPart(XMIN, XMAX, true)) {
				try(final Spectrum spectrum = sound.to_Spectrum()) {
					final Table table = spectrum.downto_Table(true, true, true, true, true, true);
					final StringBuilder sb = new StringBuilder();
					
					for(int col = 1; col < 7; col++) {
						if(col > 1) sb.append(',');
						sb.append('\"');
						sb.append(table.getColStr(col));
						sb.append('\"');
					}
					System.out.println(sb.toString());
					
					for(int row = 1; row <= 100; row++) {
						sb.setLength(0);
						for(int col = 1; col < 7; col++) {
							if(col > 1) sb.append(',');
							sb.append('\"');
							sb.append(table.getNumericValue(row, col));
							sb.append('\"');
						}
						System.out.println(sb.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSpectrumFormula() throws URISyntaxException, PraatException {
		final URL uri = 
				getClass().getResource(DEMO_SOUND);
		final File f = new File(uri.toURI());
		Assert.assertEquals(true, f.exists());
		
		try(final LongSound longSound = LongSound.open(MelderFile.fromPath(f.getAbsolutePath()))) {
			try(final Sound sound = longSound.extractPart(XMIN, XMAX, true)) {
				try(final Spectrum spectrum = sound.to_Spectrum()) {
					try(final Interpreter interpreter = Interpreter.create()) {
						spectrum.formula("if x >= 2500 then self*x else self fi", interpreter, null);
					}
				}
			}
		} catch (Exception e) {
			throw new PraatException(e);
		}
	}
	
}
