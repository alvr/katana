package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf

@ExperimentalSerializationApi
interface PreferencesSerializer<T> : Serializer<T> {
    val serializer: KSerializer<T>

    override suspend fun readFrom(input: InputStream): T = input.use { stream ->
        ProtoBuf.decodeFromByteArray(serializer, stream.readBytes())
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        output.use { stream ->
            @Suppress("BlockingMethodInNonBlockingContext")
            stream.write(ProtoBuf.encodeToByteArray(serializer, t))
        }
    }
}
