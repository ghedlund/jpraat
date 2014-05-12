/* Pitch_enums.h
 *
 * Copyright (C) 1992-2007,2013 Paul Boersma
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

enums_begin (kPitch_unit, 0)
	enums_add (kPitch_unit, 0, HERTZ, L"Hertz")
	enums_add (kPitch_unit, 1, HERTZ_LOGARITHMIC, L"Hertz (logarithmic)")
	enums_add (kPitch_unit, 2, MEL, L"mel")
	enums_add (kPitch_unit, 3, LOG_HERTZ, L"logHertz")
	enums_add (kPitch_unit, 4, SEMITONES_1, L"semitones re 1 Hz")
	enums_add (kPitch_unit, 5, SEMITONES_100, L"semitones re 100 Hz")
	enums_add (kPitch_unit, 6, SEMITONES_200, L"semitones re 200 Hz")
	enums_add (kPitch_unit, 7, SEMITONES_440, L"semitones re 440 Hz")
	enums_add (kPitch_unit, 8, ERB, L"ERB")
enums_end (kPitch_unit, 8, HERTZ)

/* End of file Pitch_enums.h */
