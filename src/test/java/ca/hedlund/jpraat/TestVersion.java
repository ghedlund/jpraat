package ca.hedlund.jpraat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.sys.PraatDir;
import ca.hedlund.jpraat.binding.sys.PraatVersion;

@RunWith(JUnit4.class)
public class TestVersion  {
	
	@Before
	public void initPraat() {
		Praat.INSTANCE.praat_lib_init();
	}

	@Test
	public void testVersionInfo() {
		final PraatVersion version = PraatVersion.getVersion();
		System.out.println(version.versionStr);
		System.out.println(version.year + " " + version.month + " " + version.day);
		System.out.println("Praat pref path: " + PraatDir.getPath());
	}
	
}
