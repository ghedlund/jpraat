/*
 * Copyright (C) 2012-2018 Gregory Hedlund
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

import com.sun.jna.Pointer;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.NativeIntptr_t;

public class SampledXY extends Sampled {
	
	public SampledXY() {
		super();
	}
	
	public SampledXY(Pointer p) {
		super(p);
	}
	
	public double getYMin() {
		return Praat.INSTANCE.SampledXY_getYMin(this);
	}	
	
	public double getYMax() {
		return Praat.INSTANCE.SampledXY_getYMax(this);
	}
	
	public long getNy() {
		return Praat.INSTANCE.SampledXY_getNy(this).longValue();
	}
	
	public double getDy() {
		return Praat.INSTANCE.SampledXY_GetDy(this);
	}
	
	public double getY1() {
		return Praat.INSTANCE.SampledXY_getY1(this);
	}
	
	public double indexToY (long   index) {
		return Praat.INSTANCE.SampledXY_indexToY(this, new NativeIntptr_t(index));
	}
	
	public double yToIndex (double y) {
		return Praat.INSTANCE.SampledXY_yToIndex(this, y);
	}
	
	public long yToLowIndex     (double y) {
		return Praat.INSTANCE.SampledXY_yToLowIndex(this, y).longValue();
	}
	
	public long yToHighIndex    (double y) {
		return Praat.INSTANCE.SampledXY_yToHighIndex(this, y).longValue();
	}
	
	public long yToNearestIndex (double y) {
		return Praat.INSTANCE.SampledXY_yToNearestIndex(this, y).longValue();
	}
	
}
