package ca.hedlund.jpraat.binding.jna;

import java.util.HashMap;

import com.sun.jna.Library;
import com.sun.jna.NativeLibrary;

public class NativeLibraryOptions extends HashMap<Object, Object> {
	
	private static final long serialVersionUID = 1L;
	
	public NativeLibraryOptions() {
		super();
		
		put(Library.OPTION_TYPE_MAPPER, new NativeTypeMapper());
	}

}
