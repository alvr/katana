package dev.alvr.katana.data.preferences.base.serializers

import android.graphics.Color
import androidx.datastore.core.CorruptionException
import dev.alvr.katana.common.tests.KoinTest4
import dev.alvr.katana.data.preferences.base.di.baseDataPreferencesModule
import dev.alvr.katana.data.preferences.base.securer.PreferencesSecurer
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import okio.Buffer
import org.junit.Test
import org.koin.core.KoinApplication
import org.koin.test.inject

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
internal class EncryptedPreferencesSerializerTest : KoinTest4() {
    private val securer by inject<PreferencesSecurer>()

    private val colorSerializer by lazy {
        EncryptedPreferencesSerializer(
            securer = securer,
            serializer = ColorSerializer,
            defaultValue = Color(),
        )
    }

    override fun KoinApplication.initKoin() {
        modules(baseDataPreferencesModule)
    }

    @Test
    fun `when reading the initial value it should fail`() = runTest {
        shouldThrowExactlyUnit<CorruptionException> {
            colorSerializer.readFrom(Buffer())
        }.message shouldBe "Aead decrypt"
    }

    @Test
    fun `when writing a value then after reading it should be the new value`() = runTest {
        val buffer = Buffer()
        colorSerializer.writeTo(
            Color(rgb = 0x123_456),
            buffer,
        )

        val color = colorSerializer.readFrom(buffer)

        color.rgb shouldBe 0x123_456
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
