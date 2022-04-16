package dev.alvr.katana.data.preferences.base

import android.util.Base64
import androidx.datastore.core.Serializer
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.experimental.inv
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
private class EncodedPreferencesSerializer<T>(
    private val delegate: PreferencesSerializer<T>,
) : Serializer<T> by delegate {
    override suspend fun readFrom(input: InputStream): T {
        val encryptedInput = Base64.decode(input.readBytes(), Base64.NO_WRAP)

        val decryptedInput = if (encryptedInput.isNotEmpty()) {
            encryptedInput.convert()
        } else {
            encryptedInput
        }

        return delegate.readFrom(decryptedInput.inputStream())
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val encodedBytes = ByteArrayOutputStream().use { stream ->
            delegate.writeTo(t, stream)
            stream.toByteArray()
        }.convert()

        @Suppress("BlockingMethodInNonBlockingContext")
        output.write(Base64.encode(encodedBytes, Base64.NO_WRAP))
    }

    private fun ByteArray.convert(): ByteArray {
        val list = ByteArray(size)

        for ((i, n) in (size - 1 downTo 0).withIndex()) {
            list[i] = this[n].inv()
        }

        return list
    }
}

@ExperimentalSerializationApi
fun <T> PreferencesSerializer<T>.encoded(): Serializer<T> = EncodedPreferencesSerializer(this)
