# NexTerm Changelog

## v0.4.0 — NexTerm Userspace Foundation

### Added
- Private NexTerm userspace layout: prefix, home, tmp, packages and bin.
- Environment initialization and diagnostics.
- Real userspace architecture and package-system security contract.
- Persistent-session architecture groundwork.

### Important
v0.4 is a foundation release. It does not claim root access, a complete Linux distribution, or a production package repository. The Android system shell remains a bootstrap/fallback until the NexTerm userspace runtime is populated.

### Next
- Persistent session service.
- Real VT parser and terminal state machine.
- Package bootstrap runtime and signed repository.
- Browser-to-device pairing.
- SSH/SFTP.
