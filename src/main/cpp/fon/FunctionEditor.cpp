/* FunctionEditor.cpp
 *
 * Copyright (C) 1992-2011,2012,2013 Paul Boersma
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

#include "FunctionEditor.h"
#include "machine.h"
#include "EditorM.h"
#include "GuiP.h"

Thing_implement (FunctionEditor, Editor, 0);

#define maximumScrollBarValue  2000000000
#define RELATIVE_PAGE_INCREMENT  0.8
#define SCROLL_INCREMENT_FRACTION  20
#define space 30
#define MARGIN 107
#define BOTTOM_MARGIN  2
#define TOP_MARGIN  3
#define TEXT_HEIGHT  50
#define BUTTON_X  3
#define BUTTON_WIDTH  40
#define BUTTON_SPACING  8

#include "prefs_define.h"
#include "FunctionEditor_prefs.h"
#include "prefs_install.h"
#include "FunctionEditor_prefs.h"
#include "prefs_copyToInstance.h"
#include "FunctionEditor_prefs.h"

#define maxGroup 100
static int nGroup = 0;
static FunctionEditor theGroup [1 + maxGroup];

static void drawWhileDragging (FunctionEditor me, double x1, double x2);

static int group_equalDomain (double tmin, double tmax) {
	if (nGroup == 0) return 1;
	for (int i = 1; i <= maxGroup; i ++)
		if (theGroup [i])
			return tmin == theGroup [i] -> d_tmin && tmax == theGroup [i] -> d_tmax;
	return 0;   // should not occur
}

static void updateScrollBar (FunctionEditor me) {
/* We cannot call this immediately after creation. */
	int slider_size = (my d_endWindow - my d_startWindow) / (my d_tmax - my d_tmin) * maximumScrollBarValue - 1;
	int increment, page_increment;
	int value = (my d_startWindow - my d_tmin) / (my d_tmax - my d_tmin) * maximumScrollBarValue + 1;
	if (slider_size < 1) slider_size = 1;
	if (value > maximumScrollBarValue - slider_size)
		value = maximumScrollBarValue - slider_size;
	if (value < 1) value = 1;
	increment = slider_size / SCROLL_INCREMENT_FRACTION + 1;
	page_increment = RELATIVE_PAGE_INCREMENT * slider_size + 1;
	my scrollBar -> f_set (NUMundefined, maximumScrollBarValue, value, slider_size, increment, page_increment);
}

static void updateGroup (FunctionEditor me) {
	if (! my group) return;
	for (int i = 1; i <= maxGroup; i ++) if (theGroup [i] && theGroup [i] != me) {
		FunctionEditor thee = theGroup [i];
		if (my pref_synchronizedZoomAndScroll ()) {
			thy d_startWindow = my d_startWindow;
			thy d_endWindow = my d_endWindow;
		}
		thy d_startSelection = my d_startSelection;
		thy d_endSelection = my d_endSelection;
		FunctionEditor_updateText (thee);
		updateScrollBar (thee);
		Graphics_updateWs (thy d_graphics);
	}
}

static void drawNow (FunctionEditor me) {
	int leftFromWindow = my d_startWindow > my d_tmin;
	int rightFromWindow = my d_endWindow < my d_tmax;
	int cursorVisible = my d_startSelection == my d_endSelection && my d_startSelection >= my d_startWindow && my d_startSelection <= my d_endWindow;
	int selection = my d_endSelection > my d_startSelection;
	int beginVisible, endVisible;
	double verticalCorrection;
	wchar_t text [100];

	/* Update selection. */

	beginVisible = my d_startSelection > my d_startWindow && my d_startSelection < my d_endWindow;
	endVisible = my d_endSelection > my d_startWindow && my d_endSelection < my d_endWindow;

	/* Update markers. */

	my numberOfMarkers = 0;
	if (beginVisible)
		my marker [++ my numberOfMarkers] = my d_startSelection;
	if (endVisible && my d_endSelection != my d_startSelection)
		my marker [++ my numberOfMarkers] = my d_endSelection;
	my marker [++ my numberOfMarkers] = my d_endWindow;
	NUMsort_d (my numberOfMarkers, my marker);

	/* Update rectangles. */

	for (int i = 0; i < 8; i++)
		my rect [i]. left = my rect [i]. right = 0;

	/* 0: rectangle for total. */

	my rect [0]. left = my functionViewerLeft + ( leftFromWindow ? 0 : MARGIN );
	my rect [0]. right = my functionViewerRight - ( rightFromWindow ? 0 : MARGIN );
	my rect [0]. bottom = BOTTOM_MARGIN;
	my rect [0]. top = BOTTOM_MARGIN + space;

	/* 1: rectangle for visible part. */

	my rect [1]. left = my functionViewerLeft + MARGIN;
	my rect [1]. right = my functionViewerRight - MARGIN;
	my rect [1]. bottom = BOTTOM_MARGIN + space;
	my rect [1]. top = BOTTOM_MARGIN + space * (my numberOfMarkers > 1 ? 2 : 3);

	/* 2: rectangle for left from visible part. */

	if (leftFromWindow) {
		my rect [2]. left = my functionViewerLeft;
		my rect [2]. right = my functionViewerLeft + MARGIN;
		my rect [2]. bottom = BOTTOM_MARGIN + space;
		my rect [2]. top = BOTTOM_MARGIN + space * 2;
	}

	/* 3: rectangle for right from visible part. */

	if (rightFromWindow) {
		my rect [3]. left = my functionViewerRight - MARGIN;
		my rect [3]. right = my functionViewerRight;
		my rect [3]. bottom = BOTTOM_MARGIN + space;
		my rect [3]. top = BOTTOM_MARGIN + space * 2;
	}

	/* 4, 5, 6: rectangles between markers visible in visible part. */

	if (my numberOfMarkers > 1) {
		double window = my d_endWindow - my d_startWindow;
		for (int i = 1; i <= my numberOfMarkers; i ++) {
			my rect [3 + i]. left = i == 1 ? my functionViewerLeft + MARGIN : my functionViewerLeft + MARGIN + (my functionViewerRight - my functionViewerLeft - MARGIN * 2) *
				(my marker [i - 1] - my d_startWindow) / window;
			my rect [3 + i]. right = my functionViewerLeft + MARGIN + (my functionViewerRight - my functionViewerLeft - MARGIN * 2) *
				(my marker [i] - my d_startWindow) / window;
			my rect [3 + i]. bottom = BOTTOM_MARGIN + space * 2;
			my rect [3 + i]. top = BOTTOM_MARGIN + space * 3;
		}
	}
	
	if (selection) {
		double window = my d_endWindow - my d_startWindow;
		double left =
			my d_startSelection == my d_startWindow ? my functionViewerLeft + MARGIN :
			my d_startSelection == my d_tmin ? my functionViewerLeft :
			my d_startSelection < my d_startWindow ? my functionViewerLeft + MARGIN * 0.3 :
			my d_startSelection < my d_endWindow ? my functionViewerLeft + MARGIN + (my functionViewerRight - my functionViewerLeft - MARGIN * 2) * (my d_startSelection - my d_startWindow) / window :
			my d_startSelection == my d_endWindow ? my functionViewerRight - MARGIN : my functionViewerRight - MARGIN * 0.7;
		double right =
			my d_endSelection < my d_startWindow ? my functionViewerLeft + MARGIN * 0.7 :
			my d_endSelection == my d_startWindow ? my functionViewerLeft + MARGIN :
			my d_endSelection < my d_endWindow ? my functionViewerLeft + MARGIN + (my functionViewerRight - my functionViewerLeft - MARGIN * 2) * (my d_endSelection - my d_startWindow) / window :
			my d_endSelection == my d_endWindow ? my functionViewerRight - MARGIN :
			my d_endSelection < my d_tmax ? my functionViewerRight - MARGIN * 0.3 : my functionViewerRight;
		my rect [7]. left = left;
		my rect [7]. right = right;
		my rect [7]. bottom = my height - space - TOP_MARGIN;
		my rect [7]. top = my height - TOP_MARGIN;
	}

	/*
	 * Be responsive: update the markers now.
	 */
	Graphics_setViewport (my d_graphics, my functionViewerLeft, my functionViewerRight, 0, my height);
	Graphics_setWindow (my d_graphics, my functionViewerLeft, my functionViewerRight, 0, my height);
	Graphics_Colour windowBackgroundColour = { 0.90, 0.90, 0.85 } ;
	Graphics_setColour (my d_graphics, windowBackgroundColour);
	Graphics_fillRectangle (my d_graphics, my functionViewerLeft + MARGIN, my selectionViewerRight - MARGIN, my height - (TOP_MARGIN + space), my height);
	Graphics_fillRectangle (my d_graphics, my functionViewerLeft, my functionViewerLeft + MARGIN, BOTTOM_MARGIN + ( leftFromWindow ? space * 2 : 0 ), my height);
	Graphics_fillRectangle (my d_graphics, my functionViewerRight - MARGIN, my functionViewerRight, BOTTOM_MARGIN + ( rightFromWindow ? space * 2 : 0 ), my height);
	Graphics_setViewport (my d_graphics, my selectionViewerLeft, my selectionViewerRight, 0, my height);
	Graphics_setWindow (my d_graphics, my selectionViewerLeft, my selectionViewerRight, 0, my height);
	Graphics_fillRectangle (my d_graphics, my selectionViewerLeft, my selectionViewerLeft + MARGIN, BOTTOM_MARGIN, my height);
	Graphics_fillRectangle (my d_graphics, my selectionViewerRight - MARGIN, my selectionViewerRight, BOTTOM_MARGIN, my height);
	Graphics_fillRectangle (my d_graphics, my selectionViewerLeft + MARGIN, my selectionViewerRight - MARGIN, 0, BOTTOM_MARGIN + space * 3);
	Graphics_setGrey (my d_graphics, 0.0);
	#if defined (macintosh)
		Graphics_line (my d_graphics, my functionViewerLeft, 2, my selectionViewerRight, 2);
		Graphics_line (my d_graphics, my functionViewerLeft, my height - 2, my selectionViewerRight, my height - 2);
	#endif

	Graphics_setViewport (my d_graphics, my functionViewerLeft, my functionViewerRight, 0, my height);
	Graphics_setWindow (my d_graphics, my functionViewerLeft, my functionViewerRight, 0, my height);
	Graphics_setTextAlignment (my d_graphics, Graphics_CENTRE, Graphics_HALF);
	for (int i = 0; i < 8; i ++) {
		double left = my rect [i]. left, right = my rect [i]. right;
		if (left < right)
			Graphics_button (my d_graphics, left, right, my rect [i]. bottom, my rect [i]. top);
	}
	verticalCorrection = my height / (my height - 111 + 11.0);
	#ifdef _WIN32
		verticalCorrection *= 1.5;
	#endif
	for (int i = 0; i < 8; i ++) {
		double left = my rect [i]. left, right = my rect [i]. right;
		double bottom = my rect [i]. bottom, top = my rect [i]. top;
		if (left < right) {
			const wchar_t *format = my v_format_long ();
			double value = NUMundefined, inverseValue = 0.0;
			switch (i) {
				case 0: format = my v_format_totalDuration (), value = my d_tmax - my d_tmin; break;
				case 1: format = my v_format_window (), value = my d_endWindow - my d_startWindow;
					/*
					 * Window domain text.
					 */	
					Graphics_setColour (my d_graphics, Graphics_BLUE);
					Graphics_setTextAlignment (my d_graphics, Graphics_LEFT, Graphics_HALF);
					Graphics_text1 (my d_graphics, left, 0.5 * (bottom + top) - verticalCorrection, Melder_fixed (my d_startWindow, my v_fixedPrecision_long ()));
					Graphics_setTextAlignment (my d_graphics, Graphics_RIGHT, Graphics_HALF);
					Graphics_text1 (my d_graphics, right, 0.5 * (bottom + top) - verticalCorrection, Melder_fixed (my d_endWindow, my v_fixedPrecision_long ()));
					Graphics_setColour (my d_graphics, Graphics_BLACK);
					Graphics_setTextAlignment (my d_graphics, Graphics_CENTRE, Graphics_HALF);
				break;
				case 2: value = my d_startWindow - my d_tmin; break;
				case 3: value = my d_tmax - my d_endWindow; break;
				case 4: value = my marker [1] - my d_startWindow; break;
				case 5: value = my marker [2] - my marker [1]; break;
				case 6: value = my marker [3] - my marker [2]; break;
				case 7: format = my v_format_selection (), value = my d_endSelection - my d_startSelection, inverseValue = 1 / value; break;
			}
			swprintf (text, 100, format, value, inverseValue);
			if (Graphics_textWidth (my d_graphics, text) < right - left) {
				Graphics_text (my d_graphics, 0.5 * (left + right), 0.5 * (bottom + top) - verticalCorrection, text);
			} else if (format == my v_format_long ()) {
				swprintf (text, 100, my v_format_short (), value);
				if (Graphics_textWidth (my d_graphics, text) < right - left)
					Graphics_text (my d_graphics, 0.5 * (left + right), 0.5 * (bottom + top) - verticalCorrection, text);
			} else {
				swprintf (text, 100, my v_format_long (), value);
				if (Graphics_textWidth (my d_graphics, text) < right - left) {
						Graphics_text (my d_graphics, 0.5 * (left + right), 0.5 * (bottom + top) - verticalCorrection, text);
				} else {
					swprintf (text, 100, my v_format_short (), my d_endSelection - my d_startSelection);
					if (Graphics_textWidth (my d_graphics, text) < right - left)
						Graphics_text (my d_graphics, 0.5 * (left + right), 0.5 * (bottom + top) - verticalCorrection, text);
				}
			}
		}
	}

	Graphics_setViewport (my d_graphics, my functionViewerLeft + MARGIN, my functionViewerRight - MARGIN, 0, my height);
	Graphics_setWindow (my d_graphics, my d_startWindow, my d_endWindow, 0, my height);
	/*Graphics_setColour (my d_graphics, Graphics_WHITE);
	Graphics_fillRectangle (my d_graphics, my d_startWindow, my d_endWindow, BOTTOM_MARGIN + space * 3, my height - (TOP_MARGIN + space));*/
	Graphics_setColour (my d_graphics, Graphics_BLACK);
	Graphics_rectangle (my d_graphics, my d_startWindow, my d_endWindow, BOTTOM_MARGIN + space * 3, my height - (TOP_MARGIN + space));

	/*
	 * Red marker text.
	 */
	Graphics_setColour (my d_graphics, Graphics_RED);
	if (cursorVisible) {
		Graphics_setTextAlignment (my d_graphics, Graphics_CENTRE, Graphics_BOTTOM);
		Graphics_text1 (my d_graphics, my d_startSelection, my height - (TOP_MARGIN + space) - verticalCorrection, Melder_fixed (my d_startSelection, my v_fixedPrecision_long ()));
	}
	if (beginVisible && selection) {
		Graphics_setTextAlignment (my d_graphics, Graphics_RIGHT, Graphics_HALF);
		Graphics_text1 (my d_graphics, my d_startSelection, my height - (TOP_MARGIN + space/2) - verticalCorrection, Melder_fixed (my d_startSelection, my v_fixedPrecision_long ()));
	}
	if (endVisible && selection) {
		Graphics_setTextAlignment (my d_graphics, Graphics_LEFT, Graphics_HALF);
		Graphics_text1 (my d_graphics, my d_endSelection, my height - (TOP_MARGIN + space/2) - verticalCorrection, Melder_fixed (my d_endSelection, my v_fixedPrecision_long ()));
	}
	Graphics_setColour (my d_graphics, Graphics_BLACK);

	/*
	 * To reduce flashing, give our descendants the opportunity to prepare their data.
	 */
	my v_prepareDraw ();

	/*
	 * Start of inner drawing.
	 */
	Graphics_setViewport (my d_graphics, my functionViewerLeft + MARGIN, my functionViewerRight - MARGIN, BOTTOM_MARGIN + space * 3, my height - (TOP_MARGIN + space));

	my v_draw ();
	Graphics_setViewport (my d_graphics, my functionViewerLeft + MARGIN, my functionViewerRight - MARGIN, BOTTOM_MARGIN + space * 3, my height - (TOP_MARGIN + space));

	/*
	 * Red dotted marker lines.
	 */
	Graphics_setWindow (my d_graphics, my d_startWindow, my d_endWindow, 0.0, 1.0);
	Graphics_setColour (my d_graphics, Graphics_RED);
	Graphics_setLineType (my d_graphics, Graphics_DOTTED);
	double bottom = my v_getBottomOfSoundAndAnalysisArea ();
	if (cursorVisible)
		Graphics_line (my d_graphics, my d_startSelection, bottom, my d_startSelection, 1.0);
	if (beginVisible)
		Graphics_line (my d_graphics, my d_startSelection, bottom, my d_startSelection, 1.0);
	if (endVisible)
		Graphics_line (my d_graphics, my d_endSelection, bottom, my d_endSelection, 1.0);
	Graphics_setColour (my d_graphics, Graphics_BLACK);
	Graphics_setLineType (my d_graphics, Graphics_DRAWN);

	/*
	 * Highlight selection.
	 */
	if (selection && my d_startSelection < my d_endWindow && my d_endSelection > my d_startWindow) {
		double left = my d_startSelection, right = my d_endSelection;
		if (left < my d_startWindow) left = my d_startWindow;
		if (right > my d_endWindow) right = my d_endWindow;
		my v_highlightSelection (left, right, 0.0, 1.0);
	}

	/*
	 * Draw the selection part.
	 */
	if (my p_showSelectionViewer) {
		Graphics_setViewport (my d_graphics, my selectionViewerLeft + MARGIN, my selectionViewerRight - MARGIN, BOTTOM_MARGIN + space * 3, my height - (TOP_MARGIN + space));
		my v_drawSelectionViewer ();
	}

	/*
	 * End of inner drawing.
	 */
	Graphics_flushWs (my d_graphics);
	Graphics_setViewport (my d_graphics, my functionViewerLeft, my selectionViewerRight, 0, my height);
}

/********** METHODS **********/

void structFunctionEditor :: v_destroy () {
	MelderAudio_stopPlaying (MelderAudio_IMPLICIT);
	if (group) {   // undangle
		int i = 1; while (theGroup [i] != this) { Melder_assert (i < maxGroup); i ++; } theGroup [i] = NULL;
		nGroup --;
	}
	forget (d_graphics);
	FunctionEditor_Parent :: v_destroy ();
}

void structFunctionEditor :: v_info () {
	FunctionEditor_Parent :: v_info ();
	MelderInfo_writeLine (L"Editor start: ", Melder_double (d_tmin), L" ", v_format_units ());
	MelderInfo_writeLine (L"Editor end: ", Melder_double (d_tmax), L" ", v_format_units ());
	MelderInfo_writeLine (L"Window start: ", Melder_double (d_startWindow), L" ", v_format_units ());
	MelderInfo_writeLine (L"Window end: ", Melder_double (d_endWindow), L" ", v_format_units ());
	MelderInfo_writeLine (L"Selection start: ", Melder_double (d_startSelection), L" ", v_format_units ());
	MelderInfo_writeLine (L"Selection end: ", Melder_double (d_endSelection), L" ", v_format_units ());
	MelderInfo_writeLine (L"Arrow scroll step: ", Melder_double (p_arrowScrollStep), L" ", v_format_units ());
	MelderInfo_writeLine (L"Group: ", group ? L"yes" : L"no");
}

/********** FILE MENU **********/

static void gui_drawingarea_cb_resize (I, GuiDrawingAreaResizeEvent event) {
	iam (FunctionEditor);
	if (my d_graphics == NULL) return;   // Could be the case in the very beginning.
	Graphics_setWsViewport (my d_graphics, 0, event -> width, 0, event -> height);
	short width = event -> width + 21;
	/*
	 * Put the function viewer at the left and the selection viewer at the right.
	 */
	my functionViewerLeft = 0;
	my functionViewerRight = my p_showSelectionViewer ? width * (2.0/3) : width;
	my selectionViewerLeft = my functionViewerRight;
	my selectionViewerRight = width;
	my height = event -> height + 111;
	Graphics_setWsWindow (my d_graphics, 0, width, 0, my height);
	Graphics_setViewport (my d_graphics, 0, width, 0, my height);
	Graphics_updateWs (my d_graphics);

	/* Save the current shell size as the user's preference for a new FunctionEditor. */

	my pref_shellWidth () = my d_windowForm -> f_getShellWidth ();
	my pref_shellHeight () = my d_windowForm -> f_getShellHeight ();
}

static void menu_cb_preferences (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	EDITOR_FORM (L"Preferences", 0)
		BOOLEAN (L"Synchronize zoom and scroll", my default_synchronizedZoomAndScroll ())
		BOOLEAN (L"Show selection viewer", my default_showSelectionViewer ())
		POSITIVE (Melder_wcscat (L"Arrow scroll step (", my v_format_units (), L")"), my default_arrowScrollStep ())
		my v_prefs_addFields (cmd);
	EDITOR_OK
		SET_INTEGER (L"Synchronize zoom and scroll", my pref_synchronizedZoomAndScroll ())
		SET_INTEGER (L"Show selection viewer", my pref_showSelectionViewer())
		SET_REAL (L"Arrow scroll step", my p_arrowScrollStep)
		my v_prefs_setValues (cmd);
	EDITOR_DO
		bool oldSynchronizedZoomAndScroll = my pref_synchronizedZoomAndScroll ();
		bool oldShowSelectionViewer = my p_showSelectionViewer;
		my pref_synchronizedZoomAndScroll () = GET_INTEGER (L"Synchronize zoom and scroll");
		my pref_showSelectionViewer () = my p_showSelectionViewer = GET_INTEGER (L"Show selection viewer");
		my pref_arrowScrollStep () = my p_arrowScrollStep = GET_REAL (L"Arrow scroll step");
		if (my p_showSelectionViewer != oldShowSelectionViewer) {
			struct structGuiDrawingAreaResizeEvent event = { my drawingArea, 0 };
			event. width = my drawingArea -> f_getWidth ();
			event. height = my drawingArea -> f_getHeight ();
			gui_drawingarea_cb_resize (me, & event);
		}
		if (! oldSynchronizedZoomAndScroll && my pref_synchronizedZoomAndScroll ()) {
			updateGroup (me);
		}
		my v_prefs_getValues (cmd);
	EDITOR_END
}

void structFunctionEditor :: v_form_pictureSelection (EditorCommand cmd) {
	BOOLEAN (L"Draw selection times", 1);
	BOOLEAN (L"Draw selection hairs", 1);
}
void structFunctionEditor :: v_ok_pictureSelection (EditorCommand cmd) {
	FunctionEditor me = (FunctionEditor) cmd -> d_editor;
	SET_INTEGER (L"Draw selection times", my pref_picture_drawSelectionTimes ());
	SET_INTEGER (L"Draw selection hairs", my pref_picture_drawSelectionHairs ());
}
void structFunctionEditor :: v_do_pictureSelection (EditorCommand cmd) {
	FunctionEditor me = (FunctionEditor) cmd -> d_editor;
	my pref_picture_drawSelectionTimes () = GET_INTEGER (L"Draw selection times");
	my pref_picture_drawSelectionHairs () = GET_INTEGER (L"Draw selection hairs");
}

/********** QUERY MENU **********/

static void menu_cb_getB (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	Melder_informationReal (my d_startSelection, my v_format_units ());
}
static void menu_cb_getCursor (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	Melder_informationReal (0.5 * (my d_startSelection + my d_endSelection), my v_format_units ());
}
static void menu_cb_getE (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	Melder_informationReal (my d_endSelection, my v_format_units ());
}
static void menu_cb_getSelectionDuration (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	Melder_informationReal (my d_endSelection - my d_startSelection, my v_format_units ());
}

/********** VIEW MENU **********/

static void menu_cb_zoom (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	EDITOR_FORM (L"Zoom", 0)
		REAL (L"From", L"0.0")
		REAL (L"To", L"1.0")
	EDITOR_OK
		SET_REAL (L"From", my d_startWindow)
		SET_REAL (L"To", my d_endWindow)
	EDITOR_DO
		my d_startWindow = GET_REAL (L"From");
		if (my d_startWindow < my d_tmin + 1e-12)
			my d_startWindow = my d_tmin;
		my d_endWindow = GET_REAL (L"To");
		if (my d_endWindow > my d_tmax - 1e-12)
			my d_endWindow = my d_tmax;
		my v_updateText ();
		updateScrollBar (me);
		/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
		updateGroup (me);
	EDITOR_END
}

static void do_showAll (FunctionEditor me) {
	my d_startWindow = my d_tmin;
	my d_endWindow = my d_tmax;
	my v_updateText ();
	updateScrollBar (me);
	/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
	if (my pref_synchronizedZoomAndScroll ()) {
		updateGroup (me);
	}
}

static void gui_button_cb_showAll (I, GuiButtonEvent event) {
	(void) event;
	iam (FunctionEditor);
	do_showAll (me);
}

static void do_zoomIn (FunctionEditor me) {
	double shift = (my d_endWindow - my d_startWindow) / 4;
	my d_startWindow += shift;
	my d_endWindow -= shift;
	my v_updateText ();
	updateScrollBar (me);
	/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
	if (my pref_synchronizedZoomAndScroll ()) {
		updateGroup (me);
	}
}

static void gui_button_cb_zoomIn (I, GuiButtonEvent event) {
	(void) event;
	iam (FunctionEditor);
	do_zoomIn (me);
}

static void do_zoomOut (FunctionEditor me) {
	double shift = (my d_endWindow - my d_startWindow) / 2;
	MelderAudio_stopPlaying (MelderAudio_IMPLICIT);   /* Quickly, before window changes. */
	my d_startWindow -= shift;
	if (my d_startWindow < my d_tmin + 1e-12)
		my d_startWindow = my d_tmin;
	my d_endWindow += shift;
	if (my d_endWindow > my d_tmax - 1e-12)
		my d_endWindow = my d_tmax;
	my v_updateText ();
	updateScrollBar (me);
	/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
	if (my pref_synchronizedZoomAndScroll ()) {
		updateGroup (me);
	}
}

static void gui_button_cb_zoomOut (I, GuiButtonEvent event) {
	(void) event;
	iam (FunctionEditor);
	do_zoomOut (me);
}

static void do_zoomToSelection (FunctionEditor me) {
	if (my d_endSelection > my d_startSelection) {
		my startZoomHistory = my d_startWindow;   // remember for Zoom Back
		my endZoomHistory = my d_endWindow;   // remember for Zoom Back
		trace ("Zooming in to %.17g ~ %.17g seconds.", my d_startSelection, my d_endSelection);
		my d_startWindow = my d_startSelection;
		my d_endWindow = my d_endSelection;
		trace ("Zoomed in to %.17g ~ %.17g seconds (1).", my d_startWindow, my d_endWindow);
		my v_updateText ();
		trace ("Zoomed in to %.17g ~ %.17g seconds (2).", my d_startWindow, my d_endWindow);
		updateScrollBar (me);
		trace ("Zoomed in to %.17g ~ %.17g seconds (3).", my d_startWindow, my d_endWindow);
		/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
		if (my pref_synchronizedZoomAndScroll ()) {
			updateGroup (me);
		}
		trace ("Zoomed in to %.17g ~ %.17g seconds (4).", my d_startWindow, my d_endWindow);
	}
}

static void gui_button_cb_zoomToSelection (I, GuiButtonEvent event) {
	(void) event;
	iam (FunctionEditor);
	do_zoomToSelection (me);
}

static void do_zoomBack (FunctionEditor me) {
	if (my endZoomHistory > my startZoomHistory) {
		my d_startWindow = my startZoomHistory;
		my d_endWindow = my endZoomHistory;
		my v_updateText ();
		updateScrollBar (me);
		/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
		if (my pref_synchronizedZoomAndScroll ()) {
			updateGroup (me);
		}
	}
}

static void gui_button_cb_zoomBack (I, GuiButtonEvent event) {
	(void) event;
	iam (FunctionEditor);
	do_zoomBack (me);
}

static void menu_cb_showAll (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	do_showAll (me);
}

static void menu_cb_zoomIn (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	do_zoomIn (me);
}

static void menu_cb_zoomOut (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	do_zoomOut (me);
}

static void menu_cb_zoomToSelection (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	do_zoomToSelection (me);
}

static void menu_cb_zoomBack (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	do_zoomBack (me);
}

static void menu_cb_play (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	EDITOR_FORM (L"Play", 0)
		REAL (L"From", L"0.0")
		REAL (L"To", L"1.0")
	EDITOR_OK
		SET_REAL (L"From", my d_startWindow)
		SET_REAL (L"To", my d_endWindow)
	EDITOR_DO
		MelderAudio_stopPlaying (MelderAudio_IMPLICIT);
		my v_play (GET_REAL (L"From"), GET_REAL (L"To"));
	EDITOR_END
}

static void menu_cb_playOrStop (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	if (MelderAudio_isPlaying) {
		MelderAudio_stopPlaying (MelderAudio_EXPLICIT);
	} else if (my d_startSelection < my d_endSelection) {
		my playingSelection = true;
		my v_play (my d_startSelection, my d_endSelection);
	} else {
		my playingCursor = true;
		if (my d_startSelection == my d_endSelection && my d_startSelection > my d_startWindow && my d_startSelection < my d_endWindow)
			my v_play (my d_startSelection, my d_endWindow);
		else
			my v_play (my d_startWindow, my d_endWindow);
	}
}

static void menu_cb_playWindow (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	MelderAudio_stopPlaying (MelderAudio_IMPLICIT);
	my playingCursor = true;
	my v_play (my d_startWindow, my d_endWindow);
}

static void menu_cb_interruptPlaying (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	MelderAudio_stopPlaying (MelderAudio_IMPLICIT);
}

/********** SELECT MENU **********/

static void menu_cb_select (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	EDITOR_FORM (L"Select", 0)
		REAL (L"Start of selection", L"0.0")
		REAL (L"End of selection", L"1.0")
	EDITOR_OK
		SET_REAL (L"Start of selection", my d_startSelection)
		SET_REAL (L"End of selection", my d_endSelection)
	EDITOR_DO
		my d_startSelection = GET_REAL (L"Start of selection");
		if (my d_startSelection < my d_tmin + 1e-12)
			my d_startSelection = my d_tmin;
		my d_endSelection = GET_REAL (L"End of selection");
		if (my d_endSelection > my d_tmax - 1e-12)
			my d_endSelection = my d_tmax;
		if (my d_startSelection > my d_endSelection) {
			double dummy = my d_startSelection;
			my d_startSelection = my d_endSelection;
			my d_endSelection = dummy;
		}
		my v_updateText ();
		/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
		updateGroup (me);
	EDITOR_END
}

static void menu_cb_moveCursorToB (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	my d_endSelection = my d_startSelection;
	my v_updateText ();
	/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
	updateGroup (me);
}

static void menu_cb_moveCursorToE (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	my d_startSelection = my d_endSelection;
	my v_updateText ();
	/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
	updateGroup (me);
}

static void menu_cb_moveCursorTo (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	EDITOR_FORM (L"Move cursor to", 0)
		REAL (L"Position", L"0.0")
	EDITOR_OK
		SET_REAL (L"Position", 0.5 * (my d_startSelection + my d_endSelection))
	EDITOR_DO
		double position = GET_REAL (L"Position");
		if (position < my d_tmin + 1e-12) position = my d_tmin;
		if (position > my d_tmax - 1e-12) position = my d_tmax;
		my d_startSelection = my d_endSelection = position;
		my v_updateText ();
		/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
		updateGroup (me);
	EDITOR_END
}

static void menu_cb_moveCursorBy (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	EDITOR_FORM (L"Move cursor by", 0)
		REAL (L"Distance", L"0.05")
	EDITOR_OK
	EDITOR_DO
		double position = 0.5 * (my d_startSelection + my d_endSelection) + GET_REAL (L"Distance");
		if (position < my d_tmin) position = my d_tmin;
		if (position > my d_tmax) position = my d_tmax;
		my d_startSelection = my d_endSelection = position;
		my v_updateText ();
		/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
		updateGroup (me);
	EDITOR_END
}

static void menu_cb_moveBby (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	EDITOR_FORM (L"Move start of selection by", 0)
		REAL (L"Distance", L"0.05")
	EDITOR_OK
	EDITOR_DO
		double position = my d_startSelection + GET_REAL (L"Distance");
		if (position < my d_tmin) position = my d_tmin;
		if (position > my d_tmax) position = my d_tmax;
		my d_startSelection = position;
		if (my d_startSelection > my d_endSelection) {
			double dummy = my d_startSelection;
			my d_startSelection = my d_endSelection;
			my d_endSelection = dummy;
		}
		my v_updateText ();
		/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
		updateGroup (me);
	EDITOR_END
}

static void menu_cb_moveEby (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	EDITOR_FORM (L"Move end of selection by", 0)
		REAL (L"Distance", L"0.05")
	EDITOR_OK
	EDITOR_DO
		double position = my d_endSelection + GET_REAL (L"Distance");
		if (position < my d_tmin) position = my d_tmin;
		if (position > my d_tmax) position = my d_tmax;
		my d_endSelection = position;
		if (my d_startSelection > my d_endSelection) {
			double dummy = my d_startSelection;
			my d_startSelection = my d_endSelection;
			my d_endSelection = dummy;
		}
		my v_updateText ();
		/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
		updateGroup (me);
	EDITOR_END
}

void FunctionEditor_shift (FunctionEditor me, double shift, bool needsUpdateGroup) {
	double windowLength = my d_endWindow - my d_startWindow;
	MelderAudio_stopPlaying (MelderAudio_IMPLICIT);   /* Quickly, before window changes. */
	trace ("shifting by %.17g", shift);
	if (shift < 0.0) {
		my d_startWindow += shift;
		if (my d_startWindow < my d_tmin + 1e-12)
			my d_startWindow = my d_tmin;
		my d_endWindow = my d_startWindow + windowLength;
		if (my d_endWindow > my d_tmax - 1e-12)
			my d_endWindow = my d_tmax;
	} else {
		my d_endWindow += shift;
		if (my d_endWindow > my d_tmax - 1e-12)
			my d_endWindow = my d_tmax;
		my d_startWindow = my d_endWindow - windowLength;
		if (my d_startWindow < my d_tmin + 1e-12)
			my d_startWindow = my d_tmin;
	}
	FunctionEditor_marksChanged (me, needsUpdateGroup);
}

static void menu_cb_pageUp (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	FunctionEditor_shift (me, -RELATIVE_PAGE_INCREMENT * (my d_endWindow - my d_startWindow), true);
}

static void menu_cb_pageDown (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	FunctionEditor_shift (me, +RELATIVE_PAGE_INCREMENT * (my d_endWindow - my d_startWindow), true);
}

static void scrollToView (FunctionEditor me, double t) {
	if (t <= my d_startWindow) {
		FunctionEditor_shift (me, t - my d_startWindow - 0.618 * (my d_endWindow - my d_startWindow), true);
	} else if (t >= my d_endWindow) {
		FunctionEditor_shift (me, t - my d_endWindow + 0.618 * (my d_endWindow - my d_startWindow), true);
	} else {
		FunctionEditor_marksChanged (me, true);
	}
}

static void menu_cb_selectEarlier (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	my d_startSelection -= my p_arrowScrollStep;
	if (my d_startSelection < my d_tmin + 1e-12)
		my d_startSelection = my d_tmin;
	my d_endSelection -= my p_arrowScrollStep;
	if (my d_endSelection < my d_tmin + 1e-12)
		my d_endSelection = my d_tmin;
	scrollToView (me, 0.5 * (my d_startSelection + my d_endSelection));
}

static void menu_cb_selectLater (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	my d_startSelection += my p_arrowScrollStep;
	if (my d_startSelection > my d_tmax - 1e-12)
		my d_startSelection = my d_tmax;
	my d_endSelection += my p_arrowScrollStep;
	if (my d_endSelection > my d_tmax - 1e-12)
		my d_endSelection = my d_tmax;
	scrollToView (me, 0.5 * (my d_startSelection + my d_endSelection));
}

static void menu_cb_moveBleft (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	my d_startSelection -= my p_arrowScrollStep;
	if (my d_startSelection < my d_tmin + 1e-12)
		my d_startSelection = my d_tmin;
	scrollToView (me, 0.5 * (my d_startSelection + my d_endSelection));
}

static void menu_cb_moveBright (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	my d_startSelection += my p_arrowScrollStep;
	if (my d_startSelection > my d_tmax - 1e-12)
		my d_startSelection = my d_tmax;
	if (my d_startSelection > my d_endSelection) {
		double dummy = my d_startSelection;
		my d_startSelection = my d_endSelection;
		my d_endSelection = dummy;
	}
	scrollToView (me, 0.5 * (my d_startSelection + my d_endSelection));
}

static void menu_cb_moveEleft (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	my d_endSelection -= my p_arrowScrollStep;
	if (my d_endSelection < my d_tmin + 1e-12)
		my d_endSelection = my d_tmin;
	if (my d_startSelection > my d_endSelection) {
		double dummy = my d_startSelection;
		my d_startSelection = my d_endSelection;
		my d_endSelection = dummy;
	}
	scrollToView (me, 0.5 * (my d_startSelection + my d_endSelection));
}

static void menu_cb_moveEright (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	my d_endSelection += my p_arrowScrollStep;
	if (my d_endSelection > my d_tmax - 1e-12)
		my d_endSelection = my d_tmax;
	scrollToView (me, 0.5 * (my d_startSelection + my d_endSelection));
}

/********** GUI CALLBACKS **********/

static void gui_cb_scroll (I, GuiScrollBarEvent event) {
	iam (FunctionEditor);
	if (my d_graphics == NULL) return;   // ignore events during creation
	double value = event -> scrollBar -> f_getValue ();
	double shift = my d_tmin + (value - 1) * (my d_tmax - my d_tmin) / maximumScrollBarValue - my d_startWindow;
	bool shifted = shift != 0.0;
	double oldSliderSize = (my d_endWindow - my d_startWindow) / (my d_tmax - my d_tmin) * maximumScrollBarValue - 1;
	double newSliderSize = event -> scrollBar -> f_getSliderSize ();
	bool zoomed = newSliderSize != oldSliderSize;
	#if ! cocoa
		zoomed = false;
	#endif
	if (shifted) {
		my d_startWindow += shift;
		if (my d_startWindow < my d_tmin + 1e-12) my d_startWindow = my d_tmin;
		my d_endWindow += shift;
		if (my d_endWindow > my d_tmax - 1e-12) my d_endWindow = my d_tmax;
	}
	if (zoomed) {
		double zoom = (newSliderSize + 1) * (my d_tmax - my d_tmin) / maximumScrollBarValue;
		my d_endWindow = my d_startWindow + zoom;
		if (my d_endWindow > my d_tmax - 1e-12) my d_endWindow = my d_tmax;
	}
	if (shifted || zoomed) {
		my v_updateText ();
		updateScrollBar (me);
		#if cocoa
			Graphics_updateWs (my d_graphics);
		#else
			/*Graphics_clearWs (my d_graphics);*/
			drawNow (me);   /* Do not wait for expose event. */
		#endif
		if (! my group || ! my pref_synchronizedZoomAndScroll ()) return;
		for (int i = 1; i <= maxGroup; i ++) if (theGroup [i] && theGroup [i] != me) {
			theGroup [i] -> d_startWindow = my d_startWindow;
			theGroup [i] -> d_endWindow = my d_endWindow;
			FunctionEditor_updateText (theGroup [i]);
			updateScrollBar (theGroup [i]);
			#if cocoa
				Graphics_updateWs (theGroup [i] -> d_graphics);
			#else
				Graphics_clearWs (theGroup [i] -> d_graphics);
				drawNow (theGroup [i]);
			#endif
		}
	}
}

static void gui_checkbutton_cb_group (I, GuiCheckButtonEvent event) {
	iam (FunctionEditor);
	(void) event;
	int i;
	my group = ! my group;
	if (my group) {
		FunctionEditor thee;
		i = 1; while (theGroup [i]) i ++; theGroup [i] = me;
		if (++ nGroup == 1) { Graphics_updateWs (my d_graphics); return; }
		i = 1; while (theGroup [i] == NULL || theGroup [i] == me) i ++; thee = theGroup [i];
		if (my pref_synchronizedZoomAndScroll ()) {
			my d_startWindow = thy d_startWindow;
			my d_endWindow = thy d_endWindow;
		}
		my d_startSelection = thy d_startSelection;
		my d_endSelection = thy d_endSelection;
		if (my d_tmin > thy d_tmin || my d_tmax < thy d_tmax) {
			if (my d_tmin > thy d_tmin) my d_tmin = thy d_tmin;
			if (my d_tmax < thy d_tmax) my d_tmax = thy d_tmax;
			my v_updateText ();
			updateScrollBar (me);
			Graphics_updateWs (my d_graphics);
		} else {
			my v_updateText ();
			updateScrollBar (me);
			Graphics_updateWs (my d_graphics);
			if (my d_tmin < thy d_tmin || my d_tmax > thy d_tmax)
				for (i = 1; i <= maxGroup; i ++) if (theGroup [i] && theGroup [i] != me) {
					if (my d_tmin < thy d_tmin)
						theGroup [i] -> d_tmin = my d_tmin;
					if (my d_tmax > thy d_tmax)
						theGroup [i] -> d_tmax = my d_tmax;
					FunctionEditor_updateText (theGroup [i]);
					updateScrollBar (theGroup [i]);
					Graphics_updateWs (theGroup [i] -> d_graphics);
				}
		}
	} else {
		i = 1; while (theGroup [i] != me) i ++; theGroup [i] = NULL;
		nGroup --;
		my v_updateText ();
		Graphics_updateWs (my d_graphics);   // for setting buttons in draw method
	}
	if (my group) updateGroup (me);
}

static void menu_cb_intro (EDITOR_ARGS) {
	EDITOR_IAM (FunctionEditor);
	Melder_help (L"Intro");
}

void structFunctionEditor :: v_createMenuItems_file (EditorMenu menu) {
	FunctionEditor_Parent :: v_createMenuItems_file (menu);
	EditorMenu_addCommand (menu, L"Preferences...", 0, menu_cb_preferences);
	EditorMenu_addCommand (menu, L"-- after preferences --", 0, 0);
}

void structFunctionEditor :: v_createMenuItems_view_timeDomain (EditorMenu menu) {
	EditorMenu_addCommand (menu, v_format_domain (), GuiMenu_INSENSITIVE, menu_cb_zoom /* dummy */);
	EditorMenu_addCommand (menu, L"Zoom...", 0, menu_cb_zoom);
	EditorMenu_addCommand (menu, L"Show all", 'A', menu_cb_showAll);
	EditorMenu_addCommand (menu, L"Zoom in", 'I', menu_cb_zoomIn);
	EditorMenu_addCommand (menu, L"Zoom out", 'O', menu_cb_zoomOut);
	EditorMenu_addCommand (menu, L"Zoom to selection", 'N', menu_cb_zoomToSelection);
	EditorMenu_addCommand (menu, L"Zoom back", 'B', menu_cb_zoomBack);
	EditorMenu_addCommand (menu, L"Scroll page back", GuiMenu_PAGE_UP, menu_cb_pageUp);
	EditorMenu_addCommand (menu, L"Scroll page forward", GuiMenu_PAGE_DOWN, menu_cb_pageDown);
}

void structFunctionEditor :: v_createMenuItems_view_audio (EditorMenu menu) {
	EditorMenu_addCommand (menu, L"-- play --", 0, 0);
	EditorMenu_addCommand (menu, L"Audio:", GuiMenu_INSENSITIVE, menu_cb_play /* dummy */);
	EditorMenu_addCommand (menu, L"Play...", 0, menu_cb_play);
	EditorMenu_addCommand (menu, L"Play or stop", GuiMenu_TAB, menu_cb_playOrStop);
	EditorMenu_addCommand (menu, L"Play window", GuiMenu_SHIFT + GuiMenu_TAB, menu_cb_playWindow);
	EditorMenu_addCommand (menu, L"Interrupt playing", GuiMenu_ESCAPE, menu_cb_interruptPlaying);
}

void structFunctionEditor :: v_createMenuItems_view (EditorMenu menu) {
	v_createMenuItems_view_timeDomain (menu);
	v_createMenuItems_view_audio (menu);
}

void structFunctionEditor :: v_createMenuItems_query (EditorMenu menu) {
	FunctionEditor_Parent :: v_createMenuItems_query (menu);
	EditorMenu_addCommand (menu, L"-- query selection --", 0, 0);
	EditorMenu_addCommand (menu, L"Get start of selection", 0, menu_cb_getB);
	EditorMenu_addCommand (menu, L"Get begin of selection", Editor_HIDDEN, menu_cb_getB);
	EditorMenu_addCommand (menu, L"Get cursor", GuiMenu_F6, menu_cb_getCursor);
	EditorMenu_addCommand (menu, L"Get end of selection", 0, menu_cb_getE);
	EditorMenu_addCommand (menu, L"Get selection length", 0, menu_cb_getSelectionDuration);
}

void structFunctionEditor :: v_createMenus () {
	FunctionEditor_Parent :: v_createMenus ();
	EditorMenu menu;

	menu = Editor_addMenu (this, L"View", 0);
	v_createMenuItems_view (menu);

	Editor_addMenu (this, L"Select", 0);
	Editor_addCommand (this, L"Select", L"Select...", 0, menu_cb_select);
	Editor_addCommand (this, L"Select", L"Move cursor to start of selection", 0, menu_cb_moveCursorToB);
	Editor_addCommand (this, L"Select", L"Move cursor to begin of selection", Editor_HIDDEN, menu_cb_moveCursorToB);
	Editor_addCommand (this, L"Select", L"Move cursor to end of selection", 0, menu_cb_moveCursorToE);
	Editor_addCommand (this, L"Select", L"Move cursor to...", 0, menu_cb_moveCursorTo);
	Editor_addCommand (this, L"Select", L"Move cursor by...", 0, menu_cb_moveCursorBy);
	Editor_addCommand (this, L"Select", L"Move start of selection by...", 0, menu_cb_moveBby);
	Editor_addCommand (this, L"Select", L"Move begin of selection by...", Editor_HIDDEN, menu_cb_moveBby);
	Editor_addCommand (this, L"Select", L"Move end of selection by...", 0, menu_cb_moveEby);
	/*Editor_addCommand (this, L"Select", L"Move cursor back by half a second", motif_, menu_cb_moveCursorBy);*/
	Editor_addCommand (this, L"Select", L"Select earlier", GuiMenu_UP_ARROW, menu_cb_selectEarlier);
	Editor_addCommand (this, L"Select", L"Select later", GuiMenu_DOWN_ARROW, menu_cb_selectLater);
	Editor_addCommand (this, L"Select", L"Move start of selection left", GuiMenu_SHIFT + GuiMenu_UP_ARROW, menu_cb_moveBleft);
	Editor_addCommand (this, L"Select", L"Move begin of selection left", Editor_HIDDEN, menu_cb_moveBleft);
	Editor_addCommand (this, L"Select", L"Move start of selection right", GuiMenu_SHIFT + GuiMenu_DOWN_ARROW, menu_cb_moveBright);
	Editor_addCommand (this, L"Select", L"Move begin of selection right", Editor_HIDDEN, menu_cb_moveBright);
	Editor_addCommand (this, L"Select", L"Move end of selection left", GuiMenu_COMMAND + GuiMenu_UP_ARROW, menu_cb_moveEleft);
	Editor_addCommand (this, L"Select", L"Move end of selection right", GuiMenu_COMMAND + GuiMenu_DOWN_ARROW, menu_cb_moveEright);
}

void structFunctionEditor :: v_createHelpMenuItems (EditorMenu menu) {
	FunctionEditor_Parent :: v_createHelpMenuItems (menu);
	EditorMenu_addCommand (menu, L"Intro", 0, menu_cb_intro);
}

static void gui_drawingarea_cb_expose (I, GuiDrawingAreaExposeEvent event) {
	iam (FunctionEditor);
	(void) event;
	if (my d_graphics == NULL) return;   // Could be the case in the very beginning.
	if (my enableUpdates)
		drawNow (me);
}

static void gui_drawingarea_cb_click (I, GuiDrawingAreaClickEvent event) {
	iam (FunctionEditor);
	if (my d_graphics == NULL) return;   // Could be the case in the very beginning.
	double xWC, yWC;
	my shiftKeyPressed = event -> shiftKeyPressed;
	Graphics_setWindow (my d_graphics, my functionViewerLeft, my functionViewerRight, 0, my height);
	Graphics_DCtoWC (my d_graphics, event -> x, event -> y, & xWC, & yWC);

	if (yWC > BOTTOM_MARGIN + space * 3 && yWC < my height - (TOP_MARGIN + space)) {   /* In signal region? */
		int needsUpdate;
		Graphics_setViewport (my d_graphics, my functionViewerLeft + MARGIN, my functionViewerRight - MARGIN,
			BOTTOM_MARGIN + space * 3, my height - (TOP_MARGIN + space));
		Graphics_setWindow (my d_graphics, my d_startWindow, my d_endWindow, 0.0, 1.0);
		Graphics_DCtoWC (my d_graphics, event -> x, event -> y, & xWC, & yWC);
		if (xWC < my d_startWindow) xWC = my d_startWindow;
		if (xWC > my d_endWindow) xWC = my d_endWindow;
		if (Melder_debug == 24) {
			Melder_casual ("FunctionEditor::gui_drawingarea_cb_click: button %d shift %d option %d command %d control %d",
				event -> button, my shiftKeyPressed, event -> optionKeyPressed, event -> commandKeyPressed, event -> extraControlKeyPressed);
		}
#if defined (macintosh)
		needsUpdate =
			event -> optionKeyPressed || event -> extraControlKeyPressed ? my v_clickB (xWC, yWC) :
			event -> commandKeyPressed ? my v_clickE (xWC, yWC) :
			my v_click (xWC, yWC, my shiftKeyPressed);
#elif defined (_WIN32)
		needsUpdate =
			event -> commandKeyPressed ? my v_clickB (xWC, yWC) :
			event -> optionKeyPressed ? my v_clickE (xWC, yWC) :
			my v_click (xWC, yWC, my shiftKeyPressed);
#else
		needsUpdate =
			event -> commandKeyPressed ? my v_clickB (xWC, yWC) :
			event -> optionKeyPressed ? my v_clickE (xWC, yWC) :
			event -> button == 1 ? my v_click (xWC, yWC, my shiftKeyPressed) :
			event -> button == 2 ? my v_clickB (xWC, yWC) : my v_clickE (xWC, yWC);
#endif
		if (needsUpdate) my v_updateText ();
		Graphics_setViewport (my d_graphics, my functionViewerLeft, my functionViewerRight, 0, my height);
		if (needsUpdate) {
			drawNow (me);
		}
		if (needsUpdate) updateGroup (me);
	}
	else   /* Clicked outside signal region? Let us hear it. */
	{
		try {
			for (int i = 0; i < 8; i ++) {
				if (xWC > my rect [i]. left && xWC < my rect [i]. right &&
					 yWC > my rect [i]. bottom && yWC < my rect [i]. top)
					switch (i) {
						case 0: my v_play (my d_tmin, my d_tmax); break;
						case 1: my v_play (my d_startWindow, my d_endWindow); break;
						case 2: my v_play (my d_tmin, my d_startWindow); break;
						case 3: my v_play (my d_endWindow, my d_tmax); break;
						case 4: my v_play (my d_startWindow, my marker [1]); break;
						case 5: my v_play (my marker [1], my marker [2]); break;
						case 6: my v_play (my marker [2], my marker [3]); break;
						case 7: my v_play (my d_startSelection, my d_endSelection); break;
					}
			}
		} catch (MelderError) {
			Melder_flushError (NULL);
		}
	}
}

void structFunctionEditor :: v_createChildren () {
	int x = BUTTON_X;

	/***** Create zoom buttons. *****/

	GuiButton_createShown (d_windowForm, x, x + BUTTON_WIDTH, -4 - Gui_PUSHBUTTON_HEIGHT, -4,
		L"all", gui_button_cb_showAll, this, 0);
	x += BUTTON_WIDTH + BUTTON_SPACING;
	GuiButton_createShown (d_windowForm, x, x + BUTTON_WIDTH, -4 - Gui_PUSHBUTTON_HEIGHT, -4,
		L"in", gui_button_cb_zoomIn, this, 0);
	x += BUTTON_WIDTH + BUTTON_SPACING;
	GuiButton_createShown (d_windowForm, x, x + BUTTON_WIDTH, -4 - Gui_PUSHBUTTON_HEIGHT, -4,
		L"out", gui_button_cb_zoomOut, this, 0);
	x += BUTTON_WIDTH + BUTTON_SPACING;
	GuiButton_createShown (d_windowForm, x, x + BUTTON_WIDTH, -4 - Gui_PUSHBUTTON_HEIGHT, -4,
		L"sel", gui_button_cb_zoomToSelection, this, 0);
	x += BUTTON_WIDTH + BUTTON_SPACING;
	GuiButton_createShown (d_windowForm, x, x + BUTTON_WIDTH, -4 - Gui_PUSHBUTTON_HEIGHT, -4,
		L"bak", gui_button_cb_zoomBack, this, 0);

	/***** Create scroll bar. *****/

	scrollBar = GuiScrollBar_createShown (d_windowForm,
		x += BUTTON_WIDTH + BUTTON_SPACING, -80 - BUTTON_SPACING, -4 - Gui_PUSHBUTTON_HEIGHT, 0,
		1, maximumScrollBarValue, 1, maximumScrollBarValue - 1, 1, 1,
		gui_cb_scroll, this, GuiScrollBar_HORIZONTAL);

	/***** Create Group button. *****/

	groupButton = GuiCheckButton_createShown (d_windowForm, -80, 0, -4 - Gui_PUSHBUTTON_HEIGHT, -4,
		L"Group", gui_checkbutton_cb_group, this, group_equalDomain (d_tmin, d_tmax) ? GuiCheckButton_SET : 0);

	/***** Create optional text field. *****/

	if (v_hasText ()) {
		text = GuiText_createShown (d_windowForm, 0, 0,
			Machine_getMenuBarHeight (),
			Machine_getMenuBarHeight () + TEXT_HEIGHT, GuiText_WORDWRAP | GuiText_MULTILINE);
		#if gtk
			Melder_assert (text -> d_widget);
			gtk_widget_grab_focus (GTK_WIDGET (text -> d_widget));   // BUG: can hardly be correct (the text should grab the focus of the window, not the global focus)
		#elif cocoa
			Melder_assert ([(NSView *) text -> d_widget window]);
			//[[(NSView *) text -> d_widget window] setInitialFirstResponder: (NSView *) text -> d_widget];
			[[(NSView *) text -> d_widget window] makeFirstResponder: (NSView *) text -> d_widget];
		#endif
	}

	/***** Create drawing area. *****/

	#if cocoa
		int marginBetweenTextAndDrawingAreaToEnsureCorrectUnhighlighting = 3;
	#else
		int marginBetweenTextAndDrawingAreaToEnsureCorrectUnhighlighting = 0;
	#endif
	drawingArea = GuiDrawingArea_createShown (d_windowForm,
		0, 0,
		Machine_getMenuBarHeight () + ( v_hasText () ? TEXT_HEIGHT + marginBetweenTextAndDrawingAreaToEnsureCorrectUnhighlighting : 0), -8 - Gui_PUSHBUTTON_HEIGHT,
		gui_drawingarea_cb_expose, gui_drawingarea_cb_click, NULL, gui_drawingarea_cb_resize, this, 0);
	drawingArea -> f_setSwipable (scrollBar, NULL);
}

void structFunctionEditor :: v_dataChanged () {
	Function function = (Function) data;
	Melder_assert (Thing_member (function, classFunction));
	d_tmin = function -> xmin;
 	d_tmax = function -> xmax;
 	if (d_startWindow < d_tmin || d_startWindow > d_tmax) d_startWindow = d_tmin;
 	if (d_endWindow < d_tmin || d_endWindow > d_tmax) d_endWindow = d_tmax;
 	if (d_startWindow >= d_endWindow) { d_startWindow = d_tmin; d_endWindow = d_tmax; }
 	if (d_startSelection < d_tmin) d_startSelection = d_tmin;
 	if (d_startSelection > d_tmax) d_startSelection = d_tmax;
 	if (d_endSelection < d_tmin) d_endSelection = d_tmin;
 	if (d_endSelection > d_tmax) d_endSelection = d_tmax;
	FunctionEditor_marksChanged (this, false);
}

static void drawWhileDragging (FunctionEditor me, double x1, double x2) {
	/*
	 * We must draw this within the window, because the window tends to have a white background.
	 * We cannot draw this in the margins, because these tend to be grey, so that Graphics_xorOn does not work properly.
	 * We draw the text twice, because we expect that not ALL of the window is white...
	 */
	double xleft, xright;
	if (x1 > x2) xleft = x2, xright = x1; else xleft = x1, xright = x2;
	Graphics_xorOn (my d_graphics, Graphics_MAROON);
	Graphics_setTextAlignment (my d_graphics, Graphics_RIGHT, Graphics_TOP);
	Graphics_text1 (my d_graphics, xleft, 1.0, Melder_fixed (xleft, 6));
	Graphics_setTextAlignment (my d_graphics, Graphics_LEFT, Graphics_TOP);
	Graphics_text1 (my d_graphics, xright, 1.0, Melder_fixed (xright, 6));
	Graphics_setTextAlignment (my d_graphics, Graphics_RIGHT, Graphics_BOTTOM);
	Graphics_text1 (my d_graphics, xleft, 0.0, Melder_fixed (xleft, 6));
	Graphics_setTextAlignment (my d_graphics, Graphics_LEFT, Graphics_BOTTOM);
	Graphics_text1 (my d_graphics, xright, 0.0, Melder_fixed (xright, 6));
	Graphics_setLineType (my d_graphics, Graphics_DOTTED);
	Graphics_line (my d_graphics, xleft, 0.0, xleft, 1.0);
	Graphics_line (my d_graphics, xright, 0.0, xright, 1.0);
	Graphics_setLineType (my d_graphics, Graphics_DRAWN);
	Graphics_xorOff (my d_graphics);
}

int structFunctionEditor :: v_click (double xbegin, double ybegin, bool shiftKeyPressed) {
	bool drag = false;
	double x = xbegin, y = ybegin;
    
    
	/*
	 * The 'anchor' is the point that will stay fixed during dragging.
	 * For instance, if she clicks and drags to the right,
	 * the location at which she originally clicked will be the anchor,
	 * even if she later chooses to drag the mouse to the left of it.
	 * Another example: if she shift-clicks near E, B will become (and stay) the anchor.
	 */

	Graphics_setWindow (d_graphics, d_startWindow, d_endWindow, 0, 1);

	double anchorForDragging = xbegin;   // the default (for if the shift key isn't pressed)
	if (shiftKeyPressed) {
		/*
		 * Extend the selection.
		 * We should always end up with a real selection (B < E),
		 * even if we start with the reversed temporal order (E < B).
		 */
		bool reversed = d_startSelection > d_endSelection;
		double firstMark = reversed ? d_endSelection : d_startSelection;
		double secondMark = reversed ? d_startSelection : d_endSelection;
		/*
		 * Undraw the old selection.
		 */
		if (d_endSelection > d_startSelection) {
			/*
			 * Determine the visible part of the old selection.
			 */
			double startVisible = d_startSelection > d_startWindow ? d_startSelection : d_startWindow;
			double endVisible = d_endSelection < d_endWindow ? d_endSelection : d_endWindow;
			/*
			 * Undraw the visible part of the old selection.
			 */
			if (endVisible > startVisible) {
				v_unhighlightSelection (startVisible, endVisible, 0, 1);
				//Graphics_flushWs (d_graphics);
			}
		}
		if (xbegin >= secondMark) {
		 	/*
			 * She clicked right from the second mark (usually E). We move E.
			 */
			d_endSelection = xbegin;
			anchorForDragging = d_startSelection;
		} else if (xbegin <= firstMark) {
		 	/*
			 * She clicked left from the first mark (usually B). We move B.
			 */
			d_startSelection = xbegin;
			anchorForDragging = d_endSelection;
		} else {
			/*
			 * She clicked in between the two marks. We move the nearest mark.
			 */
			double distanceOfClickToFirstMark = fabs (xbegin - firstMark);
			double distanceOfClickToSecondMark = fabs (xbegin - secondMark);
			/*
			 * We make sure that the marks are in the unmarked B - E order.
			 */
			if (reversed) {
				/*
				 * Swap B and E.
				 */
				d_startSelection = firstMark;
				d_endSelection = secondMark;
			}
			/*
			 * Move the nearest mark.
			 */
			if (distanceOfClickToFirstMark < distanceOfClickToSecondMark) {
				d_startSelection = xbegin;
				anchorForDragging = d_endSelection;
			} else {
				d_endSelection = xbegin;
				anchorForDragging = d_startSelection;
			}
		}
		/*
		 * Draw the new selection.
		 */
		if (d_endSelection > d_startSelection) {
			/*
			 * Determine the visible part of the new selection.
			 */
			double startVisible = d_startSelection > d_startWindow ? d_startSelection : d_startWindow;
			double endVisible = d_endSelection < d_endWindow ? d_endSelection : d_endWindow;
			/*
			 * Draw the visible part of the new selection.
			 */
			if (endVisible > startVisible)
				v_highlightSelection (startVisible, endVisible, 0, 1);
		}
	}
	/*
	 * Find out whether this is a click or a drag.
	 */
    
	while (Graphics_mouseStillDown (d_graphics)) {
		Graphics_getMouseLocation (d_graphics, & x, & y);
		if (x < d_startWindow) x = d_startWindow;
		if (x > d_endWindow) x = d_endWindow;
		if (fabs (Graphics_dxWCtoMM (d_graphics, x - xbegin)) > 1.5) {
			drag = true;
			break;
		}
	}
    
	if (drag) {
		/*
		 * First undraw the old selection.
		 */
		if (d_endSelection > d_startSelection) {
			/*
			 * Determine the visible part of the old selection.
			 */
			double startVisible = d_startSelection > d_startWindow ? d_startSelection : d_startWindow;
			double endVisible = d_endSelection < d_endWindow ? d_endSelection : d_endWindow;
			/*
			 * Undraw the visible part of the old selection.
			 */
			if (endVisible > startVisible)
				v_unhighlightSelection (startVisible, endVisible, 0, 1);
		}
		/*
		 * Draw the text at least once.
		 */
		/*if (x < d_startWindow) x = d_startWindow; else if (x > d_endWindow) x = d_endWindow;*/
        drawWhileDragging (this, anchorForDragging, x);
		/*
		 * Draw the dragged selection at least once.
		 */
		{
			double x1, x2;
			if (x > anchorForDragging) x1 = anchorForDragging, x2 = x; else x1 = x, x2 = anchorForDragging;
			v_highlightSelection (x1, x2, 0, 1);
		}
		/*
		 * Drag for the new selection.
		 */
        
		while (Graphics_mouseStillDown (d_graphics))
		{
			double xold = x, x1, x2;
			Graphics_getMouseLocation (d_graphics, & x, & y);
			/*
			 * Clip to the visible window. Ideally, we should perform autoscrolling instead, though...
			 */
			if (x < d_startWindow) x = d_startWindow; else if (x > d_endWindow) x = d_endWindow;
            
			if (x == xold)
				continue;
            
			/*
			 * Undraw previous dragged selection.
			 */
			if (xold > anchorForDragging) x1 = anchorForDragging, x2 = xold; else x1 = xold, x2 = anchorForDragging;
			if (x1 != x2) v_unhighlightSelection (x1, x2, 0, 1);
			/*
			 * Undraw the text.
			 */
			drawWhileDragging (this, anchorForDragging, xold);
			/*
			 * Redraw the text at the new location.
			 */
            drawWhileDragging (this, anchorForDragging, x);
			/*
			 * Draw new dragged selection.
			 */
			if (x > anchorForDragging) x1 = anchorForDragging, x2 = x; else x1 = x, x2 = anchorForDragging;
			if (x1 != x2) v_highlightSelection (x1, x2, 0, 1);
        } ;
		/*
		 * Set the new selection.
		 */
		if (x > anchorForDragging) d_startSelection = anchorForDragging, d_endSelection = x;
		else d_startSelection = x, d_endSelection = anchorForDragging;
	} else if (! shiftKeyPressed) {
		/*
		 * Move the cursor to the clicked position.
		 */
		d_startSelection = d_endSelection = xbegin;
	}
	return FunctionEditor_UPDATE_NEEDED;
}

int structFunctionEditor :: v_clickB (double xWC, double yWC) {
	(void) yWC;
	d_startSelection = xWC;
	if (d_startSelection > d_endSelection) {
		double dummy = d_startSelection;
		d_startSelection = d_endSelection;
		d_endSelection = dummy;
	}
	return 1;
}

int structFunctionEditor :: v_clickE (double xWC, double yWC) {
	d_endSelection = xWC;
	(void) yWC;
	if (d_startSelection > d_endSelection) {
		double dummy = d_startSelection;
		d_startSelection = d_endSelection;
		d_endSelection = dummy;
	}
	return 1;
}

void FunctionEditor_insetViewport (FunctionEditor me) {
	Graphics_setViewport (my d_graphics, my functionViewerLeft + MARGIN, my functionViewerRight - MARGIN,
		BOTTOM_MARGIN + space * 3, my height - (TOP_MARGIN + space));
	Graphics_setWindow (my d_graphics, my d_startWindow, my d_endWindow, 0, 1);
}

int structFunctionEditor :: v_playCallback (int phase, double a_tmin, double a_tmax, double t) {
	/*
	 * This callback will often be called by the Melder workproc during playback.
	 * However, it will sometimes be called by Melder_stopPlaying with phase=3.
	 * This will occur at unpredictable times, perhaps when the LongSound is updated.
	 * So we had better make no assumptions about the current viewport.
	 */
	double x1NDC, x2NDC, y1NDC, y2NDC;
	(void) a_tmin;
	Graphics_inqViewport (d_graphics, & x1NDC, & x2NDC, & y1NDC, & y2NDC);
	FunctionEditor_insetViewport (this);
	Graphics_xorOn (d_graphics, Graphics_MAROON);
	/*
	 * Undraw the play cursor at its old location.
	 * BUG: during scrolling, zooming, and exposure, an ugly line may remain.
	 */
	if (phase != 1 && playCursor >= d_startWindow && playCursor <= d_endWindow) {
		Graphics_setLineWidth (d_graphics, 3.0);
		Graphics_line (d_graphics, playCursor, 0, playCursor, 1);
		Graphics_setLineWidth (d_graphics, 1.0);
	}
	/*
	 * Draw the play cursor at its new location.
	 */
	if (phase != 3 && t >= d_startWindow && t <= d_endWindow) {
		Graphics_setLineWidth (d_graphics, 3.0);
		Graphics_line (d_graphics, t, 0, t, 1);
		Graphics_setLineWidth (d_graphics, 1.0);
	}
	Graphics_xorOff (d_graphics);
	/*
	 * Usually, there will be an event test after each invocation of this callback,
	 * because the asynchronicity is kMelder_asynchronicityLevel_INTERRUPTABLE or kMelder_asynchronicityLevel_ASYNCHRONOUS.
	 * However, if the asynchronicity is just kMelder_asynchronicityLevel_CALLING_BACK,
	 * there is no event test. Which means: no server round trip.
	 * Which means: no automatic flushing of graphics output.
	 * So: we force the flushing ourselves, lest we see too few moving cursors.
	 */
	Graphics_flushWs (d_graphics);
	Graphics_setViewport (d_graphics, x1NDC, x2NDC, y1NDC, y2NDC);
	playCursor = t;
	if (phase == 3) {
		if (t < a_tmax && MelderAudio_stopWasExplicit ()) {
			if (t > d_startSelection && t < d_endSelection)
				d_startSelection = t;
			else
				d_startSelection = d_endSelection = t;
			v_updateText ();
			/*Graphics_updateWs (d_graphics);*/ drawNow (this);
			updateGroup (this);
		}
		playingCursor = false;
		playingSelection = false;
	}
	return 1;
}

int theFunctionEditor_playCallback (void *void_me, int phase, double a_tmin, double a_tmax, double t) {
	iam (FunctionEditor);
	return my v_playCallback (phase, a_tmin, a_tmax, t);
}

void structFunctionEditor :: v_highlightSelection (double left, double right, double bottom, double top) {
	Graphics_highlight (d_graphics, left, right, bottom, top);
}

void structFunctionEditor :: v_unhighlightSelection (double left, double right, double bottom, double top) {
	Graphics_unhighlight (d_graphics, left, right, bottom, top);
}

void FunctionEditor_init (FunctionEditor me, const wchar_t *title, Function data) {
	my d_tmin = data -> xmin;   // set before adding children (see group button)
	my d_tmax = data -> xmax;
	Editor_init (me, 0, 0, my pref_shellWidth (), my pref_shellHeight (), title, data);

	my d_startWindow = my d_tmin;
	my d_endWindow = my d_tmax;
	my d_startSelection = my d_endSelection = 0.5 * (my d_tmin + my d_tmax);
	#if motif
		Melder_assert (XtWindow (my drawingArea -> d_widget));
	#endif
	my d_graphics = Graphics_create_xmdrawingarea (my drawingArea);
	Graphics_setFontSize (my d_graphics, 12);

// This exdents because it's a hack:
struct structGuiDrawingAreaResizeEvent event = { my drawingArea, 0 };
event. width  = my drawingArea -> f_getWidth  ();
event. height = my drawingArea -> f_getHeight ();
gui_drawingarea_cb_resize (me, & event);

	my v_updateText ();
	if (group_equalDomain (my d_tmin, my d_tmax))
		gui_checkbutton_cb_group (me, NULL);   // BUG: NULL
	my enableUpdates = true;
}

void FunctionEditor_marksChanged (FunctionEditor me, bool needsUpdateGroup) {
	my v_updateText ();
	updateScrollBar (me);
	/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
	if (needsUpdateGroup)
		updateGroup (me);
}

void FunctionEditor_updateText (FunctionEditor me) {
	my v_updateText ();
}

void FunctionEditor_redraw (FunctionEditor me) {
	/*Graphics_updateWs (my d_graphics);*/ drawNow (me);
}

void FunctionEditor_enableUpdates (FunctionEditor me, bool enable) {
	my enableUpdates = enable;
}

void FunctionEditor_ungroup (FunctionEditor me) {
	if (! my group) return;
	my group = false;
	my groupButton -> f_setValue (false);
	int i = 1;
	while (theGroup [i] != me) i ++;
	theGroup [i] = NULL;
	nGroup --;
	my v_updateText ();
	Graphics_updateWs (my d_graphics);   // for setting buttons in v_draw() method
}

void FunctionEditor_drawRangeMark (FunctionEditor me, double yWC, const wchar_t *yWC_string, const wchar_t *units, int verticalAlignment) {
	static MelderString text = { 0 };
	MelderString_empty (& text);
	MelderString_append (& text, yWC_string, units);
	double textWidth = Graphics_textWidth (my d_graphics, text.string) + Graphics_dxMMtoWC (my d_graphics, 0.5);
	Graphics_setColour (my d_graphics, Graphics_BLUE);
	Graphics_line (my d_graphics, my d_endWindow, yWC, my d_endWindow + textWidth, yWC);
	Graphics_setTextAlignment (my d_graphics, Graphics_LEFT, verticalAlignment);
	if (verticalAlignment == Graphics_BOTTOM) yWC -= Graphics_dyMMtoWC (my d_graphics, 0.5);
	Graphics_text (my d_graphics, my d_endWindow, yWC, text.string);
}

void FunctionEditor_drawCursorFunctionValue (FunctionEditor me, double yWC, const wchar_t *yWC_string, const wchar_t *units) {
	Graphics_setColour (my d_graphics, Graphics_CYAN);
	Graphics_line (my d_graphics, my d_startWindow, yWC, 0.99 * my d_startWindow + 0.01 * my d_endWindow, yWC);
	Graphics_fillCircle_mm (my d_graphics, 0.5 * (my d_startSelection + my d_endSelection), yWC, 1.5);
	Graphics_setColour (my d_graphics, Graphics_BLUE);
	Graphics_setTextAlignment (my d_graphics, Graphics_RIGHT, Graphics_HALF);
	Graphics_text2 (my d_graphics, my d_startWindow, yWC, yWC_string, units);
}

void FunctionEditor_insertCursorFunctionValue (FunctionEditor me, double yWC, const wchar_t *yWC_string, const wchar_t *units, double minimum, double maximum) {
	static MelderString text = { 0 };
	MelderString_empty (& text);
	MelderString_append (& text, yWC_string, units);
	double textX = my d_endWindow, textY = yWC, textWidth;
	int tooHigh = Graphics_dyWCtoMM (my d_graphics, maximum - textY) < 5.0;
	int tooLow = Graphics_dyWCtoMM (my d_graphics, textY - minimum) < 5.0;
	if (yWC < minimum || yWC > maximum) return;
	Graphics_setColour (my d_graphics, Graphics_CYAN);
	Graphics_line (my d_graphics, 0.99 * my d_endWindow + 0.01 * my d_startWindow, yWC, my d_endWindow, yWC);
	Graphics_fillCircle_mm (my d_graphics, 0.5 * (my d_startSelection + my d_endSelection), yWC, 1.5);
	if (tooHigh) {
		if (tooLow) textY = 0.5 * (minimum + maximum);
		else textY = maximum - Graphics_dyMMtoWC (my d_graphics, 5.0);
	} else if (tooLow) {
		textY = minimum + Graphics_dyMMtoWC (my d_graphics, 5.0);
	}
	textWidth = Graphics_textWidth (my d_graphics, text.string);
	Graphics_fillCircle_mm (my d_graphics, my d_endWindow + textWidth + Graphics_dxMMtoWC (my d_graphics, 1.5), textY, 1.5);
	Graphics_setColour (my d_graphics, Graphics_RED);
	Graphics_setTextAlignment (my d_graphics, Graphics_LEFT, Graphics_HALF);
	Graphics_text (my d_graphics, textX, textY, text.string);
}

void FunctionEditor_drawHorizontalHair (FunctionEditor me, double yWC, const wchar_t *yWC_string, const wchar_t *units) {
	Graphics_setColour (my d_graphics, Graphics_RED);
	Graphics_line (my d_graphics, my d_startWindow, yWC, my d_endWindow, yWC);
	Graphics_setTextAlignment (my d_graphics, Graphics_RIGHT, Graphics_HALF);
	Graphics_text2 (my d_graphics, my d_startWindow, yWC, yWC_string, units);
}

void FunctionEditor_drawGridLine (FunctionEditor me, double yWC) {
	Graphics_setColour (my d_graphics, Graphics_CYAN);
	Graphics_setLineType (my d_graphics, Graphics_DOTTED);
	Graphics_line (my d_graphics, my d_startWindow, yWC, my d_endWindow, yWC);
	Graphics_setLineType (my d_graphics, Graphics_DRAWN);
}

void FunctionEditor_garnish (FunctionEditor me) {
	if (my pref_picture_drawSelectionTimes ()) {
		if (my d_startSelection >= my d_startWindow && my d_startSelection <= my d_endWindow)
			Graphics_markTop (my pictureGraphics, my d_startSelection, true, true, false, NULL);
		if (my d_endSelection != my d_startSelection && my d_endSelection >= my d_startWindow && my d_endSelection <= my d_endWindow)
			Graphics_markTop (my pictureGraphics, my d_endSelection, true, true, false, NULL);
	}
	if (my pref_picture_drawSelectionHairs ()) {
		if (my d_startSelection >= my d_startWindow && my d_startSelection <= my d_endWindow)
			Graphics_markTop (my pictureGraphics, my d_startSelection, false, false, true, NULL);
		if (my d_endSelection != my d_startSelection && my d_endSelection >= my d_startWindow && my d_endSelection <= my d_endWindow)
			Graphics_markTop (my pictureGraphics, my d_endSelection, false, false, true, NULL);
	}
}

/* End of file FunctionEditor.cpp */
