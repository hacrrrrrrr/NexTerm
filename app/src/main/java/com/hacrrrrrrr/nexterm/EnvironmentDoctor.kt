package com.hacrrrrrrr.nexterm

import android.content.Context

data class DoctorResult(val name: String, val ok: Boolean, val detail: String)

object EnvironmentDoctor {
    fun check(context: Context): List<DoctorResult> {
        NexTermEnvironment.initialize(context)
        val root = NexTermEnvironment.root(context)
        val home = NexTermEnvironment.home(context)
        val bin = NexTermEnvironment.bin(context)
        return listOf(
            DoctorResult("userspace", root.isDirectory, root.absolutePath),
            DoctorResult("home", home.isDirectory, home.absolutePath),
            DoctorResult("bin", bin.isDirectory, bin.absolutePath),
            DoctorResult("writable", root.canWrite(), "private app storage"),
            DoctorResult("architecture", true, System.getProperty("os.arch") ?: "unknown")
        )
    }
}
