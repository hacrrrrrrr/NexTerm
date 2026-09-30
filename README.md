# NexTerm

NexTerm is a real native Android terminal project. It launches a real interactive Android shell through a kernel-backed PTY; it is not a command-output simulator.

## Architecture

Android UI -> Kotlin -> JNI/NDK -> forkpty() -> /system/bin/sh

The first runtime is rootless and app-private. Future releases will add a dedicated userspace, package manager, native developer tools, and optional rootless Linux distributions.

## Features in the bootstrap

- Real Linux/Android PTY session
- Native NDK process bridge
- Interactive /system/bin/sh
- PTY read/write/resize primitives
- App-private HOME
- Terminal keyboard handling
- No root requirement

## Roadmap

- Full VT/xterm parser and ANSI 256 colors
- Cursor, alternate screen, scrollback and selection
- Tabs and persistent sessions
- Dedicated PREFIX/userspace
- Signed package repository
- Bash, Git, OpenSSH, curl, Python and Clang
- Rootless PRoot Debian/Alpine/Ubuntu environments
- File manager and developer tools

## Build

Requirements: Android Studio, JDK 17+, Android SDK 35, NDK 27.2.12479018.

Build with:

    ./gradlew :app:assembleDebug

Install with:

    adb install app/build/outputs/apk/debug/app-debug.apk

NexTerm does not request root. Android's sandbox and security model remain in force.

## License

MIT
