package com.hacrrrrrrr.nexterm

import android.content.Context
import java.io.File

object NexTermEnvironment {
    fun root(context: Context) = File(context.filesDir, "nexterm")
    fun prefix(context: Context) = File(root(context), "prefix")
    fun home(context: Context) = File(root(context), "home")
    fun bin(context: Context) = File(prefix(context), "bin")
    fun tmp(context: Context) = File(root(context), "tmp")
    fun packages(context: Context) = File(root(context), "packages")

    fun initialize(context: Context) {
        listOf(root(context), prefix(context), home(context), bin(context), tmp(context), packages(context))
            .forEach { it.mkdirs() }
        File(home(context), ".profile").writeText(
            "export PREFIX='" + prefix(context).absolutePath + "'\n" +
            "export HOME='" + home(context).absolutePath + "'\n" +
            "export TMPDIR='" + tmp(context).absolutePath + "'\n" +
            "export PATH='\$PREFIX/bin:/system/bin:/system/xbin'\n"
        )
    }
}
