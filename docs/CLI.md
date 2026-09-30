# NexTerm CLI

The official first-party CLI is called nx.

Current command surface:

    nx help
    nx version
    nx doctor
    nx shell
    nx session list
    nx session open
    nx session close <id>
    nx package search <name>
    nx package install <name>
    nx package remove <name>
    nx package update
    nx package list
    nx api health

The current implementation is a real native C++ executable. Service-backed commands report their current backend state instead of pretending the package repository or API is already complete.

Roadmap:
- connect sessions to the real PTY/session manager
- connect packages to the signed package database
- connect API commands to the NexTerm API
- shell completion
- JSON output
