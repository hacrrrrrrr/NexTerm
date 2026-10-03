package com.hacrrrrrrr.nexterm

import android.content.Context
import org.json.JSONObject

/**
 * Pairing credentials are ephemeral. The bearer token is kept only in memory;
 * persistent storage contains non-secret metadata so a process restart cannot
 * resurrect a bearer credential.
 */
object PairingStore {
    private const val PREFS = "nexterm_pairing"
    private const val KEY_SESSION = "session"
    @Volatile private var memorySession: PairingSession? = null

    fun save(c: Context, s: PairingSession) {
        require(PairingSessionManager.isValid(s))
        memorySession = s
        val j = JSONObject()
            .put("id", s.id)
            .put("endpoint", s.endpoint)
            .put("expiresAt", s.expiresAt)
        c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SESSION, j.toString())
            .apply()
    }

    fun load(c: Context): PairingSession? {
        val session = memorySession
        return if (session != null && PairingSessionManager.isValid(session)) {
            session
        } else {
            clear(c)
            null
        }
    }

    fun clear(c: Context) {
        memorySession = null
        c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_SESSION)
            .apply()
    }
}
