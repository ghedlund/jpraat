package ca.hedlund.jpraat.binding.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

/**
 * Converter for {@link NativeEnum}s
 * 
 */
public class NativeEnumConverter implements TypeConverter {

	public Object fromNative(Object obj, FromNativeContext ctx) {
		final Integer nativeVal = (Integer) obj;
		final Class<?> targetClazz = ctx.getTargetType();
		Object retVal = null;
		if(targetClazz != null && 
				NativeEnum.class.isAssignableFrom(targetClazz)) {
			final Object[] enumVals = targetClazz.getEnumConstants();
			for(Object enumVal:enumVals) {
				final NativeEnum ne = (NativeEnum)enumVal;
				if(ne.getNativeValue() == nativeVal) {
					retVal = ne;
					break;
				}
			}
		}
		return retVal;
	}

	public Class<?> nativeType() {
		return Integer.class;
	}

	public Object toNative(Object obj, ToNativeContext ctx) {
		final NativeEnum ne = (NativeEnum)obj;
		return ne.getNativeValue();
	}

}
