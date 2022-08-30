package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.CorruptionException
import dev.alvr.katana.common.tests.RoboTest
import dev.alvr.katana.data.preferences.base.Color
import dev.alvr.katana.data.preferences.base.ColorSerializer
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
internal class EncodedPreferencesSerializerTest : RoboTest() {
    @Test
    fun `when reading the initial value it should fail`() {
        runTest {
            val colorSerializer = ColorSerializer.encoded()

            shouldThrowExactlyUnit<CorruptionException> {
                colorSerializer.readFrom(ByteArrayInputStream(byteArrayOf()))
            }.message shouldBe "reading secured value failed"
        }
    }

    @Test
    fun `when writing a value then after reading it should be the new value`() {
        runTest {
            val outputStream = ByteArrayOutputStream()

            val colorSerializer = ColorSerializer.encoded()
            colorSerializer.writeTo(
                Color(rgb = 0x123_456),
                outputStream,
            )

            val inputStream = ByteArrayInputStream(outputStream.toByteArray())
            val color = colorSerializer.readFrom(inputStream)
            color.rgb shouldBe 0x123_456
        }
    }
}
