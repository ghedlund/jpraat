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
package ca.hedlund.jpraat.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.hedlund.jpraat.binding.Praat;

@RunWith(JUnit4.class)
public class TestCreateWrapper {

	private final static String CODEGEN_FOLDER = "target/generated-sources/cpp/";
	
	private final static String CODEGEN_FILE = "jpraat.cpp";
	
	/**
	 * Generates file target/generated-sources/cpp/jpraat.cpp
	 * during test phase.
	 * 
	 * @throws IOException
	 */
	@Test
	public void createWrapperCode() throws IOException {
		final File folder = new File(CODEGEN_FOLDER);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		final File f = new File(folder, CODEGEN_FILE);
		final PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
		
		final WrapperGenerator generator = new WrapperGenerator();
		generator.processClass(Praat.class);
		
		out.write(generator.getGeneratedSource());
		out.flush();
		out.close();
	}
	
}
