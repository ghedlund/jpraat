package ca.hedlund.jpraat.binding.fon;

import ca.hedlund.jpraat.binding.Praat;
import ca.hedlund.jpraat.binding.jna.Header;

@Header(value="fon/Pitch.h")
public class Pitch extends Sampled {

	/*
			Function:
				create an empty pitch contour (voiceless), or NULL if out of memory.
			Preconditions:
				tmax > tmin;
				nt >= 1;
				dt > 0.0;
				maxnCandidates >= 2;
			Postconditions:
				my xmin == tmin;
				my xmax == tmax;
				my nx == nt;
				my dx == dt;
				my x1 == t1;
				my ceiling == ceiling;
				my maxnCandidates == maxnCandidates;
				my frame [1..nt]. nCandidates == 1;
				my frame [1..nt]. candidate [1]. frequency == 0.0; // unvoiced
				my frame [1..nt]. candidate [1]. strength == 0.0; // aperiodic
				my frame [1..nt]. intensity == 0.0; // silent
	 */
	public static Pitch create (double tmin, double tmax, long nt, double dt, double t1,
			double ceiling, int maxnCandidates) {
		return Praat.INSTANCE.Pitch_create(tmin, tmax, nt, dt, t1, ceiling, maxnCandidates);
	}
	
	
	
}
