package com.hacrrrrrrr.nexterm

import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class PtyProcess(
    private val home: File,
    private val prefix: File,
    private val onOutput: (ByteArray) -> Unit,
    private val onExit: (Int) -> Unit
) {
    companion object {
        init { System.loadLibrary("nexterm-pty") }
        private const val MIN_ROWS = 2
        private const val MAX_ROWS = 500
        private const val MIN_COLS = 8
        private const val MAX_COLS = 500
    }

    @JvmField var nativeFd: Int = -1
    private var pid = -1
    private val running = AtomicBoolean(false)
    private var reader: Thread? = null

    private external fun nativeSpawn(shell: String, home: String, path: String): Int
    private external fun nativeRead(out: ByteArray): Int
    private external fun nativeWrite(data: ByteArray, len: Int): Int
    private external fun nativeResize(rows: Int, cols: Int)
    private external fun nativeClose()

    fun start() {
        home.mkdirs()
        prefix.mkdirs()
        val path = prefix.absolutePath + "/bin:" +
            home.absolutePath + "/bin:/system/bin:/system/xbin:/vendor/bin"
        val runtimeShell = File(prefix, "bin/sh")
        val shell = if (runtimeShell.canExecute()) runtimeShell.absolutePath else "/system/bin/sh"

        pid = nativeSpawn(shell, home.absolutePath, path)
        if (pid < 0) {
            onExit(127)
            return
        }

        running.set(true)
        reader = Thread({
            val buffer = ByteArray(8192)
            var exitCode = 0
            while (running.get()) {
                val n = nativeRead(buffer)
                if (n > 0) {
                    onOutput(buffer.copyOf(n))
                    continue
                }
                if (n < 0) exitCode = 1
                break
            }
            running.set(false)
            onExit(exitCode)
        }, "NexTerm-PTY-Reader").also { it.start() }
    }

    fun write(text: String): Boolean {
        if (!running.get()) return false
        val bytes = text.toByteArray(Charsets.UTF_8)
        var offset = 0
        while (offset < bytes.size && running.get()) {
            val remaining = bytes.copyOfRange(offset, bytes.size)
            val written = nativeWrite(remaining, remaining.size)
            if (written <= 0) return false
            offset += written
        }
        return offset == bytes.size
    }

    fun resize(rows: Int, cols: Int) {
        if (rows !in MIN_ROWS..MAX_ROWS || cols !in MIN_COLS..MAX_COLS) return
        if (running.get()) nativeResize(rows, cols)
    }

    fun close() {
        if (running.getAndSet(false)) {
            nativeClose()
            reader?.interrupt()
        }
    }
}
