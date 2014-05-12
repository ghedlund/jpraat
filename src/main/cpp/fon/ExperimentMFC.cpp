/* ExperimentMFC.cpp
 *
 * Copyright (C) 2001-2011,2013 Paul Boersma
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
 * pb 2002/10/31 NUMlog2
 * pb 2003/03/08 inter-stimulus interval; version 2
 * pb 2003/09/14 MelderDir_relativePathToFile
 * pb 2004/06/22 added response keys; version 3
 * pb 2004/08/12 removed a bug (something said carrierBefore instead of carrierAfter)
 *     that caused Praat to crash if the carrier before was longer than the carrier after
 * pb 2005/11/21 added replay button; version 4
 * pb 2005/12/02 response sounds are read
 * pb 2006/10/28 erased MacOS 9 stuff
 * pb 2006/12/20 stereo
 * pb 2007/08/12 wchar_t
 * pb 2007/09/26 added font size; version 5
 * pb 2007/10/01 can write as encoding
 * pb 2008/04/08 in ExtractResults, check that a resonse was given
 * pb 2008/10/20 except nonstandard sound files
 * pb 2009/03/18 removed a bug introduced by the previous change (an error check got hidden)
 * pb 2011/03/03 added reaction times; version 2 of ResultsMFC
 * pb 2011/03/15 allowed result extraction from incomplete experiments
 * pb 2011/03/23 C++
 * pb 2011/07/06 C++
 * pb 2013/01/01 added blank while playing; version 6
 */

#include "ExperimentMFC.h"

#include "oo_DESTROY.h"
#include "ExperimentMFC_def.h"
#include "oo_COPY.h"
#include "ExperimentMFC_def.h"
#include "oo_EQUAL.h"
#include "ExperimentMFC_def.h"
#include "oo_CAN_WRITE_AS_ENCODING.h"
#include "ExperimentMFC_def.h"
#include "oo_READ_TEXT.h"
#include "ExperimentMFC_def.h"
#include "oo_WRITE_TEXT.h"
#include "ExperimentMFC_def.h"
#include "oo_READ_BINARY.h"
#include "ExperimentMFC_def.h"
#include "oo_WRITE_BINARY.h"
#include "ExperimentMFC_def.h"
#include "oo_DESCRIPTION.h"
#include "ExperimentMFC_def.h"

Thing_implement (ExperimentMFC, Data, 6);

#include "enums_getText.h"
#include "Experiment_enums.h"
#include "enums_getValue.h"
#include "Experiment_enums.h"

static void readSound (ExperimentMFC me, const wchar_t *fileNameHead, const wchar_t *fileNameTail,
	double medialSilenceDuration, wchar_t **name, Sound *sound)
{
	wchar_t fileNameBuffer [256], pathName [256], *fileNames = & fileNameBuffer [0];
	structMelderFile file = { 0 };
	wcscpy (fileNameBuffer, *name);
	/*
	 * The following conversion is needed when fileNameHead is an absolute path,
	 * and the stimulus names contain slashes for relative paths.
	 * An ugly case, but allowed.
	 */
	#if defined (_WIN32)
		for (;;) { wchar_t *slash = wcschr (fileNames, '/'); if (! slash) break; *slash = '\\'; }
	#endif
	forget (*sound);
	/*
	 * 'fileNames' can contain commas, which separate partial file names.
	 * The separate files should be concatenated.
	 */
	for (;;) {
		/*
		 * Determine partial file name.
		 */
		wchar_t *comma = wcschr (fileNames, ',');
		if (comma) *comma = '\0';
		/*
		 * Determine complete (relative) file name.
		 */
		swprintf (pathName, 256, L"%ls%ls%ls", fileNameHead, fileNames, fileNameTail);
		/*
		 * Make sure we are in the correct directory.
		 */
		if (MelderDir_isNull (& my rootDirectory)) {
			/*
			 * Absolute file name.
			 */
			Melder_pathToFile (pathName, & file);
		} else {
			/*
			 * Relative or absolute file name.
			 */
			MelderDir_relativePathToFile (& my rootDirectory, pathName, & file);
			if (Melder_debug == 32) {
				MelderInfo_open ();
				MelderInfo_writeLine (L"Path name <", pathName, L">");
				MelderInfo_writeLine (L"Root directory <", my rootDirectory.path, L">");
				MelderInfo_writeLine (L"Full path name <", file.path, L">");
				MelderInfo_close ();
			}
		}
		/*
		 * Read the substimulus.
		 */
		autoSound substimulus = (Sound) Data_readFromFile (& file);   // Sound_readFromSoundFile (& file);
		if (substimulus -> classInfo != classSound)
			Melder_throw ("File ", & file, " contains a ", Thing_className (substimulus.peek()), " instead of a sound.");
		/*
		 * Check whether all sounds have the same number of channels.
		 */
		if (my numberOfChannels == 0) {
			my numberOfChannels = substimulus -> ny;
		} else if (substimulus -> ny != my numberOfChannels) {
			Melder_throw ("The sound in file ", & file, " has a different number of channels than some other sound.");
		}
		/*
		 * Check whether all sounds have the same sampling frequency.
		 */
		if (my samplePeriod == 0.0) {
			my samplePeriod = substimulus -> dx;   /* This must be the first sound read. */
		} else if (substimulus -> dx != my samplePeriod) {
			Melder_throw ("The sound in file ", & file, " has a different sampling frequency than some other sound.");
		}
		/*
		 * Append the substimuli, perhaps with silent intervals.
		 */
		if (*sound == NULL) {
			*sound = substimulus.transfer();
		} else {
			autoSound newStimulus = Sounds_append (*sound, medialSilenceDuration, substimulus.peek());
			Melder_assert (sound == & (*sound));
			forget (*sound);
			*sound = newStimulus.transfer();
		}
		/*
		 * Cycle.
		 */
		if (comma == NULL) break;
		fileNames = & comma [1];
	}
}

static void permuteRandomly (ExperimentMFC me, long first, long last) {
	for (long itrial = first; itrial < last; itrial ++) {
		long jtrial = NUMrandomInteger (itrial, last);
		long dummy = my stimuli [jtrial];
		my stimuli [jtrial] = my stimuli [itrial];
		my stimuli [itrial] = dummy;
	}
}

void ExperimentMFC_start (ExperimentMFC me) {
	try {
		long maximumStimulusPlaySamples, maximumResponsePlaySamples, maximumPlaySamples;
		long stimulusCarrierBeforeSamples = 0, stimulusCarrierAfterSamples = 0, maximumStimulusSamples = 0;
		long responseCarrierBeforeSamples = 0, responseCarrierAfterSamples = 0, maximumResponseSamples = 0;
		Melder_warningOff ();
		my trial = 0;
		NUMvector_free <long> (my stimuli, 1);
		NUMvector_free <long> (my responses, 1);
		NUMvector_free <double> (my goodnesses, 1);
		NUMvector_free <double> (my reactionTimes, 1);
		forget (my playBuffer);
		my pausing = FALSE;
		my numberOfTrials = my numberOfDifferentStimuli * my numberOfReplicationsPerStimulus;
		my stimuli = NUMvector <long> (1, my numberOfTrials);
		my responses = NUMvector <long> (1, my numberOfTrials);
		my goodnesses = NUMvector <double> (1, my numberOfTrials);
		my reactionTimes = NUMvector <double> (1, my numberOfTrials);
		/*
		 * Read all the sounds. They must all have the same sampling frequency and number of channels.
		 */
		my samplePeriod = 0.0;
		my numberOfChannels = 0;
		if (my stimuliAreSounds) {
			if (my stimulusCarrierBefore. name && my stimulusCarrierBefore. name [0]) {
				readSound (me, my stimulusFileNameHead, my stimulusFileNameTail, my stimulusMedialSilenceDuration,
					& my stimulusCarrierBefore. name, & my stimulusCarrierBefore. sound);
				stimulusCarrierBeforeSamples = my stimulusCarrierBefore. sound -> nx;
			}
			if (my stimulusCarrierAfter. name && my stimulusCarrierAfter. name [0]) {
				readSound (me, my stimulusFileNameHead, my stimulusFileNameTail, my stimulusMedialSilenceDuration,
					& my stimulusCarrierAfter. name, & my stimulusCarrierAfter. sound);
				stimulusCarrierAfterSamples = my stimulusCarrierAfter. sound -> nx;
			}
			for (long istim = 1; istim <= my numberOfDifferentStimuli; istim ++) {
				readSound (me, my stimulusFileNameHead, my stimulusFileNameTail, my stimulusMedialSilenceDuration,
					& my stimulus [istim]. name, & my stimulus [istim]. sound);
				if (my stimulus [istim]. sound -> nx > maximumStimulusSamples)
					maximumStimulusSamples = my stimulus [istim]. sound -> nx;
			}
		}
		if (my responsesAreSounds) {
			if (my responseCarrierBefore. name && my responseCarrierBefore. name [0]) {
				readSound (me, my responseFileNameHead, my responseFileNameTail, my responseMedialSilenceDuration,
					& my responseCarrierBefore. name, & my responseCarrierBefore. sound);
				responseCarrierBeforeSamples = my responseCarrierBefore. sound -> nx;
			}
			if (my responseCarrierAfter. name && my responseCarrierAfter. name [0]) {
				readSound (me, my responseFileNameHead, my responseFileNameTail, my responseMedialSilenceDuration,
					& my responseCarrierAfter. name, & my responseCarrierAfter. sound);
				responseCarrierAfterSamples = my responseCarrierAfter. sound -> nx;
			}
			for (long iresp = 1; iresp <= my numberOfDifferentResponses; iresp ++) {
				readSound (me, my responseFileNameHead, my responseFileNameTail, my responseMedialSilenceDuration,
					& my response [iresp]. name, & my response [iresp]. sound);
				if (my response [iresp]. sound -> nx > maximumResponseSamples)
					maximumResponseSamples = my response [iresp]. sound -> nx;
			}
		}
		/*
		 * Create the play buffer.
		 */
		maximumStimulusPlaySamples =
			floor (my stimulusInitialSilenceDuration / my samplePeriod + 0.5)
			+ floor (my stimulusFinalSilenceDuration / my samplePeriod + 0.5)
			+ stimulusCarrierBeforeSamples + maximumStimulusSamples + stimulusCarrierAfterSamples + 2;
		maximumResponsePlaySamples =
			floor (my responseInitialSilenceDuration / my samplePeriod + 0.5)
			+ floor (my responseFinalSilenceDuration / my samplePeriod + 0.5)
			+ responseCarrierBeforeSamples + maximumResponseSamples + responseCarrierAfterSamples + 2;
		maximumPlaySamples = maximumStimulusPlaySamples > maximumResponsePlaySamples ? maximumStimulusPlaySamples : maximumResponsePlaySamples;
		my playBuffer = Sound_create (my numberOfChannels, 0.0, maximumPlaySamples * my samplePeriod,
			maximumPlaySamples, my samplePeriod, 0.5 * my samplePeriod);
		/*
		 * Determine the order in which the stimuli will be presented to the subject.
		 */
		if (my randomize == kExperiment_randomize_CYCLIC_NON_RANDOM) {
			for (long itrial = 1; itrial <= my numberOfTrials; itrial ++)
				my stimuli [itrial] = (itrial - 1) % my numberOfDifferentStimuli + 1;
		} else if (my randomize == kExperiment_randomize_PERMUTE_ALL) {
			for (long itrial = 1; itrial <= my numberOfTrials; itrial ++)
				my stimuli [itrial] = (itrial - 1) % my numberOfDifferentStimuli + 1;
			permuteRandomly (me, 1, my numberOfTrials);
		} else if (my randomize == kExperiment_randomize_PERMUTE_BALANCED) {
			for (long ireplica = 1; ireplica <= my numberOfReplicationsPerStimulus; ireplica ++) {
				long offset = (ireplica - 1) * my numberOfDifferentStimuli;
				for (long istim = 1; istim <= my numberOfDifferentStimuli; istim ++)
					my stimuli [offset + istim] = istim;
				permuteRandomly (me, offset + 1, offset + my numberOfDifferentStimuli);
			}
		} else if (my randomize == kExperiment_randomize_PERMUTE_BALANCED_NO_DOUBLETS) {
			for (long ireplica = 1; ireplica <= my numberOfReplicationsPerStimulus; ireplica ++) {
				long offset = (ireplica - 1) * my numberOfDifferentStimuli;
				for (long istim = 1; istim <= my numberOfDifferentStimuli; istim ++)
					my stimuli [offset + istim] = istim;
				do {
					permuteRandomly (me, offset + 1, offset + my numberOfDifferentStimuli);
				} while (ireplica != 1 && my stimuli [offset + 1] == my stimuli [offset] && my numberOfDifferentStimuli > 1);
			}
		} else if (my randomize == kExperiment_randomize_WITH_REPLACEMENT) {
			for (long itrial = 1; itrial <= my numberOfTrials; itrial ++)
				my stimuli [itrial] = NUMrandomInteger (1, my numberOfDifferentStimuli);
		}
		Melder_warningOn ();
	} catch (MelderError) {
		Melder_warningOn ();
		my numberOfTrials = 0;
		NUMvector_free (my stimuli, 1); my stimuli = NULL;
		Melder_throw (me, ": not started.");
	}
}

static void playSound (ExperimentMFC me, Sound sound, Sound carrierBefore, Sound carrierAfter,
	double initialSilenceDuration, double finalSilenceDuration)
{
	long numberOfSamplesWritten = 0;

	long initialSilenceSamples = floor (initialSilenceDuration / my samplePeriod + 0.5);
	for (long channel = 1; channel <= my numberOfChannels; channel ++) {
		for (long i = 1; i <= initialSilenceSamples; i ++) {
			my playBuffer -> z [channel] [i] = 0.0;
		}
	}
	numberOfSamplesWritten += initialSilenceSamples;

	if (carrierBefore) {
		for (long channel = 1; channel <= my numberOfChannels; channel ++) {
			NUMvector_copyElements <double> (carrierBefore -> z [channel],
				my playBuffer -> z [channel] + numberOfSamplesWritten, 1, carrierBefore -> nx);
		}
		numberOfSamplesWritten += carrierBefore -> nx;
	}

	if (sound) {
		for (long channel = 1; channel <= my numberOfChannels; channel ++) {
			NUMvector_copyElements <double> (sound -> z [channel],
				my playBuffer -> z [channel] + numberOfSamplesWritten, 1, sound -> nx);
		}
		numberOfSamplesWritten += sound -> nx;
	}

	if (carrierAfter) {
		for (long channel = 1; channel <= my numberOfChannels; channel ++) {
			NUMvector_copyElements <double> (carrierAfter -> z [channel],
				my playBuffer -> z [channel] + numberOfSamplesWritten, 1, carrierAfter -> nx);
		}
		numberOfSamplesWritten += carrierAfter -> nx;
	}

	long finalSilenceSamples = floor (finalSilenceDuration / my samplePeriod + 0.5);
	for (long channel = 1; channel <= my numberOfChannels; channel ++) {
		for (long i = 1; i <= finalSilenceSamples; i ++) {
			my playBuffer -> z [channel] [i + numberOfSamplesWritten] = 0.0;
		}
	}
	numberOfSamplesWritten += finalSilenceSamples;

	if (! my blankWhilePlaying)
		my startingTime = Melder_clock ();
	Sound_playPart (my playBuffer, 0.0, numberOfSamplesWritten * my samplePeriod, 0, NULL);
	if (my blankWhilePlaying)
		my startingTime = Melder_clock ();
}

void ExperimentMFC_playStimulus (ExperimentMFC me, long istim) {
	playSound (me, my stimulus [istim]. sound,
		my stimulusCarrierBefore. sound, my stimulusCarrierAfter. sound, my stimulusInitialSilenceDuration, my stimulusFinalSilenceDuration);
}

void ExperimentMFC_playResponse (ExperimentMFC me, long iresp) {
	playSound (me, my response [iresp]. sound,
		my responseCarrierBefore. sound, my responseCarrierAfter. sound, my responseInitialSilenceDuration, my responseFinalSilenceDuration);
}

Thing_implement (ResultsMFC, Data, 2);

ResultsMFC ResultsMFC_create (long numberOfTrials) {
	try {
		autoResultsMFC me = Thing_new (ResultsMFC);
		my numberOfTrials = numberOfTrials;
		my result = NUMvector <structTrialMFC> (1, my numberOfTrials);
		return me.transfer();
	} catch (MelderError) {
		Melder_throw ("ResultsMFC not created.");
	}
}

ResultsMFC ExperimentMFC_extractResults (ExperimentMFC me) {
	try {
		if (my trial == 0 || my trial <= my numberOfTrials)
			Melder_warning ("The experiment was not finished. Only the first ", my trial - 1 + my pausing, " responses are valid.");
		autoResultsMFC thee = ResultsMFC_create (my numberOfTrials);
		for (long trial = 1; trial <= my numberOfTrials; trial ++) {
			wchar_t *pipe = my stimulus [my stimuli [trial]]. visibleText ?
				wcschr (my stimulus [my stimuli [trial]]. visibleText, '|') : NULL;
			if (pipe) {
				long nameLength = wcslen (my stimulus [my stimuli [trial]]. name);
				long buttonTextLength = wcslen (pipe);
				thy result [trial]. stimulus = Melder_calloc (wchar_t, nameLength + buttonTextLength + 1);
				wcscpy (thy result [trial]. stimulus, my stimulus [my stimuli [trial]]. name);
				wcscat (thy result [trial]. stimulus, pipe);
			} else {
				thy result [trial]. stimulus = Melder_wcsdup (my stimulus [my stimuli [trial]]. name);
			}
			//if (my responses [trial] < 1) Melder_throw ("No response for trial ", trial, ".")
			thy result [trial]. response = Melder_wcsdup (my responses [trial] ? my response [my responses [trial]]. name : L"");
			thy result [trial]. goodness = my goodnesses [trial];
			thy result [trial]. reactionTime = my reactionTimes [trial];
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": results not extracted.");
	}
}

ResultsMFC ResultsMFC_removeUnsharedStimuli (ResultsMFC me, ResultsMFC thee) {
	try {
		autoResultsMFC him = ResultsMFC_create (thy numberOfTrials);
		his numberOfTrials = 0;
		for (long i = 1; i <= thy numberOfTrials; i ++) {
			bool present = false;
			for (long j = 1; j <= my numberOfTrials; j ++) {
				if (wcsequ (thy result [i]. stimulus, my result [j]. stimulus)) {
					present = true;
					break;
				}
			}
			if (present) {
				his numberOfTrials ++;
				his result [his numberOfTrials]. stimulus = Melder_wcsdup (thy result [i]. stimulus);
				his result [his numberOfTrials]. response = Melder_wcsdup (thy result [i]. response);
			}
		}
		if (his numberOfTrials == 0)
			Melder_throw ("No shared stimuli.");
		return him.transfer();
	} catch (MelderError) {
		Melder_throw (me, " & ", thee, ": unshared stimuli not removed.");
	}
}

Table ResultsMFCs_to_Table (Collection me) {
	try {
		long irow = 0;
		bool hasGoodnesses = false, hasReactionTimes = false;
		for (long iresults = 1; iresults <= my size; iresults ++) {
			ResultsMFC results = (ResultsMFC) my item [iresults];
			for (long itrial = 1; itrial <= results -> numberOfTrials; itrial ++) {
				irow ++;
				if (results -> result [itrial]. goodness != 0)
					hasGoodnesses = true;
				if (results -> result [itrial]. reactionTime != 0.0)
					hasReactionTimes = true;
			}
		}
		autoTable thee = Table_create (irow, 3 + hasGoodnesses + hasReactionTimes);
		Table_setColumnLabel (thee.peek(), 1, L"subject");
		Table_setColumnLabel (thee.peek(), 2, L"stimulus");
		Table_setColumnLabel (thee.peek(), 3, L"response");
		if (hasGoodnesses)
			Table_setColumnLabel (thee.peek(), 4, L"goodness");
		if (hasReactionTimes)
			Table_setColumnLabel (thee.peek(), 4 + hasGoodnesses, L"reactionTime");
		irow = 0;
		for (long iresults = 1; iresults <= my size; iresults ++) {
			ResultsMFC results = (ResultsMFC) my item [iresults];
			for (long itrial = 1; itrial <= results -> numberOfTrials; itrial ++) {
				irow ++;
				Table_setStringValue (thee.peek(), irow, 1, results -> name);
				Table_setStringValue (thee.peek(), irow, 2, results -> result [itrial]. stimulus);
				Table_setStringValue (thee.peek(), irow, 3, results -> result [itrial]. response);
				if (hasGoodnesses) {
					Table_setNumericValue (thee.peek(), irow, 4, results -> result [itrial]. goodness);
				}
				if (hasReactionTimes) {
					Table_setNumericValue (thee.peek(), irow, 4 + hasGoodnesses, results -> result [itrial]. reactionTime);
				}
			}
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw ("ResultsMFC objects not collected to Table.");
	}
}

Categories ResultsMFC_to_Categories_stimuli (ResultsMFC me) {
	try {
		autoCategories thee = Categories_create ();
		for (long trial = 1; trial <= my numberOfTrials; trial ++) {
			autoSimpleString category = SimpleString_create (my result [trial]. stimulus);
			Collection_addItem (thee.peek(), category.transfer());
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": stimuli not converted to Categories.");
	}
}

Categories ResultsMFC_to_Categories_responses (ResultsMFC me) {
	try {
		autoCategories thee = Categories_create ();
		for (long trial = 1; trial <= my numberOfTrials; trial ++) {
			autoSimpleString category = SimpleString_create (my result [trial]. response);
			Collection_addItem (thee.peek(), category.transfer());
		}
		return thee.transfer();
	} catch (MelderError) {
		Melder_throw (me, ": responses not converted to Categories.");
	}
}

static int compare (SimpleString me, SimpleString thee) {
	return wcscmp (my string, thy string);
}
void Categories_sort (Categories me) {
	NUMsort_p (my size, my item, (int (*) (const void *, const void *)) compare);
}

double Categories_getEntropy (Categories me) {
	long numberOfTokens = 0;
	wchar_t *previousString = NULL;
	double entropy = 0.0;
	autoCategories thee = Data_copy (me);
	Categories_sort (thee.peek());
	for (long i = 1; i <= thy size; i ++) {
		SimpleString s = (SimpleString) thy item [i];
		wchar_t *string = s -> string;
		if (previousString != NULL && ! wcsequ (string, previousString)) {
			double p = (double) numberOfTokens / thy size;
			entropy -= p * NUMlog2 (p);
			numberOfTokens = 1;
		} else {
			numberOfTokens ++;
		}
		previousString = string;
	}
	if (numberOfTokens) {
		double p = (double) numberOfTokens / thy size;
		entropy -= p * NUMlog2 (p);
	}
	return entropy;
}

/* End of file ExperimentMFC.cpp */
