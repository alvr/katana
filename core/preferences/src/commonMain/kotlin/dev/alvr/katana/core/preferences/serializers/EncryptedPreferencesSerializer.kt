package dev.alvr.katana.core.preferences.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import okio.BufferedSink
import okio.BufferedSource
import okio.use

@OptIn(ExperimentalEncodingApi::class, ExperimentalSerializationApi::class)
internal class EncryptedPreferencesSerializer<T>(
    private val encrypt: PreferencesEncrypt,
    private val serializer: KSerializer<T>,
    override val defaultValue: T,
) : OkioSerializer<T> {
    override suspend fun readFrom(source: BufferedSource): T =
        operation("secured read") {
            val input = source.use { buffered ->
                buffered.readByteArray()
                    .let { encrypted -> Base64.decode(encrypted) }
                    .let { decoded -> encrypt.decrypt(decoded) }
            }

            ProtoBuf.decodeFromByteArray(serializer, input)
        }

    override suspend fun writeTo(t: T, sink: BufferedSink) {
        operation("secured write") {
            val output = ProtoBuf.encodeToByteArray(serializer, t)
                .let { encoded -> encrypt.encrypt(encoded) }
                .let { encrypted -> Base64.encodeToByteArray(encrypted) }

            sink.use { buffered -> buffered.write(output) }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private inline fun <R> operation(message: String, block: () -> R): R = try {
        block()
    } catch (e: Throwable) {
        throw CorruptionException(message, e)
    }
}
