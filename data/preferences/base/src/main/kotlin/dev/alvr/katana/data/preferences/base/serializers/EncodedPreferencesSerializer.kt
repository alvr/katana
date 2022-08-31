package dev.alvr.katana.data.preferences.base.serializers

import androidx.datastore.core.Serializer
import kotlin.experimental.inv
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
private class EncodedPreferencesSerializer<T>(
    delegate: PreferencesSerializer<T>,
) : SecuredPreferencesSerializer<T>(delegate) {
    override fun ByteArray.toSecured() = also { convert() }
    override fun ByteArray.fromSecured() = also { convert() }

    private fun ByteArray.convert() {
        val midPoint = size / 2 - 1
        if (midPoint < 0) return
        var reverseIndex = lastIndex
        for (index in 0..midPoint) {
            val tmp = this[index].inv()
            this[index] = this[reverseIndex].inv()
            this[reverseIndex] = tmp
            reverseIndex--
        }
    }
}

@ExperimentalSerializationApi
fun <T> PreferencesSerializer<T>.encoded(): Serializer<T> = EncodedPreferencesSerializer(this)
