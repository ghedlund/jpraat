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

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.util.Arrays;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

/**
 * Wrapper type for c++ (<code>char32_t</code>) strings.  Use this class for
 * parameters and return values with type <code>const char32_t*</code>.
 */
public class Str32 extends PointerType implements CharSequence, Comparable<Str32> {
	
	private final int CHAR32_SIZE = 4;
	
	private final static String CHAR32_ENCODING = "UTF_32LE";
	
	public Str32() {
		super();
	}
	
	public Str32(Pointer p) {
		super(p);
	}
	
	public Str32(String val) {
		super();
		setValue(val);
	}
	
	protected void setValue(String val) {
		byte[] charBytes = new byte[0];
		try {
			charBytes = val.getBytes(CHAR32_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// sizeof string + '\0'
		int len = charBytes.length + CHAR32_SIZE;
		byte[] buffer = Arrays.copyOf(charBytes, len);
		// set null terminator
		buffer[len-4] = buffer[len-3] = buffer[len-2] = buffer[len-1] = '\0';

		Pointer myPtr = new Memory(buffer.length);
		myPtr.write(0, buffer, 0, len);
		setPointer(myPtr);
	}
	
	public byte[] getBytes() {
		int offset = 0;
		Pointer p = getPointer();
		// use int since it has the same size as char32_t
		while( p.getInt(offset) != '\0' ) {
			offset += CHAR32_SIZE;
		}
		
		return p.getByteArray(0, offset);
	}
	
	@Override
	public String toString() {
		String retVal = "";
		try {
			retVal = new String(getBytes(), CHAR32_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Str32 && toString().equals(o.toString());
	}

	@Override
	public int compareTo(Str32 o) {
		return toString().compareTo(o.toString());
	}

	@Override
	public char charAt(int arg0) {
		return toString().charAt(arg0);
	}

	@Override
	public int length() {
		return toString().length();
	}

	@Override
	public CharSequence subSequence(int arg0, int arg1) {
		return CharBuffer.wrap(toString()).subSequence(arg0, arg1);
	}

}
