# NexTerm Packages

NexTerm's own package system provides the metadata and local package-state foundation for reproducible, signed, architecture-aware packages.

## Kotlin package layer

packages/cli/src/main/kotlin contains the shared Kotlin package model:

- PackageSpec — validates package name, version and architecture metadata.
- PackageIndex — thread-safe in-memory package index for Android/UI-facing consumers.

## CLI commands

    nx package search <name>
    nx package install <name>
    nx package remove <name>
    nx package update
    nx package list
    nx package info <name>

The native CLI keeps local package state under the NexTerm state directory. Remote installation remains gated on a configured NexTerm repository; the CLI does not claim a package was downloaded when no repository is configured.

Packages are installed inside the NexTerm userspace and do not modify Android system partitions.
