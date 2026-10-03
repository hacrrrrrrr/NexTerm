# Security Policy

## Scope

NexTerm combines an Android PTY, a private userspace, browser pairing, and a WebSocket session protocol. Security bugs affecting command execution, session isolation, credential handling, package installation, path traversal, or remote browser access are in scope.

## Hardening rules

- Browser pairing uses short-lived bearer credentials.
- Pairing credentials must be validated server-side for session, token and expiry.
- Production browser connections must use WSS.
- Long-lived credentials must never be embedded in QR payloads.
- Package archives must verify integrity before extraction.
- Archive extraction must reject absolute paths and `..` traversal.
- PTY dimensions and protocol message sizes are bounded.
- Browser/gateway implementations should enforce origin allowlists and rate limits.
- Do not expose an Android PTY directly to an untrusted network.

## Reporting

Please report suspected vulnerabilities privately to **hunterkritik@gmail.com** rather than opening a public issue with an exploitable proof of concept.

Include the affected version/commit, component, reproduction steps, impact, and any mitigation you identified.
