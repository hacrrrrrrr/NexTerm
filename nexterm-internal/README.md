# NexTerm Internal

Internal platform interfaces for sessions, configuration, packages and runtime services.

## Kotlin runtime layer

src/main/kotlin/com/hacrrrrrrr/nexterm/internal/SessionStore.kt provides a thread-safe session registry for Android-facing components. Sessions have stable IDs and creation timestamps and can be created, queried, closed, counted and cleared.

The existing Java API remains available for compatibility while new Android code can use the Kotlin API.

Internal APIs are versioned independently from the public NexTerm API.
