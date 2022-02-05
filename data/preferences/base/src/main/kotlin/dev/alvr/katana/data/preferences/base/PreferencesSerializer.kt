package dev.alvr.katana.data.preferences.base

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf

@ExperimentalSerializationApi
interface PreferencesSerializer<T> : Serializer<T> {

    val serializer: KSerializer<T>

    override suspend fun readFrom(input: InputStream): T = try {
        ProtoBuf.decodeFromByteArray(serializer, input.readBytes())
    } catch (e: SerializationException) {
        throw CorruptionException("Error deserializing proto", e)
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        @Suppress("BlockingMethodInNonBlockingContext")
        output.write(ProtoBuf.encodeToByteArray(serializer, t))
    }
}
