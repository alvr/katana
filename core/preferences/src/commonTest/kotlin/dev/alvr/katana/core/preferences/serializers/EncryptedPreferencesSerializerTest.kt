package dev.alvr.katana.core.preferences.serializers

import androidx.datastore.core.CorruptionException
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import okio.Buffer

internal class EncryptedPreferencesSerializerTest : FreeSpec() {
    private val encrypt = mock<PreferencesEncrypt>()

    private val serializer = EncryptedPreferencesSerializer(
        encrypt = encrypt,
        serializer = ColorSerializer,
        defaultValue = Color(),
    )

    init {
        "writing and reading from the buffer" {
            val source = Buffer()

            every { encrypt.encrypt(any()) } calls { it.arg(0) as ByteArray }
            every { encrypt.decrypt(any()) } calls { it.arg(0) as ByteArray }

            serializer.writeTo(Color(0x123456), source)
            serializer.readFrom(source) shouldBe Color(0x123456)

            verify {
                encrypt.encrypt(any())
                encrypt.decrypt(any())
            }
        }

        "reading from an empty buffer" {
            val source = Buffer()

            every { encrypt.encrypt(any()) } returns byteArrayOf()
            every { encrypt.decrypt(any()) } returns byteArrayOf()

            shouldThrowExactlyUnit<CorruptionException> {
                serializer.readFrom(source)
            }.message shouldBe "secured read"

            verify { encrypt.decrypt(any()) }
        }

        "reading an invalid data" {
            val source = Buffer()
            source.write(byteArrayOf())

            every { encrypt.encrypt(any()) } returns byteArrayOf()
            every { encrypt.decrypt(any()) } returns byteArrayOf()

            shouldThrowExactlyUnit<CorruptionException> {
                serializer.readFrom(source)
            }.message shouldBe "secured read"

            verify { encrypt.decrypt(any()) }
        }

        "error when writing secure data" {
            val source = Buffer()

            every { encrypt.encrypt(any()) } calls { error("oops") }
            every { encrypt.decrypt(any()) } returns byteArrayOf()

            shouldThrowExactlyUnit<CorruptionException> {
                serializer.writeTo(Color(0x123456), source)
            }.message shouldBe "secured write"

            verify { encrypt.encrypt(any()) }
        }

        "error when reading secure data" {
            val source = Buffer()

            every { encrypt.encrypt(any()) } returns byteArrayOf()
            every { encrypt.decrypt(any()) } calls { error("oops") }

            shouldThrowExactlyUnit<CorruptionException> {
                serializer.readFrom(source)
            }.message shouldBe "secured read"

            verify { encrypt.decrypt(any()) }
        }
    }

    @Serializable(with = ColorSerializer::class)
    private data class Color(val rgb: Int = 0x000000)

    private object ColorSerializer : KSerializer<Color> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Color) {
            val string = value.rgb.toString(16).padStart(6, '0')
            encoder.encodeString(string)
        }

        override fun deserialize(decoder: Decoder): Color {
            val string = decoder.decodeString()
            return Color(string.toInt(16))
        }
    }
}
