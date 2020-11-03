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
package ca.hedlund.jpraat.binding.jna;

import com.sun.jna.*;

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
