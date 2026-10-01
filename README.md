# NexTerm

**Real Android terminal + browser terminal project.**

> Sponsorship / collaboration: **hunterkritik@gmail.com**

NexTerm is a native Android terminal project with a browser client for explicitly connected remote PTY sessions. It uses a real kernel-backed PTY rather than simulated command output.

## v0.5.0 — Actual Userspace Runtime Foundation

v0.5 establishes the runtime boundary: versioned private runtime directories, package verification primitives, and PTY selection of `prefix/bin/sh` when an executable NexTerm shell is installed. Until a tested runtime bundle is installed, Android's system shell remains the explicit bootstrap fallback.

### v0.5 additions
- Runtime manager and status
- Runtime `prefix/bin`, `lib`, `share`, `etc`
- SHA-256 package verification
- Safe extraction path validation
- PTY prefers the NexTerm runtime shell

**Next: v0.6 — ship and test the actual multi-ABI runtime bundle, package installer/repository, persistent sessions, VT parser and SSH/SFTP.**

## v0.4.0 — NexTerm Userspace Foundation

NexTerm now creates its own private unprivileged userspace (`prefix/`, `home/`, `tmp/`, `packages/`, `bin/`) on Android. This is the foundation for a real Linux environment rather than simply wrapping `/system/bin/sh`.

### v0.4 additions
- Private NexTerm userspace initialization
- Environment diagnostics
- Package verification/extraction contract
- Persistent-session architecture
- Security boundary documentation

**Important:** v0.4 does not pretend that a complete distro or package repository already exists. The remaining work is the runtime/bootstrap layer that populates this userspace.

## v0.3.0 — Terminal Core Update

- Real native Android PTY foundation
- Interactive command history
- Terminal clear action
- Automatic PTY resize from viewport size
- Browser WebSocket terminal with connection status
- Browser reconnect and keyboard handling
- Protocol v2 with authenticated `hello` and `resize`
- API capability/session contract
- GitHub Actions Android build validation

## Two-in-one architecture

**Android:** Android UI → TerminalView → PTY → NDK → Android/Linux userspace.

**Browser:** Browser → authenticated WebSocket → NexTerm Gateway → PTY.

The browser does not fake a shell; a gateway/session must explicitly provide the PTY.

## Project layout

    app/
    terminal-emulator/
    nexterm-shared/
    browser/
    api/
    cli/
    docs/

## Roadmap

### Terminal
- Production VT100/VT220/xterm parser
- ANSI colors, cursor state and alternate screen
- Tabs, splits and persistent background sessions
- Searchable scrollback and session recording

### Linux environment
- PREFIX/bootstrap filesystem
- Package manager and signed package metadata
- Git, OpenSSH, curl, Python and Clang integration
- Rootless Linux distribution support

### Browser / Gateway
- Authenticated session gateway
- Short-lived session credentials
- Origin allowlists and rate limits
- PWA/offline shell UI

### Developer platform
- `https://api.nexterm.github.io`
- OpenAPI specification in `api/openapi.yaml`
- Shared protocol in `nexterm-shared/protocol.json`
- CLI and SDK tooling

## Build Android

Requirements: Android Studio, Android SDK 35 and NDK 27.2.12479018.

    ./gradlew :app:assembleDebug
    adb install app/build/outputs/apk/debug/app-debug.apk

## Security

NexTerm must not expose a privileged Android shell to arbitrary web content. Production browser sessions should use short-lived credentials and an authenticated gateway. Do not place long-lived secrets in browser URLs.

## Status

**v0.3.0 — active engineering.** The PTY foundation is real; production terminal parsing, persistent sessions, package/bootstrap support and the gateway are the next major milestones.

## Sponsorship

For sponsorship, hardware support, infrastructure or collaboration:

**hunterkritik@gmail.com**

## License

MIT
