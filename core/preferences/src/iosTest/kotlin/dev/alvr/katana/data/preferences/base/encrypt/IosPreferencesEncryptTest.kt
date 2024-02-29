package dev.alvr.katana.data.preferences.base.encrypt

import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.core.spec.style.FreeSpec

internal class IosPreferencesEncryptTest : FreeSpec() {
    private val encrypt = IosPreferencesEncrypt()

    init {
        "decrypt" {
            val input = "Test".encodeToByteArray()
            shouldNotThrowUnit<NotImplementedError> { encrypt.decrypt(input) }
        }

        "encrypt" {
            val input = "Test".encodeToByteArray()
            shouldNotThrowUnit<NotImplementedError> { encrypt.encrypt(input) }
        }
    }
}
