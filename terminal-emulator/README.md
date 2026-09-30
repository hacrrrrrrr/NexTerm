# Terminal Emulator

Presentation and input layer shared by NexTerm clients.

Components: TerminalView, TerminalBuffer, AnsiParser, TerminalKeyMapper, TerminalSession.

The emulator is separated from the PTY backend so Android and browser clients can use the same session protocol.
