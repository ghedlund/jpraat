package ca.hedlund.jpraat.binding.jna;

import java.lang.reflect.Method;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;

public class NativeFunctionMapper implements FunctionMapper {

	public String getFunctionName(NativeLibrary arg0, Method arg1) {
		return "_" + arg1.getName();
	}

}
