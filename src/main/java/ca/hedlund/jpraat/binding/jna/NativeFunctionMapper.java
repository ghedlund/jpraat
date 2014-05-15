package ca.hedlund.jpraat.binding.jna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.imageio.plugins.common.InputStreamAdapter;
import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;

public class NativeFunctionMapper implements FunctionMapper {
	
	private final static Logger LOGGER = 
			Logger.getLogger(NativeFunctionMapper.class.getName());
	
	private final static String SYMTBL = "META-INF/symbolmap";
	
	private final Map<String, String> symbolMap = new LinkedHashMap<String, String>();
	
	public NativeFunctionMapper() {
		super();
		loadSymbolMap();
	}
	
	private void loadSymbolMap() {
		final ClassLoader cl = ClassLoader.getSystemClassLoader();
		final InputStream in = cl.getResourceAsStream(SYMTBL);
		if(in != null) {
			try {
				final BufferedReader reader = new BufferedReader(
						new InputStreamReader(in, "UTF-8"));
				String line = null;
				while((line = reader.readLine()) != null) {
					final String[] tokens = line.split(";");
					if(tokens.length == 2) {
						symbolMap.put(tokens[0], tokens[1]);
					}
				}
				reader.close();
			} catch (UnsupportedEncodingException e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	public String getFunctionName(NativeLibrary arg0, Method arg1) {
		return symbolMap.get(arg1.getName());
	}

}
