package dev.alvr.katana.core.preferences.encrypt

import me.tatarka.inject.annotations.Inject

@Inject
class IosPreferencesEncrypt internal constructor() : PreferencesEncrypt {
    override fun decrypt(input: ByteArray) = input
    override fun encrypt(input: ByteArray) = input
}
