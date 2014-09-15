package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;

public class Intensity extends Vector {
	
	public static final int UNITS_ENERGY = 1;
	public static final int UNITS_SONES = 2;
	public static final int UNITS_DB = 3;

	public static final int AVERAGING_MEDIAN = 0;
	public static final int AVERAGING_ENERGY = 1;
	public static final int AVERAGING_SONES = 2;
	public static final int AVERAGING_DB = 3;
	
	public static Intensity create (double tmin, double tmax, long nt, double dt, double t1) {
		return Praat.INSTANCE.Intensity_create(tmin, tmax, nt, dt, t1);
	}
	
	public static Matrix Intensity_to_Matrix (Intensity me) {
		return Praat.INSTANCE.Intensity_to_Matrix(me);
	}
	
	public static Intensity Matrix_to_Intensity (Matrix me) {
		return Praat.INSTANCE.Matrix_to_Intensity(me);
	}
	
	public Matrix to_Matrix() {
		return Intensity_to_Matrix(this);
	}

	public double getQuantile (double tmin, double tmax, double quantile) {
		return Praat.INSTANCE.Intensity_getQuantile(this, tmin, tmax, quantile);
	}
	
	public double getAverage (double tmin, double tmax, int averagingMethod) {
		return Praat.INSTANCE.Intensity_getAverage(this, tmin, tmax, averagingMethod);
	}
	
}
