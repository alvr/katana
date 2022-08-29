package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.Serializer
import kotlin.experimental.inv
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
private class EncodedPreferencesSerializer<T>(
    delegate: PreferencesSerializer<T>,
) : SecuredPreferencesSerializer<T>(delegate) {
    override fun ByteArray.toSecured() = convert()
    override fun ByteArray.fromSecured() = convert()

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
