package ca.hedlund.jpraat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.hedlund.jpraat.binding.Praat;

import com.sun.jna.Platform;

/**
 * 
 */
public class PraatMain {
	
	private final static Logger LOGGER = Logger.getLogger(PraatMain.class.getName());
	
	private static AtomicReference<Process> processRef = new AtomicReference<Process>();

	private static void invokeMainInNewProcess(String className, String[] args) {
		final String javaHome = System.getProperty("java.home");
		final String javaBin = javaHome + File.separator + "bin" + File.separator + "java" + 
				(Platform.isWindows() ? ".exe" : "");
		final String cp = System.getProperty("java.class.path");
		final String libPath = System.getProperty("java.library.path");
		
		List<String> fullCmd = new ArrayList<String>();
		String[] cmd = {
				javaBin,
				"-cp", cp,
				"-Djava.library.path=" + libPath
		};
		fullCmd.addAll(Arrays.asList(cmd));
		
		fullCmd.add(className);
		fullCmd.addAll(Arrays.asList(args));
		
		final ProcessBuilder pb = new ProcessBuilder(fullCmd);
		pb.redirectErrorStream(true);
		
		final StringBuilder builder = new StringBuilder();
		builder.append("Executing command:\n");
		for(String txt:fullCmd) {
			builder.append("\t" + txt + "\n");
		}
		
		LOGGER.info(builder.toString());
		try {
			final Process p = pb.start();
			processRef.getAndSet(p);
			final int exit = p.waitFor();
			LOGGER.log((exit == 0 ? Level.INFO : Level.WARNING), 
					"Praat exited with code " + exit);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			processRef.getAndSet(null);
		}
	}
	
	public static boolean isRunning() {
		final Process process = processRef.get();
		return process != null;
	}
	
	public static void runPraat() {
		runPraat(new String[0]);
	}
	
	public static void runPraat(final String[] args) {
		if(isRunning()) return;
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				invokeMainInNewProcess(PraatMain.class.getName(), args);
			}
			
		};
		final Thread th = new Thread(runnable);
		th.start();
	}
	
	public static void main(String[] args) {
		Praat.INSTANCE.praat_main(args.length, args);
	}
	
}
