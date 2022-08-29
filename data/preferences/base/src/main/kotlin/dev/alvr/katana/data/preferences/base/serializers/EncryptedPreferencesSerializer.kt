package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
private class EncryptedPreferencesSerializer<T>(
    delegate: PreferencesSerializer<T>,
    private val aead: Aead,
) : SecuredPreferencesSerializer<T>(delegate) {
    override fun ByteArray.fromSecured(): ByteArray = aead.decrypt(this, null)
    override fun ByteArray.toSecured(): ByteArray = aead.encrypt(this, null)
}

@ExperimentalSerializationApi
fun <T> PreferencesSerializer<T>.encrypted(aead: Aead): Serializer<T> =
    EncryptedPreferencesSerializer(this, aead)
