/* PitchTier_to_PointProcess.cpp
 *
 * Copyright (C) 1992-2011 Paul Boersma
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 * pb 2002/07/16 GPL
 * pb 2007/08/12 wchar_t
 * pb 2011/06/05
 */

#include "PitchTier_to_PointProcess.h"
#include "Pitch_to_PitchTier.h"

PointProcess PitchTier_to_PointProcess (PitchTier me) {
	try {
		autoPointProcess thee = PointProcess_create (my xmin, my xmax, 1000);
		double area = 0.5;   // imagine an event half a period before the beginning
		long size = my points -> size;
		if (size == 0) return thee.transfer();
		for (long interval = 0; interval <= size; interval ++) {
			double t1 = interval == 0 ? my xmin : ((RealPoint) my points -> item [interval]) -> number;
			Melder_assert (NUMdefined (t1));
			double t2 = interval == size ? my xmax : ((RealPoint) my points -> item [interval + 1]) -> number;
			Melder_assert (NUMdefined (t2));
			double f1 = ((RealPoint) my points -> item [interval == 0 ? 1 : interval]) -> value;
			Melder_assert (NUMdefined (f1));
			double f2 = ((RealPoint) my points -> item [interval == size ? size : interval + 1]) -> value;
			Melder_assert (NUMdefined (f2));
			area += (t2 - t1) * 0.5 * (f1 + f2);
			while (area >= 1.0) {
				double slope = (f2 - f1) / (t2 - t1), discriminant;
				area -= 1.0;
				discriminant = f2 * f2 - 2.0 * area * slope;
				if (discriminant < 0.0) discriminant = 0.0;   // catch rounding errors
				PointProcess_addPoint (thee.peek(), t2 - 2.0 * area / (f2 + sqrt (discriminant)));
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": not converted to PointProcess.");
	}
}

PointProcess PitchTier_Pitch_to_PointProcess (PitchTier me, Pitch vuv) {
	try {
		autoPointProcess fullPoint = PitchTier_to_PointProcess (me);
		autoPointProcess thee = PointProcess_create (my xmin, my xmax, fullPoint -> maxnt);
		/*
		 * Copy only voiced parts to result.
		 */
		for (long i = 1; i <= fullPoint -> nt; i ++) {
			double t = fullPoint -> t [i];
			if (Pitch_isVoiced_t (vuv, t)) {
				PointProcess_addPoint (thee.peek(), t);
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, " & ", vuv, ": not converted to PointProcess.");
	}
}

static int PointProcess_isVoiced_t (PointProcess me, double t, double maxT) {
	long imid = PointProcess_getNearestIndex (me, t);
	if (imid == 0) return 0;
	double tmid = my t [imid];
	int leftVoiced = imid > 1 && tmid - my t [imid - 1] <= maxT;
	int rightVoiced = imid < my nt && my t [imid + 1] - tmid <= maxT;
	if ((leftVoiced && t <= tmid) || (rightVoiced && t >= tmid)) return 1;
	if (leftVoiced && t < 1.5 * tmid - 0.5 * my t [imid - 1]) return 1;
	if (rightVoiced && t > 1.5 * tmid - 0.5 * my t [imid + 1]) return 1;
	return 0;
}

PointProcess PitchTier_Point_to_PointProcess (PitchTier me, PointProcess vuv, double maxT) {
	try {
		autoPointProcess fullPoint = PitchTier_to_PointProcess (me);
		autoPointProcess thee = PointProcess_create (my xmin, my xmax, fullPoint -> maxnt);
		/*
		 * Copy only voiced parts to result.
		 */
		for (long i = 1; i <= fullPoint -> nt; i ++) {
			double t = fullPoint -> t [i];
			if (PointProcess_isVoiced_t (vuv, t, maxT)) {
				PointProcess_addPoint (thee.peek(), t);
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, " & ", vuv, ": not converted to PointProcess.");
	}
}

PitchTier PointProcess_to_PitchTier (PointProcess me, double maximumInterval) {
	try {
		autoPitchTier thee = PitchTier_create (my xmin, my xmax);
		for (long i = 1; i < my nt; i ++) {
			double interval = my t [i + 1] - my t [i];
			if (interval <= maximumInterval) {
				RealTier_addPoint (thee.peek(), my t [i] + 0.5 * interval, 1.0 / interval);
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": not converted to PitchTier.");
	}
}

PitchTier Pitch_PointProcess_to_PitchTier (Pitch me, PointProcess pp) {
	try {
		autoPitchTier temp = Pitch_to_PitchTier (me);
		autoPitchTier thee = PitchTier_PointProcess_to_PitchTier (temp.peek(), pp);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, " & ", pp, ": not converted to PitchTier.");
	}
}

PitchTier PitchTier_PointProcess_to_PitchTier (PitchTier me, PointProcess pp) {
	try {
		if (my points -> size == 0) Melder_throw ("No pitch points.");
		autoPitchTier thee = PitchTier_create (pp -> xmin, pp -> xmax);
		for (long i = 1; i <= pp -> nt; i ++) {
			double time = pp -> t [i];
			double value = RealTier_getValueAtTime (me, time);
			RealTier_addPoint (thee.peek(), time, value);
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, " & ", pp, ": not converted to PitchTier.");
	}
}

TableOfReal PitchTier_downto_TableOfReal (PitchTier me, int useSemitones) {
	try {
		autoTableOfReal thee = RealTier_downto_TableOfReal (me, L"Time", L"F0");
		if (useSemitones)
			for (long i = 1; i <= thy numberOfRows; i ++)
				thy data [i] [2] = NUMhertzToSemitones (thy data [i] [2]);
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": not converted to TableOfReal.");
	}
}

/* End of file PitchTier_to_PointProcess.cpp */
