# NexTerm v0.5.7 — Browser to Android Pairing

Android creates a five-minute pairing session containing a random session ID and high-entropy token. The app renders its nexterm://pair payload as a QR code. A browser extracts the endpoint/session/token, opens the WebSocket and sends protocol-v2 hello. The gateway must authenticate the token, validate expiration and bind the browser to the requested PTY session.

Production endpoints should use WSS. Pairing is not identity proof. Tokens should be revoked after successful handoff.

v0.5.7 implements the pairing/session contract; it does not pretend a gateway exists where none is deployed.
