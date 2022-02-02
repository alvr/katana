package dev.alvr.katana.data.preferences.base

import android.util.Base64
import androidx.datastore.core.Serializer
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.experimental.inv

private class EncodedPreferencesSerializer<T>(
    private val delegate: Serializer<T>
) : Serializer<T> by delegate {
    override suspend fun readFrom(input: InputStream): T {
        val source = input.readBytes().reversedArray().map(Byte::inv).toByteArray()
        return delegate.readFrom(Base64.decode(source, Base64.NO_WRAP).inputStream())
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val encodedBytes = ByteArrayOutputStream().use { stream ->
            delegate.writeTo(t, stream)
            stream.toByteArray()
        }.map(Byte::inv).toByteArray().reversedArray().let { bytes ->
            Base64.encode(bytes, Base64.NO_WRAP)
        }

        @Suppress("BlockingMethodInNonBlockingContext")
        output.write(encodedBytes)
    }
}

fun <T> Serializer<T>.encoded(): Serializer<T> = EncodedPreferencesSerializer(this)
