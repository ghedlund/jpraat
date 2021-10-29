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
package ca.hedlund.jpraat.binding.sys;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import ca.hedlund.jpraat.binding.*;

@RunWith(JUnit4.class)
public class TestVersion  {
	
	@Before
	public void initPraat() {
		Praat.initLibrary();
	}

	@Test
	public void testVersionInfo() {
		final PraatVersion version = PraatVersion.getVersion();
		System.out.println(version.versionStr);
		System.out.println(version.year + " " + version.month + " " + version.day);
		System.out.println("Praat pref path: " + PraatDir.getPath());
	}
	
}
