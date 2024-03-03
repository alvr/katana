package dev.alvr.katana.core.preferences

import androidx.datastore.core.okio.OkioSerializer
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import dev.alvr.katana.core.preferences.serializers.EncryptedPreferencesSerializer
import kotlinx.serialization.KSerializer

fun <T> KSerializer<T>.encrypted(defaultValue: T, securer: PreferencesEncrypt): OkioSerializer<T> =
    EncryptedPreferencesSerializer(securer, this, defaultValue)
