# NexTerm v0.4 architecture

NexTerm is moving from an Android-shell frontend toward a real unprivileged Linux userspace.

    Android UI
        |
    Terminal / VT parser
        |
    Session service
        |
    Native PTY
        |
    NexTerm userspace
        |
    prefix/bin + package store

The Android system shell is only bootstrap/fallback. The long-term shell executes from the NexTerm prefix.

## Userspace

NexTerm owns prefix/, home/, tmp/, packages/ and bin/ under private app storage. No root access is assumed.

## Package security

Downloaded packages must be verified before execution. Extraction must reject absolute paths and ../ traversal and remain inside the private prefix.

## v0.4 milestones

- Bootstrap environment
- Persistent session architecture
- Native PTY lifecycle
- Verified package contract
- VT parser architecture
- Browser pairing architecture
