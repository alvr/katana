package dev.alvr.katana.data.preferences.base

import androidx.datastore.core.okio.OkioSerializer
import dev.alvr.katana.data.preferences.base.securer.PreferencesSecurer
import dev.alvr.katana.data.preferences.base.serializers.EncryptedPreferencesSerializer
import kotlinx.serialization.KSerializer

fun <T> KSerializer<T>.encrypted(defaultValue: T, securer: PreferencesSecurer): OkioSerializer<T> =
    EncryptedPreferencesSerializer(securer, this, defaultValue)