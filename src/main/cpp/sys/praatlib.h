//
//  praatlib.h
//  praat64
//

#ifndef _praatlib_h
#define _praatlib_h

#ifdef WIN32
#ifdef __cplusplus
#define PRAAT_LIB_EXPORT extern "C" __declspec( dllexport )
#else
#define PRAAT_LIB_EXPORT __declspec( dllexport )
#endif
#else
#ifdef __cplusplus
#define PRAAT_LIB_EXPORT extern "C"
#else
#define PRAAT_LIB_EXPORT
#endif
#endif

#endif
