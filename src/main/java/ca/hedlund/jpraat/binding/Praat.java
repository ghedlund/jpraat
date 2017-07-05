package ca.hedlund.jpraat.binding;

import java.util.concurrent.locks.ReentrantLock;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import ca.hedlund.jpraat.annotations.Custom;
import ca.hedlund.jpraat.annotations.Declared;
import ca.hedlund.jpraat.annotations.NativeType;
import ca.hedlund.jpraat.annotations.Wrapped;
import ca.hedlund.jpraat.binding.fon.Formant;
import ca.hedlund.jpraat.binding.fon.Function;
import ca.hedlund.jpraat.binding.fon.Intensity;
import ca.hedlund.jpraat.binding.fon.IntervalTier;
import ca.hedlund.jpraat.binding.fon.LongSound;
import ca.hedlund.jpraat.binding.fon.Ltas;
import ca.hedlund.jpraat.binding.fon.Matrix;
import ca.hedlund.jpraat.binding.fon.Pitch;
import ca.hedlund.jpraat.binding.fon.PointProcess;
import ca.hedlund.jpraat.binding.fon.Sampled;
import ca.hedlund.jpraat.binding.fon.SampledXY;
import ca.hedlund.jpraat.binding.fon.Sound;
import ca.hedlund.jpraat.binding.fon.Spectrogram;
import ca.hedlund.jpraat.binding.fon.Spectrum;
import ca.hedlund.jpraat.binding.fon.TextGrid;
import ca.hedlund.jpraat.binding.fon.TextInterval;
import ca.hedlund.jpraat.binding.fon.TextPoint;
import ca.hedlund.jpraat.binding.fon.TextTier;
import ca.hedlund.jpraat.binding.fon.Vector;
import ca.hedlund.jpraat.binding.fon.kPitch_unit;
import ca.hedlund.jpraat.binding.fon.kSound_to_Spectrogram_windowShape;
import ca.hedlund.jpraat.binding.fon.kSound_windowShape;
import ca.hedlund.jpraat.binding.fon.kSounds_convolve_scaling;
import ca.hedlund.jpraat.binding.fon.kSounds_convolve_signalOutsideTimeDomain;
import ca.hedlund.jpraat.binding.jna.NativeLibraryOptions;
import ca.hedlund.jpraat.binding.jna.Str32;
import ca.hedlund.jpraat.binding.stat.SimpleDouble;
import ca.hedlund.jpraat.binding.stat.Table;
import ca.hedlund.jpraat.binding.stat.TableOfReal;
import ca.hedlund.jpraat.binding.sys.ClassInfo;
import ca.hedlund.jpraat.binding.sys.Daata;
import ca.hedlund.jpraat.binding.sys.Interpreter;
import ca.hedlund.jpraat.binding.sys.MelderFile;
import ca.hedlund.jpraat.binding.sys.MelderQuantity;
import ca.hedlund.jpraat.binding.sys.PraatVersion;
import ca.hedlund.jpraat.binding.sys.Strings;
import ca.hedlund.jpraat.binding.sys.Thing;
import ca.hedlund.jpraat.exceptions.PraatException;

/**
 * Main JNA interface class for praat library.  These methods should not be used directly,
 * use more specific binding classes for object creation and manipulation.
 * 
 */
public interface Praat extends Library {
	
	/**
	 * Static instance of Praat native library
	 */
	Praat INSTANCE = (Praat)
			Native.loadLibrary("jpraat", Praat.class, new NativeLibraryOptions());
	
	
	/**
	 * {@link Wrapped} methods need to use this lock before calling
	 * the wrapped C++ function and unlock <em>after</em> calling
	 * {@link #checkAndClearLastError}
	 */
	static ReentrantLock wrapperLock = new ReentrantLock();
	
	public static NativeLibrary getNativeLibrary() {
		return NativeLibrary.getInstance("jpraat");
	}
	
	public static ClassInfo getClassInfo(Class<? extends Thing> clazz) {
		Pointer p = getNativeLibrary().getGlobalVariableAddress("class" + clazz.getSimpleName());
		return new ClassInfo(p);
	}
	
	@Custom
	public static void checkAndClearLastError() throws PraatException {
		final String msg = INSTANCE.jpraat_last_error();
		INSTANCE.jpraat_clear_error();
		if(msg != null && msg.length() > 0) {
			throw new PraatException(msg);
		}
	}
	
	@Custom
	public void jpraat_clear_error();
	
	@Custom
	public String jpraat_last_error();
	
	@Declared("sys/praat_version.h")
	@Custom
	public PraatVersion praat_version();
	
	@Declared("sys/praatlib.h")
	@Custom
	public void praat_lib_init();
	
	@Declared("fon/praat_Fon.cpp")
	@Custom
	public void praat_lib_uvafon_init();
	
	@Declared("sys/praat.h")
	@Custom
	public Str32 praat_dir();
	
	@Declared("sys/Thing.h")
	Str32 Thing_className (Thing me);
	
	@Declared("sys/Thing.h")
	@Wrapped
	public void _Thing_forget_wrapped(Thing me);
	
	@Declared("sys/Thing.h")
	@Wrapped
	public void _Thing_forget_nozero_wrapped (Thing me);
	
	@Declared("sys/Thing.h")
	public void Thing_info (Thing me);
	
	@Declared("sys/Thing.h")
	public void Thing_infoWithId (Thing me, NativeLong id);
	
	@Declared("sys/Thing.h")
	@Wrapped(autoPtrUnwrap=true)
	public Thing Thing_newFromClassName_wrapped (Str32 className, 
			@NativeType("int*") Pointer iFormatVersion);
	
	/* Return a pointer to your internal name (which can be NULL). */
	@Declared("sys/Thing.h")
	public Str32 Thing_getName (Thing me);

	/*
		Function:
			remember that you are called 'name'.
		Postconditions:
			my name *and* my name are copies of 'name'.
	 */
	@Declared("sys/Thing.h")
	public void Thing_setName (Thing me, Str32 name);

	@Declared("sys/Thing.h")
	public void Thing_swap (Thing me, Thing thee);
	
	@Declared("sys/Data.h")
	public boolean Data_equal (Daata data1, Daata data2);
	
	@Declared("sys/Data.h")
	public boolean Data_canWriteAsEncoding (Daata me, int outputEncoding);

	@Declared("sys/Data.h")
	public boolean Data_canWriteText (Daata me);

	@Declared("sys/Data.h")
	@Wrapped
	public MelderFile Data_createTextFile_wrapped (Daata me, MelderFile file, boolean verbose);

	@Declared("sys/Data.h")
	@Wrapped
	public void Data_writeText_wrapped (Daata me, MelderFile openFile);

	@Declared("sys/Data.h")
	@Wrapped
	public void Data_writeToTextFile_wrapped (Daata me, MelderFile file);
	
	@Declared("sys/Data.h")
	@Wrapped
	public void Data_writeToShortTextFile_wrapped (Daata me, MelderFile file);

	@Declared("sys/Data.h")
	public boolean Data_canWriteBinary (Daata me);

	@Declared("sys/Data.h")
	@Wrapped
	public void Data_writeToBinaryFile_wrapped (Daata me, MelderFile file);

	@Declared("sys/Data.h")
	public boolean Data_canWriteLisp (Daata me);

	@Declared("sys/Data.h")
	public void Data_writeLispToConsole (Daata me);
	
	@Declared("sys/Data.h")
	public boolean Data_canReadText (Daata me);

	@Declared("sys/Data.h")
	@Wrapped(autoPtrUnwrap=true)
	public @NativeType("Thing") Pointer Data_readFromTextFile_wrapped (MelderFile file);

	@Declared("sys/Data.h")
	public boolean Data_canReadBinary (Daata me);

	@Declared("sys/Data.h")
	@Wrapped(autoPtrUnwrap=true)
	public @NativeType("Thing") Pointer Data_readFromBinaryFile_wrapped (MelderFile file);

	@Declared("sys/Data.h")
	@Wrapped(autoPtrUnwrap=true)
	public @NativeType("Thing") Pointer Data_readFromFile_wrapped (MelderFile file);
	
	@Declared("sys/Simple.h")
	@Wrapped(autoPtrUnwrap=true)
	public SimpleDouble SimpleDouble_create_wrapped(double number) throws PraatException;
	
	@Declared("sys/Simple_def.h")
	@Custom
	public double SimpleDouble_getNumber(SimpleDouble me);
	
	@Declared("sys/Simple_def.h")
	@Custom
	public void SimpleDouble_setNumber(SimpleDouble me, double number);
	
	@Declared("sys/melder.h")
	@Custom
	public MelderFile MelderFile_new();
	
	@Declared("sys/melder.h")
	@Wrapped
	public MelderFile MelderFile_create_wrapped(MelderFile file);
	
	@Declared("sys/melder.h")
	@Wrapped
	public MelderFile MelderFile_open_wrapped (MelderFile file);
	
	@Declared("sys/melder.h")
	@Wrapped
	public void Melder_pathToFile_wrapped (Str32 path, MelderFile file);
	
	@Declared("sys/melder.h")
	public Str32 Melder_fileToPath (MelderFile file);
	
	@Declared("sys/melder.h")
	public NativeLong MelderFile_length (MelderFile file);
	
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
	public int Function_getMinimumUnit (NativeLong ilevel);
	
	@Declared("fon/Function.h")
	public int Function_getMaximumUnit (NativeLong ilevel);
	
	@Declared("fun/Function.h")
	public Str32 Function_getUnitText(Function me, NativeLong ilevel, int unit, NativeLong flags);
	
	@Declared("fon/Function.h")
	public boolean Function_isUnitLogarithmic (Function me, NativeLong ilevel, int unit);

	@Declared("fon/Function.h")
	public double Function_convertStandardToSpecialUnit (Function me, double value, NativeLong ilevel, int unit);
	
	@Declared("fon/Function.h")
	public double Function_convertSpecialToStandardUnit (Function me, double value, NativeLong ilevel, int unit);
	
	@Declared("fon/Function.h")
	public double Function_convertToNonlogarithmic (Function me, double value, NativeLong ilevel, int unit);
	
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
	
	@Declared("fon/Function_def.h")
	@Custom
	public double Function_getXmin (Function me);

	@Declared("fon/Function_def.h")
	@Custom
	public double Function_getXmax (Function me);
	
	@Declared("fon/Function_def.h")
	@Custom
	public MelderQuantity Function_domainQuantity (Function me);
	
	@Declared("fon/Function_def.h")
	@Custom
	public void Function_shiftX (Function me, double xfrom, double xto);
	
	@Declared("fon/Function_def.h")
	@Custom
	public void Function_scaleX (Function me, double xminfrom, double xmaxfrom, double xminto, double xmaxto);
	
	@Declared("fon/LongSound.h")
	@Wrapped(autoPtrUnwrap=true)
	public LongSound LongSound_open_wrapped (MelderFile fs);

	@Declared("fon/LongSound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound LongSound_extractPart_wrapped (LongSound me, double tmin, double tmax, int preserveTimes);
	
	@Declared("fon/LongSound.h")
	@Wrapped
	public boolean LongSound_haveWindow_wrapped (LongSound me, double tmin, double tmax);
	
	@Declared("fon/LongSound.h")
	@Wrapped
	public void LongSound_getWindowExtrema_wrapped (LongSound me, double tmin, double tmax, int channel, 
			@NativeType("double*") Pointer minimum, @NativeType("double*") Pointer maximum);
	
	@Declared("fon/LongSound.h")
	@Wrapped
	public void LongSound_savePartAsAudioFile_wrapped (LongSound me, int audioFileType, double tmin,
			double tmax, MelderFile file, int numberOfBitsPerSamplePoint);

	@Declared("fon/LongSound.h")
	@Wrapped
	public void LongSound_saveChannelAsAudioFile_wrapped (LongSound me, int audioFileType, int channel, 
			MelderFile file);
	
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
	public double Vector_getValueAtX (Vector me, double x, NativeLong channel, int interpolation);
	
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
	public void Vector_getMinimumAndX (Vector me, double xmin, double xmax, NativeLong channel, int interpolation,
			 Pointer return_minimum, Pointer return_xOfMinimum);
	
	/**
	 * 
	 * @param me
	 * @param xmin
	 * @param xmax
	 * @param interpolation
	 * @param return_minimum (pointer to double)
	 * @param return_xOfMinimum (pointer to double)
	 * @param return_channelOfMinimum (pointer to NativeLong)
	 */
	@Declared("fon/Vector.h")
	public void Vector_getMinimumAndXAndChannel (Vector me, double xmin, double xmax, int interpolation,
			Pointer return_minimum, Pointer return_xOfMinimum, Pointer return_channelOfMinimum);
	
	@Declared("fon/Vector.h")
	public void Vector_getMaximumAndX (Vector me, double xmin, double xmax, NativeLong channel, int interpolation,
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
	public NativeLong Vector_getChannelOfMinimum (Vector me, double xmin, double xmax, int interpolation);
	
	@Declared("fon/Vector.h")
	public NativeLong Vector_getChannelOfMaximum (Vector me, double xmin, double xmax, int interpolation);

	@Declared("fon/Vector.h")
	public double Vector_getMean (Vector me, double xmin, double xmax, NativeLong channel);
	
	@Declared("fon/Vector.h")
	public double Vector_getStandardDeviation (Vector me, double xmin, double xmax, NativeLong channel);

	@Declared("fon/Vector.h")
	public void Vector_addScalar (Vector me, double scalar);
	
	@Declared("fon/Vector.h")
	public void Vector_subtractMean (Vector me);
	
	@Declared("fon/Vector.h")
	public void Vector_multiplyByScalar (Vector me, double scalar);
	
	@Declared("fon/Vector.h")
	public void Vector_scale (Vector me, double scale);
	
	@Declared("fon/Intensity.h")
	@Wrapped(autoPtrUnwrap=true)
	public Intensity Intensity_create_wrapped (double tmin, double tmax, NativeLong nt, double dt, double t1);
	
	@Declared("fon/Intensity.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Intensity_to_Matrix_wrapped (Intensity me);
	
	@Declared("fon/Intensity.h")
	@Wrapped(autoPtrUnwrap=true)
	public Intensity Matrix_to_Intensity_wrapped (Matrix me);

	@Declared("fon/Intensity.h")
	@Wrapped
	public double Intensity_getQuantile_wrapped (Intensity me, double tmin, double tmax, double quantile);
	
	@Declared("fon/Intensity.h")
	@Wrapped
	public double Intensity_getAverage_wrapped (Intensity me, double tmin, double tmax, int averagingMethod);
	
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
	@Wrapped(autoPtrUnwrap=true)
	public Intensity Sound_to_Intensity_wrapped (Sound me, double minimumPitch, double timeStep, int subtractMean)
		throws PraatException;
	
	@Declared("fon/Sampled.h")
	public double Sampled_getXMin(Sampled me);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getXMax(Sampled me);
	
	@Declared("fon/Sampled.h")
	public NativeLong Sampled_getNx(Sampled me);

	@Declared("fon/Sampled.h")
	public double Sampled_getDx(Sampled me);

	@Declared("fon/Sampled.h")
	public double Sampled_getX1(Sampled me);
	
	@Declared("fon/Sampled.h")
	public double Sampled_indexToX (Sampled me, NativeLong i);

	@Declared("fon/Sampled.h")
	public double Sampled_xToIndex (Sampled me, double x);

	@Declared("fon/Sampled.h")
	public NativeLong Sampled_xToLowIndex (Sampled me, double x);

	@Declared("fon/Sampled.h")
	public NativeLong Sampled_xToHighIndex (Sampled me, double x);

	@Declared("fon/Sampled.h")
	public NativeLong Sampled_xToNearestIndex (Sampled me, double x);

	@Declared("fon/Sampled.h")
	public NativeLong Sampled_getWindowSamples (Sampled me, double xmin, double xmax, Pointer ixmin, Pointer ixmax);

	@Declared("fon/Sampled.h")
	@Wrapped
	public void Sampled_shortTermAnalysis_wrapped (Sampled me, double windowDuration, double timeStep,
			@NativeType("long*") Pointer numberOfFrames, @NativeType("double*") Pointer firstTime);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getValueAtSample (Sampled me, NativeLong isamp, NativeLong ilevel, int unit);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getValueAtX (Sampled me, double x, NativeLong ilevel, int unit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public NativeLong Sampled_countDefinedSamples (Sampled me, NativeLong ilevel, int unit);
	
	@Declared("fon/Sampled.h")
	public @NativeType("double*") Pointer Sampled_getSortedValues (Sampled me, NativeLong ilevel, int unit, Pointer numberOfValues);

	@Declared("fon/Sampled.h")
	@Wrapped
	public double Sampled_getQuantile_wrapped
		(Sampled me, double xmin, double xmax, double quantile, NativeLong ilevel, int unit);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getMean
		(Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getMean_standardUnit
		(Sampled me, double xmin, double xmax, NativeLong ilevel, int averagingUnit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getIntegral
		(Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getIntegral_standardUnit
		(Sampled me, double xmin, double xmax, NativeLong ilevel, int averagingUnit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getStandardDeviation
		(Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getStandardDeviation_standardUnit
		(Sampled me, double xmin, double xmax, NativeLong ilevel, int averagingUnit, boolean interpolate);

	@Declared("fon/Sampled.h")
	public void Sampled_getMinimumAndX (Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate,
		Pointer return_minimum, Pointer return_xOfMinimum);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getMinimum (Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getXOfMinimum (Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public void Sampled_getMaximumAndX (Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate,
		Pointer return_maximum, Pointer return_xOfMaximum);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getMaximum (Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate);
	
	@Declared("fon/Sampled.h")
	public double Sampled_getXOfMaximum (Sampled me, double xmin, double xmax, NativeLong ilevel, int unit, boolean interpolate);

	@Declared("fon/SampledXY.h")
	public double SampledXY_getYMin(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_getYMax(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public NativeLong SampledXY_getNy(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_GetDy(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_getY1(SampledXY me);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_indexToY (SampledXY me, NativeLong   index);
	
	@Declared("fon/SampledXY.h")
	public double SampledXY_yToIndex (SampledXY me, double y);
	
	@Declared("fon/SampledXY.h")
	public NativeLong SampledXY_yToLowIndex     (SampledXY me, double y);
	
	@Declared("fon/SampledXY.h")
	public NativeLong SampledXY_yToHighIndex    (SampledXY me, double y);
	
	@Declared("fon/SampledXY.h")
	public NativeLong SampledXY_yToNearestIndex (SampledXY me, double y);
	
	@Declared("fon/Matrix.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Matrix_create_wrapped
		(double xmin, double xmax, NativeLong nx, double dx, double x1,
		 double ymin, double ymax, NativeLong ny, double dy, double y1);
	
	@Declared("fon/Matrix.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Matrix_createSimple_wrapped (NativeLong numberOfRows, NativeLong numberOfColumns);
	
	@Declared("fon/Matrix.h")
	public NativeLong Matrix_getWindowSamplesX (Matrix me, double xmin, double xmax, 
			@NativeType("long*") Pointer ixmin, @NativeType("long*") Pointer ixmax);
	
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
	public NativeLong Matrix_xToLowColumn (Matrix me, double x);   /* Return floor (Matrix_xToColumn (me, x)). */
	
	@Declared("fon/Matrix.h")
	public NativeLong Matrix_xToHighColumn (Matrix me, double x);   /* Return ceil (Matrix_xToColumn (me, x)). */
	
	@Declared("fon/Matrix.h")
	public NativeLong Matrix_xToNearestColumn (Matrix me, double x);   /* Return floor (Matrix_xToColumn (me, x) + 0.5). */
	
	@Declared("fon/Matrix.h")
	public double Matrix_yToRow (Matrix me, double y);   /* Return (y - ymin) / my dy + 1. */
	
	@Declared("fon/Matrix.h")
	public NativeLong Matrix_yToLowRow (Matrix me, double y);   /* Return floor (Matrix_yToRow (me, y)). */
	
	@Declared("fon/Matrix.h")
	public NativeLong Matrix_yToHighRow (Matrix me, double x);   /* Return ceil (Matrix_yToRow (me, y)). */
	
	@Declared("fon/Matrix.h")
	public NativeLong Matrix_yToNearestRow (Matrix me, double y);   /* Return floor (Matrix_yToRow (me, y) + 0.5). */
	
	@Declared("fon/Matrix.h")
	public NativeLong Matrix_getWindowSamplesY (Matrix me, double ymin, double ymax, 
			@NativeType("long*") Pointer iymin, @NativeType("long*") Pointer iymax);
	
	@Declared("fon/Matrix.h")
	public NativeLong Matrix_getWindowExtrema (Matrix me, NativeLong ixmin, NativeLong ixmax, NativeLong iymin, NativeLong iymax,
		Pointer minimum, Pointer maximum);
	
	@Declared("fon/Matrix.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Matrix_readFromRawTextFile_wrapped (MelderFile file);
	
	@Declared("fon/Matrix.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Matrix_readAP_wrapped (MelderFile file);

	// XXX add support for autoThings
//	@Declared("fon/Matrix.h")
//	@Wrapped
//	public void Matrix_eigen_wrapped (Matrix me,
//			@NativeType("Matrix*") Matrix eigenvectors, @NativeType("Matrix*") Matrix eigenvalues);
	
	@Declared("fon/Matrix.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Matrix_power_wrapped (Matrix me, NativeLong power);
	
	@Declared("fon/Matrix.h")
	@Wrapped
	void Matrix_scaleAbsoluteExtremum_wrapped (Matrix me, double scale);
	
	@Declared("fon/Matrix.h")
	@Wrapped
	public void Matrix_writeToMatrixTextFile_wrapped (Matrix me, MelderFile file);
	
	@Declared("fon/Matrix.h")
	@Wrapped
	public void Matrix_writeToHeaderlessSpreadsheetFile_wrapped (Matrix me, MelderFile file);
	
	@Declared("fon/Matrix.h")
	@Wrapped
	public void Matrix_formula_wrapped(Matrix me, Str32 expression, Interpreter interpreter, Matrix target)
		throws PraatException;
	
	@Declared("fon/Matrix.h")
	@Wrapped
	public void Matrix_formula_part_wrapped(Matrix me, double xmin, double xmax, double ymin, double ymax,
			Str32 expression, Interpreter interpreter, Matrix target)
		throws PraatException;
	
	/**
	 * See {@link Pitch#create(double, double, NativeLong, double, double, double, int)}
	 */
	@Declared("fon/Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Pitch_create_wrapped (double tmin, double tmax, NativeLong nt, double dt, double t1,
			double ceiling, int maxnCandidates);
	
	/**
	 * See {@link Pitch#isVoiced_i(NativeLong)}
	 */
	@Declared("fon/Pitch.h")
	public boolean Pitch_isVoiced_i (Pitch me, NativeLong index);
	
	/**
	 * See {@link Pitch#isVoiced_t(double)}
	 */
	@Declared("fon/Pitch.h")
	public boolean Pitch_isVoiced_t (Pitch me, double t);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getValueAtTime (Pitch me, double time, int unit, boolean interpolate);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getStrengthAtTime (Pitch me, double time, int unit, boolean interpolate);

	@Declared("fon/Pitch.h")
	public NativeLong Pitch_countVoicedFrames (Pitch me);

	@Declared("fon/Pitch.h")
	public double Pitch_getMean (Pitch me, double tmin, double tmax, int unit);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getMeanStrength (Pitch me, double tmin, double tmax, int unit);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getQuantile (Pitch me, double tmin, double tmax, double quantile, int unit);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getStandardDeviation (Pitch me, double tmin, double tmax, int unit);
	
	@Declared("fon/Pitch.h")
	public void Pitch_getMaximumAndTime (Pitch me, double tmin, double tmax, int unit, boolean interpolate,
		@NativeType("double*") Pointer return_maximum, @NativeType("double*") Pointer return_timeOfMaximum);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getMaximum (Pitch me, double tmin, double tmax, int unit, boolean interpolate);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getTimeOfMaximum (Pitch me, double tmin, double tmax, int unit, boolean interpolate);
	
	@Declared("fon/Pitch.h")
	public void Pitch_getMinimumAndTime (Pitch me, double tmin, double tmax, int unit, boolean interpolate,
		Pointer return_minimum, Pointer return_timeOfMinimum);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getMinimum (Pitch me, double tmin, double tmax, int unit, boolean interpolate);
	
	@Declared("fon/Pitch.h")
	public double Pitch_getTimeOfMinimum (Pitch me, double tmin, double tmax, int unit, boolean interpolate);
	
	@Declared("fon/Pitch.h")
	public int Pitch_getMaxnCandidates (Pitch me);

	@Declared("fon/Pitch.h")
	public void Pitch_setCeiling (Pitch me, double ceiling);
	
	@Declared("fon/Pitch.h")
	@Wrapped
	public void Pitch_pathFinder_wrapped (Pitch me, double silenceThreshold, double voicingThreshold,
			double octaveCost, double octaveJumpCost, double voicedUnvoicedCost,
			double ceiling, int pullFormants);
	
	@Declared("fon/Pitch.h")
	public void Pitch_difference (Pitch me, Pitch thee);

	@Declared("fon/Pitch.h")
	public NativeLong Pitch_getMeanAbsSlope_hertz (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public NativeLong Pitch_getMeanAbsSlope_mel (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public NativeLong Pitch_getMeanAbsSlope_semitones (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public NativeLong Pitch_getMeanAbsSlope_erb (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	public NativeLong Pitch_getMeanAbsSlope_noOctave (Pitch me, Pointer slope);
	
	@Declared("fon/Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Pitch_killOctaveJumps_wrapped (Pitch me);

	@Declared("fon/Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Pitch_interpolate_wrapped (Pitch me);

	@Declared("fon/Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Pitch_subtractLinearFit_wrapped (Pitch me, int unit);

	@Declared("fon/Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Pitch_smooth_wrapped (Pitch me, double bandWidth);

	@Declared("fon/Pitch.h")
	public void Pitch_step (Pitch me, double step, double precision, double tmin, double tmax);
	
	@Declared("fon/Pitch.h")
	@Wrapped
	public void Pitch_formula_wrapped (Pitch me, Str32 formula, Interpreter interpreter)
		throws PraatException;
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public MelderQuantity Pitch_domainQuantity (Pitch me);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public int Pitch_getMinimumUnit (Pitch me, NativeLong ilevel);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public int Pitch_getMaximumUnit (Pitch me, NativeLong ilevel);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public Str32 Pitch_getUnitText (Pitch me, NativeLong ilevel, kPitch_unit unit, NativeLong flags);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public boolean Pitch_isUnitLogarithmic (Pitch me, NativeLong ilevel, int unit);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public double Pitch_convertStandardToSpecialUnit (Pitch me, double value, NativeLong ilevel, int unit);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public double Pitch_convertSpecialToStandardUnit (Pitch me, double value, NativeLong ilevel, int unit);
	
	@Declared("fon/Pitch_def.h")
	@Custom
	public double Pitch_getValueAtSample (Pitch me, NativeLong isamp, NativeLong ilevel, int unit);
	
	/**
	 * Read sound from file
	 * 
	 * @param file
	 * @return
	 */
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_readFromSoundFile_wrapped (MelderFile file);
	
	@Declared("fon/Sound.h")
	@Wrapped
	public void Sound_saveAsAudioFile_wrapped(Sound me, MelderFile file, int audioFileType, int numberOfBitsPerSamplePoint);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_readFromRawSoundFile_wrapped (MelderFile file, int encoding, int numberOfChannels, double sampleRate);
	
	@Declared("fon/Sound.h")
	@Wrapped
	public void Sound_saveAsRawSoundFile_wrapped (Sound me, MelderFile file, int encoding);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_create_wrapped (NativeLong numberOfChannels, double xmin, double xmax, NativeLong nx, double dx, double x1);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_createAsPureTone_wrapped (long numberOfChannels, double startingTime, double endTime,
		double sampleRate, double frequency, double amplitude, double fadeInDuration, double fadeOutDuration);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_createAsToneComplex_wrapped (double startingTime, double endTime,
		double sampleRate, int phase, double frequencyStep,
		double firstFrequency, double ceiling, long numberOfComponents);
	
	/**
	 * Create a sound object
	 * 
	 * @param numberOfChannels
	 * @param duration
	 * @param samplingFrequency
	 * @return
	 */
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_createSimple_wrapped (NativeLong numberOfChannels, double duration, double samplingFrequency);
	
	@Declared("fon/Sound.h")
	public double Sound_getEnergyInAir (Sound me);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_convertToMono_wrapped (Sound me);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_convertToStereo_wrapped (Sound me);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_extractChannel_wrapped (Sound me, NativeLong ichannel);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_upsample_wrapped (Sound me);   /* By a factor 2. */

	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_resample_wrapped (Sound me, double samplingFrequency, NativeLong precision);
	
	// XXX Fix collection handling
//	@Declared("fon/Sound.h")
//	@Wrapped
//	public Sound Sounds_combineToStereo_wrapped(Collection me);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sounds_append_wrapped(Sound me, double silenceDuration, Sound thee);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sounds_convolve_wrapped (Sound me, Sound thee, 
			kSounds_convolve_scaling scaling, kSounds_convolve_signalOutsideTimeDomain signalOutsideTimeDomain);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sounds_crossCorrelate_wrapped (Sound me, Sound thee,
			kSounds_convolve_scaling scaling, kSounds_convolve_signalOutsideTimeDomain signalOutsideTimeDomain);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sounds_crossCorrelate_short_wrapped (Sound me, Sound thee, double tmin, double tmax, int normalize);
	
	@Declared("fon/Sound.h")
	public double Sound_getRootMeanSquare (Sound me, double xmin, double xmax);
	
	@Declared("fon/Sound.h")
	public double Sound_getEnergy (Sound me, double xmin, double xmax);
	
	@Declared("fon/Sound.h")
	public double Sound_getPower (Sound me, double xmin, double xmax);
	
	@Declared("fon/Sound.h")
	public double Sound_getPowerInAir (Sound me);
	
	@Declared("fon/Sound.h")
	public double Sound_getIntensity_dB (Sound me);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_autoCorrelate_wrapped (Sound me, kSounds_convolve_scaling scaling, kSounds_convolve_signalOutsideTimeDomain signalOutsideTimeDomain);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_extractPart_wrapped (Sound me, double t1, double t2, kSound_windowShape windowShape, double relativeWidth, boolean preserveTimes);
	
	@Declared("fon/Sound.h")
	public double Sound_getNearestZeroCrossing (Sound me, double position, long ichannel);
	
	@Declared("fon/Sound.h")
	void Sound_setZero (Sound me, double tmin, double tmax, int roundTimesToNearestZeroCrossing);
	
	/// Fix collections
//	@Declared("fon/Sound.h")
//	@Wrapped
//	public Sound Sounds_concatenate_e_wrapped (Collection me, double overlapTime);
	
	@Declared("fon/Sound.h")
	public void Sound_multiplyByWindow (Sound me, kSound_windowShape windowShape);
	
	@Declared("fon/Sound.h")
	public void Sound_scaleIntensity (Sound me, double newAverageIntensity);
	
	@Declared("fon/Sound.h")
	public void Sound_overrideSamplingFrequency (Sound me, double newSamplingFrequency);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_extractPartForOverlap_wrapped (Sound me, double t1, double t2, double overlap);
	
	@Declared("fon/Sound.h")
	@Wrapped
	public void Sound_filterWithFormants_wrapped (Sound me, double tmin, double tmax,
		int numberOfFormants, double formant [], double bandwidth []);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_filter_oneFormant_wrapped (Sound me, double frequency, double bandwidth);
	
	@Declared("fon/Sound.h")
	@Wrapped
	public void Sound_filterWithOneFormantInline_wrapped (Sound me, double frequency, double bandwidth);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_filter_preemphasis_wrapped (Sound me, double frequency);
	
	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_filter_deemphasis_wrapped (Sound me, double frequency);

	@Declared("fon/Sound.h")
	@Wrapped(autoPtrUnwrap=true)
	public void Sound_reverse_wrapped (Sound me, double tmin, double tmax);
	
	@Declared("fon/Spectrogram.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrogram Spectrogram_create_wrapped (double tmin, double tmax, NativeLong nt, double dt, double t1,
					double fmin, double fmax, NativeLong nf, double df, double f1);

	@Declared("fon/Spectrogram.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrogram Matrix_to_Spectrogram_wrapped (Matrix me);

	@Declared("fon/Spectrogram.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Spectrogram_to_Matrix_wrapped (Spectrogram me);
	
	@Declared("fon/Spectrogram.h")
	@Custom
	public double Spectrogram_getZ(Spectrogram me, int ix, int iy);
	
	@Declared("fon/Sound_and_Spectrogram.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrogram Sound_to_Spectrogram_wrapped (Sound me, double effectiveAnalysisWidth, double fmax,
			double minimumTimeStep1, double minimumFreqStep1, kSound_to_Spectrogram_windowShape windowShape,
			double maximumTimeOversampling, double maximumFreqOversampling);
	
	@Declared("fon/Sound_to_Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Sound_to_Pitch_wrapped (Sound me, double timeStep,
			double minimumPitch, double maximumPitch);

	@Declared("fon/Sound_to_Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Sound_to_Pitch_ac_wrapped (Sound me, double timeStep, double minimumPitch,
		double periodsPerWindow, int maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch);

	@Declared("fon/Sound_to_Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Sound_to_Pitch_cc_wrapped (Sound me, double timeStep, double minimumPitch,
		double periodsPerWindow, int maxnCandidates, int accurate,
		double silenceThreshold, double voicingThreshold, double octaveCost,
		double octaveJumpCost, double voicedUnvoicedCost, double maximumPitch);
	
	@Declared("fon/Sound_to_Pitch.h")
	@Wrapped(autoPtrUnwrap=true)
	public Pitch Sound_to_Pitch_any_wrapped (Sound me,

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
	
	@Declared("fon/Spectrum.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrum Spectrum_create_wrapped (double fmax, long nf);
	
	@Declared("fon/Spectrum.h")
	@Custom
	public double Spectrum_getValueAtSample(Spectrum me, long isamp, long which, int units);
	
	@Declared("fon/Spectrum.h") 
	public int Spectrum_getPowerDensityRange (Spectrum me, 
			@NativeType("double*") Pointer minimum, @NativeType("double*") Pointer maximum);   /* Return 0 if all zeroes. */
	
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getBandDensity (Spectrum me, double fmin, double fmax);   /* Pa2 / Hz2 */
	
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getBandEnergy (Spectrum me, double fmin, double fmax);   /* Pa2 sec */
	
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getBandDensityDifference (Spectrum me,
		double lowBandMin, double lowBandMax, double highBandMin, double HighBandMax);
	
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getBandEnergyDifference (Spectrum me,
		double lowBandMin, double lowBandMax, double highBandMin, double highBandMax);
	
	/*
	 Spectral moments.
	*/
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getCentreOfGravity (Spectrum me, double power);
	
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getCentralMoment (Spectrum me, double moment, double power);
	
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getStandardDeviation (Spectrum me, double power);
	
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getSkewness (Spectrum me, double power);
	
	@Declared("fon/Spectrum.h") 
	public double Spectrum_getKurtosis (Spectrum me, double power);
	
	@Declared("fon/Spectrum.h") 
	@Wrapped(autoPtrUnwrap=true)
	public Table Spectrum_downto_Table_wrapped (Spectrum me, boolean includeBinNumbers, boolean includeFrequency,
			boolean includeRealPart, boolean includeImaginaryPart, boolean includeEnergyDensity, boolean includePowerDensity);

	@Declared("fon/Spectrum.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrum Matrix_to_Spectrum_wrapped (Matrix me);

	@Declared("fon/Spectrum.h") 
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Spectrum_to_Matrix_wrapped (Spectrum me);

	@Declared("fon/Spectrum.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrum Spectrum_cepstralSmoothing_wrapped (Spectrum me, double bandWidth);

	@Declared("fon/Spectrum.h") 
	public void Spectrum_passHannBand (Spectrum me, double fmin, double fmax, double smooth);
	
	@Declared("fon/Spectrum.h") 
	public void Spectrum_stopHannBand (Spectrum me, double fmin, double fmax, double smooth);

	@Declared("fon/Spectrum.h")
	@Wrapped
	public void Spectrum_getNearestMaximum_wrapped (Spectrum me, double frequency, 
			@NativeType("double*") Pointer frequencyOfMaximum, @NativeType("double*") Pointer heightOfMaximum);
	
	@Declared("fon/Sound_and_Spectrum.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrum Sound_to_Spectrum_wrapped (Sound me, int fast);
	
	@Declared("fon/Sound_and_Spectrum.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Spectrum_to_Sound_wrapped (Spectrum me);
	
	@Declared("fon/Sound_and_Spectrum.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrum Spectrum_lpcSmoothing_wrapped (Spectrum me, int numberOfPeaks, double preemphasisFrequency);

	@Declared("fon/Sound_and_Spectrum.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_filter_passHannBand_wrapped (Sound me, double fmin, double fmax, double smooth);
	
	@Declared("fon/Sound_and_Spectrum.h")
	@Wrapped(autoPtrUnwrap=true)
	public Sound Sound_filter_stopHannBand_wrapped (Sound me, double fmin, double fmax, double smooth);
	
	@Declared("fon/Spectrum_and_Spectrogram.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrum Spectrogram_to_Spectrum_wrapped (Spectrogram me, double time);

	@Declared("fon/Spectrum_and_Spectrogram.h")
	@Wrapped(autoPtrUnwrap=true)
	public Spectrogram Spectrum_to_Spectrogram_wrapped (Spectrum me);
	
	@Declared("fon/Formant.h")
	@Wrapped(autoPtrUnwrap=true)
	public Formant Formant_create_wrapped (double tmin, double tmax, NativeLong nt, double dt, double t1, int maxnFormants);

	@Declared("fon/Formant.h")
	public NativeLong Formant_getMinNumFormants (Formant me);
	
	@Declared("fon/Formant.h")
	public NativeLong Formant_getMaxNumFormants (Formant me);

	@Declared("fon/Formant.h")
	public double Formant_getValueAtTime (Formant me, int iformant, double time, int bark);
	
	@Declared("fon/Formant.h")
	public double Formant_getBandwidthAtTime (Formant me, int iformant, double time, int bark);

	@Declared("fon/Formant.h")
	public void Formant_getExtrema (Formant me, int iformant, double tmin, double tmax, 
			@NativeType("double*") Pointer fmin, @NativeType("double*") Pointer fmax);
	
	@Declared("fon/Formant.h")
	public void Formant_getMinimumAndTime (Formant me, int iformant, double tmin, double tmax, int bark, int interpolate,
			@NativeType("double*") Pointer return_minimum, @NativeType("double*") Pointer return_timeOfMinimum);
	
	@Declared("fon/Formant.h")
	public void Formant_getMaximumAndTime (Formant me, int iformant, double tmin, double tmax, int bark, int interpolate,
			@NativeType("double*") Pointer return_maximum, @NativeType("double*") Pointer return_timeOfMaximum);
	
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
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Formant_to_Matrix_wrapped (Formant me, int iformant);
	
	@Declared("fon/Formant.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Formant_to_Matrix_bandwidths_wrapped (Formant me, int iformant);
	
	@Declared("fon/Formant.h")
	@Wrapped
	public void Formant_formula_frequencies_wrapped(Formant me, Str32 formula, Interpreter interpreter)
		throws PraatException;
	
	@Declared("fon/Formant.h")
	public void Formant_formula_bandwidths_wrapped(Formant me, Str32 formula, Interpreter interpreter)
		throws PraatException;

	@Declared("fon/Formant.h")
	@Wrapped(autoPtrUnwrap=true)
	public Formant Formant_tracker_wrapped (Formant me, int numberOfTracks,
		double refF1, double refF2, double refF3, double refF4, double refF5,
		double dfCost,   /* Per kHz. */
		double bfCost, double octaveJumpCost);

	@Declared("fon/Formant.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Formant_downto_Table_wrapped (Formant me, int includeFrameNumbers,
			int includeTimes, int timeDecimals,
			int includeIntensity, int intensityDecimals,
			int includeNumberOfFormants, int frequencyDecimals,
			int includeBandwidths);
	
	@Declared("fon/Formant.h")
	@Custom
	public double Formant_getValueAtSample(Formant me, NativeLong iframe, NativeLong which, int units);
	
	@Declared("fon/Formant.h")
	@Custom
	public double Formant_getIntensityAtSample(Formant me, NativeLong iframe);
	
	@Declared("fon/Sound_to_Formant.h")
	@Wrapped(autoPtrUnwrap=true)
	public Formant Sound_to_Formant_any_wrapped (Sound me, double timeStep, int numberOfPoles, double maximumFrequency,
		double halfdt_window, int which, double preemphasisFrequency, double safetyMargin);

	@Declared("fon/Sound_to_Formant.h")
	@Wrapped(autoPtrUnwrap=true)
	public Formant Sound_to_Formant_burg_wrapped (Sound me, double timeStep, double maximumNumberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency);

	@Declared("fon/Sound_to_Formant.h")
	@Wrapped(autoPtrUnwrap=true)
	public Formant Sound_to_Formant_keepAll_wrapped (Sound me, double timeStep, double maximumNumberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency);

	@Declared("fon/Sound_to_Formant.h")
	@Wrapped(autoPtrUnwrap=true)
	public Formant Sound_to_Formant_willems_wrapped (Sound me, double timeStep, double numberOfFormants,
		double maximumFormantFrequency, double windowLength, double preemphasisFrequency);
	
	@Declared("sys/sendpraat.c")
	public String sendpraat (Object display, String programName, NativeLong timeOut,  String text);
	
	@Declared("sys/sendpraat.c")
	public WString sendpraatW (Object display, String programName, NativeLong timeOut, WString text);
	
	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_createWithColumnNames_wrapped (NativeLong numberOfRows, Str32 columnNames);

	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_createWithoutColumnNames_wrapped (NativeLong numberOfRows, NativeLong numberOfColumns);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_appendRow_wrapped (Table me);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_appendColumn_wrapped (Table me, Str32 label);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_appendSumColumn_wrapped (Table me, NativeLong column1, NativeLong column2, Str32 label);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_appendDifferenceColumn_wrapped (Table me, NativeLong column1, NativeLong column2, Str32 label);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_appendProductColumn_wrapped (Table me, NativeLong column1, NativeLong column2, Str32 label);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_appendQuotientColumn_wrapped (Table me, NativeLong column1, NativeLong column2, Str32 label);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_removeRow_wrapped (Table me, NativeLong row);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_removeColumn_wrapped (Table me, NativeLong column);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_insertRow_wrapped (Table me, NativeLong row);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_insertColumn_wrapped (Table me, NativeLong column, Str32 label);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_setColumnLabel_wrapped (Table me, NativeLong column, Str32 label);
	
	@Declared("stat/Table.h")
	public NativeLong Table_findColumnIndexFromColumnLabel (Table me, Str32 label);
	
	@Declared("stat/Table.h")
	@Wrapped
	public NativeLong Table_getColumnIndexFromColumnLabel_wrapped (Table me, Str32 columnLabel);
	
	@Declared("stat/Table.h")
	@Wrapped
	public @NativeType("long*") Pointer Table_getColumnIndicesFromColumnLabelString_wrapped (Table me, Str32 string, @NativeType("long*") Pointer numberOfTokens);
	
	@Declared("stat/Table.h")
	public NativeLong Table_searchColumn (Table me, NativeLong column, Str32 value);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_setStringValue_wrapped (Table me, NativeLong row, NativeLong column, Str32 value);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_setNumericValue_wrapped (Table me, NativeLong row, NativeLong column, double value);
	
	@Declared("stat/Table.h")
	@Wrapped
	public Str32 Table_getStringValue_Assert_wrapped (Table me, NativeLong row, NativeLong column);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getNumericValue_Assert_wrapped (Table me, NativeLong row, NativeLong column);

	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getQuantile_wrapped (Table me, NativeLong column, double quantile);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getMean_wrapped (Table me, NativeLong column);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getMaximum_wrapped (Table me, NativeLong icol);

	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getMinimum_wrapped (Table me, NativeLong icol);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getGroupMean_wrapped (Table me, NativeLong column, NativeLong groupColumn, Str32 group);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getStdev_wrapped (Table me, NativeLong column);
	
	@Declared("stat/Table.h")
	@Wrapped
	public NativeLong Table_drawRowFromDistribution_wrapped (Table me, NativeLong column);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getCorrelation_pearsonR_wrapped (Table me, NativeLong column1, NativeLong column2, double significanceLevel,
		@NativeType("double*") Pointer out_significance, @NativeType("double*") Pointer out_lowerLimit, @NativeType("double*") Pointer out_upperLimit);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getCorrelation_kendallTau_wrapped (Table me, NativeLong column1, NativeLong column2, double significanceLevel,
		@NativeType("double*") Pointer out_significance, @NativeType("double*") Pointer out_lowerLimit, @NativeType("double*") Pointer out_upperLimit);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getMean_studentT_wrapped (Table me, NativeLong column, double significanceLevel,
		@NativeType("double*") Pointer out_tFromZero, @NativeType("double*") Pointer out_numberOfDegreesOfFreedom, 
		@NativeType("double*") Pointer out_significanceFromZero, @NativeType("double*") Pointer out_lowerLimit, @NativeType("double*") Pointer out_upperLimit);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getDifference_studentT_wrapped (Table me, NativeLong column1, NativeLong column2, double significanceLevel,
		@NativeType("double*") Pointer out_t, @NativeType("double*") Pointer out_numberOfDegreesOfFreedom, 
		@NativeType("double*") Pointer out_significance, @NativeType("double*") Pointer out_lowerLimit, @NativeType("double*") Pointer out_upperLimit);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getGroupMean_studentT_wrapped (Table me, NativeLong column, NativeLong groupColumn, Str32 group1, double significanceLevel,
		@NativeType("double*") Pointer out_tFromZero, @NativeType("double*") Pointer out_numberOfDegreesOfFreedom, 
		@NativeType("double*") Pointer out_significanceFromZero, @NativeType("double*") Pointer out_lowerLimit, @NativeType("double*") Pointer out_upperLimit);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getGroupDifference_studentT_wrapped (Table me, NativeLong column, NativeLong groupColumn, Str32 group1, Str32 group2, double significanceLevel,
		@NativeType("double*") Pointer out_tFromZero, @NativeType("double*") Pointer out_numberOfDegreesOfFreedom,
		@NativeType("double*") Pointer out_significanceFromZero, @NativeType("double*") Pointer out_lowerLimit, @NativeType("double*") Pointer out_upperLimit);
	
	@Declared("stat/Table.h")
	@Wrapped
	public double Table_getGroupDifference_wilcoxonRankSum_wrapped (Table me, NativeLong column, NativeLong groupColumn, Str32 group1, Str32 group2,
		@NativeType("double*") Pointer out_rankSum, @NativeType("double*") Pointer out_significanceFromZero);
	
//	@Declared("stat/Table.h")
//	@Wrapped
//	public double Table_getVarianceRatio_wrapped (Table me, NativeLong column1, NativeLong column2, double significanceLevel,
//		@NativeType("double*") Pointer out_significance, @NativeType("double*") Pointer out_lowerLimit, @NativeType("double*") Pointer out_upperLimit);
	
	@Declared("stat/Table.h")
	@Wrapped
	public boolean Table_getExtrema_wrapped (Table me, NativeLong icol, @NativeType("double*") Pointer minimum, @NativeType("double*") Pointer maximum);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_sortRows_Assert_wrapped (Table me, @NativeType("long*") Pointer columns, NativeLong numberOfColumns);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_sortRows_string_wrapped (Table me, Str32 columns_string);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_randomizeRows_wrapped (Table me);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_reflectRows_wrapped (Table me);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_writeToTabSeparatedFile_wrapped (Table me, MelderFile file);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_writeToCommaSeparatedFile_wrapped (Table me, MelderFile file);
	
	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_readFromTableFile_wrapped (MelderFile file);
	
	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_readFromCharacterSeparatedTextFile_wrapped (MelderFile file, char separator);
	
	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_extractRowsWhereColumn_number_wrapped (Table me, NativeLong column, int which_Melder_NUMBER, double criterion);
	
	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_extractRowsWhereColumn_string_wrapped (Table me, NativeLong column, int which_Melder_STRING, Str32 criterion);
	
	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_collapseRows_wrapped (Table me, Str32 factors_string, Str32 columnsToSum_string,
		Str32 columnsToAverage_string, Str32 columnsToMedianize_string,
		Str32 columnsToAverageLogarithmically_string, Str32 columnsToMedianizeLogarithmically_string);
	
	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_rowsToColumns_wrapped (Table me, Str32 factors_string, NativeLong columnToTranspose, Str32 columnsToExpand_string);
	
	@Declared("stat/Table.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table Table_transpose_wrapped (Table me);

	@Declared("stat/Table.h")
	@Wrapped
	public void Table_checkSpecifiedRowNumberWithinRange_wrapped (Table me, NativeLong rowNumber);
	
	@Declared("stat/Table.h")
	@Wrapped
	public void Table_checkSpecifiedColumnNumberWithinRange_wrapped (Table me, NativeLong columnNumber);
	
	@Declared("stat/Table.h")
	public boolean Table_isCellNumeric_ErrorFalse (Table me, NativeLong rowNumber, NativeLong columnNumber);
	
	@Declared("stat/Table.h")
	public boolean Table_isColumnNumeric_ErrorFalse (Table me, NativeLong columnNumber);
	
	@Declared("stat/Table.h")
	public double Table_getNrow (Table me);
	
	@Declared("stat/Table.h")
	public double Table_getNcol (Table me);
	
	@Declared("stat/Table.h")
	public Str32  Table_getColStr (Table me, NativeLong columnNumber);
	
	@Declared("stat/Table.h")
	public double Table_getMatrix (Table m, NativeLong rowNumber, NativeLong columnNumber);
	
	@Declared("stat/Table.h")
	public Str32  Table_getMatrixStr (Table me, NativeLong rowNumber, NativeLong columnNumber);
	
	@Declared("stat/Table.h")
	public double Table_getColIndex  (Table me, Str32 columnLabel);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_init_wrapped (TableOfReal me, NativeLong numberOfRows, NativeLong numberOfColumns);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal TableOfReal_create_wrapped (NativeLong numberOfRows, NativeLong numberOfColumns);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_removeRow_wrapped (TableOfReal me, NativeLong irow);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_removeColumn_wrapped (TableOfReal me, NativeLong icol);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_insertRow_wrapped (TableOfReal me, NativeLong irow);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_insertColumn_wrapped (TableOfReal me, NativeLong icol);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_setRowLabel_wrapped (TableOfReal me, NativeLong irow, Str32 label);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_setColumnLabel_wrapped (TableOfReal me, NativeLong icol, Str32 label);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public NativeLong TableOfReal_rowLabelToIndex_wrapped (TableOfReal me, Str32 label);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public NativeLong TableOfReal_columnLabelToIndex_wrapped (TableOfReal me, Str32 label);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public double TableOfReal_getColumnMean_wrapped (TableOfReal me, NativeLong icol);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public double TableOfReal_getColumnStdev_wrapped (TableOfReal me, NativeLong icol);

	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal Table_to_TableOfReal_wrapped (Table me, NativeLong labelColumn);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table TableOfReal_to_Table_wrapped (TableOfReal me, Str32 labelOfFirstColumn);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_sortByLabel_wrapped (TableOfReal me, NativeLong column1, NativeLong column2);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_sortByColumn_wrapped (TableOfReal me, NativeLong column1, NativeLong column2);

	@Declared("stat/TableOfReal.h")
	@Wrapped
	public void TableOfReal_writeToHeaderlessSpreadsheetFile_wrapped (TableOfReal me, MelderFile file);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal TableOfReal_readFromHeaderlessSpreadsheetFile_wrapped (MelderFile file);

	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal TableOfReal_extractRowRanges_wrapped (TableOfReal me, Str32 ranges);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal TableOfReal_extractColumnRanges_wrapped (TableOfReal me, Str32 ranges);

	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal TableOfReal_extractRowsWhereColumn_wrapped (TableOfReal me, NativeLong icol, int which_Melder_NUMBER, double criterion);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal TableOfReal_extractColumnsWhereRow_wrapped (TableOfReal me, NativeLong icol, int which_Melder_NUMBER, double criterion);

	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal TableOfReal_extractRowsWhereLabel_wrapped (TableOfReal me, int which_Melder_STRING, Str32 criterion);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	public TableOfReal TableOfReal_extractColumnsWhereLabel_wrapped (TableOfReal me, int which_Melder_STRING, Str32 criterion);

	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	Strings TableOfReal_extractRowLabelsAsStrings_wrapped (TableOfReal me);
	
	@Declared("stat/TableOfReal.h")
	@Wrapped(autoPtrUnwrap=true)
	Strings TableOfReal_extractColumnLabelsAsStrings_wrapped (TableOfReal me);
	
	@Declared("sys/Strings_.h")
	@Wrapped(autoPtrUnwrap=true)
	public Strings Strings_createAsFileList_wrapped (Str32 path);
	
	@Declared("sys/Strings_.h")
	@Wrapped(autoPtrUnwrap=true)
	public Strings Strings_createAsDirectoryList_wrapped (Str32 path);
	
	@Declared("sys/Strings_.h")
	@Wrapped(autoPtrUnwrap=true)
	public Strings Strings_readFromRawTextFile_wrapped (MelderFile file);
	
	@Declared("sys/Strings_.h")
	@Wrapped
	public void Strings_writeToRawTextFile_wrapped (Strings me, MelderFile file);

	@Declared("sys/Strings_.h")
	public void Strings_randomize (Strings me);

	@Declared("sys/Strings_.h")
	public void Strings_genericize (Strings me);

	@Declared("sys/Strings_.h")
	public void Strings_nativize (Strings me);

	@Declared("sys/Strings_.h")
	public void Strings_sort (Strings me);
	
	@Declared("sys/Strings_.h")
	@Wrapped
	public void Strings_remove_wrapped (Strings me, NativeLong position);
	
	@Declared("sys/Strings_.h")
	@Wrapped
	public void Strings_replace_wrapped (Strings me, NativeLong position, Str32 text);
	
	@Declared("sys/Strings_.h")
	@Wrapped
	public void Strings_insert_wrapped (Strings me, NativeLong position, Str32 text);
	
	@Declared("fon/PointProcess.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess PointProcess_create_wrapped (double startingTime, double finishingTime, NativeLong initialMaxnt);
	
	@Declared("fon/PointProcess.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess PointProcess_createPoissonProcess_wrapped (double startingTime, double finishingTime, double density);
	
	@Declared("fon/PointProcess.h")
	public void PointProcess_init (PointProcess me, double startingTime, double finishingTime, NativeLong initialMaxnt);
	
	@Declared("fon/PointProcess.h")
	public NativeLong PointProcess_getLowIndex (PointProcess me, double t);
	
	@Declared("fon/PointProcess.h")
	public NativeLong PointProcess_getHighIndex (PointProcess me, double t);
	
	@Declared("fon/PointProcess.h")
	public NativeLong PointProcess_getNearestIndex (PointProcess me, double t);
	
	@Declared("fon/PointProcess.h")
	public NativeLong PointProcess_getWindowPoints (PointProcess me, double tmin, double tmax, 
			@NativeType("long*") Pointer imin, @NativeType("long*") Pointer imax);
	
	@Declared("fon/PointProcess.h")
	@Wrapped
	public void PointProcess_addPoint_wrapped (PointProcess me, double t);
	
	@Declared("fon/PointProcess.h")
	public NativeLong PointProcess_findPoint (PointProcess me, double t);
	
	@Declared("fon/PointProcess.h")
	public void PointProcess_removePoint (PointProcess me, NativeLong index);
	
	@Declared("fon/PointProcess.h")
	public void PointProcess_removePointNear (PointProcess me, double t);
	
	@Declared("fon/PointProcess.h")
	public void PointProcess_removePoints (PointProcess me, NativeLong first, NativeLong last);
	
	@Declared("fon/PointProcess.h")
	public void PointProcess_removePointsBetween (PointProcess me, double fromTime, double toTime);

	@Declared("fon/PointProcess.h")
	public double PointProcess_getInterval (PointProcess me, double t);
	
	@Declared("fon/PointProcess.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess PointProcesses_union_wrapped (PointProcess me, PointProcess thee);
	
	@Declared("fon/PointProcess.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess PointProcesses_intersection_wrapped (PointProcess me, PointProcess thee);
	
	@Declared("fon/PointProcess.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess PointProcesses_difference_wrapped (PointProcess me, PointProcess thee);
	
	@Declared("fon/PointProcess.h")
	@Wrapped
	public void PointProcess_fill_wrapped (PointProcess me, double tmin, double tmax, double period);
	
	@Declared("fon/PointProcess.h")
	@Wrapped
	public void PointProcess_voice_wrapped (PointProcess me, double period, double maxT);

	@Declared("fon/PointProcess.h")
	public NativeLong PointProcess_getNumberOfPeriods (PointProcess me, double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor);
	
	@Declared("fon/PointProcess.h")
	public double PointProcess_getMeanPeriod (PointProcess me, double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor);

	@Declared("fon/PointProcess.h")
	public double PointProcess_getStdevPeriod (PointProcess me, double tmin, double tmax,
		double minimumPeriod, double maximumPeriod, double maximumPeriodFactor);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextPoint TextPoint_create_wrapped (double time, Str32 mark);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextPoint_setText_wrapped (TextPoint me, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextInterval TextInterval_create_wrapped (double tmin, double tmax, Str32 text);

	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextInterval_setText_wrapped (TextInterval me, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextTier TextTier_create_wrapped (double tmin, double tmax);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextTier_addPoint_wrapped (TextTier me, double time, Str32 mark);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextTier TextTier_readFromXwaves_wrapped (MelderFile file);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess TextTier_getPoints_wrapped (TextTier me, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextTier_removePoint_wrapped (TextTier me, NativeLong ipoint);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public IntervalTier IntervalTier_create_wrapped (double tmin, double tmax);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public IntervalTier IntervalTier_readFromXwaves_wrapped (MelderFile file);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void IntervalTier_writeToXwaves_wrapped (IntervalTier me, MelderFile file);
	
	@Declared("fon/TextGrid.h")
	public NativeLong IntervalTier_timeToLowIndex (IntervalTier me, double t);
	
	@Declared("fon/TextGrid.h")
	@Deprecated
	public NativeLong IntervalTier_timeToIndex (IntervalTier me, double t); 

	@Declared("fon/TextGrid.h")
	public NativeLong IntervalTier_timeToHighIndex (IntervalTier me, double t);
	
	@Declared("fon/TextGrid.h")
	public NativeLong IntervalTier_hasTime (IntervalTier me, double t);
	
	@Declared("fon/TextGrid.h")
	public NativeLong IntervalTier_hasBoundary (IntervalTier me, double t);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess IntervalTier_getStartingPoints_wrapped (IntervalTier me, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess IntervalTier_getEndPoints_wrapped (IntervalTier me, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess IntervalTier_getCentrePoints_wrapped (IntervalTier me, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess IntervalTier_PointProcess_startToCentre_wrapped (IntervalTier tier, PointProcess point, double phase);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess IntervalTier_PointProcess_endToCentre_wrapped (IntervalTier tier, PointProcess point, double phase);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void IntervalTier_removeLeftBoundary_wrapped (IntervalTier me, NativeLong iinterval);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextGrid TextGrid_createWithoutTiers_wrapped (double tmin, double tmax);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextGrid TextGrids_merge_wrapped (TextGrid grid1, TextGrid grid2);

	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextGrid_extendTime_wrapped (TextGrid me, double delta_time, int position);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextGrid_setTierName_wrapped (TextGrid me, NativeLong itier, Str32 newName);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextTier_changeLabels_wrapped (TextTier me, NativeLong from, NativeLong to, 
			Str32 search, Str32 replace, int use_regexp, 
			@NativeType("long*") Pointer nmatches, @NativeType("long*") Pointer nstringmatches);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextGrid_changeLabels_wrapped (TextGrid me, int tier, NativeLong from, NativeLong to, 
			Str32 search, Str32 replace, int use_regexp, 
			@NativeType("long*") Pointer nmatches, @NativeType("long*") Pointer nstringmatches);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void IntervalTier_removeBoundariesBetweenIdenticallyLabeledIntervals_wrapped (IntervalTier me, Str32 label);

	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void IntervalTier_cutIntervalsOnLabelMatch_wrapped (IntervalTier me, Str32 label);


	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void IntervalTier_cutIntervals_minimumDuration_wrapped (IntervalTier me, Str32 label, double minimumDuration);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void IntervalTier_setLaterEndTime_wrapped (IntervalTier me, double xmax, Str32 mark);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void IntervalTier_setEarlierStartTime_wrapped (IntervalTier me, double xmin, Str32 mark);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void IntervalTier_moveBoundary_wrapped (IntervalTier me, NativeLong interval, boolean atStart, double newTime);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextTier_setLaterEndTime_wrapped (TextTier me, double xmax, Str32 mark);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextTier_setEarlierStartTime_wrapped (TextTier me, double xmin, Str32 mark);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextGrid_setEarlierStartTime_wrapped (TextGrid me, double xmin, Str32 imark, Str32 pmark);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextGrid_setLaterEndTime_wrapped (TextGrid me, double xmax, Str32 imark, Str32 pmark);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void IntervalTiers_append_inline_wrapped (IntervalTier me, IntervalTier thee, boolean preserveTimes);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextTiers_append_inline_wrapped (TextTier me, TextTier thee, boolean preserveTimes);
	
	@Declared("dwtools/TextGrid_extensions.h")
	@Wrapped
	public void TextGrids_append_inline_wrapped (TextGrid me, TextGrid thee, boolean preserveTimes);
	
	// XXX fix collections
//	@Declared("dwtools/TextGrid_extensions.h")
//	@Wrapped(autoPtrUnwrap=true)
//	public TextGrid TextGrids_to_TextGrid_appendContinuous_wrapped (Collection me, boolean preserveTimes);

	/**
	 * 
	 * @param tmin
	 * @param tmax
	 * @param tierNames Tier names tokenized by space, max length for each tier name is 400 chars
	 * @param pointTiers Tier names tokenized by space, max length for each tier name is 400 chars
	 * @return
	 */
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextGrid TextGrid_create_wrapped (double tmin, double tmax, Str32 tierNames, Str32 pointTiers);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public NativeLong TextGrid_countLabels_wrapped (TextGrid me, NativeLong itier, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess TextGrid_getStartingPoints_wrapped (TextGrid me, NativeLong itier, int which_Melder_STRING, Str32 criterion);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess TextGrid_getEndPoints_wrapped (TextGrid me, NativeLong itier, int which_Melder_STRING, Str32 criterion);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess TextGrid_getCentrePoints_wrapped (TextGrid me, NativeLong itier, int which_Melder_STRING, Str32 criterion);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess TextGrid_getPoints_wrapped (TextGrid me, NativeLong itier, int which_Melder_STRING, Str32 criterion);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess TextGrid_getPoints_preceded_wrapped (TextGrid me, NativeLong tierNumber,
		int which_Melder_STRING, Str32 criterion,
		int which_Melder_STRING_precededBy, Str32 criterion_precededBy);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public PointProcess TextGrid_getPoints_followed_wrapped (TextGrid me, NativeLong tierNumber,
		int which_Melder_STRING, Str32 criterion,
		int which_Melder_STRING_followedBy, Str32 criterion_followedBy);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public Function TextGrid_checkSpecifiedTierNumberWithinRange_wrapped (TextGrid me, NativeLong tierNumber);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public IntervalTier TextGrid_checkSpecifiedTierIsIntervalTier_wrapped (TextGrid me, NativeLong tierNumber);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public TextTier TextGrid_checkSpecifiedTierIsPointTier_wrapped (TextGrid me, NativeLong tierNumber);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_addTier_copy_wrapped (TextGrid me, Function tier);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	@Custom
	public void TextGrid_removeTier_wrapped (TextGrid me, NativeLong tierNumber);
	
	// XXX fix collections
//	@Declared("fon/TextGrid.h")
//	@Wrapped(autoPtrUnwrap=true)
//	public TextGrid TextGrid_merge_wrapped (Collection textGrids);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextGrid TextGrid_extractPart_wrapped (TextGrid me, double tmin, double tmax, int preserveTimes);
	
	@Declared("fon/TextGrid.h")
	public NativeLong TextInterval_labelLength (TextInterval me);
	
	@Declared("fon/TextGrid.h")
	public NativeLong TextPoint_labelLength (TextPoint me);
	
	@Declared("fon/TextGrid.h")
	public NativeLong IntervalTier_maximumLabelLength (IntervalTier me);
	
	@Declared("fon/TextGrid.h")
	public NativeLong TextTier_maximumLabelLength (TextTier me);
	
	@Declared("fon/TextGrid.h")
	public NativeLong TextGrid_maximumLabelLength (TextGrid me);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_convertToBackslashTrigraphs_wrapped (TextGrid me);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_convertToUnicode_wrapped (TextGrid me);
	
	@Declared("fon/TextGrid.h")
	public void TextInterval_removeText (TextInterval me);
	
	@Declared("fon/TextGrid.h")
	public void TextPoint_removeText (TextPoint me);
	
	@Declared("fon/TextGrid.h")
	public void IntervalTier_removeText (IntervalTier me);
	
	@Declared("fon/TextGrid.h")
	public void TextTier_removeText (TextTier me);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_insertBoundary_wrapped (TextGrid me, int itier, double t);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_removeBoundaryAtTime_wrapped (TextGrid me, int itier, double t);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_setIntervalText_wrapped (TextGrid me, int itier, NativeLong iinterval, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_insertPoint_wrapped (TextGrid me, int itier, double t, Str32 mark);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_setPointText_wrapped (TextGrid me, int itier, NativeLong ipoint, Str32 text);
	
	@Declared("fon/TextGrid.h")
	@Wrapped
	public void TextGrid_writeToChronologicalTextFile_wrapped (TextGrid me, MelderFile file);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextGrid TextGrid_readFromChronologicalTextFile_wrapped (MelderFile file);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public TextGrid TextGrid_readFromCgnSyntaxFile_wrapped (MelderFile file);
	
	@Declared("fon/TextGrid.h")
	@Wrapped(autoPtrUnwrap=true)
	public Table TextGrid_downto_Table_wrapped (TextGrid me, boolean includeLineNumbers, int timeDecimals, boolean includeTierNames, boolean includeEmptyIntervals);

	@Declared("fon/TextGrid_def.h")
	@Custom
	public NativeLong TextTier_numberOfPoints (TextTier me);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public TextPoint TextTier_point (TextTier me, NativeLong i);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void TextTier_removePoints (TextTier me, int which_Melder_STRING, Str32 criterion);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public MelderQuantity TextTier_domainQuantity (TextTier me);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void TextTier_shiftX (TextTier me, double xfrom, double xto);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void TextTier_scaleX (TextTier me, double xminfrom, double xmaxfrom, double xminto, double xmaxto);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public NativeLong IntervalTier_numberOfIntervals (IntervalTier me);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public TextInterval IntervalTier_interval (IntervalTier me, NativeLong i);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public MelderQuantity IntervalTier_domainQuantity (IntervalTier me);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void IntervalTier_shiftX (IntervalTier me, double xfrom, double xto);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void IntervalTier_scaleX (IntervalTier me, double xminfrom, double xmaxfrom, double xminto, double xmaxto);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void IntervalTier_addInterval (IntervalTier me, double tmin, double tmax, Str32 label);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void IntervalTier_removeInterval (IntervalTier me, NativeLong iinterval);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public NativeLong TextGrid_numberOfTiers (TextGrid me);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public Function TextGrid_tier (TextGrid me, NativeLong i);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void TextGrid_removePoints (TextGrid me, NativeLong tierNumber, int which_Melder_STRING, Str32 criterion);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void TextGrid_repair (TextGrid me);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public MelderQuantity TextGrid_domainQuantity (TextGrid me);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void TextGrid_shiftX (TextGrid me, double xfrom, double xto);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public void TextGrid_scaleX (TextGrid me, double xminfrom, double xmaxfrom, double xminto, double xmaxto);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public Str32 TextPoint_getText(TextPoint me);
	
	@Declared("fon/TextGrid_def.h")
	@Custom
	public Str32 TextInterval_getText(TextInterval me);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas Ltas_create_wrapped(NativeLong nx, double dx);
	
	@Declared("fon/Ltas.h")
	@Custom
	public MelderQuantity Ltas_domainQuantity(Ltas me);
	
	@Declared("fon/Ltas.h")
	@Custom
	public double Ltas_convertSpecialToStandardUnit(Ltas me, double value, NativeLong ilevel, int unit);
	
	@Declared("fon/Ltas.h")
	@Custom
	public double Ltas_convertStandardToSpecialUnit(Ltas me, double value, NativeLong ilevel, int unit);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas Matrix_to_Ltas_wrapped(Matrix me);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Matrix Ltas_to_Matrix_wrapped(Ltas me);
	
	// XXX fix collections
//	@Declared("fon/Ltas.h")
//	@Wrapped(autoPtrUnwrap=true)
//	public Ltas Ltases_merge_wrapped(Collection ltases);
	
//	@Declared("fon/Ltas.h")
//	@Wrapped(autoPtrUnwrap=true)
//	public Ltas Ltases_average_wrapped(Collection ltases);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas Ltas_computeTrendLine_wrapped(Ltas me, double fmin, double fmax);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas Ltas_subtractTrendLine_wrapped(Ltas me, double fmin, double fmax);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas Spectrum_to_Ltas_wrapped(Spectrum me, double bandwidth);

	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas Spectrum_to_Ltas_1to1_wrapped (Spectrum me);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas PointProcess_Sound_to_Ltas_wrapped (PointProcess pulses, Sound sound,
		double maximumFrequency, double bandWidth,
		double shortestPeriod, double longestPeriod, double maximumPeriodFactor);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas PointProcess_Sound_to_Ltas_harmonics_wrapped (PointProcess pulses, Sound sound,
		long maximumHarmonic,
		double shortestPeriod, double longestPeriod, double maximumPeriodFactor);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas Sound_to_Ltas_wrapped (Sound me, double bandwidth);
	
	@Declared("fon/Ltas.h")
	@Wrapped(autoPtrUnwrap=true)
	public Ltas Sound_to_Ltas_pitchCorrected_wrapped (Sound sound, double minimumPitch, double maximumPitch,
		double maximumFrequency, double bandWidth,
		double shortestPeriod, double longestPeriod, double maximumPeriodFactor);

	@Declared("fon/Ltas.h")
	public double Ltas_getSlope (Ltas me, double f1min, double f1max, double f2min, double f2max, int averagingUnits);
	
	@Declared("fon/Ltas.h")
	public double Ltas_getLocalPeakHeight (Ltas me, double environmentMin, 
			double environmentMax, double peakMin, double peakMax, int averagingUnits);
	
	@Declared("sys/Interpreter.h")
	@Wrapped(autoPtrUnwrap=true)
	public Interpreter Interpreter_create_wrapped(@NativeType("char32*") Pointer environmentName, ClassInfo editorClass)
		throws PraatException;

}
