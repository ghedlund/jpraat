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
public class CreateWrapper {

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
