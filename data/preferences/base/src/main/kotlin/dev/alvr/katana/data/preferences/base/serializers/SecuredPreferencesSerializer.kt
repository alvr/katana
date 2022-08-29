package dev.alvr.katana.data.preferences.base.serializers

import android.util.Base64
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.GeneralSecurityException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException

@ExperimentalSerializationApi
internal sealed class SecuredPreferencesSerializer<T>(
    private val prefsSerializer: PreferencesSerializer<T>,
) : Serializer<T> by prefsSerializer {
    override suspend fun readFrom(input: InputStream): T = op("reading secured value failed") {
        val securedInput = Base64.decode(input.readBytes(), Base64.NO_WRAP)

        val normalInput = with(securedInput) {
            fromSecured().takeIf { isNotEmpty() } ?: this
        }

        prefsSerializer.readFrom(normalInput.inputStream())
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        op("writing secured value failed") {
            val securedOutput = ByteArrayOutputStream().use { stream ->
                prefsSerializer.writeTo(t, stream)
                stream.toByteArray()
            }.toSecured()

            @Suppress("BlockingMethodInNonBlockingContext")
            output.write(Base64.encode(securedOutput, Base64.NO_WRAP))
        }
    }

    private inline fun <R> op(message: String, block: () -> R): R = try {
        block()
    } catch (e: SerializationException) {
        throw CorruptionException(message, e)
    } catch (e: GeneralSecurityException) {
        throw CorruptionException(message, e)
    }

    abstract fun ByteArray.fromSecured(): ByteArray
    abstract fun ByteArray.toSecured(): ByteArray
}
