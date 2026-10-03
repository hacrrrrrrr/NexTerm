package com.hacrrrrrrr.nexterm

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64

data class PairingSession(
    val id: String,
    val token: String,
    val endpoint: String,
    val expiresAt: Long,
    val singleUse: Boolean = true
) {
    fun qrPayload(): String {
        require(PairingSessionManager.isSupportedEndpoint(endpoint))
        return buildString {
            append("nexterm://pair?")
            append("endpoint=").append(encode(endpoint))
            append("&session=").append(encode(id))
            append("&token=").append(encode(token))
            append("&expires=").append(expiresAt)
        }
    }

    private fun encode(value: String) =
        URLEncoder.encode(value, StandardCharsets.UTF_8.name())
}

object PairingSessionManager {
    private val random = SecureRandom()
    private const val DEFAULT_LIFETIME_MS = 5 * 60 * 1000L
    private const val MAX_LIFETIME_MS = 10 * 60 * 1000L

    fun create(endpoint: String, lifetimeMs: Long = DEFAULT_LIFETIME_MS): PairingSession {
        require(isSupportedEndpoint(endpoint)) { "Pairing endpoint must use ws:// or wss://" }
        val lifetime = lifetimeMs.coerceIn(1_000L, MAX_LIFETIME_MS)
        return PairingSession(
            id = randomBytes(16),
            token = randomBytes(32),
            endpoint = endpoint,
            expiresAt = System.currentTimeMillis() + lifetime
        )
    }

    fun isValid(s: PairingSession, nowMs: Long = System.currentTimeMillis()): Boolean =
        s.expiresAt > nowMs && s.id.isNotBlank() && s.token.length >= 32

    fun isSupportedEndpoint(endpoint: String): Boolean =
        endpoint.startsWith("wss://", ignoreCase = true) ||
            endpoint.startsWith("ws://", ignoreCase = true)

    private fun randomBytes(n: Int): String {
        val bytes = ByteArray(n)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}
