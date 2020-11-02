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

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

/**
 * Wrapper for the type intptr_t
 * 
 * Praat declares
 * <pre>using integer as intptr_t</pre>
 */
public final class NativeIntptr_t extends IntegerType {

	private static final long serialVersionUID = 1L;
	
	public final static int SIZE = Native.LONG_SIZE;
	
	public NativeIntptr_t() {
		this(0L);
	}
	
	/** Create a NativeIntptr_t with the given value. */
    public NativeIntptr_t(long value) {
        this(value, false);
    }

    /** Create a NativeIntptr_t with the given value, optionally unsigned. */
    public NativeIntptr_t(long value, boolean unsigned) {
        super(SIZE, value, unsigned);
    }
    
}
