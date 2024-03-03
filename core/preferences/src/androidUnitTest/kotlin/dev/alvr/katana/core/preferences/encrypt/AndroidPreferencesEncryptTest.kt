package dev.alvr.katana.core.preferences.encrypt

import com.google.crypto.tink.Aead
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class AndroidPreferencesEncryptTest : FreeSpec() {
    private val aead = mockk<Aead>()
    private val encrypt = AndroidPreferencesEncrypt(aead)

    init {
        "decrypt using Aead" {
            every { aead.decrypt(any(), any()) } answers { firstArg<ByteArray>().reversedArray() }

            val input = "Test".encodeToByteArray()
            encrypt.decrypt(input) shouldBe "tseT".encodeToByteArray()

            verify(exactly = 1) { aead.decrypt(input, null) }
        }

        "encrypt using Aead" {
            every { aead.encrypt(any(), any()) } answers { firstArg<ByteArray>().reversedArray() }

            val input = "Test".encodeToByteArray()
            encrypt.encrypt(input) shouldBe "tseT".encodeToByteArray()

            verify(exactly = 1) { aead.encrypt(input, null) }
        }
    }
}
