package com.hacrrrrrrr.nexterm

import java.io.File

class PtyProcess(
    private val home: File,
    private val prefix: File,
    private val onOutput: (ByteArray) -> Unit,
    private val onExit: (Int) -> Unit
) {
    companion object { init { System.loadLibrary("nexterm-pty") } }
    @JvmField var nativeFd: Int = -1
    private var pid = -1
    @Volatile private var running = false
    private var reader: Thread? = null
    private external fun nativeSpawn(shell: String, home: String, path: String): Int
    private external fun nativeRead(out: ByteArray): Int
    private external fun nativeWrite(data: ByteArray, len: Int): Int
    private external fun nativeResize(rows: Int, cols: Int)
    private external fun nativeClose()

    fun start() {
        home.mkdirs(); prefix.mkdirs()
        val path = prefix.absolutePath + "/bin:" + home.absolutePath + "/bin:/system/bin:/system/xbin:/vendor/bin"
        val runtimeShell = File(prefix, "bin/sh")
        val shell = if (runtimeShell.canExecute()) runtimeShell.absolutePath else "/system/bin/sh"
        pid = nativeSpawn(shell, home.absolutePath, path)
        if (pid < 0) { onExit(127); return }
        running = true
        reader = Thread {
            val buffer = ByteArray(8192)
            while (running) {
                val n = nativeRead(buffer)
                if (n <= 0) break
                onOutput(buffer.copyOf(n))
            }
            running = false
            onExit(0)
        }.apply { name = "NexTerm-PTY-Reader"; start() }
    }
    fun write(text:String) {
        if (!running) return
        val bytes=text.toByteArray(Charsets.UTF_8); nativeWrite(bytes,bytes.size)
    }
    fun resize(rows:Int,cols:Int)=nativeResize(rows,cols)
    fun close(){running=false;nativeClose();reader?.interrupt()}
}
