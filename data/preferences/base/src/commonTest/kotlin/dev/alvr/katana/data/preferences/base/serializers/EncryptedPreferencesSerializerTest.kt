package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.CorruptionException
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.data.preferences.base.encrypt.MockPreferencesEncrypt
import dev.alvr.katana.data.preferences.base.encrypt.PreferencesEncrypt
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
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(PreferencesEncrypt::class)
internal class EncryptedPreferencesSerializerTest : FreeSpec() {
    private val mocker = Mocker()
    private val encrypt = MockPreferencesEncrypt(mocker)

    private val serializer = EncryptedPreferencesSerializer(
        encrypt = encrypt,
        serializer = ColorSerializer,
        defaultValue = Color(),
    )

    init {
        "writing and reading from the buffer" {
            val source = Buffer()

            mocker.every { encrypt.encrypt(isAny()) } runs { it.first() as ByteArray }
            mocker.every { encrypt.decrypt(isAny()) } runs { it.first() as ByteArray }

            serializer.writeTo(Color(0x123456), source)
            serializer.readFrom(source) shouldBe Color(0x123456)

            mocker.verify {
                encrypt.encrypt(isAny())
                encrypt.decrypt(isAny())
            }
        }

        "reading from an empty buffer" {
            val source = Buffer()

            mocker.every { encrypt.encrypt(isAny()) } returns byteArrayOf()
            mocker.every { encrypt.decrypt(isAny()) } returns byteArrayOf()

            shouldThrowExactlyUnit<CorruptionException> {
                serializer.readFrom(source)
            }.message shouldBe "secured read"

            mocker.verify { encrypt.decrypt(isAny()) }
        }

        "reading an invalid data" {
            val source = Buffer()
            source.write(byteArrayOf())

            mocker.every { encrypt.encrypt(isAny()) } returns byteArrayOf()
            mocker.every { encrypt.decrypt(isAny()) } returns byteArrayOf()

            shouldThrowExactlyUnit<CorruptionException> {
                serializer.readFrom(source)
            }.message shouldBe "secured read"

            mocker.verify { encrypt.decrypt(isAny()) }
        }

        "error when writing secure data" {
            val source = Buffer()

            mocker.every { encrypt.encrypt(isAny()) } runs { error("oops") }
            mocker.every { encrypt.decrypt(isAny()) } returns byteArrayOf()

            shouldThrowExactlyUnit<CorruptionException> {
                serializer.writeTo(Color(0x123456), source)
            }.message shouldBe "secured write"

            mocker.verify {
                threw<IllegalStateException> { encrypt.encrypt(isAny()) }
            }
        }

        "error when reading secure data" {
            val source = Buffer()

            mocker.every { encrypt.encrypt(isAny()) } returns byteArrayOf()
            mocker.every { encrypt.decrypt(isAny()) } runs { error("oops") }

            shouldThrowExactlyUnit<CorruptionException> {
                serializer.readFrom(source)
            }.message shouldBe "secured read"

            mocker.verify {
                threw<IllegalStateException> { encrypt.decrypt(isAny()) }
            }
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

    override fun extensions() = listOf(mocker())
}
