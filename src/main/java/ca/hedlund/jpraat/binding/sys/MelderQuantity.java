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
package ca.hedlund.jpraat.binding.sys;

import ca.hedlund.jpraat.binding.jna.NativeEnum;

public enum MelderQuantity implements NativeEnum {
	NONE("", "", "", ""),
	TIME_SECONDS("Time", "Time (s)", "seconds", "s"),
	FREQUENCY_HERTZ("Frequency", "Frequency (Hz)", "Hertz", "Hz"),
	FREQUENCY_BARK("Frequency", "Frequency (Bark)", "Bark", "Bark"),
	DISTANCE_FROM_GLOTTIS_METRES("Distance from glottis", "Distance from glottis (m)", "metres", "m");

	private final String text;
	
	private final String withUnitText;
	
	private final String longUnitText;
	
	private final String shortUnitText;
	
	private MelderQuantity(String text, String withUnitText, String longUnitText, String shortUnitText) {
		this.text = text;
		this.withUnitText = withUnitText;
		this.longUnitText = longUnitText;
		this.shortUnitText = shortUnitText;
	}
	
	public String getText() {
		return text;
	}

	public String getWithUnitText() {
		return withUnitText;
	}

	public String getLongUnitText() {
		return longUnitText;
	}

	public String getShortUnitText() {
		return shortUnitText;
	}

	@Override
	public int getNativeValue() {
		return ordinal();
	}

}
