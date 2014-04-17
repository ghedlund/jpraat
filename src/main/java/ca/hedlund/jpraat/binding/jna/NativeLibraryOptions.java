package ca.hedlund.jpraat.binding.jna;

import java.util.HashMap;

import com.sun.jna.Library;
import com.sun.jna.NativeLibrary;
import com.sun.jna.win32.StdCallFunctionMapper;

public class NativeLibraryOptions extends HashMap<Object, Object> {
	
	private static final long serialVersionUID = 1L;
	
	public NativeLibraryOptions() {
		super();
		
//		put(Library.OPTION_FUNCTION_MAPPER, new NativeFunctionMapper());
		put(Library.OPTION_TYPE_MAPPER, new NativeTypeMapper());
	}

}
