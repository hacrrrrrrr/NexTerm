package com.hacrrrrrrr.nexterm

import android.content.Context
import java.io.File
import java.security.MessageDigest

data class RuntimeStatus(val installed:Boolean,val version:String,val architecture:String,val prefix:String)

object RuntimeManager {
    const val RUNTIME_VERSION="0.5.0"
    fun status(context:Context):RuntimeStatus {
        NexTermEnvironment.initialize(context)
        val marker=File(NexTermEnvironment.prefix(context),".runtime")
        return RuntimeStatus(marker.isFile,if(marker.isFile) marker.readText().trim() else "none",System.getProperty("os.arch") ?: "unknown",NexTermEnvironment.prefix(context).absolutePath)
    }
    fun installBootstrap(context:Context):RuntimeStatus {
        NexTermEnvironment.initialize(context)
        val p=NexTermEnvironment.prefix(context)
        listOf("bin","lib","share","etc").forEach{File(p,it).mkdirs()}
        File(p,".runtime").writeText(RUNTIME_VERSION)
        return status(context)
    }
    fun sha256(file:File):String {
        val d=MessageDigest.getInstance("SHA-256")
        file.inputStream().use{input->val b=ByteArray(8192);while(true){val n=input.read(b);if(n<0)break;d.update(b,0,n)}}
        return d.digest().joinToString(""){"%02x".format(it)}
    }
}
