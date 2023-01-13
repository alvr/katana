package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.CorruptionException
import com.google.crypto.tink.Aead
import dev.alvr.katana.common.tests.KoinTest4
import dev.alvr.katana.data.preferences.base.Color
import dev.alvr.katana.data.preferences.base.ColorSerializer
import dev.alvr.katana.data.preferences.base.di.baseDataPreferencesModule
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test
import org.koin.core.KoinApplication
import org.koin.test.inject

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
internal class EncryptedPreferencesSerializerTest : KoinTest4() {
    private val aead by inject<Aead>()

    override fun KoinApplication.initKoin() {
        modules(baseDataPreferencesModule)
    }

    @Test
    fun `when reading the initial value it should fail`() = runTest {
        val colorSerializer = ColorSerializer.encrypted(aead)

        shouldThrowExactlyUnit<CorruptionException> {
            colorSerializer.readFrom(ByteArrayInputStream(byteArrayOf()))
        }.message shouldBe "reading secured value failed"
    }

    @Test
    fun `when writing a value then after reading it should be the new value`() = runTest {
        val output = ByteArrayOutputStream()
        val colorSerializer = ColorSerializer.encrypted(aead)
        colorSerializer.writeTo(
            Color(rgb = 0x123_456),
            output,
        )

        val input = ByteArrayInputStream(output.toByteArray())
        val color = colorSerializer.readFrom(input)

        color.rgb shouldBe 0x123_456
    }
}
