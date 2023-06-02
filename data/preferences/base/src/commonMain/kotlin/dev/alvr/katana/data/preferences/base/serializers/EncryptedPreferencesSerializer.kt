package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import dev.alvr.katana.data.preferences.base.securer.PreferencesEncrypt
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf
import okio.BufferedSink
import okio.BufferedSource
import okio.use

@OptIn(ExperimentalEncodingApi::class, ExperimentalSerializationApi::class)
internal class EncryptedPreferencesSerializer<T>(
    private val securer: PreferencesEncrypt,
    private val serializer: KSerializer<T>,
    override val defaultValue: T,
) : OkioSerializer<T> {
    override suspend fun readFrom(source: BufferedSource): T =
        operation("secured read") {
            val securedInput = source.use { buffered ->
                buffered.readByteArray()
                    .let { secured -> Base64.decode(secured) }
                    .let { decoded -> securer.decrypt(decoded) }
            }

            ProtoBuf.decodeFromByteArray(serializer, securedInput)
        }

    override suspend fun writeTo(t: T, sink: BufferedSink) {
        operation("secured write") {
            val securedOutput = ProtoBuf.encodeToByteArray(serializer, t)
                .let { encoded -> securer.encrypt(encoded) }
                .let { secured -> Base64.encodeToByteArray(secured) }

            sink.use { buffered -> buffered.write(securedOutput) }
        }
    }

    private inline fun <R> operation(message: String, block: () -> R): R = try {
        block()
    } catch (e: SerializationException) {
        throw CorruptionException(message, e)
    }
}
