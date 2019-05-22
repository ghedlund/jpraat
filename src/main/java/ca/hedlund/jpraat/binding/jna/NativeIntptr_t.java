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
