# NexTerm Browser Pairing Guide

## Goal

Generate a temporary pairing credential on Android, encode it as a QR code, and let the NexTerm browser client connect to the corresponding authenticated gateway session.

## Payload

The payload uses the custom URI scheme:

    nexterm://pair?endpoint=<url>&session=<id>&token=<temporary-token>&expires=<unix-ms>

The Android PairingSessionManager creates the session ID and token using secure random bytes and defaults to a five-minute lifetime.

## End-to-end requirements

A complete deployment needs three pieces:

1. Android NexTerm: creates the pairing session and owns the PTY.
2. NexTerm Gateway: authenticates the temporary token and bridges the WebSocket to the selected PTY.
3. Browser client: receives the QR payload and opens the WebSocket connection.

Vercel can host the browser client, but it is not the PTY gateway. The gateway needs a persistent server/runtime capable of accepting WebSocket connections and securely reaching the Android session.

## Test payload

    nexterm://pair?endpoint=wss%3A%2F%2Fgateway.example%2Fsession&session=demo-session&token=demo-token&expires=4102444800000

Use this only to verify browser parsing. It is not a live connection.

## Local development

For a gateway on a computer at 192.168.1.50:8080, a development payload could point to:

    ws://192.168.1.50:8080/session

The Android phone and computer must be reachable on the same network. For production use HTTPS/WSS and server-side authentication.

## User instructions

1. Start a NexTerm session on Android.
2. Choose Browser Pairing / Connect Browser.
3. Generate a temporary pairing session.
4. Display the QR code.
5. Open the NexTerm browser client.
6. Scan the QR or paste the payload.
7. Confirm the browser reports an authenticated connection.
8. Revoke/expire the pairing credential after handoff.

## Current v0.5.7 boundary

The session and browser payload contract are implemented. The production gateway and Android QR UI are not yet presented as complete. v0.6 should implement the missing end-to-end gateway/session service.