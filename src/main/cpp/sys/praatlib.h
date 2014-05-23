//
//  praatlib.h
//  praat64
//

#ifndef _praatlib_h
#define _praatlib_h

#ifdef WIN32
#define PRAAT_LIB_EXPORT extern "C" __declspec( dllexport )
#else
#define PRAAT_LIB_EXPORT extern "C"
#endif

#endif
