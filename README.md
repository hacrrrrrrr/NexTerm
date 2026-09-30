# NexTerm

**Real Android terminal + browser terminal project.**

> Sponsorship / collaboration: **hunterkritik@gmail.com**

NexTerm is being built as a real native Termux-class Android terminal, not a command-output simulator.

## Two-in-one vision

**Android app:** Android UI -> Terminal Emulator -> PTY -> NDK -> Linux/Android userspace.

**Browser client:** Browser -> authenticated WebSocket -> NexTerm Gateway -> PTY.

The browser side does not fake a shell. It requires an explicit gateway/session.

## Project layout

    app/
    terminal-emulator/
    nexterm-shared/
    browser/
    docs/

## Features

- Real kernel-backed PTY sessions
- Native NDK process layer
- Dedicated terminal-emulator module
- Java + Kotlin + C++ architecture
- Shared Android/browser session protocol
- Browser terminal UI
- VT100/VT220/xterm parser roadmap
- 256-color and true-color rendering
- Cursor and alternate-screen support
- Mouse reporting, selection and clipboard
- Tabs, splits and persistent sessions
- Searchable scrollback and session recording
- Hardware keyboard and custom terminal toolbar
- File manager and environment profiles
- Dedicated PREFIX filesystem
- Native package manager and signed package repository
- Git, OpenSSH, curl, Python and Clang
- Rootless Linux distributions
- NexTerm-native CLI and package manager
- Browser WebSocket gateway and authentication
- PWA-friendly browser terminal

## Security

NexTerm will not expose a privileged Android shell to arbitrary web content. Android recommends origin-allowlisted WebView messaging for native/web communication rather than unrestricted JavaScript bridges.

## Status

Early engineering stage. The PTY foundation is real; the complete Linux userspace, package manager, production terminal parser, browser gateway and polished UI are under development.

## Build

Open in Android Studio with Android SDK 35 and NDK 27.2.12479018.

    ./gradlew :app:assembleDebug

    adb install app/build/outputs/apk/debug/app-debug.apk

## Sponsorship

For sponsorship, hardware support, infrastructure or collaboration:

**hunterkritik@gmail.com**
