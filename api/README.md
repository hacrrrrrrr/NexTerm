# NexTerm API

NexTerm owns a versioned API for applications, browser clients and automation.

Base path: /api/v1

- GET /health
- GET /capabilities
- GET /sessions
- POST /sessions
- DELETE /sessions/{id}
- GET /packages
- GET /packages/{name}

Interactive terminal data uses the NexTerm WebSocket session protocol instead of exposing a raw PTY endpoint.

Network deployments must enforce authentication, authorization, origin checks, rate limits and audit logging.
