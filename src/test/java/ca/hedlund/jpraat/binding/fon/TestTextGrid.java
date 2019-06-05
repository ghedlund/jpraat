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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.Daata;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;
import junit.framework.Assert;

@RunWith(JUnit4.class)
public class TestTextGrid {

	@Before
	public void loadPraat() {
		Praat.INSTANCE.praat_lib_init();
	}
	
	@Test
	public void testCreate() throws PraatException {
		try(TextGrid tg = TextGrid.createWithoutTiers(0.0, 10.0)) {
			// don't autoclose tiers - they are deleted with textgrid
			IntervalTier tier = IntervalTier.create(0.0, 10.0);
			tier.setName("Hello world");
			tier.removeInterval(1);
			tier.addInterval(1.0, 3.0, "Goodbye");
			tier.addInterval(4.0, 6.0, "Sanity");
			
			Assert.assertEquals(2, tier.numberOfIntervals());
			
			tg.addTier(tier);
			
			Assert.assertEquals(1, tg.numberOfTiers());
			
			// write file
			tg.writeToTextFile(MelderFile.fromPath("target/hello_world.TextGrid"));
			
			try(final TextGrid tg2 = TextGrid.readFromFile(TextGrid.class, MelderFile.fromPath("target/hello_world.TextGrid"))) {
				Assert.assertEquals(tg.numberOfTiers(), tg2.numberOfTiers());
				Assert.assertEquals(tg.getXmin(), tg2.getXmin());
				Assert.assertEquals(tg.getXmax(), tg2.getXmax());
			}
			
			tg.writeToBinaryFile(MelderFile.fromPath("target/hello_world_bin.TextGrid"));
			
			try(final TextGrid tg3 = TextGrid.readFromFile(TextGrid.class, MelderFile.fromPath("target/hello_world_bin.TextGrid"))) {
				Assert.assertEquals(tg.numberOfTiers(), tg3.numberOfTiers());
				Assert.assertEquals(tg.getXmin(), tg3.getXmin());
				Assert.assertEquals(tg.getXmax(), tg3.getXmax());
			}
		} catch (Exception e) {
			throw new PraatException(e);
		}
	}
	
	@Test
	public void testRead() throws PraatException {
		final String path = "src/test/resources/ca/hedlund/jpraat/binding/fon/test.TextGrid";
		final MelderFile f = MelderFile.fromPath(path);
		
		try(TextGrid tg = Daata.readFromFile(TextGrid.class, f)) {
			Assert.assertEquals(5, tg.numberOfTiers());
		} catch (Exception e) {
			throw new PraatException(e);
		}
	}

}
