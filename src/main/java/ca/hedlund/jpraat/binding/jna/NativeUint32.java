package ca.hedlund.jpraat.binding.jna;

import com.sun.jna.IntegerType;

public class NativeUint32 extends IntegerType {
	
	private static final long serialVersionUID = 1L;
	
	// 4 bytes
	public final static int SIZE = 4;
	
	public NativeUint32() {
		this(0);
	}
	
	public NativeUint32(int value) {
		super(SIZE, value, true);
	}
	
}
