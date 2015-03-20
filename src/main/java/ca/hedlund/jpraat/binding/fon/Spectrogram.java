package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.exceptions.PraatException;

public class Spectrogram extends Matrix {
	
	public Spectrogram() {
		super();
	}
	
	public Spectrogram(Pointer p) {
		super(p);
	}
	
	public static Spectrogram create (double tmin, double tmax, long nt, double dt, double t1,
			double fmin, double fmax, long nf, double df, double f1) throws PraatException {
		Spectrogram retVal = Praat.INSTANCE.Spectrogram_create_wrapped (tmin, tmax, new NativeLong(nt), dt, t1, fmin, fmax, 
				new NativeLong(nf), df, f1);
		Praat.checkAndClearLastError();
		return retVal;
	}

	public static Spectrogram fromMatrix (Matrix me) throws PraatException {
		Spectrogram retVal = Praat.INSTANCE.Matrix_to_Spectrogram_wrapped (me);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public Matrix Spectrogram_to_Matrix () throws PraatException {
		Matrix retVal = Praat.INSTANCE.Spectrogram_to_Matrix_wrapped (this);
		Praat.checkAndClearLastError();
		return retVal;
	}
	
	public double getZ(int ix, int iy) {
		return Praat.INSTANCE.Spectrogram_getZ(this, ix, iy);
	}

}
