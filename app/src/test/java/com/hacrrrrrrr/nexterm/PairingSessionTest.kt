package com.hacrrrrrrr.nexterm

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PairingSessionTest {
    @Test fun createsShortLivedCredential() {
        val s = PairingSessionManager.create("wss://example.invalid/session")
        assertTrue(s.token.length >= 43)
        assertTrue(PairingSessionManager.isValid(s))
        assertTrue(s.qrPayload().startsWith("nexterm://pair?"))
    }

    @Test fun rejectsUnsupportedEndpoint() {
        assertFalse(PairingSessionManager.isSupportedEndpoint("https://example.invalid"))
        assertFalse(PairingSessionManager.isSupportedEndpoint("ftp://example.invalid"))
        assertTrue(PairingSessionManager.isSupportedEndpoint("ws://127.0.0.1/session"))
    }

    @Test fun expiredSessionIsInvalid() {
        val s = PairingSessionManager.create("wss://example.invalid", 1000)
        assertFalse(PairingSessionManager.isValid(s, s.expiresAt + 1))
    }
}
