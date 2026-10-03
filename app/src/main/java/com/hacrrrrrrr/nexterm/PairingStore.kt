package com.hacrrrrrrr.nexterm

import android.content.Context
import org.json.JSONObject

/**
 * Pairing credentials are intentionally ephemeral. The bearer token is never
 * persisted to SharedPreferences, reducing credential exposure after backup,
 * extraction, or device compromise.
 */
object PairingStore {
    private const val PREFS = "nexterm_pairing"
    private const val KEY_SESSION = "session"

    fun save(c: Context, s: PairingSession) {
        require(PairingSessionManager.isValid(s))
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
        clear(c)
        return null
    }

    fun clear(c: Context) {
        c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_SESSION)
            .apply()
    }
}
