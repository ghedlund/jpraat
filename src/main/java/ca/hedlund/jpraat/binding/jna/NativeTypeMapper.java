package ca.hedlund.jpraat.binding.jna;

import com.sun.jna.DefaultTypeMapper;

public class NativeTypeMapper extends DefaultTypeMapper {

	public NativeTypeMapper() {
		super();
		
		addTypeConverter(NativeEnum.class, new NativeEnumConverter());
	}
	
}
