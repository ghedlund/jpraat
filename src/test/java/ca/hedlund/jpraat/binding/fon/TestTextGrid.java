package ca.hedlund.jpraat.binding.fon;

import java.io.File;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.stat.Table;
import ca.hedlund.jpraat.binding.sys.Data;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.exceptions.PraatException;

@RunWith(JUnit4.class)
public class TestTextGrid {

	@Before
	public void loadPraat() {
		Praat.INSTANCE.praat_lib_init();
	}
	
	@Test
	public void testCreate() throws PraatException {
		TextGrid tg = TextGrid.create(0.0, 10.0, "Hello World", "");
		Assert.assertEquals(2, tg.numberOfTiers());
		
		TextGrid tg2 = TextGrid.createWithoutTiers(0.0, 10.0);
		
		IntervalTier tier = IntervalTier.create(0.0, 10.0);
		tier.setName(new WString("Hello World"));
		tier.removeInterval(1);
		tier.addInterval(1.0, 3.0, "Goodbye");
		tier.addInterval(4.0, 6.0, "Sanity");
		
		Assert.assertEquals(2, tier.numberOfIntervals());
		
		tg2.addTier(tier);
		
		Assert.assertEquals(1, tg2.numberOfTiers());
		
		// write file
		tg2.writeToTextFile(MelderFile.fromPath("hello_world.TextGrid"));
	}
	
	@Test
	public void testRead() throws PraatException {
		final String path = "src/test/resources/ca/hedlund/jpraat/binding/fon/test.TextGrid";
		final MelderFile f = MelderFile.fromPath(path);
		
		TextGrid tg = Data.readFromFile(TextGrid.class, f);
		Assert.assertEquals(5, tg.numberOfTiers());
		
		Table tbl = tg.downto_Table(false, 2, true, false);
		final StringBuilder sb = new StringBuilder();
		
		for(int col = 1; col < tbl.getNcol(); col++) {
			if(col > 1) sb.append(',');
			sb.append('\"');
			sb.append(tbl.getColStr(col));
			sb.append('\"');
		}
		System.out.println(sb.toString());
		
		for(int row = 1; row <= tbl.getNrow(); row++) {
			sb.setLength(0);
			for(int col = 1; col < tbl.getNcol(); col++) {
				if(col > 1) sb.append(',');
				sb.append('\"');
				sb.append(tbl.getNumericValue(row, col));
				sb.append('\"');
			}
			System.out.println(sb.toString());
		}
	}

}
