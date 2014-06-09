/* sendpraat.h */
/* Paul Boersma, September 27, 2000 */


#ifdef PRAAT_LIB
#include "praatlib.h"
#endif

#ifdef __cplusplus
	extern "C" {
#endif

PRAAT_LIB_EXPORT char *sendpraat (void *display, const char *programName, long timeOut, const char *text);

#ifdef __cplusplus
	}
#endif

/* End of file sendpraat.h */
