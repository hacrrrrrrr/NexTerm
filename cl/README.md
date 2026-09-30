# NexTerm CLI

The `nx` CLI is NexTerm's command-line control plane.

## Commands

    nx version
    nx doctor
    nx shell
    nx session list
    nx session open
    nx package search <name>
    nx package install <name>
    nx package update
    nx api health

## Termux-oriented workflow

NexTerm aims for a familiar Android terminal workflow: POSIX shell usage plus common tools such as Git, SSH, curl and standard Unix utilities.

NexTerm-specific functionality uses the `nx` namespace. Existing Unix programs remain normal userspace programs rather than being fake implementations inside the CLI.

NexTerm does not claim binary compatibility with Termux; compatibility will be documented as features mature.
