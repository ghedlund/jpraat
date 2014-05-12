/* Ltas_to_SpectrumTier.cpp
 *
 * Copyright (C) 2007-2011 Paul Boersma
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

#include "Ltas_to_SpectrumTier.h"

SpectrumTier Ltas_to_SpectrumTier_peaks (Ltas me) {
	try {
		return (SpectrumTier) Vector_to_RealTier_peaks (me, 1, classSpectrumTier);
	} catch (MelderError) {
		Melder_throw (me, ": peaks not analyzed as SpectrumTier.");
	}
}

/* End of file Ltas_to_SpectrumTier.cpp */
