package ca.hedlund.jpraat.binding;

import ca.hedlund.jpraat.binding.fon.LongSound;
import ca.hedlund.jpraat.binding.fon.Matrix;
import ca.hedlund.jpraat.binding.fon.Pitch;
import ca.hedlund.jpraat.binding.fon.Sampled;
import ca.hedlund.jpraat.binding.fon.SampledXY;
import ca.hedlund.jpraat.binding.fon.Sound;
import ca.hedlund.jpraat.binding.fon.Spectrogram;
import ca.hedlund.jpraat.binding.fon.Vector;
import ca.hedlund.jpraat.binding.fon.kSound_to_Spectrogram_windowShape;
import ca.hedlund.jpraat.binding.fon.kSound_windowShape;
import ca.hedlund.jpraat.binding.fon.kSounds_convolveScaling;
import ca.hedlund.jpraat.binding.fon.kSounds_convolveSignalOutsideTimeDomain;
import ca.hedlund.jpraat.binding.jna.NativeLibraryOptions;
import ca.hedlund.jpraat.binding.sys.Data;
import ca.hedlund.jpraat.binding.sys.MelderFile;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

/**
 * Main JNA interface class for praat.dll.
 * 
 * @author Greg J. Hedlund <ghedlund@mun.ca>
 */
public interface Praat extends Library {
	
	Praat INSTANCE = (Praat)
			Native.loadLibrary("praat", Praat.class, new NativeLibraryOptions());
	
	/* sys/Data.h */
	public boolean Data_equal (Data data1, Data data2);
	
	public boolean Data_canWriteAsEncoding (Data me, int outputEncoding);

	public boolean Data_canWriteText (Data me);

	public MelderFile Data_createTextFile (Data me, MelderFile file, boolean verbose);

	public void Data_writeText (Data me, MelderFile openFile);

	public void Data_writeToTextFile (Data me, MelderFile file);
	
	public void Data_writeToShortTextFile (Data me, MelderFile file);

	public boolean Data_canWriteBinary (Data me);

	public void Data_writeToBinaryFile (Data me, MelderFile file);

	public boolean Data_canWriteLisp (Data me);

	public void Data_writeLispToConsole (Data me);
	
	public boolean Data_canReadText (Data me);

	public Pointer Data_readFromTextFile (MelderFile file);

	public boolean Data_canReadBinary (Data me);

	public Pointer Data_readFromBinaryFile (MelderFile file);

	public Pointer Data_readFromFile (MelderFile file);
	
	/* sys/melder.h */
	public MelderFile MelderFile_new();
	
	public MelderFile MelderFile_create(MelderFile file);
	
	public MelderFile MelderFile_open (MelderFile file);
	
	public void Melder_pathToFile (WString path, MelderFile file);
	
	public WString Melder_fileToPath (MelderFile file);
	
	public long MelderFile_length (MelderFile file);
	
	public boolean MelderFile_isNull (MelderFile file);
	
	public String Melder_getError ();
	
	public String Melder_getShellDirectory ();
	
	/* fon/LongSound.h */
	public LongSound LongSound_open (MelderFile fs);

	public Sound LongSound_extractPart (LongSound me, double tmin, double tmax, int preserveTimes);
	
	/* fon/Vector.h */
	/**
	 * Get value at given x position and channel using the specified
	 * interpolation.
	 * 
	 * @param me
	 * @param x
	 * @param channel
	 * @param interpolation
	 * @return
	 */
	public double Vector_getValueAtX (Vector me, double x, long channel, int interpolation);
	
	/**
	 * Find the minimum value and x location for the minmum.
	 * 
	 * Args <code>return_minimum</code> and <code>return_xOfMiniumum</code>
	 * should be a pointer to a block of memory 1 * sizeof(double).
	 * 
	 * @param me
	 * @param xmin
	 * @param xmax
	 * @param channel
	 * @param interpolation
	 * @param return_minimum (pointer to double)
	 * @param return_xOfMinimum (pointer to double)
	 */
	public void Vector_getMinimumAndX (Vector me, double xmin, double xmax, long channel, int interpolation,
			 Pointer return_minimum, Pointer return_xOfMinimum);
	
	/**
	 * 
	 * @param me
	 * @param xmin
	 * @param xmax
	 * @param interpolation
	 * @param return_minimum (pointer to double)
	 * @param return_xOfMinimum (pointer to double)
	 * @param return_channelOfMinimum (pointer to long)
	 */
	public void Vector_getMinimumAndXAndChannel (Vector me, double xmin, double xmax, int interpolation,
			Pointer return_minimum, Pointer return_xOfMinimum, Pointer return_channelOfMinimum);
	
	public void Vector_getMaximumAndX (Vector me, double xmin, double xmax, long channel, int interpolation,
			Pointer return_maximum, Pointer return_xOfMaximum);
	
	public void Vector_getMaximumAndXAndChannel (Vector me, double xmin, double xmax, int interpolation,
			Pointer return_maximum, Pointer return_xOfMaximum, Pointer return_channelOfMaximum);
	
	public double Vector_getMinimum (Vector me, double xmin, double xmax, int interpolation);
	
	public double Vector_getAbsoluteExtremum (Vector me, double xmin, double xmax, int interpolation);
	public double Vector_getXOfMinimum (Vector me, double xmin, double xmax, int interpolation);
	public double Vector_getXOfMaximum (Vector me, double xmin, double xmax, int interpolation);
	public long Vector_getChannelOfMinimum (Vector me, double xmin, double xmax, int interpolation);
	public long Vector_getChannelOfMaximum (Vector me, double xmin, double xmax, int interpolation);


	public double Vector_getMean (Vector me, double xmin, double xmax, long channel);
	public double Vector_getStandardDeviation (Vector me, double xmin, double xmax, long channel);

	public void Vector_addScalar (Vector me, double scalar);
	public void Vector_subtractMean (Vector me);
	public void Vector_multiplyByScalar (Vector me, double scalar);
	public void Vector_scale (Vector me, double scale);
	
	/* fon/Sampled.h */
	public double Sampled_getXMin(Sampled me);
	public double Sampled_getXMax(Sampled me);
	
	public long Sampled_getNx(Sampled me);

	public double Sampled_getDx(Sampled me);

	public double Sampled_getX1(Sampled me);
	
	public double Sampled_indexToX (Sampled me, long i);

	public double Sampled_xToIndex (Sampled me, double x);

	public long Sampled_xToLowIndex (Sampled me, double x);

	public long Sampled_xToHighIndex (Sampled me, double x);

	public long Sampled_xToNearestIndex (Sampled me, double x);

	public long Sampled_getWindowSamples (Sampled me, double xmin, double xmax, Pointer ixmin, Pointer ixmax);

	public void Sampled_shortTermAnalysis (Sampled me, double windowDuration, double timeStep,
			Pointer numberOfFrames, Pointer firstTime);
	
	public double Sampled_getValueAtSample (Sampled me, long isamp, long ilevel, int unit);
	public double Sampled_getValueAtX (Sampled me, double x, long ilevel, int unit, int interpolate);
	public long Sampled_countDefinedSamples (Sampled me, long ilevel, int unit);
	public Pointer Sampled_getSortedValues (Sampled me, long ilevel, int unit, Pointer numberOfValues);

	public double Sampled_getQuantile
		(Sampled me, double xmin, double xmax, double quantile, long ilevel, int unit);
	public double Sampled_getMean
		(Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	public double Sampled_getMean_standardUnit
		(Sampled me, double xmin, double xmax, long ilevel, int averagingUnit, int interpolate);
	public double Sampled_getIntegral
		(Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	public double Sampled_getIntegral_standardUnit
		(Sampled me, double xmin, double xmax, long ilevel, int averagingUnit, int interpolate);
	public double Sampled_getStandardDeviation
		(Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	public double Sampled_getStandardDeviation_standardUnit
		(Sampled me, double xmin, double xmax, long ilevel, int averagingUnit, int interpolate);

	public void Sampled_getMinimumAndX (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate,
		Pointer return_minimum, Pointer return_xOfMinimum);
	public double Sampled_getMinimum (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	public double Sampled_getXOfMinimum (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	public void Sampled_getMaximumAndX (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate,
		Pointer return_maximum, Pointer return_xOfMaximum);
	public double Sampled_getMaximum (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	public double Sampled_getXOfMaximum (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);

	/* fon/SampledXY.h */
	public double SampledXY_getYMin(SampledXY me);
	public double SampledXY_getYMax(SampledXY me);
	public long SampledXY_getNy(SampledXY me);
	public double SampledXY_GetDy(SampledXY me);
	public double SampledXY_getY1(SampledXY me);
	
	public double Matrix_columnToX (SampledXY me, double column);   /* Return my x1 + (column - 1) * my dx.	 */

	public double Matrix_rowToY (SampledXY me, double row);   /* Return my y1 + (row - 1) * my dy. */

	public double Matrix_xToColumn (SampledXY me, double x);   /* Return (x - xmin) / my dx + 1. */

	public long Matrix_xToLowColumn (SampledXY me, double x);   /* Return floor (Matrix_xToColumn (me, x)). */

	public long Matrix_xToHighColumn (SampledXY me, double x);   /* Return ceil (Matrix_xToColumn (me, x)). */

	public long Matrix_xToNearestColumn (SampledXY me, double x);   /* Return floor (Matrix_xToColumn (me, x) + 0.5). */

	public double Matrix_yToRow (SampledXY me, double y);   /* Return (y - ymin) / my dy + 1. */

	public long Matrix_yToLowRow (SampledXY me, double y);   /* Return floor (Matrix_yToRow (me, y)). */

	public long Matrix_yToHighRow (SampledXY me, double x);   /* Return ceil (Matrix_yToRow (me, y)). */

	public long Matrix_yToNearestRow (SampledXY me, double y);   /* Return floor (Matrix_yToRow (me, y) + 0.5). */

	public long Matrix_getWindowSamplesX (SampledXY me, double xmin, double xmax, Pointer ixmin, Pointer ixmax);
	
	public long Matrix_getWindowSamplesY (SampledXY me, double ymin, double ymax, Pointer iymin, Pointer iymax);
	
	/* fon/Matrix.h */
	public Matrix Matrix_create
		(double xmin, double xmax, long nx, double dx, double x1,
		 double ymin, double ymax, long ny, double dy, double y1);
	
	public Matrix Matrix_createSimple (long numberOfRows, long numberOfColumns);
	
	public long Matrix_getWindowSamplesX (Matrix me, double xmin, double xmax, Pointer ixmin, Pointer ixmax);
	
	public double Matrix_getValueAtXY (Matrix me, double x, double y);
	
	public double Matrix_getSum (Matrix me);
	public double Matrix_getNorm (Matrix me);
	
	public double Matrix_columnToX (Matrix me, double column);   /* Return my x1 + (column - 1) * my dx.	 */
	
	public double Matrix_rowToY (Matrix me, double row);   /* Return my y1 + (row - 1) * my dy. */
	
	public double Matrix_xToColumn (Matrix me, double x);   /* Return (x - xmin) / my dx + 1. */
	
	public long Matrix_xToLowColumn (Matrix me, double x);   /* Return floor (Matrix_xToColumn (me, x)). */
	
	public long Matrix_xToHighColumn (Matrix me, double x);   /* Return ceil (Matrix_xToColumn (me, x)). */
	
	public long Matrix_xToNearestColumn (Matrix me, double x);   /* Return floor (Matrix_xToColumn (me, x) + 0.5). */
	
	public double Matrix_yToRow (Matrix me, double y);   /* Return (y - ymin) / my dy + 1. */
	
	public long Matrix_yToLowRow (Matrix me, double y);   /* Return floor (Matrix_yToRow (me, y)). */
	
	public long Matrix_yToHighRow (Matrix me, double x);   /* Return ceil (Matrix_yToRow (me, y)). */
	
	public long Matrix_yToNearestRow (Matrix me, double y);   /* Return floor (Matrix_yToRow (me, y) + 0.5). */
	
	public long Matrix_getWindowSamplesY (Matrix me, double ymin, double ymax, Pointer iymin, Pointer iymax);
	
	public long Matrix_getWindowExtrema (Matrix me, long ixmin, long ixmax, long iymin, long iymax,
		Pointer minimum, Pointer maximum);
	
//	public void Matrix_formula (Matrix me, WString expression, Interpreter interpreter, Matrix target);
//	
//	public void Matrix_formula_part (Matrix me, double xmin, double xmax, double ymin, double ymax,
//		WString expression, Interpreter interpreter, Matrix target);
	
	public Matrix Matrix_readFromRawTextFile (MelderFile file);
	public Matrix Matrix_readAP (MelderFile file);
//	public Matrix Matrix_appendRows (Matrix me, Matrix thee, ClassInfo klas);
	
	public void Matrix_eigen (Matrix me, Pointer eigenvectors, Pointer eigenvalues);
	public Matrix Matrix_power (Matrix me, long power);
	
	void Matrix_scaleAbsoluteExtremum (Matrix me, double scale);
	
//	Matrix Table_to_Matrix (Table me);
	public void Matrix_writeToMatrixTextFile (Matrix me, MelderFile file);
	public void Matrix_writeToHeaderlessSpreadsheetFile (Matrix me, MelderFile file);
	
//	Matrix TableOfReal_to_Matrix (Matrix me);
//	TableOfReal Matrix_to_TableOfReal (Matrix me);
	
	/* fon/Pitch.h */
	public Pitch Pitch_create (double tmin, double tmax, long nt, double dt, double t1,
			double ceiling, int maxnCandidates);
	
	/* fon/Sound.h */
	/**
	 * Read sound from file
	 * 
	 * @param file
	 * @return
	 */
	public Sound Sound_readFromSoundFile (MelderFile file);
	
	public Sound Sound_create (long numberOfChannels, double xmin, double xmax, long nx, double dx, double x1);
	
	/**
	 * Create a sound object
	 * 
	 * @param numberOfChannels
	 * @param duration
	 * @param samplingFrequency
	 * @return
	 */
	public Sound Sound_createSimple (long numberOfChannels, double duration, double samplingFrequency);
	
	public double Sound_getEnergyInAir (Sound me);
	
	public Sound Sound_convertToMono (Sound me);
	public Sound Sound_convertToStereo (Sound me);
	public Sound Sound_extractChannel (Sound me, long ichannel);
	
	public Sound Sound_upsample (Sound me);   /* By a factor 2. */

	public Sound Sound_resample (Sound me, double samplingFrequency, long precision);
	
	public Sound Sound_autoCorrelate (Sound me, kSounds_convolveScaling scaling, kSounds_convolveSignalOutsideTimeDomain signalOutsideTimeDomain);
	
	public Sound Sound_extractPart (Sound me, double t1, double t2, kSound_windowShape windowShape, double relativeWidth, boolean preserveTimes);
	
	/* fon/Spectrogram.h */
	public Spectrogram Spectrogram_create (double tmin, double tmax, long nt, double dt, double t1,
					double fmin, double fmax, long nf, double df, double f1);

	public Spectrogram Matrix_to_Spectrogram (Matrix me);

	public Matrix Spectrogram_to_Matrix (Spectrogram me);
	
	public double Spectrogram_getZ(Spectrogram me, int ix, int iy);
	
	/* fon/Sound_and_Spectrogram.h */
	public Spectrogram Sound_to_Spectrogram (Sound me, double effectiveAnalysisWidth, double fmax,
			double minimumTimeStep1, double minimumFreqStep1, kSound_to_Spectrogram_windowShape windowShape,
			double maximumTimeOversampling, double maximumFreqOversampling);
	
	/* sendpraat.c */
	public String sendpraat (Object display, String programName, long timeOut,  String text);
	
	public WString sendpraatW (Object display, String programName, long timeOut, WString text);
	
}
