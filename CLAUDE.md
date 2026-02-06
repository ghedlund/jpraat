# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

jpraat is a Java 21 binding library for the [Praat](https://www.fon.hum.uva.nl/praat/) speech analysis toolkit. It uses JNA (Java Native Access) to call into a native C++ Praat library (`libjpraat`) without JNI boilerplate.

## Build Commands

```bash
./gradlew build                        # Build and run tests
./gradlew build -x test                # Build without tests
./gradlew test                         # Run all tests
./gradlew test --tests TestVersion     # Run a single test class
./gradlew test --tests TestTextGrid.testRead  # Run a single test method
```

Distribution is via GitHub Maven Packages (`ca.hedlund:jpraat`). Publish with `./gradlew publish`.

## Architecture

### JNA Binding Layer

`Praat.java` is the central JNA interface. It declares all native method bindings and loads the `jpraat` shared library at class init. **Do not call Praat.INSTANCE methods directly** — use the higher-level wrapper classes.

Thread safety: A static `ReentrantLock wrapperLock` guards all native calls. Every wrapped method must acquire this lock, call the native function, then call `Praat.checkAndClearLastError()` before releasing.

### Object Hierarchy

All Praat objects extend `Thing` (a JNA `PointerType`) and implement `AutoCloseable`:

```
Thing → Daata → Function → Sampled → Sound / LongSound
                         → TextGrid, Pitch, Formant, Intensity, Spectrum, Spectrogram
              → Table, TableOfReal
```

Key packages under `ca.hedlund.jpraat.binding`:
- `fon/` — Phonetic analysis objects (Sound, TextGrid, Pitch, Formant, Spectrum, etc.)
- `sys/` — Core system types (Thing, Daata, MelderFile, Interpreter, SendPraat)
- `stat/` — Statistical tables (Table, TableOfReal)
- `melder/` — Melder utility types and enums
- `jna/` — JNA infrastructure (NativeTypeMapper, Str32, NativeEnum converters)

### Annotation-Driven Code Generation

- `@Wrapped` — Method has a C++ wrapper (native name has `_wrapped` suffix). `WrapperGenerator` reads these to emit C++ wrapper source with mutex locking and error handling.
- `@Custom` — Pure Java method, no native counterpart.
- `@Declared` — Documents which C++ header/source declares the binding.

### Error Handling Pattern

All wrapped native calls follow this pattern:

```java
Praat.wrapperLock.lock();
try {
    result = Praat.INSTANCE.someMethod_wrapped(...);
    Praat.checkAndClearLastError();
} catch (PraatException e) {
    throw e;
} finally {
    Praat.wrapperLock.unlock();
}
```

### Native Library

Platform-specific native libraries are separate Maven artifacts (`libjpraat-darwin`, `libjpraat-win64`) versioned by the `libjpraatVersion` property in `build.gradle.kts`. These track upstream Praat releases (currently 6.3.02). Resolved from GitHub Packages — requires `gpr.user`/`gpr.key` in `gradle.properties` or `GITHUB_ACTOR`/`GITHUB_TOKEN` env vars.

### Tests

JUnit 4. Each test must call `Praat.initLibrary()` in `@Before`. Test resources (WAV, TextGrid) are in `src/test/resources/ca/hedlund/jpraat/binding/fon/`. Tests fork per class (`forkEvery = 1`).

### Java Module (JPMS)

Module name: `ca.hedlund.jpraat` (`src/main/java/module-info.java`). Requires `com.sun.jna` transitively. Binding packages are opened to `com.sun.jna` for reflective access.
