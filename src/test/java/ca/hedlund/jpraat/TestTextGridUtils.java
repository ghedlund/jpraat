package ca.hedlund.jpraat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.fon.TextGrid;
import ca.hedlund.jpraat.binding.fon.TextInterval;
import ca.hedlund.jpraat.binding.sys.Data;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import junit.framework.Assert;

@RunWith(JUnit4.class)
public class TestTextGridUtils {
	
	@Before
	public void loadPraat() {
		Praat.INSTANCE.praat_lib_init();
	}

	@Test
	public void textReadContiguousIntervals() throws Exception {
		final long tierIdx = 2;
		final double tolerence = 0.05;
		final double maxLength = 2.0;
		final String delim = "#";
		
		final String path = "src/test/resources/ca/hedlund/jpraat/binding/fon/test.TextGrid";
		final MelderFile f = MelderFile.fromPath(path);
		
		TextGrid tg = Data.readFromFile(TextGrid.class, f);
		
		List<TextInterval> intervals = TextGridUtils.getContiguousIntervals(tg, tierIdx, tolerence, maxLength, delim);
		Assert.assertEquals(13, intervals.size());
		for(TextInterval interval:intervals) {
			System.out.println(interval.getText() + " (xmin = " + interval.getXmin() + ", xmax = " + interval.getXmax() + ")");
		}
	}
	
}
