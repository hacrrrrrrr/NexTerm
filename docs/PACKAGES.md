# NexTerm package system

A package contains name, version, architecture, archive URL, SHA-256 and dependencies.

The installer must download over HTTPS, verify SHA-256 before extraction, extract into a temporary directory, reject absolute paths and ../ traversal, atomically activate under prefix/, record metadata, and never write outside the NexTerm prefix.

The production repository should add signed metadata. Checksums detect corruption; signatures establish repository authenticity.

An index entry is not an installed package until verification succeeds.
