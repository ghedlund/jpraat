package ca.hedlund.jpraat.binding;

import ca.hedlund.jpraat.binding.fon.Formant;
import ca.hedlund.jpraat.binding.fon.Function;
import ca.hedlund.jpraat.binding.fon.Intensity;
import ca.hedlund.jpraat.binding.fon.LongSound;
import ca.hedlund.jpraat.binding.fon.Matrix;
import ca.hedlund.jpraat.binding.fon.Pitch;
import ca.hedlund.jpraat.binding.fon.Sampled;
import ca.hedlund.jpraat.binding.fon.SampledXY;
import ca.hedlund.jpraat.binding.fon.Sound;
import ca.hedlund.jpraat.binding.fon.Spectrogram;
import ca.hedlund.jpraat.binding.fon.Vector;
import ca.hedlund.jpraat.binding.fon.kPitch_unit;
import ca.hedlund.jpraat.binding.fon.kSound_to_Spectrogram_windowShape;
import ca.hedlund.jpraat.binding.fon.kSound_windowShape;
import ca.hedlund.jpraat.binding.fon.kSounds_convolveScaling;
import ca.hedlund.jpraat.binding.fon.kSounds_convolveSignalOutsideTimeDomain;
import ca.hedlund.jpraat.binding.jna.Custom;
import ca.hedlund.jpraat.binding.jna.Declared;
import ca.hedlund.jpraat.binding.jna.NativeLibraryOptions;
import ca.hedlund.jpraat.binding.stat.Table;
import ca.hedlund.jpraat.binding.sys.Data;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.MelderQuantity;
import ca.hedlund.jpraat.binding.sys.PraatVersion;
import ca.hedlund.jpraat.binding.sys.Thing;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

/**
 * Main JNA interface class for praat library.
 * 
 */
public interface Praat extends Library {
	
	/**
	 * Static instance of Praat native library
	 */
	Praat INSTANCE = (Praat)
			Native.loadLibrary("praat", Praat.class, new NativeLibraryOptions());
	
	@Declared("sys/praat_version.h")
	@Custom
	public PraatVersion praat_version();
	
	@Declared("sys/praatlib.h")
	@Custom
	public void praat_lib_init();
	
	@Declared("sys/praat.h")
	@Custom
	public WString praat_dir();
	
	@Declared("sys/Thing.h")
	WString Thing_className (Thing me);
	
	@Declared("sys/Thing.h")
	public void _Thing_forget(Thing me);
	
	@Declared("sys/Thing.h")
	public void Thing_info (Thing me);
	
	@Declared("sys/Thing.h")
	public void Thing_infoWithId (Thing me, long id);
	
	@Declared("sys/Thing.h")
	public Object Thing_newFromClassName (WString className);
	
	/* Return a pointer to your internal name (which can be NULL). */
	@Declared("sys/Thing.h")
	public WString Thing_getName (Thing me);

	/*
		Function:
			remember that you are called 'name'.
		Postconditions:
			my name *and* my name are copies of 'name'.
	 */
	@Declared("sys/Thing.h")
	public void Thing_setName (Thing me, WString name);

	@Declared("sys/Thing.h")
	public void Thing_swap (Thing me, Thing thee);
	
	@Declared("sys/Data.h")
	public boolean Data_equal (Data data1, Data data2);
	
	@Declared("sys/Data.h")
	public boolean Data_canWriteAsEncoding (Data me, int outputEncoding);

	@Declared("sys/Data.h")
	public boolean Data_canWriteText (Data me);

	@Declared("sys/Data.h")
	public MelderFile Data_createTextFile (Data me, MelderFile file, boolean verbose);

	@Declared("sys/Data.h")
	public void Data_writeText (Data me, MelderFile openFile);

	@Declared("sys/Data.h")
	public void Data_writeToTextFile (Data me, MelderFile file);
	
	@Declared("sys/Data.h")
	public void Data_writeToShortTextFile (Data me, MelderFile file);

	@Declared("sys/Data.h")
	public boolean Data_canWriteBinary (Data me);

	@Declared("sys/Data.h")
	public void Data_writeToBinaryFile (Data me, MelderFile file);

	@Declared("sys/Data.h")
	public boolean Data_canWriteLisp (Data me);

	@Declared("sys/Data.h")
	public void Data_writeLispToConsole (Data me);
	
	@Declared("sys/Data.h")
	public boolean Data_canReadText (Data me);

	@Declared("sys/Data.h")
	public Pointer Data_readFromTextFile (MelderFile file);

	@Declared("sys/Data.h")
	public boolean Data_canReadBinary (Data me);

	@Declared("sys/Data.h")
	public Pointer Data_readFromBinaryFile (MelderFile file);

	@Declared("sys/Data.h")
	public Pointer Data_readFromFile (MelderFile file);
	
	@Declared("sys/melder.h")
	@Custom
	public MelderFile MelderFile_new();
	
	@Declared("sys/melder.h")
	public MelderFile MelderFile_create(MelderFile file);
	
	@Declared("sys/melder.h")
	public MelderFile MelderFile_open (MelderFile file);
	
	@Declared("sys/melder.h")
	public void Melder_pathToFile (WString path, MelderFile file);
	
	@Declared("sys/melder.h")
	public WString Melder_fileToPath (MelderFile file);
	
	@Declared("sys/melder.h")
	public long MelderFile_length (MelderFile file);
	
	@Declared("sys/melder.h")
	public boolean MelderFile_isNull (MelderFile file);
	
	@Declared("sys/melder.h")
	public String Melder_getError ();
	
	@Declared("sys/melder.h")
	public String Melder_getShellDirectory ();
	
	@Declared("fon/Function.h")
	public void Function_init (Function me, double xmin, double xmax);
	
	@Declared("fon/Function.h")
	public MelderQuantity Function_getDomainQuantity ();

	@Declared("fon/Function.h")
	public int Function_getMinimumUnit (long ilevel);
	
	@Declared("fon/Function.h")
	public int Function_getMaximumUnit (long ilevel);
	
	@Declared("fun/Function.h")
	public WString Function_getUnitText(Function me, long ilevel, int unit, long flags);
	
	@Declared("fon/Function.h")
	public boolean Function_isUnitLogarithmic (Function me, long ilevel, int unit);

	@Declared("fon/Function.h")
	public double Function_convertStandardToSpecialUnit (Function me, double value, long ilevel, int unit);
	
	@Declared("fon/Function.h")
	public double Function_convertSpecialToStandardUnit (Function me, double value, long ilevel, int unit);
	
	@Declared("fon/Function.h")
	public double Function_convertToNonlogarithmic (Function me, double value, long ilevel, int unit);
	
	@Declared("fon/Function.h")
	public double Function_window (double tim, int windowType);
	
	@Declared("fon/Function.h")
	public void Function_unidirectionalAutowindow (Function me, Pointer xmin, Pointer xmax);
	
	@Declared("fon/Function.h")
	public void Function_bidirectionalAutowindow (Function me,Pointer x1, Pointer x2);
	
	@Declared("fon/Function.h")
	public boolean Function_intersectRangeWithDomain (Function me, Pointer x1, Pointer x2);

	@Declared("fon/Function.h")
	public void Function_shiftXBy (Function me, double shift);
	
	@Declared("fon/Function.h")
	public void Function_shiftXTo (Function me, double xfrom, double xto);
	
	@Declared("fon/Function.h")
	public void Function_scaleXBy (Function me, double factor);
	
	@Declared("fon/Function.h")
	public void Function_scaleXTo (Function me, double xminto, double xmaxto);
	
	@Declared("fon/LongSound.h")
	public LongSound LongSound_open (MelderFile fs);

	@Declared("fon/LongSound.h")
	public Sound LongSound_extractPart (LongSound me, double tmin, double tmax, int preserveTimes);
	
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
	@Declared("fon/Vector.h")
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
	@Declared("fon/Vector.h")
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
	@Declared("fon/Vector.h")
	public void Vector_getMinimumAndXAndChannel (Vector me, double xmin, double xmax, int interpolation,
			Pointer return_minimum, Pointer return_xOfMinimum, Pointer return_channelOfMinimum);
	
	@Declared("fon/Vector.h")
	public void Vector_getMaximumAndX (Vector me, double xmin, double xmax, long channel, int interpolation,
			Pointer return_maximum, Pointer return_xOfMaximum);
	
	@Declared("fon/Vector.h")
	public void Vector_getMaximumAndXAndChannel (Vector me, double xmin, double xmax, int interpolation,
			Pointer return_maximum, Pointer return_xOfMaximum, Pointer return_channelOfMaximum);
	
	@Declared("fon/Vector.h")
	public double Vector_getMinimum (Vector me, double xmin, double xmax, int interpolation);
	
	@Declared("fon/Vector.h")
	public double Vector_getAbsoluteExtremum (Vector me, double xmin, double xmax, int interpolation);
	
	@Declared("fon/Vector.h")
	public double Vector_getXOfMinimum (Vector me, double xmin, double xmax, int interpolation);
	
	@Declared("fon/Vector.h")
	public double Vector_getXOfMaximum (Vector me, double xmin, double xmax, int interpolation);
	
	@Declared("fon/Vector.h")
	public long Vector_getChannelOfMinimum (Vector me, double xmin, double xmax, int interpolation);
	
	@Declared("fon/Vector.h")
	public long Vector_getChannelOfMaximum (Vector me, double xmin, double xmax, int interpolation);

	@Declared("fon/Vector.h")
	public double Vector_getMean (Vector me, double xmin, double xmax, long channel);
	
	@Declared("fon/Vector.h")
	public double Vector_getStandardDeviation (Vector me, double xmin, double xmax, long channel);

	@Declared("fon/Vector.h")
	public void Vector_addScalar (Vector me, double scalar);
	
	@Declared("fon/Vector.h")
	public void Vector_subtractMean (Vector me);
	
	@Declared("fon/Vector.h")
	public void Vector_multiplyByScalar (Vector me, double scalar);
	
	@Declared("fon/Vector.h")
	public void Vector_scale (Vector me, double scale);
	
	@Declared("fon/Intensity.h")
	public Intensity Intensity_create (double tmin, double tmax, long nt, double dt, double t1);
	
	@Declared("fon/Intensity.h")
	public Matrix Intensity_to_Matrix (Intensity me);
	
	@Declared("fon/Intensity.h")
	public Intensity Matrix_to_Intensity (Matrix me);

	@Declared("fon/Intensity.h")
	public double Intensity_getQuantile (Intensity me, double tmin, double tmax, double quantile);
	
	@Declared("fon/Intensity.h")
	public double Intensity_getAverage (Intensity me, double tmin, double tmax, int averagingMethod);
	
	/*
		Function:
			smooth away the periodic part of a signal,
			by convolving the square of the signal with a Kaiser(20.24) window;
			and resample on original sample points.
		Arguments:
			'minimumPitch':
				the minimum periodicity frequency that will be smoothed away
				to at most 0.00001 %.
				The Hanning/Hamming-equivalent window length will be 3.2 / 'minimumPitch'.
				The actual window length will be twice that.
			'timeStep':
				if <= 0.0, then 0.8 / minimumPitch.
		Performance:
			every periodicity frequency greater than 'minimumPitch'
			will be smoothed away to at most 0.00001 %;
			if 'timeStep' is 0 or less than 3.2 / 'minimumPitch',
			aliased frequencies will be at least 140 dB down.
		Example:
			minimumPitch = 100 Hz;
			Hanning/Hanning-equivalent window duration = 32 ms;
			actual window duration = 64 ms;
	 */
	@Declared("fon/Sound_to_Intensity.h")
	public Intensity Sound_to_Intensity (Sound me, double minimumPitch, double timeStep, int subtractMean);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getXMin(Sampled me);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getXMax(Sampled me);
	
	@Declared("fon/Sampled.h")
	public long Sampled_getNx(Sampled me);

	@Declared("fon/Sampled.h")
	public double Sampled_getDx(Sampled me);

	@Declared("fon/Sampled.h")
	public double Sampled_getX1(Sampled me);
	
	@Declared("fon/Sampled.h")
	public double Sampled_indexToX (Sampled me, long i);

	@Declared("fon/Sampled.h")
	public double Sampled_xToIndex (Sampled me, double x);

	@Declared("fon/Sampled.h")
	public long Sampled_xToLowIndex (Sampled me, double x);

	@Declared("fon/Sampled.h")
	public long Sampled_xToHighIndex (Sampled me, double x);

	@Declared("fon/Sampled.h")
	public long Sampled_xToNearestIndex (Sampled me, double x);

	@Declared("fon/Sampled.h")
	public long Sampled_getWindowSamples (Sampled me, double xmin, double xmax, Pointer ixmin, Pointer ixmax);

	@Declared("fon/Sampled.h")
	public void Sampled_shortTermAnalysis (Sampled me, double windowDuration, double timeStep,
			Pointer numberOfFrames, Pointer firstTime);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getValueAtSample (Sampled me, long isamp, long ilevel, int unit);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getValueAtX (Sampled me, double x, long ilevel, int unit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public long Sampled_countDefinedSamples (Sampled me, long ilevel, int unit);
	
	@Declared("fon/Sampled.h")
	public Pointer Sampled_getSortedValues (Sampled me, long ilevel, int unit, Pointer numberOfValues);

	@Declared("fon/Sampled.h")
	public double Sampled_getQuantile
		(Sampled me, double xmin, double xmax, double quantile, long ilevel, int unit);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getMean
		(Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getMean_standardUnit
		(Sampled me, double xmin, double xmax, long ilevel, int averagingUnit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getIntegral
		(Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getIntegral_standardUnit
		(Sampled me, double xmin, double xmax, long ilevel, int averagingUnit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getStandardDeviation
		(Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getStandardDeviation_standardUnit
		(Sampled me, double xmin, double xmax, long ilevel, int averagingUnit, int interpolate);

	@Declared("fon/Sampled.h")
	public void Sampled_getMinimumAndX (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate,
		Pointer return_minimum, Pointer return_xOfMinimum);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getMinimum (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getXOfMinimum (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public void Sampled_getMaximumAndX (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate,
		Pointer return_maximum, Pointer return_xOfMaximum);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getMaximum (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getXOfMaximum (Sampled me, double xmin, double xmax, long ilevel, int unit, int interpolate);

	@Declared("fon/SampledXY.h")
	public double SampledXY_getYMin(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_getYMax(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public long SampledXY_getNy(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_GetDy(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_getY1(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_indexToY (SampledXY me, long   index);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_yToIndex (SampledXY me, double y);
	
	@Declared("fon/SampledXY.h")
	public long SampledXY_yToLowIndex     (SampledXY me, double y);
	
	@Declared("fon/SampledXY.h")
	public long SampledXY_yToHighIndex    (SampledXY me, double y);
	
	@Declared("fon/SampledXY.h")
	public long SampledXY_yToNearestIndex (SampledXY me, double y);
	
	@Declared("fon/Matrix.h")
	public Matrix Matrix_create
		(double xmin, double xmax, long nx, double dx, double x1,
		 double ymin, double ymax, long ny, double dy, double y1);
	
	@Declared("fon/Matrix.h")
	public Matrix Matrix_createSimple (long numberOfRows, long numberOfColumns);
	
	@Declared("fon/Matrix.h")
	public long Matrix_getWindowSamplesX (Matrix me, double xmin, double xmax, Pointer ixmin, Pointer ixmax);
	
	@Declared("fon/Matrix.h")
	public double Matrix_getValueAtXY (Matrix me, double x, double y);
	
	@Declared("fon/Matrix.h")
	public double Matrix_getSum (Matrix me);
	
	@Declared("fon/Matrix.h")
	public double Matrix_getNorm (Matrix me);
	
	@Declared("fon/Matrix.h")
	public double Matrix_columnToX (Matrix me, double column);   /* Return my x1 + (column - 1) * my dx.	 */
	
	@Declared("fon/Matrix.h")
	public double Matrix_rowToY (Matrix me, double row);   /* Return my y1 + (row - 1) * my dy. */
	
	@Declared("fon/Matrix.h")
	public double Matrix_xToColumn (Matrix me, double x);   /* Return (x - xmin) / my dx + 1. */
	
	@Declared("fon/Matrix.h")
	public long Matrix_xToLowColumn (Matrix me, double x);   /* Return floor (Matrix_xToColumn (me, x)). */
	
	@Declared("fon/Matrix.h")
	public long Matrix_xToHighColumn (Matrix me, double x);   /* Return ceil (Matrix_xToColumn (me, x)). */
	
	@Declared("fon/Matrix.h")
	public long Matrix_xToNearestColumn (Matrix me, double x);   /* Return floor (Matrix_xToColumn (me, x) + 0.5). */
	
	@Declared("fon/Matrix.h")
	public double Matrix_yToRow (Matrix me, double y);   /* Return (y - ymin) / my dy + 1. */
	
	@Declared("fon/Matrix.h")
	public long Matrix_yToLowRow (Matrix me, double y);   /* Return floor (Matrix_yToRow (me, y)). */
	
	@Declared("fon/Matrix.h")
	public long Matrix_yToHighRow (Matrix me, double x);   /* Return ceil (Matrix_yToRow (me, y)). */
	
	@Declared("fon/Matrix.h")
	public long Matrix_yToNearestRow (Matrix me, double y);   /* Return floor (Matrix_yToRow (me, y) + 0.5). */
	
	@Declared("fon/Matrix.h")
	public long Matrix_getWindowSamplesY (Matrix me, double ymin, double ymax, Pointer iymin, Pointer iymax);
	
	@Declared("fon/Matrix.h")
	public long Matrix_getWindowExtrema (Matrix me, long ixmin, long ixmax, long iymin, long iymax,
		Pointer minimum, Pointer maximum);
	
	@Declared("fon/Matrix.h")
	public Matrix Matrix_readFromRawTextFile (MelderFile file);
	
	@Declared("fon/Matrix.h")
	public Matrix Matrix_readAP (MelderFile file);
	
	@Declared("fon/Matrix.h")
	public void Matrix_eigen (Matrix me, Matrix eigenvectors, Matrix eigenvalues);
	
	@Declared("fon/Matrix.h")
	public Matrix Matrix_power (Matrix me, long power);
	
	@Declared("fon/Matrix.h")
	void Matrix_scaleAbsoluteExtremum (Matrix me, double scale);
	
	@Declared("fon/Matrix.h")
	public void Matrix_writeToMatrixTextFile (Matrix me, MelderFile file);
	
	@Declared("fon/Matrix.h")
	public void Matrix_writeToHeaderlessSpreadsheetFile (Matrix me, MelderFile file);
	
	/**
	 * See {@link Pitch#create(double, double, long, double, double, double, int)}
	 */
	@Declared("fon/Pitch.h")
	public Pitch Pitch_create (double tmin, double tmax, long nt, double dt, double t1,
			double ceiling, int maxnCandidates);
	
	/**
	 * See {@link Pitch#isVoiced_i(long)}
	 */
	@Declared("fon/Pitch.h")
	public boolean Pitch_isVoiced_i (Pitch me, long index);
	
	/**
	 * See {@link Pitch#isVoiced_t(double)}
	 */
	@Declared("fon/Pitch.h")
	public boolean Pitch_isVoiced_t (Pitch me, double t);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getValueAtTime (Pitch me, double time, int unit, int interpolate);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getStrengthAtTime (Pitch me, double time, int unit, int interpolate);

	@Declared("fon/Pitch.h")
	public long Pitch_countVoicedFrames (Pitch me);

	@Declared("fon/Pitch.h")
	public double Pitch_getMean (Pitch me, double tmin, double tmax, int unit);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getMeanStrength (Pitch me, double tmin, double tmax, int unit);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getQuantile (Pitch me, double tmin, double tmax, double quantile, int unit);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getStandardDeviation (Pitch me, double tmin, double tmax, int unit);
	
	@Declared("fon/Pitch.h")
	public void Pitch_getMaximumAndTime (Pitch me, double tmin, double tmax, int unit, int interpolate,
		Pointer return_maximum, Pointer return_timeOfMaximum);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getMaximum (Pitch me, double tmin, double tmax, int unit, int interpolate);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getTimeOfMaximum (Pitch me, double tmin, double tmax, int unit, int interpolate);
	
	@Declared("fon/Pitch.h")
	public void Pitch_getMinimumAndTime (Pitch me, double tmin, double tmax, int unit, int interpolate,
		Pointer return_minimum, Pointer return_timeOfMinimum);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getMinimum (Pitch me, double tmin, double tmax, int unit, int interpolate);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getTimeOfMinimum (Pitch me, double tmin, double tmax, int unit, int interpolate);
	
	@Declared("fon/Pitch.h")
	public int Pitch_getMaxnCandidates (Pitch me);

	@Declared("fon/Pitch.h")
	public void Pitch_setCeiling (Pitch me, double ceiling);
	
	@Declared("fon/Pitch.h")
	public void Pitch_pathFinder (Pitch me, double silenceThreshold, double voicingThreshold,
			double octaveCost, double octaveJumpCost, double voicedUnvoicedCost,
			double ceiling, int pullFormants);
	
	@Declared("fon/Pitch.h")
	public void Pitch_difference (Pitch me, Pitch thee);

	@Declared("fon/Pitch.h")
	public long Pitch_getMeanAbsSlope_hertz (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public long Pitch_getMeanAbsSlope_mel (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public long Pitch_getMeanAbsSlope_semitones (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public long Pitch_getMeanAbsSlope_erb (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public long Pitch_getMeanAbsSlope_noOctave (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public Pitch Pitch_killOctaveJumps (Pitch me);

	@Declared("fon/Pitch.h")
	public Pitch Pitch_interpolate (Pitch me);

	@Declared("fon/Pitch.h")
	public Pitch Pitch_subtractLinearFit (Pitch me, int unit);

	@Declared("fon/Pitch.h")
	public Pitch Pitch_smooth (Pitch me, double bandWidth);

	@Declared("fon/Pitch.h")
	public void Pitch_step (Pitch me, double step, double precision, double tmin, double tmax);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public MelderQuantity Pitch_domainQuantity (Pitch me);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public int Pitch_getMinimumUnit (Pitch me, long ilevel);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public int Pitch_getMaximumUnit (Pitch me, long ilevel);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public WString Pitch_getUnitText (Pitch me, long ilevel, kPitch_unit unit, long flags);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public boolean Pitch_isUnitLogarithmic (Pitch me, long ilevel, int unit);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public double Pitch_convertStandardToSpecialUnit (Pitch me, double value, long ilevel, int unit);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public double Pitch_convertSpecialToStandardUnit (Pitch me, double value, long ilevel, int unit);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public double Pitch_getValueAtSample (Pitch me, long isamp, long ilevel, int unit);
	
	/**
	 * Read sound from file
	 * 
	 * @param file
	 * @return
	 */
	@Declared("fon/Sound.h")
	public Sound Sound_readFromSoundFile (MelderFile file);
	
	@Declared("fon/Sound.h")
	public Sound Sound_create (long numberOfChannels, double xmin, double xmax, long nx, double dx, double x1);
	
	/**
	 * Create a sound object
	 * 
	 * @param numberOfChannels
	 * @param duration
	 * @param samplingFrequency
	 * @return
	 */
	@Declared("fon/Sound.h")
	public Sound Sound_createSimple (long numberOfChannels, double duration, double samplingFrequency);
	
	@Declared("fon/Sound.h")
	public double Sound_getEnergyInAir (Sound me);
	
	@Declared("fon/Sound.h")
	public Sound Sound_convertToMono (Sound me);
	
	@Declared("fon/Sound.h")
	public Sound Sound_convertToStereo (Sound me);
	
	@Declared("fon/Sound.h")
	public Sound Sound_extractChannel (Sound me, long ichannel);
	
	@Declared("fon/Sound.h")
	public Sound Sound_upsample (Sound me);   /* By a factor 2. */

	@Declared("fon/Sound.h")
	public Sound Sound_resample (Sound me, double samplingFrequency, long precision);
	
	@Declared("fon/Sound.h")
	public Sound Sound_autoCorrelate (Sound me, kSounds_convolveScaling scaling, kSounds_convolveSignalOutsideTimeDomain signalOutsideTimeDomain);
	
	@Declared("fon/Sound.h")
	public Sound Sound_extractPart (Sound me, double t1, double t2, kSound_windowShape windowShape, double relativeWidth, boolean preserveTimes);
	
	@Declared("fon/Spectrogram.h")
	public Spectrogram Spectrogram_create (double tmin, double tmax, long nt, double dt, double t1,
					double fmin, double fmax, long nf, double df, double f1);

	@Declared("fon/Spectrogram.h")
	public Spectrogram Matrix_to_Spectrogram (Matrix me);

	@Declared("fon/Spectrogram.h")
	public Matrix Spectrogram_to_Matrix (Spectrogram me);
	
	@Declared("fon/Spectrogram.h")
	@Custom
	public double Spectrogram_getZ(Spectrogram me, int ix, int iy);
	
	@Declared("fon/Sound_to_Spectrogram.h")
	public Spectrogram Sound_to_Spectrogram (Sound me, double effectiveAnalysisWidth, double fmax,
			double minimumTimeStep1, double minimumFreqStep1, kSound_to_Spectrogram_windowShape windowShape,
			double maximumTimeOversampling, double maximumFreqOversampling);
	
	@Declared("fon/Sound_to_Pitch.h")
	public Pitch Sound_to_Pitch (Sound me, double timeStep,
			double minimumPitch, double maximumPitch);

	@Declared("fon/Sound_to_Pitch.h")
	public Pitch Sound_to_Pitch_ac (Sound me, double timeStep, double minimumPitch,
		double periodsPerWindow, int maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch);

	@Declared("fon/Sound_to_Pitch.h")
	public Pitch Sound_to_Pitch_cc (Sound me, double timeStep, double minimumPitch,
		double periodsPerWindow, int maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch);
	
	@Declared("fon/Sound_to_Pitch.h")
	public Pitch Sound_to_Pitch_any (Sound me,

		double dt,                 /* time step (seconds); 0.0 = automatic = periodsPerWindow / minimumPitch / 4 */
		double minimumPitch,       /* (Hz) */
		double periodsPerWindow,   /* ac3 for pitch analysis, 6 or 4.5 for HNR, 1 for FCC */
		int maxnCandidates,        /* maximum number of candidates per frame */
		int method,                /* 0 or 1 = AC, 2 or 3 = FCC, 0 or 2 = fast, 1 or 3 = accurate */

		double silenceThreshold,   /* relative to purely periodic; default 0.03 */
		double voicingThreshold,   /* relative to purely periodic; default 0.45 */
		double octaveCost,         /* favours higher pitches; default 0.01 */
		double octaveJumpCost,     /* default 0.35 */
		double voicedUnvoicedCost, /* default 0.14 */
		double maximumPitch);      /* (Hz) */
	
	@Declared("fon/Formant.h")
	public Formant Formant_create (double tmin, double tmax, long nt, double dt, double t1, int maxnFormants);

	@Declared("fon/Formant.h")
	public long Formant_getMinNumFormants (Formant me);
	
	@Declared("fon/Formant.h")
	public long Formant_getMaxNumFormants (Formant me);

	@Declared("fon/Formant.h")
	public double Formant_getValueAtTime (Formant me, int iformant, double time, int bark);
	
	@Declared("fon/Formant.h")
	public double Formant_getBandwidthAtTime (Formant me, int iformant, double time, int bark);

	@Declared("fon/Formant.h")
	public void Formant_getExtrema (Formant me, int iformant, double tmin, double tmax, Pointer fmin, Pointer fmax);
	
	@Declared("fon/Formant.h")
	public void Formant_getMinimumAndTime (Formant me, int iformant, double tmin, double tmax, int bark, int interpolate,
		Pointer return_minimum, Pointer return_timeOfMinimum);
	
	@Declared("fon/Formant.h")
	public void Formant_getMaximumAndTime (Formant me, int iformant, double tmin, double tmax, int bark, int interpolate,
		Pointer return_maximum, Pointer return_timeOfMaximum);
	
	@Declared("fon/Formant.h")
	public double Formant_getMinimum (Formant me, int iformant, double tmin, double tmax, int bark, int interpolate);
	
	@Declared("fon/Formant.h")
	public double Formant_getMaximum (Formant me, int iformant, double tmin, double tmax, int bark, int interpolate);
	
	@Declared("fon/Formant.h")
	public double Formant_getTimeOfMaximum (Formant me, int iformant, double tmin, double tmax, int bark, int interpolate);
	
	@Declared("fon/Formant.h")
	public double Formant_getTimeOfMinimum (Formant me, int iformant, double tmin, double tmax, int bark, int interpolate);

	@Declared("fon/Formant.h")
	public double Formant_getQuantile (Formant me, int iformant, double quantile, double tmin, double tmax, int bark);
	
	@Declared("fon/Formant.h")
	public double Formant_getQuantileOfBandwidth (Formant me, int iformant, double quantile, double tmin, double tmax, int bark);
	
	@Declared("fon/Formant.h")
	public double Formant_getMean (Formant me, int iformant, double tmin, double tmax, int bark);
	
	@Declared("fon/Formant.h")
	public double Formant_getStandardDeviation (Formant me, int iformant, double tmin, double tmax, int bark);

	@Declared("fon/Formant.h")
	public void Formant_sort (Formant me);

	@Declared("fon/Formant.h")
	public Matrix Formant_to_Matrix (Formant me, int iformant);
	
	@Declared("fon/Formant.h")
	public Matrix Formant_to_Matrix_bandwidths (Formant me, int iformant);

	@Declared("fon/Formant.h")
	public Formant Formant_tracker (Formant me, int numberOfTracks,
		double refF1, double refF2, double refF3, double refF4, double refF5,
		double dfCost,   /* Per kHz. */
		double bfCost, double octaveJumpCost);

	@Declared("fon/Formant.h")
	public Table Formant_downto_Table (Formant me, int includeFrameNumbers,
			int includeTimes, int timeDecimals,
			int includeIntensity, int intensityDecimals,
			int includeNumberOfFormants, int frequencyDecimals,
			int includeBandwidths);
	
	@Declared("fon/Formant.h")
	@Custom
	public double Formant_getValueAtSample(Formant me, long iframe, long which, int units);
	
	@Declared("fon/Formant.h")
	@Custom
	public double Formant_getIntensityAtSample(Formant me, long iframe);
	
	@Declared("fon/Sound_to_Formant.h")
	public Formant Sound_to_Formant_any (Sound me, double timeStep, int numberOfPoles, double maximumFrequency,
		double halfdt_window, int which, double preemphasisFrequency, double safetyMargin);

	@Declared("fon/Sound_to_Formant.h")
	public Formant Sound_to_Formant_burg (Sound me, double timeStep, double maximumNumberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency);

	@Declared("fon/Sound_to_Formant.h")
	public Formant Sound_to_Formant_keepAll (Sound me, double timeStep, double maximumNumberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency);

	@Declared("fon/Sound_to_Formant.h")
	public Formant Sound_to_Formant_willems (Sound me, double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency);
	
	@Declared("sys/sendpraat.c")
	public String sendpraat (Object display, String programName, long timeOut,  String text);
	
	@Declared("sys/sendpraat.c")
	public WString sendpraatW (Object display, String programName, long timeOut, WString text);
	
	@Declared("stat/Table.h")
	public Table Table_createWithColumnNames (long numberOfRows, WString columnNames);

	@Declared("stat/Table.h")
	public Table Table_createWithoutColumnNames (long numberOfRows, long numberOfColumns);
	
	@Declared("stat/Table.h")
	public void Table_appendRow (Table me);
	
	@Declared("stat/Table.h")
	public void Table_appendColumn (Table me, WString label);
	
	@Declared("stat/Table.h")
	public void Table_appendSumColumn (Table me, long column1, long column2, WString label);
	
	@Declared("stat/Table.h")
	public void Table_appendDifferenceColumn (Table me, long column1, long column2, WString label);
	
	@Declared("stat/Table.h")
	public void Table_appendProductColumn (Table me, long column1, long column2, WString label);
	
	@Declared("stat/Table.h")
	public void Table_appendQuotientColumn (Table me, long column1, long column2, WString label);
	
	@Declared("stat/Table.h")
	public void Table_removeRow (Table me, long row);
	
	@Declared("stat/Table.h")
	public void Table_removeColumn (Table me, long column);
	
	@Declared("stat/Table.h")
	public void Table_insertRow (Table me, long row);
	
	@Declared("stat/Table.h")
	public void Table_insertColumn (Table me, long column, WString label);
	
	@Declared("stat/Table.h")
	public void Table_setColumnLabel (Table me, long column, WString label);
	
	@Declared("stat/Table.h")
	public long Table_findColumnIndexFromColumnLabel (Table me, WString label);
	
	@Declared("stat/Table.h")
	public long Table_getColumnIndexFromColumnLabel (Table me, WString columnLabel);
	
	@Declared("stat/Table.h")
	public Pointer Table_getColumnIndicesFromColumnLabelString (Table me, WString string, Pointer numberOfTokens);
	
	@Declared("stat/Table.h")
	public long Table_searchColumn (Table me, long column, WString value);
	
	/*
	 * Procedure for reading strings or numbers from table cells:
	 * use the following two calls exclusively.
	 */
	public WString  Table_getStringValue_Assert (Table me, long row, long column);
	public double Table_getNumericValue_Assert (Table me, long row, long column);

	/*
	 * Procedure for writing strings or numbers into table cells:
	 * use the following two calls exclusively.
	 */
	public void Table_setStringValue (Table me, long row, long column, WString value);
	public void Table_setNumericValue (Table me, long row, long column, double value);

	/* For optimizations only (e.g. conversion to Matrix or TableOfReal). */
	public void Table_numericize_Assert (Table me, long columnNumber);
	
	public double Table_getQuantile (Table me, long column, double quantile);
	
	@Declared("stat/Table.h")
	public double Table_getMean (Table me, long column);
	
	public double Table_getMaximum (Table me, long icol);
	public double Table_getMinimum (Table me, long icol);
	public double Table_getGroupMean (Table me, long column, long groupColumn, WString group);
	public double Table_getStdev (Table me, long column);
	public long Table_drawRowFromDistribution (Table me, long column);
	public double Table_getCorrelation_pearsonR (Table me, long column1, long column2, double significanceLevel,
		Pointer out_significance, Pointer out_lowerLimit, Pointer out_upperLimit);
	public double Table_getCorrelation_kendallTau (Table me, long column1, long column2, double significanceLevel,
		Pointer out_significance, Pointer out_lowerLimit, Pointer out_upperLimit);
	public double Table_getMean_studentT (Table me, long column, double significanceLevel,
		Pointer out_tFromZero, Pointer out_numberOfDegreesOfFreedom, Pointer out_significanceFromZero, Pointer out_lowerLimit, Pointer out_upperLimit);
	public double Table_getDifference_studentT (Table me, long column1, long column2, double significanceLevel,
		Pointer out_t, Pointer out_numberOfDegreesOfFreedom, Pointer out_significance, Pointer out_lowerLimit, Pointer out_upperLimit);
	public double Table_getGroupMean_studentT (Table me, long column, long groupColumn, WString group1, double significanceLevel,
		Pointer out_tFromZero, Pointer out_numberOfDegreesOfFreedom, Pointer out_significanceFromZero, Pointer out_lowerLimit, Pointer out_upperLimit);
	public double Table_getGroupDifference_studentT (Table me, long column, long groupColumn, WString group1, WString group2, double significanceLevel,
		Pointer out_tFromZero, Pointer out_numberOfDegreesOfFreedom, Pointer out_significanceFromZero, Pointer out_lowerLimit, Pointer out_upperLimit);
	public double Table_getGroupDifference_wilcoxonRankSum (Table me, long column, long groupColumn, WString group1, WString group2,
		Pointer out_rankSum, Pointer out_significanceFromZero);
	public double Table_getVarianceRatio (Table me, long column1, long column2, double significanceLevel,
		Pointer out_significance, Pointer out_lowerLimit, Pointer out_upperLimit);
	public boolean Table_getExtrema (Table me, long icol, Pointer minimum, Pointer maximum);
	
	public void Table_sortRows_Assert (Table me, Pointer columns, long numberOfColumns);
	public void Table_sortRows_string (Table me, WString columns_string);
	public void Table_randomizeRows (Table me);
	public void Table_reflectRows (Table me);
	
	public void Table_writeToTabSeparatedFile (Table me, MelderFile file);
	public void Table_writeToCommaSeparatedFile (Table me, MelderFile file);
	public Table Table_readFromTableFile (MelderFile file);
	public Table Table_readFromCharacterSeparatedTextFile (MelderFile file, char separator);
	
	public Table Table_extractRowsWhereColumn_number (Table me, long column, int which_Melder_NUMBER, double criterion);
	public Table Table_extractRowsWhereColumn_string (Table me, long column, int which_Melder_STRING, WString criterion);
	public Table Table_collapseRows (Table me, WString factors_string, WString columnsToSum_string,
		WString columnsToAverage_string, WString columnsToMedianize_string,
		WString columnsToAverageLogarithmically_string, WString columnsToMedianizeLogarithmically_string);
	public Table Table_rowsToColumns (Table me, WString factors_string, long columnToTranspose, WString columnsToExpand_string);
	public Table Table_transpose (Table me);

	public void Table_checkSpecifiedRowNumberWithinRange (Table me, long rowNumber);
	public void Table_checkSpecifiedColumnNumberWithinRange (Table me, long columnNumber);
	public boolean Table_isCellNumeric_ErrorFalse (Table me, long rowNumber, long columnNumber);
	public boolean Table_isColumnNumeric_ErrorFalse (Table me, long columnNumber);
	
	public double Table_getNrow (Table me);
	public double Table_getNcol (Table me);
	public WString  Table_getColStr (Table me, long columnNumber);
	public double Table_getMatrix (Table m, long rowNumber, long columnNumber);
	public WString  Table_getMatrixStr (Table me, long rowNumber, long columnNumber);
	public double Table_getColIndex  (Table me, WString columnLabel);
	
}
