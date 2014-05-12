/* TimeSoundEditor_enums.h
 *
 * Copyright (C) 2012 Paul Boersma
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

enums_begin (kTimeSoundEditor_scalingStrategy, 1)
	enums_add (kTimeSoundEditor_scalingStrategy, 1, BY_WHOLE, L"by whole")
	enums_add (kTimeSoundEditor_scalingStrategy, 2, BY_WINDOW, L"by window")
	enums_add (kTimeSoundEditor_scalingStrategy, 3, BY_WINDOW_AND_CHANNEL, L"by window and channel")
	enums_add (kTimeSoundEditor_scalingStrategy, 4, FIXED_HEIGHT, L"fixed height")
	enums_add (kTimeSoundEditor_scalingStrategy, 5, FIXED_RANGE, L"fixed range")
enums_end (kTimeSoundEditor_scalingStrategy, 5, BY_WINDOW)

/* End of file TimeSoundEditor_enums.h */
