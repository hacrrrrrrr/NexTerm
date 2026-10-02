package com.hacrrrrrrr.nexterm.pkg

import java.util.concurrent.ConcurrentHashMap

/** In-memory package index used by the CLI and Android package UI. */
class PackageIndex {
    private val packages = ConcurrentHashMap<String, PackageSpec>()

    fun publish(spec: PackageSpec) {
        packages[spec.name] = spec
    }

    fun find(name: String): PackageSpec? = packages[name]

    fun list(): List<PackageSpec> = packages.values.sortedBy { it.name }

    fun remove(name: String): Boolean = packages.remove(name) != null
}
