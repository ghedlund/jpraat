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
package ca.hedlund.jpraat;

import java.util.*;

import org.junit.*;
import org.junit.Test;
import org.junit.runner.*;
import org.junit.runners.*;

import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.fon.*;
import ca.hedlund.jpraat.binding.sys.*;
import ca.hedlund.jpraat.exceptions.*;
import junit.framework.Assert;

@RunWith(JUnit4.class)
public class TestTextGridUtils {
	
	@Before
	public void loadPraat() {
		Praat.initLibrary();
	}

	@Test
	public void textReadContiguousIntervals() throws Exception {
		final long tierIdx = 2;
		final double tolerence = 0.05;
		final double maxLength = 2.0;
		final String delim = "#";
		
		final String path = "src/test/resources/ca/hedlund/jpraat/binding/fon/test.TextGrid";
		final MelderFile f = MelderFile.fromPath(path);
		
		try(TextGrid tg = Daata.readFromFile(TextGrid.class, f)) {
			List<TextInterval> intervals = TextGridUtils.getContiguousIntervals(tg, tierIdx, tolerence, maxLength, delim);
			Assert.assertEquals(13, intervals.size());
			for(TextInterval interval:intervals) {
				System.out.println(interval.getText() + " (xmin = " + interval.getXmin() + ", xmax = " + interval.getXmax() + ")");
			}
			
			// delete intervals
			intervals.forEach(t -> {
				try {
					t.forget();
				} catch (PraatException e) {
					e.printStackTrace();
				}
			});
		}
	}
	
}
