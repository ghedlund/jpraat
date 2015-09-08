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
		Spectrogram retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrogram_create_wrapped(
					tmin, tmax, new NativeLong(nt), dt, t1, fmin, fmax,
					new NativeLong(nf), df, f1);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}

	public static Spectrogram fromMatrix (Matrix me) throws PraatException {
		Spectrogram retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE
					.Matrix_to_Spectrogram_wrapped(me);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public Matrix Spectrogram_to_Matrix () throws PraatException {
		Matrix retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrogram_to_Matrix_wrapped(this);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	/**
	Function:
		Create a time slice from the Spectrogram at the time nearest to 'time'.
		Return NULL in case of failure (no memory).
	Postconditions:
		result -> xmin == my ymin;   // Lowest frequency; often 0.
		result -> xmax == my ymax;   // Highest frequency.
		result -> nx == my ny;   // Number of frequency bands.
		result -> dx == my dy;   // Frequency step.
		result -> x1 == my y1;   // Centre of first frequency band.
		for (iy = 1; iy <= my ny; iy ++) {  
			result -> z [1] [i] == sqrt (my z [i] ['time']);
			result -> z [2] [i] == 0.0;
		}
	 */
	public Spectrum to_Spectrum(double time) throws PraatException {
		Spectrum retVal = null;
		try {
			Praat.wrapperLock.lock();
			retVal = Praat.INSTANCE.Spectrogram_to_Spectrum_wrapped(this, time);
			Praat.checkAndClearLastError();
		} catch (PraatException e) {
			throw e;
		} finally {
			Praat.wrapperLock.unlock();
		}
		return retVal;
	}
	
	public double getZ(int ix, int iy) {
		return Praat.INSTANCE.Spectrogram_getZ(this, ix, iy);
	}

}
