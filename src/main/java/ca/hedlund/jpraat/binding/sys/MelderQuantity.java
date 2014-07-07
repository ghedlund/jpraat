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
