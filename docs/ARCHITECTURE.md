# NexTerm Architecture

Android: Android UI -> Terminal Emulator -> PTY Session -> NDK -> forkpty -> Android userspace.

Browser: Browser Terminal -> authenticated WebSocket -> NexTerm Gateway -> PTY Session.

The browser never receives an unrestricted native Android bridge. Web content and privileged native operations remain separated.
