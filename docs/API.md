# NexTerm API Documentation

> **Status:** Early development  
> **API version:** `v1`  
> **Sponsorship / collaboration:** hunterkritik@gmail.com

NexTerm provides a first-party API for the Android application, browser tools, CLI, package services and terminal sessions.

## API design

The public API is versioned under:

```
/api/v1
```

Architecture:

```
Client
  ├── HTTP API       → control, metadata, packages
  ├── WebSocket API  → interactive terminal sessions
  └── OpenAPI        → documentation and SDK generation
```

## Planned production base URL

```
https://api.nexterm.github.io/api/v1
```

This is the planned developer-facing hostname. GitHub Pages can host the documentation and static developer portal, but a real server-side API requires a backend service.

## Authentication

User-specific and terminal-session operations will require authentication.

```
Authorization: Bearer <access-token>
```

Health and capability endpoints may remain public.

Never commit API tokens or secrets to the repository.

## Core endpoints

### Health

```
GET /api/v1/health
```

Example:

```json
{
  "status": "ok",
  "service": "nexterm-api",
  "api_version": "v1"
}
```

### Capabilities

```
GET /api/v1/capabilities
```

Returns supported API capabilities and protocol versions.

### Sessions

```
POST   /api/v1/sessions
GET    /api/v1/sessions
GET    /api/v1/sessions/{session_id}
DELETE /api/v1/sessions/{session_id}
```

Interactive terminal traffic will use the NexTerm WebSocket protocol rather than exposing a raw PTY directly over HTTP.

### Packages

```
GET /api/v1/packages
GET /api/v1/packages/{package_name}
```

Package operations will verify metadata, checksums and signatures before installation.

## WebSocket API

Interactive sessions are planned under:

```
wss://<api-host>/ws/v1/sessions/{session_id}
```

The protocol will carry terminal input, output, resize events and session state.

Example input:

```json
{
  "type": "input",
  "data": "ls -la\\n"
}
```

Example output:

```json
{
  "type": "output",
  "data": "..."
}
```

The wire protocol will be versioned before production release.

## NexTerm CLI

The first-party CLI is `nx`:

```bash
nx api health
nx session list
nx session open
nx package search <name>
nx package install <name>
```

The CLI, Android app and browser client will use the same versioned API/session model.

## OpenAPI

The initial contract is:

```
api/openapi.yaml
```

It will become the source for API documentation, API Studio, SDK generation, examples and automated validation.

## Versioning

NexTerm uses explicit versions:

```
/api/v1
/api/v2
```

Breaking changes require a new major API version. Backward-compatible additions can be introduced within the current major version.

## Error format

Planned error response:

```json
{
  "error": {
    "code": "SESSION_NOT_FOUND",
    "message": "The requested session does not exist.",
    "request_id": "req_..."
  }
}
```

Clients should use the machine-readable `code`, not parse human-readable messages.

## Security requirements

Before public deployment, NexTerm must implement:

- Authentication and authorization
- TLS
- Request validation
- Rate limiting
- Session ownership and isolation
- Package signature verification
- Audit logging
- Secret/token protection
- WebSocket authentication

The API must never provide arbitrary unauthenticated access to an Android device shell.

## Developer portal

The NexTerm API Studio is a static GitHub Pages application for generating and inspecting OpenAPI contracts and developer examples.

Planned portal sections:

```
API Studio
API Documentation
OpenAPI
API Explorer
SDK Generator
Examples
```

## Roadmap

1. Implement the real API server.
2. Implement authentication.
3. Connect sessions to the PTY/session manager.
4. Connect package endpoints to the NexTerm package database.
5. Implement the WebSocket terminal protocol.
6. Add API integration tests.
7. Deploy the production backend.
8. Connect the developer portal to the production API.

NexTerm API is currently an engineering project and is not yet a production service.
