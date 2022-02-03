package dev.alvr.katana.data.preferences.token

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf

@ExperimentalSerializationApi
internal object TokenSerializer : Serializer<Token> {
    override val defaultValue: Token = Token()

    override suspend fun readFrom(input: InputStream): Token = try {
        ProtoBuf.decodeFromByteArray(Token.serializer(), input.readBytes())
    } catch (e: SerializationException) {
        throw CorruptionException("Error deserializing proto", e)
    }

    override suspend fun writeTo(t: Token, output: OutputStream) {
        @Suppress("BlockingMethodInNonBlockingContext")
        output.write(ProtoBuf.encodeToByteArray(Token.serializer(), t))
    }
}
