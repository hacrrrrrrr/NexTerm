package com.hacrrrrrrr.nexterm.internal

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/** Thread-safe session registry shared by Android-facing runtime components. */
class SessionStore {
    data class Session(
        val id: String = UUID.randomUUID().toString(),
        val createdAtMs: Long = System.currentTimeMillis()
    )

    private val sessions = ConcurrentHashMap<String, Session>()

    fun create(): Session = Session().also { sessions[it.id] = it }

    fun get(id: String): Session? = sessions[id]

    fun close(id: String): Boolean = sessions.remove(id) != null

    fun size(): Int = sessions.size

    fun clear() {
        sessions.clear()
    }
}
