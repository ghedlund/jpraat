package ca.hedlund.jpraat;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.fon.LongSound;
import ca.hedlund.jpraat.binding.fon.Sound;
import ca.hedlund.jpraat.binding.fon.Spectrogram;
import ca.hedlund.jpraat.binding.fon.Vector;
import ca.hedlund.jpraat.binding.fon.kSound_to_Spectrogram_windowShape;
import ca.hedlund.jpraat.binding.sys.MelderFile;

public class Main {
	
	public static void main(String[] args) {
		final String path = "/Users/ghedlund/PhonWorkspace/EnglishDemoCorpus/__res/media/DemoVideo.wav";
		final LongSound longSound = LongSound.open(MelderFile.fromPath(path));
		final Sound sound = longSound.extractPart(7.737, 10.478, 1);
		final Spectrogram spectrogram = sound.toSpectrogram(0.005, 5000.0, 0.002, 20.0, 
				kSound_to_Spectrogram_windowShape.GAUSSIAN, 8.0, 8.0);
		System.out.println("xmin: " + spectrogram.getXMin());
		System.out.println("xmax: " + spectrogram.getXMax());
		System.out.println("dx: " + spectrogram.getDx());
		System.out.println("x1: " + spectrogram.getX1());
		System.out.println("nx: " + spectrogram.getNx());
		System.out.println("ymin: " + spectrogram.getYMin());
		System.out.println("ymax: " + spectrogram.getYMax());
		System.out.println("dy: " + spectrogram.getDy());
		System.out.println("y1: " + spectrogram.getY1());
		System.out.println("ny: " + spectrogram.getNy());
	}

}
