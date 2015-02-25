package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.binding.Praat;

public class Spectrogram extends Matrix {
	
	public static Spectrogram create (double tmin, double tmax, long nt, double dt, double t1,
			double fmin, double fmax, long nf, double df, double f1) {
		return Praat.INSTANCE.Spectrogram_create(tmin, tmax, nt, dt, t1, fmin, fmax, nf, df, f1);
	}

	public static Spectrogram fromMatrix (Matrix me) {
		return Praat.INSTANCE.Matrix_to_Spectrogram(me);
	}
	
	public Matrix Spectrogram_to_Matrix () {
		return Praat.INSTANCE.Spectrogram_to_Matrix(this);
	}
	
	public double getZ(int ix, int iy) {
		return Praat.INSTANCE.Spectrogram_getZ(this, ix, iy);
	}

}
