package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.CorruptionException
import com.google.crypto.tink.Aead
import dagger.hilt.android.testing.HiltAndroidTest
import dev.alvr.katana.common.tests.HiltTest
import dev.alvr.katana.common.tests.RobolectricKeyStore
import dev.alvr.katana.data.preferences.base.Color
import dev.alvr.katana.data.preferences.base.ColorSerializer
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.BeforeClass
import org.junit.Test

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
internal class EncryptedPreferencesSerializerTest : HiltTest() {

    @Inject
    internal lateinit var aead: Aead

    @Test
    fun `when reading the initial value it should fail`() {
        runTest {
            val colorSerializer = ColorSerializer.encrypted(aead)

            shouldThrowExactlyUnit<CorruptionException> {
                colorSerializer.readFrom(ByteArrayInputStream(byteArrayOf()))
            }.message shouldBe "reading secured value failed"
        }
    }

    @Test
    fun `when writing a value then after reading it should be the new value`() {
        runTest {
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

    companion object {
        @JvmStatic
        @BeforeClass
        fun setUpClass() {
            RobolectricKeyStore.setup
        }
    }
}
