package com.hacrrrrrrr.nexterm.pkg

/** Shared package metadata model used by the NexTerm package layer. */
data class PackageSpec(
    val name: String,
    val version: String,
    val architecture: String,
    val sha256: String? = null
) {
    init {
        require(name.matches(Regex("[A-Za-z0-9._+-]+"))) { "Invalid package name: $name" }
        require(version.isNotBlank()) { "Package version must not be empty" }
        require(architecture.isNotBlank()) { "Package architecture must not be empty" }
    }
}
