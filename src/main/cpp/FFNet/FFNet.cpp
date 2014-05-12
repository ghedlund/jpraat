/* FFNet.cpp
 *
 * Copyright (C) 1997-2011 David Weenink
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 djmw 20020712 GPL header
 djmw 20040420 Modified FFNet_create and FFNet_init parameters.
 djmw 20040422 FFNet_drawActivation: nodes with activity > 0.05 had incorrect size.
 djmw 20040422 FFNet_extractWeights added.
 djmw 20040425 FFNet_drawTopology fill input units; increase distance from arrow for output labels
 djmw 20040513 Info changes.
 djmw 20040526 Adapted FFNet_drawCostHistory.
 djmw 20050131 Reversed sign of derivative in minimumCrossEntropy.
 djmw 20060811 Changed %d to %ld in sprintf for longs.
 djmw 20061212 Changed info to Melder_writeLine<x> format.
 djmw 20070902 FFNet_createNameFromTopology to wchar_t
 djmw 20071014 Melder_error<n>
 djmw 20080121 float -> double
 djmw 20110304 Thing_new
*/

#include "FFNet_Matrix.h"
#include "Matrix_extensions.h"
#include "TableOfReal_extensions.h"
#include "Pattern.h"
#include "Collection.h"
#include "Categories.h"

static void bookkeeping (FFNet me);

#include "oo_DESTROY.h"
#include "FFNet_def.h"
#include "oo_COPY.h"
#include "FFNet_def.h"
#include "oo_EQUAL.h"
#include "FFNet_def.h"
#include "oo_CAN_WRITE_AS_ENCODING.h"
#include "FFNet_def.h"
#include "oo_WRITE_TEXT.h"
#include "FFNet_def.h"
#include "oo_WRITE_BINARY.h"
#include "FFNet_def.h"
#include "oo_READ_TEXT.h"
#include "FFNet_def.h"
#include "oo_READ_BINARY.h"
#include "FFNet_def.h"
#include "oo_DESCRIPTION.h"
#include "FFNet_def.h"

Thing_implement (FFNet, Data, 0);

static void FFNet_checkLayerNumber (FFNet me, long layer) {
	if (layer < 1 || layer > my nLayers) {
		if (layer == 0) {
			Melder_throw (L"A Layer number of 0 is not allowed.");
		} else if (layer < 0) {
			Melder_throw (L"A negative layer number is not allowed.");
		} else if (layer > my nLayers) {
			Melder_throw (L"A layer number of ", layer, " is too big.");
		}

		Melder_error_ ("This FFNet has ", layer, " layer", (my nLayers > 1 ? "s\n" : "\n"));
		if (my nLayers == 1) {
			Melder_throw (L"Layer number must be equal to 1.");
		} else if (my nLayers == 2) {
			Melder_throw (L"Layer number must be equal to 1 or 2.");
		} else if (my nLayers == 3) {
			Melder_throw (L"Layer number must be equal to 1, 2 or 3.");
		} else {
			Melder_throw (L"Layer number must be in the range 1 to ", my nLayers);
		}
	}
}

wchar_t *FFNet_createNameFromTopology (FFNet me) {
	MelderString name = { 0 };
	MelderString_copy (&name, Melder_integer (my nUnitsInLayer[0]));
	for (long i = 1; i <= my nLayers; i++) {
		MelderString_appendCharacter (&name, '-');
		MelderString_append (&name, Melder_integer (my nUnitsInLayer[i]));
	}
	return name.string;
}

/****** non-linearities ****************************************************/

static double sigmoid (I, double x, double *deriv) {
	double act = NUMsigmoid (x);
	(void) void_me;
	*deriv = act * (1.0 - act);
	return act;
}


/* ******************* cost functions ****************************************/

/*
	For the errors calculated in the cost functions:
		if target > activity ==> error > 0
		if target < activity ==> error < 0
*/

static double minimumSquaredError (I, const double target[]) {
	iam (FFNet);
	long k = my nNodes - my nOutputs + 1;
	double cost = 0.0;
	for (long i = 1; i <= my nOutputs; i++, k++) {
		double e = my error[k] = target[i] - my activity[k];
		cost += e * e;
	}
	return 0.5 * cost;
}

/* E = - sum (i=1; i=nPatterns; sum (k=1;k=nOutputs; t[k]*ln (o[k]) + (1-t[k])ln (1-o[k]))) */
/* dE/do[k] = -(1-t[k])/ (1-o[k]) + t[k]/o[k] */
/* werkt niet bij (grote?) netten */
static double minimumCrossEntropy (I, const double target[]) {
	iam (FFNet);
	long k = my nNodes - my nOutputs + 1;
	double cost = 0.0;

	for (long i = 1; i <= my nOutputs; i++, k++) {
		double t1 = 1.0 - target[i];
		double o1 = 1.0 - my activity[k];

		cost -= target[i] * log (my activity[k]) + t1 * log (o1);
		my error[k] = -t1 / o1 + target[i] / my activity[k];
	}
	return cost;
}


/* *********************************************************************** */

void bookkeeping (FFNet me) {
	long nWeights = 0;
	my nNodes = my nUnitsInLayer[0];
	for (long i = 1; i <= my nLayers; i++) {
		my nNodes += my nUnitsInLayer[i] + 1;
		nWeights += my nUnitsInLayer[i] * (my nUnitsInLayer[i - 1] + 1);
	}
	if (my nWeights > 0 && my nWeights != nWeights) {
		Melder_throw ("Number of weights is incorret.");
	}
	my nWeights = nWeights;

	// The following test is essential because when an FFNet is read from file the w array already exists
	if (my w == 0) {
		my w = NUMvector<double> (1, my nWeights);
	}
	my activity = NUMvector<double> (1, my nNodes);
	my isbias = NUMvector<long> (1, my nNodes);
	my nodeFirst = NUMvector<long> (1, my nNodes);
	my nodeLast = NUMvector<long> (1, my nNodes);
	my wFirst = NUMvector<long> (1, my nNodes);
	my wLast = NUMvector<long> (1, my nNodes);
	my wSelected = NUMvector<long> (1, my nWeights);
	my error = NUMvector<double> (1, my nNodes);
	my deriv = NUMvector<double> (1, my nNodes);
	my dwi = NUMvector<double> (1, my nWeights);
	my dw = NUMvector<double> (1, my nWeights);
	my nInputs = my nUnitsInLayer[0];
	my nOutputs = my nUnitsInLayer[my nLayers];
	my isbias[my nInputs + 1] = my activity[my nInputs + 1] = 1;

	long n = my nUnitsInLayer[0] + 2;
	long firstNodeInPrevious = 1, lastWeightInPrevious = 0;
	for (long j = 1; j <= my nLayers; j++,  n++) {
		for (long i = 1; i <= my nUnitsInLayer[j]; i++, n++) {
			my isbias[n] = 0;
			my nodeFirst[n] = firstNodeInPrevious;
			my nodeLast[n] = my nodeFirst[n] + my nUnitsInLayer[j - 1];
			my wFirst[n] = lastWeightInPrevious + (i - 1) * (my nUnitsInLayer[j - 1] + 1) + 1;
			my wLast[n] = my wFirst[n] + my nUnitsInLayer[j - 1];
		}
		if (j != my nLayers) {
			my isbias[n] = my activity[n] = 1;
		}
		lastWeightInPrevious = my wLast[n - 1];
		firstNodeInPrevious += my nUnitsInLayer[j - 1] + 1;
	}
	FFNet_selectAllWeights (me);
}

void structFFNet :: v_info () {
	structData :: v_info ();
	MelderInfo_writeLine (L"Number of layers: ", Melder_integer (nLayers));
	MelderInfo_writeLine (L"Total number of units: ", Melder_integer (FFNet_getNumberOfUnits (this)));
	MelderInfo_writeLine (L"   Number of units in layer ", Melder_integer (nLayers), L" (output): ",
	                       Melder_integer (nUnitsInLayer[nLayers]));
	for (long i = nLayers - 1; i >= 1; i--) {
		MelderInfo_writeLine (L"   Number of units in layer ", Melder_integer (i), L" (hidden): ",
		                       Melder_integer (nUnitsInLayer[i]));
	}
	MelderInfo_writeLine (L"   Number of units in layer 0 (input): ", Melder_integer (nUnitsInLayer[0]));
	MelderInfo_writeLine (L"Outputs are linear: ", Melder_boolean (outputsAreLinear));
	MelderInfo_writeLine (L"Number of weights: ", Melder_integer (nWeights), L" (",
	                       Melder_integer (FFNet_dimensionOfSearchSpace (this)), L" selected)");
	MelderInfo_writeLine (L"Number of nodes: ", Melder_integer (nNodes));
}

void FFNet_init (FFNet me, long numberOfInputs, long nodesInLayer1, long nodesInLayer2,
                 long numberOfOutputs, int outputsAreLinear) {
	long numberOfLayers = 3;

	if (numberOfInputs < 1) {
		Melder_throw ("Number of inputs must be a natrural number.");
	}
	if (numberOfOutputs < 1) {
		Melder_throw ("Number of outputs must be a natrural number.");
	}
	if (nodesInLayer1 < 1) {
		numberOfLayers--;
	}
	if (nodesInLayer2 < 1) {
		numberOfLayers--;
	}
	my nLayers = numberOfLayers;
	my nUnitsInLayer = NUMvector<long> (0, numberOfLayers);

	my nUnitsInLayer[numberOfLayers--] = numberOfOutputs;
	if (nodesInLayer2 > 0) {
		my nUnitsInLayer[numberOfLayers--] = nodesInLayer2;
	}
	if (nodesInLayer1 > 0) {
		my nUnitsInLayer[numberOfLayers--] = nodesInLayer1;
	}
	my nUnitsInLayer[numberOfLayers] = numberOfInputs;
	Melder_assert (numberOfLayers == 0);
	my outputsAreLinear = outputsAreLinear;

	bookkeeping (me);

	FFNet_setCostFunction (me, FFNet_COST_MSE);
	FFNet_setNonLinearity (me, FFNet_NONLIN_SIGMOID);
	FFNet_reset (me, 0.1);
}

void FFNet_setOutputCategories (FFNet me, Categories thee) {
	autoCategories uniq = Categories_selectUniqueItems (thee, 1);
	if (uniq -> size == thy size) {
		forget (my outputCategories);
		my outputCategories = uniq.transfer();
	}
}

FFNet FFNet_create (long numberOfInputs, long numberInLayer1, long numberInLayer2,
                    long numberOfOutputs, int outputsAreLinear) {
	try {
		autoFFNet me = Thing_new (FFNet);
		FFNet_init (me.peek(), numberOfInputs, numberInLayer1, numberInLayer2, numberOfOutputs, outputsAreLinear);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("FFNet not created.");
	}
}

void FFNet_setNonLinearity (FFNet me, int nonLinearityType) {
	my nonLinearityType = nonLinearityType;
	my nonLinearity = sigmoid;
	my nlClosure = NULL;
}

void FFNet_setCostFunction (FFNet me, int costType) {
	my costFunctionType = costType;
	if (costType == 2) {
		my costFunction = minimumCrossEntropy;
	} else {
		my costFunction = minimumSquaredError;
	}
	my cfClosure = NULL;
}

double FFNet_getBias (FFNet me, long layer, long unit) {
	try {
		long node = FFNet_getNodeNumberFromUnitNumber (me, unit, layer);
		if (node < 1) {
			Melder_throw ("Not a valid unit / layer combination.");
		}
		long bias_unit = my wLast[node];
		return my w[bias_unit];
	} catch (MelderError) {
		return NUMundefined;
	}
}

void FFNet_setBias (FFNet me, long layer, long unit, double value) {
	long node = FFNet_getNodeNumberFromUnitNumber (me, unit, layer);
	if (node < 1) {
		Melder_throw ("Not a valid unit / layer combination.");
	}
	long bias_unit = my wLast[node]; // ??? +1
	my w[bias_unit] = value;
}

void FFNet_setWeight (FFNet me, long layer, long unit, long unit_from, double value) {
	long node = FFNet_getNodeNumberFromUnitNumber (me, unit, layer);
	if (node < 1) {
		Melder_throw ("Not a valid unit / layer combination.");
	}
	long nodef = FFNet_getNodeNumberFromUnitNumber (me, unit_from, layer - 1);
	if (nodef < 1) {
		Melder_throw ("Not a valid unit / layer combination.");
	}
	long w_unit = my wFirst[node] + unit_from - 1;
	my w[w_unit] = value;
}

double FFNet_getWeight (FFNet me, long layer, long unit, long unit_from) {
	long node = FFNet_getNodeNumberFromUnitNumber (me, unit, layer);
	if (node < 1) {
		Melder_throw ("Not a valid unit / layer combination.");
	}
	long nodef = FFNet_getNodeNumberFromUnitNumber (me, unit_from, layer - 1);
	if (nodef < 1) {
		Melder_throw ("Not a valid unit / layer combination.");
	}
	long w_unit = my wFirst[node] + unit_from - 1;
	return my w[w_unit];
}

void FFNet_reset (FFNet me, double wrange) {
	for (long i = 1; i <= my nWeights; i++)
		if (my wSelected[i]) {
			my w[i] = NUMrandomUniform (-wrange, wrange);
		}
	for (long i = 1; i <= my nNodes; i++) {
		my activity[i] = (my isbias[i] ? 1.0 : 0.0);
	}
	my accumulatedCost = 0.0;
	forget (my minimizer);
}


/***** OPERATION: ***********************************************************/
/* step 1 */
void FFNet_propagate (FFNet me, const double input[], double output[]) {
	// clamp input pattern on the network
	for (long i = 1; i <= my nUnitsInLayer[0]; i++) {
		my activity[i] = input[i];
	}

	// on hidden units use activation function
	long k = 1, nNodes = my outputsAreLinear ? my nNodes - my nOutputs : my nNodes;
	for (long i = my nUnitsInLayer[0] + 2; i <= nNodes; i++) {
		if (my isbias[i]) {
			continue;
		}
		double act = 0.0;
		for (long j = my nodeFirst[i]; j <= my nodeLast[i]; j++, k++) {
			act += my w[k] * my activity[j];
		}
		my activity[i] = my nonLinearity (me, act, & my deriv[i]);
	}
	// on output units use another activation function
	if (my outputsAreLinear) {
		for (long i = nNodes + 1; i <= my nNodes; i++) {
			if (my isbias[i]) {
				continue;
			}
			double act = 0.0;
			for (long j = my nodeFirst[i]; j <= my nodeLast[i]; j++, k++) {
				act += my w[k] * my activity[j];
			}
			my activity[i] = act;
			my deriv[i] = 1.0;
		}
	}
	k = my nNodes - my nOutputs + 1;
	if (output) for (long i = 1; i <= my nOutputs; i++, k++) {
			output[i] = my activity[k];
		}
}


double FFNet_computeError (FFNet me, const double target[]) {
	// compute error at output layer
	double cost = my costFunction (me, target);
	for (long i = 1; i <= my nNodes - my nOutputs; i++) {
		my error[i] = 0.0;
	}
	// backpropagation of errors from output to first hidden layer
	for (long i = my nNodes; i > my nInputs + 1; i--) {
		if (my isbias[i]) {
			continue;
		}
		my error[i] *= my deriv[i];
		if (my nodeFirst[i] > my nInputs + 1) {
			long k = my wFirst[i];
			for (long j = my nodeFirst[i]; j <= my nodeLast[i] - 1; j++, k++) {
				my error[j] += my error[i] * my w[k];
			}
		}
	}
	return cost;
}

void FFNet_computeDerivative (FFNet me) {
	long k = 1;
	for (long i = my nInputs + 2; i <= my nNodes; i++) {
		if (! my isbias[i]) {
			for (long j = my nodeFirst[i]; j <= my nodeLast[i]; j++, k++) {
				my dwi[k] = - my error[i] * my activity[j];
			}
		}
	}
}

/******* end operation ******************************************************/

long FFNet_getWinningUnit (FFNet me, int labeling) {
	long pos = 1, k = my nNodes - my nOutputs;
	if (labeling == 2) { /* stochastic */
		double sum = 0;
		for (long i = 1; i <= my nOutputs; i++) {
			sum += my activity[k + i];
		}
		double random = NUMrandomUniform (0, sum);
		for (pos = my nOutputs; pos >= 2; pos--) if (random > (sum -= my activity[k + pos])) {
				break;
			}
	} else { /* winner-takes-all */
		double max = my activity[k + 1];
		for (long i = 2; i <= my nOutputs; i++) if (my activity[k + i] > max) {
				max = my activity[k + i];
				pos = i;
			}
	}
	return pos;
}

void FFNet_propagateToLayer (FFNet me, const double input[], double activity[], long layer) {
	Melder_assert (activity);
	long k = 0;
	FFNet_propagate (me, input, 0);
	for (long i = 0; i < layer; i++) {
		k += my nUnitsInLayer[i] + 1;
	}
	for (long i = 1; i <= my nUnitsInLayer[layer]; i++) {
		activity[i] = my activity[k + i];
	}
}

void FFNet_selectAllWeights (FFNet me) {
	for (long i = 1; i <= my nWeights; i++) {
		my wSelected[i] = 1;
	}
	my dimension = my nWeights;
}

long FFNet_dimensionOfSearchSpace (FFNet me) {
	long n = 0;
	for (long i = 1; i <= my nWeights; i++) {
		if (my wSelected[i]) {
			n++;
		}
	}
	return n;
}

void FFNet_selectBiasesInLayer (FFNet me, long layer) {
	long node = my nUnitsInLayer[0] + 1;
	if (layer < 1 || layer > my nLayers) {
		return;
	}
	for (long i = 1; i <= my nWeights; i++) {
		my wSelected[i] = 0;
	}
	for (long i = 1; i < layer; i++) {
		node += my nUnitsInLayer[i] + 1;
	}
	for (long i = node + 1; i <= node + my nUnitsInLayer[layer]; i++) {
		my wSelected[my wLast[i]] = 1;
	}
	my dimension = my nUnitsInLayer[layer];
}

void FFNet_weightConnectsUnits (FFNet me, long index, long *fromUnit, long *toUnit, long *layer) {
	Melder_assert (index > 0 && index <= my nWeights);

	long i = 1, np = 0, nw = my nUnitsInLayer[1] * (my nInputs + 1);
	while (index > nw) {
		i++;
		nw += (np = my nUnitsInLayer[i] * (my nUnitsInLayer[i - 1] + 1));
	}
	if (i > 1) {
		index -= nw - np;
	}
	*fromUnit = index % (my nUnitsInLayer[i - 1] + 1);
	*toUnit = (index - 1) / (my nUnitsInLayer[i - 1] + 1) + 1;
	*layer = i;
}

long FFNet_getNodeNumberFromUnitNumber (FFNet me, long unit, long layer) {
	if (layer < 0 || layer > my nLayers || unit > my nUnitsInLayer[layer]) {
		return -1;
	}

	long node = unit;
	for (long i = 0; i < layer; i++) {
		node += my nUnitsInLayer[i] + 1;
	}
	return node;
}

void FFNet_nodeToUnitInLayer (FFNet me, long node, long *unit, long *layer) {
	Melder_assert (node > 0 && node <= my nNodes);

	long i = 0, nn = my nUnitsInLayer[0] + 1;
	while (node > nn) {
		nn += my nUnitsInLayer[++i] + 1;
	}
	if (i > 0) {
		node -= nn - (my nUnitsInLayer[i] + 1);
	}
	*unit = node % (my nUnitsInLayer[i] + 1);
	*layer = i;
}

long FFNet_getNumberOfWeights (FFNet me) {
	return my nWeights;
}

long FFNet_getNumberOfLayers (FFNet me) {
	return my nLayers;
}

long FFNet_getNumberOfUnits (FFNet me) {
	return my nNodes - my nLayers;
}

long FFNet_getNumberOfHiddenLayers (FFNet me) {
	return my nLayers - 1;
}

long FFNet_getNumberOfUnitsInLayer (FFNet me, int layer) {
	if (layer > my nLayers || layer < 0) {
		return 0;
	}
	return my nUnitsInLayer[layer];
}

double FFNet_getMinimum (FFNet me) {
	return my minimizer ? Minimizer_getMinimum (my minimizer) : NUMundefined;
}

void FFNet_drawTopology (FFNet me, Graphics g) {
	long maxNumOfUnits = my nUnitsInLayer[0];
	int dxIsFixed = 1;
	double dy = 1.0 / (my nLayers + 1);

	for (long i = 1; i <= my nLayers; i++) {
		if (my nUnitsInLayer[i] > maxNumOfUnits) {
			maxNumOfUnits = my nUnitsInLayer[i];
		}
	}
	double dx = 1.0 / maxNumOfUnits;
	double radius = dx / 10;
	Graphics_setInner (g);
	Graphics_setWindow (g, 0.0, 1.0, 0.0, 1.0);
	for (long i = 0; i <= my nLayers; i++) {
		double dx2 = dx, x2WC, y2WC = dy / 2 + i * dy;
		double x2 = (maxNumOfUnits - my nUnitsInLayer[i] + 1) * dx2 / 2;
		/* draw the units */
		if (! dxIsFixed) {
			dx2 = 1.0 / my nUnitsInLayer[i];
			x2 = dx2 / 2;
		}
		if (i == 0) {
			Graphics_setTextAlignment (g, Graphics_CENTRE, Graphics_TOP);
			x2WC = x2;
			for (long j = 1; j <= my nInputs; j++) {
				Graphics_arrow (g, x2WC, y2WC - radius - dy / 4, x2WC, y2WC - radius);
				x2WC += dx2;
			}
		}
		Graphics_setColour (g, Graphics_RED);
		x2WC = x2;
		for (long j = 1; j <= my nUnitsInLayer[i]; j++) {
			Graphics_circle (g, x2WC, y2WC, radius);
			if (i > 0) {
				Graphics_fillCircle (g, x2WC, y2WC, radius);
			}
			x2WC += dx2;
		}
		Graphics_setColour (g, Graphics_BLACK);
		if (i > 0) {
			double dx1 = dx;
			double x1 = (maxNumOfUnits - my nUnitsInLayer[i - 1] + 1) * dx1 / 2;
			double y1WC = y2WC - dy;
			if (! dxIsFixed) {
				dx1 = 1.0 / my nUnitsInLayer[i - 1];
				x1 = dx1 / 2;
			}
			x2WC = x2;
			for (long j = 1; j <= my nUnitsInLayer[i]; j++) {
				double x1WC = x1;
				for (long k = 1; k <= my nUnitsInLayer[i - 1]; k++) {
					double xd = x2WC - x1WC;
					double cosa = xd / sqrt (xd * xd + dy * dy);
					double sina = dy / sqrt (xd * xd + dy * dy);
					Graphics_line (g, x1WC + radius * cosa, y1WC + radius * sina, x2WC - radius * cosa, y2WC - radius * sina);
					x1WC += dx1;
				}
				x2WC += dx2;
			}
		}
		if (i == my nLayers) {
			x2WC = x2;
			Graphics_setTextAlignment (g, Graphics_CENTRE, Graphics_BOTTOM);
			for (long j = 1; j <= my nOutputs; j++) {
				Graphics_arrow (g, x2WC, y2WC + radius, x2WC, y2WC + radius + dy / 4);
				if (my outputCategories) {
					Categories_drawItem (my outputCategories, g, j, x2WC, y2WC + radius + dy / 4);
				}
				x2WC += dx2;
			}
		}
	}
	Graphics_unsetInner (g);
}

void FFNet_drawActivation (FFNet me, Graphics g) {
	long node = 1, maxNumOfUnits = my nUnitsInLayer[0];
	int dxIsFixed = 1;
	Graphics_Colour colour = Graphics_inqColour (g);
	double dy = 1.0 / (my nLayers + 1);

	Graphics_setInner (g);
	Graphics_setWindow (g, 0.0, 1.0, 0.0, 1.0);
	for (long i = 1; i <= my nLayers; i++) {
		if (my nUnitsInLayer[i] > maxNumOfUnits) {
			maxNumOfUnits = my nUnitsInLayer[i];
		}
	}
	double dx = 1.0 / maxNumOfUnits;
	double r1 = dx / 2; /* May touch when neighbouring activities are both 1 (very rare). */
	for (long i = 0; i <= my nLayers; i++, node++) {
		double dx2 = dx, x2WC, y2WC = dy / 2 + i * dy;
		double x2 = (maxNumOfUnits - my nUnitsInLayer[i] + 1) * dx2 / 2;
		if (! dxIsFixed) {
			dx2 = 1.0 / my nUnitsInLayer[i];
			x2 = dx2 / 2;
		}
		x2WC = x2;
		for (long j = 1; j <= my nUnitsInLayer[i]; j++, node++) {
			double activity = my activity[node];
			double radius = r1 * (fabs (activity) < 0.05 ? 0.05 : fabs (activity));
			/*Graphics_setColour (g, activity < 0 ? Graphics_BLACK : Graphics_RED);*/
			Graphics_circle (g, x2WC, y2WC, radius);
			if (activity < 0) {
				Graphics_fillCircle (g, x2WC, y2WC, radius);
			}
			x2WC += dx2;
		}
	}
	Graphics_setColour (g, colour);
	Graphics_unsetInner (g);
}

/* This routine is deprecated since praat-4.2.4 20040422 and will be removed in the future. */
void FFNet_drawWeightsToLayer (FFNet me, Graphics g, int layer, int scaling, int garnish) {
	if (layer < 1 || layer > my nLayers) {
		Melder_throw ("Layer must be in [1,", my nLayers, "].");
	}
	autoMatrix weights = FFNet_weightsToMatrix (me, layer, 0);
	Matrix_scale (weights.peek(), scaling);
	Matrix_drawAsSquares (weights.peek(), g, 0, 0, 0, 0, 0);
	if (garnish) {
		double x1WC, x2WC, y1WC, y2WC; wchar_t text[30];
		Graphics_inqWindow (g, & x1WC, & x2WC, & y1WC, & y2WC);
		swprintf (text, 30, L"Units in layer %ld ->", layer);
		Graphics_textBottom (g, 0, text);
		if (layer == 1) {
			wcscpy (text, L"Input units ->");
		} else {
			swprintf (text, 30, L"Units in layer %ld ->", layer - 1);
		}
		Graphics_textLeft (g, 0, text);
		/* how do I find out the current settings ??? */
		Graphics_setTextAlignment (g, Graphics_RIGHT, Graphics_HALF);
		Graphics_setInner (g);
		Graphics_text (g, 0.5, weights->ny, L"bias");
		Graphics_unsetInner (g);
	}
}

void FFNet_drawWeights (FFNet me, Graphics g, long layer, int garnish) {
	autoTableOfReal thee = FFNet_extractWeights (me, layer);
	TableOfReal_drawAsSquares (thee.peek(), g, 1, thy numberOfRows, 1, thy numberOfColumns, garnish);
}

void FFNet_drawCostHistory (FFNet me, Graphics g, long iFrom, long iTo, double costMin, double costMax, int garnish) {
	if (my minimizer) Minimizer_drawHistory (my minimizer, g, iFrom, iTo,
		        costMin, costMax, 0);
	if (garnish) {
		Graphics_drawInnerBox (g);
		Graphics_textLeft (g, 1, my costFunctionType == FFNet_COST_MSE ?
		                   L"Minimum squared error" : L"Minimum cross entropy");
		Graphics_marksLeft (g, 2, 1, 1, 0);
		Graphics_textBottom (g, 1, L"Number of epochs");
		Graphics_marksBottom (g, 2, 1, 1, 0);
	}
}

Collection FFNet_createIrisExample (long numberOfHidden1, long numberOfHidden2) {
	try {
		autoCollection c = Collection_create (classData, 3);
		autoCategories uniq = Categories_sequentialNumbers (3);
		autoFFNet me = FFNet_create (4, numberOfHidden1, numberOfHidden2, 3, 0);
		FFNet_setOutputCategories (me.peek(), uniq.peek());
		autostring name = FFNet_createNameFromTopology (me.peek());
		Thing_setName (me.peek(), name.peek());
		Collection_addItem (c.peek(), me.transfer());
		autoTableOfReal iris = TableOfReal_createIrisDataset ();

		// Scale data to interval [0-1]

		for (long i = 1; i <= 150; i++) {
			for (long j = 1; j <= 4; j++) {
				iris -> data[i][j] /= 10.0;
			}
		}

		Pattern thee = 0; Categories him = 0;
		TableOfReal_to_Pattern_and_Categories (iris.peek(), 0, 0, 0, 0, &thee, &him);
		autoPattern ap = thee; autoCategories ac = him;
		Thing_setName (ap.peek(), L"iris");
		Thing_setName (ac.peek(), L"iris");
		Collection_addItem (c.peek(), ap.transfer());
		Collection_addItem (c.peek(), ac.transfer());
		return c.transfer();
	} catch (MelderError) {
		Melder_throw ("Iris example not created.");
	}
}

TableOfReal FFNet_extractWeights (FFNet me, long layer) {
	try {
		FFNet_checkLayerNumber (me, layer);

		long numberOfUnitsFrom = my nUnitsInLayer[layer - 1] + 1;
		long numberOfUnitsTo = my nUnitsInLayer[layer];
		autoTableOfReal thee = TableOfReal_create (numberOfUnitsFrom, numberOfUnitsTo);

		wchar_t label[20];
		for (long i = 1; i <= numberOfUnitsFrom - 1; i++) {
			swprintf (label, 20, L"L%ld-%ld", layer - 1, i);
			TableOfReal_setRowLabel (thee.peek(), i, label);
		}
		TableOfReal_setRowLabel (thee.peek(), numberOfUnitsFrom, L"Bias");
		for (long i = 1; i <= numberOfUnitsTo; i++) {
			swprintf (label, 20, L"L%ld-%ld", layer, i);
			TableOfReal_setColumnLabel (thee.peek(), i, label);
		}

		long node = 1;
		for (long i = 0; i < layer; i++) {
			node += my nUnitsInLayer[i] + 1;
		}
		for (long i = 1; i <= numberOfUnitsTo; i++, node++) {
			long k = 1;
			for (long j = my wFirst[node]; j <= my wLast[node]; j++) {
				thy data[k++][i] = my w[j];
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no TableOfReal created.");
	}
}

FFNet FFNet_and_TabelOfReal_to_FFNet (FFNet me, TableOfReal him, long layer) {
	try {
		FFNet_checkLayerNumber (me, layer);
		if ( (my nUnitsInLayer[layer] != his numberOfColumns) ||
		        (my nUnitsInLayer[layer] == his numberOfColumns && my nUnitsInLayer[layer - 1] + 1 == his numberOfRows)) {
			long trys[3], rows[3], cols[3], ntry = my nLayers > 3 ? 3 : my nLayers, ok = 0;
			if (my nLayers > 3) {
				Melder_throw ("Dimensions don't fit.");
			}
			for (long i = 1; i <= ntry; i++) {
				cols[i] = my nUnitsInLayer[i] == his numberOfColumns;
				rows[i] = my nUnitsInLayer[i - 1] + 1 == his numberOfRows;
				trys[i] = rows[i] && cols[i];
				if (trys[i]) {
					ok ++;
				}
			}
			if (! rows[layer]) Melder_throw ("The number of rows in the TableOfReal does not equal \n"
				                                 "the number of units in the layer that connect to layer ", layer, L".");
			else
				Melder_throw ("The number of columns in the TableOfReal does not equal \n"
				              "the number of units in layer ", layer, L".");
			if (ok == 0) {
				Melder_throw ("Please quit, there is no appropriate layer in the FFNet for this TableOfReal.");
			} else {
				if (ok == 1)
					Melder_throw ("Please try again with layer number ",
					              trys[1] ? trys[1] : (trys[2] ? trys[2] : trys[3]), L".");
				else {
					Melder_throw ("Please try again with one of the other two layer numbers.");
				}
			}
		}
		autoFFNet thee = Data_copy (me);
		long node = 1;
		for (long i = 0; i < layer; i++) {
			node += thy nUnitsInLayer[i] + 1;
		}
		for (long i = 1; i <= thy nUnitsInLayer[layer]; i++, node++) {
			long k = 1;
			for (long j = thy wFirst[node]; j <= thy wLast[node]; j++, k++) {
				thy w[j] = his data[k][i];
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": no FFNet created.");
	}
}

/* End of file FFNet.cpp */
