package ca.hedlund.jpraat.binding.fon;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

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
		TextGrid tg = TextGrid.createWithoutTiers(0.0, 10.0);
		
		IntervalTier tier = IntervalTier.create(0.0, 10.0);
		tier.setName(new WString("Hello World"));
		tier.removeInterval(1);
		tier.addInterval(1.0, 3.0, "Goodbye");
		tier.addInterval(4.0, 6.0, "Sanity");
		
		Assert.assertEquals(2, tier.numberOfIntervals());
		
		tg.addTier(tier);
		
		Assert.assertEquals(1, tg.numberOfTiers());
		
		// write file
		tg.writeToTextFile(MelderFile.fromPath("target/hello_world.TextGrid"));
		
		final TextGrid tg2 = TextGrid.readFromFile(TextGrid.class, MelderFile.fromPath("target/hello_world.TextGrid"));
		Assert.assertEquals(tg.numberOfTiers(), tg2.numberOfTiers());
		Assert.assertEquals(tg.getXmin(), tg2.getXmin());
		Assert.assertEquals(tg.getXmax(), tg2.getXmax());
		
		tg.writeToBinaryFile(MelderFile.fromPath("target/hello_world_bin.TextGrid"));
		
		final TextGrid tg3 = TextGrid.readFromFile(TextGrid.class, MelderFile.fromPath("target/hello_world_bin.TextGrid"));
		Assert.assertEquals(tg.numberOfTiers(), tg3.numberOfTiers());
		Assert.assertEquals(tg.getXmin(), tg3.getXmin());
		Assert.assertEquals(tg.getXmax(), tg3.getXmax());
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
