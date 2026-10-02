# NexTerm CLI (nx)

First-party NexTerm command-line interface.

## Commands

    nx help
    nx version
    nx doctor
    nx shell
    nx session list
    nx session open [id]
    nx session close <id>
    nx package search <name>
    nx package install <name>
    nx package remove <name>
    nx package update
    nx package list
    nx api health

The native CLI stores local session and package state under the user's NexTerm state directory. Set NEXTERM_PREFIX for a configured userspace and NEXTERM_API_BASE for an API endpoint.

The Android-facing Kotlin package model lives in packages/cli/src/main/kotlin and shares package metadata semantics with the CLI layer.
