# NexTerm Changelog

## v0.3.0 — Terminal Core Update

### Added
- Protocol v2 with client hello and session negotiation.
- Explicit PTY resize messages and protocol limits.
- Browser reconnect/status UI and keyboard handling.
- Browser session/token fields.
- Android command history and terminal clear action.
- Automatic Android PTY resize from the terminal viewport.
- API capability/version contract.

### Security
- Browser gateway sessions are explicitly authenticated by protocol design.
- Production deployments should use short-lived session credentials rather than permanent browser URL secrets.

### Next
- Production VT100/VT220/xterm parser.
- Alternate-screen and cursor state machine.
- Persistent background PTY service.
- Session reconnect after Android process recreation.
- Package bootstrap and signed repository metadata.
- Gateway rate limits and origin allowlists.
