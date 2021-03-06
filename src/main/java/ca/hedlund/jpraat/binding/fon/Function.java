/*
 * Copyright (C) 2012-2020 Gregory Hedlund
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
package ca.hedlund.jpraat.binding.fon;

import com.sun.jna.*;

import ca.hedlund.jpraat.annotations.*;
import ca.hedlund.jpraat.binding.*;
import ca.hedlund.jpraat.binding.jna.*;
import ca.hedlund.jpraat.binding.sys.*;


/**
	An object of type Function represents a function f (x, ...) on the domain [xmin, xmax] * ....
	Class invariants:
		xmax >= xmin;
		xmin, xmax are constant;
*/
@Header("fon/Function.h")
public class Function extends Daata {
	
	public final static int UNIT_TEXT_SHORT = 0x00000001;
	public final static int UNIT_TEXT_GRAPHICAL = 0x00000002;
	public final static int UNIT_TEXT_MENU = 0x00000004;
	
	/* The domain of a function can be changed by windowing. */
	/* Here follow some window functions. */
	public final static int RECTANGULAR  = 0;
	public final static int TRIANGULAR = 1;
	public final static int PARABOLIC = 2;
	public final static int HANNING = 3;
	public final static int HAMMING = 4;
	public final static int POTTER = 5;
	public final static int KAISER12 = 6;
	public final static int KAISER20 = 7;
	public final static int GAUSSIAN = 8;

	public Function() {
		super();
	}
	
	public Function(Pointer p) {
		super(p);
	}
	
	/**
		Preconditions:
			xmin <= xmax;
		Postconditions:
			result -> xmin == xmin;
			result -> xmax == xmax;
	 */
	public void init (double xmin, double xmax) {
		Praat.INSTANCE.Function_init(this, xmin, xmax);
	}
	
	public MelderQuantity getDomainQuantity () {
		return Praat.INSTANCE.Function_getDomainQuantity();
	}
	
	/*
	 * A function value is often expressed in some unit, such as:
	 * Pa, Hz, dB, sones.
	 * In the following, 'ilevel' is for multidimensional functions; it could be the row number of a matrix,
	 * or pitch height (Hz) vs. pitch strength (dimensionless), and so on.
	 * 'unit' is enumerated type that has to be defined in the header files of the descendant class,
	 * starting from 0, which should be the default unit; e.g. for pitch: 0 = Hz, 1 = logHz, 2 = semitones, 3 = mel.
	 */
	public int getMinimumUnit (long ilevel) {
		return Praat.INSTANCE.Function_getMinimumUnit(new NativeIntptr_t(ilevel));
	}
	
	public int getMaximumUnit (long ilevel) {
		return Praat.INSTANCE.Function_getMaximumUnit(new NativeIntptr_t(ilevel));
	}
	
	public String getUnitText (long ilevel, int unit, int flags) {
		Str32 txt32 = Praat.INSTANCE.Function_getUnitText(this, new NativeIntptr_t(ilevel), unit, new NativeUint32(flags));
		return (txt32 != null ? txt32.toString() : null);
	}
	
	public boolean isUnitLogarithmic (long ilevel, int unit) {
		return Praat.INSTANCE.Function_isUnitLogarithmic(this, new NativeIntptr_t(ilevel), unit);
	}
	
	public double convertStandardToSpecialUnit (double value, long ilevel, int unit) {
		return Praat.INSTANCE.Function_convertStandardToSpecialUnit(this, value, new NativeIntptr_t(ilevel), unit);
	}
	
	public double convertSpecialToStandardUnit (double value, long ilevel, int unit) {
		return Praat.INSTANCE.Function_convertSpecialToStandardUnit(this, value, new NativeIntptr_t(ilevel), unit);
	}
	
	public double convertToNonlogarithmic (double value, long ilevel, int unit) {
		return Praat.INSTANCE.Function_convertToNonlogarithmic(this, value, new NativeIntptr_t(ilevel), unit);
	}
	
	/**
		Return value:
			a number between 0 and 1, zero outside the "domain":
			domain [-0.5, 0.5]: rectangular, triangular, parabolic, hanning, hamming;
			domain [-0.77, 0.77]: potter, kaiser12;
			domain [-1, 1]: kaiser20;
			domain [-inf, +inf]: gaussian.
			Rectangular: 1;
			Triangular ("Parzen"): 1 - tim / 0.5
			Parabolic ("Welch"): 1 - (tim / 0.5) ^ 2
			Hanning (raised cosine): 0.5 + 0.5 * cos (2 * pi * tim)
			Hamming (raised cosine): 0.54 + 0.46 * cos (2 * pi * tim)
			Gaussian: exp (- (pi * tim) ^ 2)
			Kaiser12: besselI0 (12 * sqrt (1 - (tim / 0.77) ^ 2)) / besselI0 (12)
			Kaiser20: besselI0 (20.24 * sqrt (1 - tim ^ 2)) / besselI0 (20.24)
		Usage:
			the preferred window for almost every application is Kaiser20;
			its only shortcoming is the usual double computation time.
		Properties:
			Highest sidelobe:
				Rectangular: -10 dB
				Hanning: -30 dB
				Hamming: -40 dB
				Potter, Kaiser12: -90 dB
				Kaiser20: -180 dB
				Gaussian: -inf dB
			Height at 'tim' is +0.5 or -0.5 (limit from below):
				Rectangular: 1
				Triangular, Parabolic, Hanning: 0
				Hamming: 0.08
				Gaussian: 0.085
				Kaiser20: 0.0715
				Potter: ...
			Area:
				Rectangular: 1
				Parabolic: 2/3
				Gaussian: 1 / sqrt (pi)
				Kaiser20: 0.9813318115591122859 / sqrt (pi)
				Hamming: 0.54
				Triangular, Hanning: 1/2
			Bandwidth (-20 dB):
				...
	 */
	public static double window (double tim, int windowType) {
		return Praat.INSTANCE.Function_window(tim, windowType);
	}
	
	public void unidirectionalAutowindow (Pointer xmin, Pointer xmax) {
		Praat.INSTANCE.Function_unidirectionalAutowindow(this, xmin, xmax);
	}
	
	public void bidirectionalAutowindow (Pointer x1, Pointer x2) {
		Praat.INSTANCE.Function_bidirectionalAutowindow(this, x1, x2);
	}
	
	public boolean intersectRangeWithDomain (Pointer x1, Pointer x2) {
		return Praat.INSTANCE.Function_intersectRangeWithDomain(this, x1, x2);
	}

	public void shiftXBy (double shift) {
		Praat.INSTANCE.Function_shiftXBy(this, shift);
	}
	
	public void shiftXTo (double xfrom, double xto) {
		Praat.INSTANCE.Function_shiftXTo(this, xfrom, xto);
	}
	
	public void scaleXBy (double factor) {
		Praat.INSTANCE.Function_scaleXBy(this, factor);
	}
	
	public void scaleXTo (double xminto, double xmaxto) {
		Praat.INSTANCE.Function_scaleXTo(this, xminto, xmaxto);
	}

	public double getXmin () {
		return Praat.INSTANCE.Function_getXmin(this);
	}

	public double getXmax () {
		return Praat.INSTANCE.Function_getXmax(this);
	}
	
	public MelderQuantity domainQuantity () {
		return Praat.INSTANCE.Function_domainQuantity(this);
	}
	
	public void shiftX ( double xfrom, double xto) {
		Praat.INSTANCE.Function_shiftX(this, xfrom, xto);
	}
	
	public void  scaleX (double xminfrom, double xmaxfrom, double xminto, double xmaxto) {
		Praat.INSTANCE.Function_scaleX(this, xminfrom, xmaxfrom, xminto, xmaxto);
	}
	
}
