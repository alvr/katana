package dev.alvr.katana.common.session.data.di

import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt

internal class FakePreferencesEncrypt : PreferencesEncrypt {
    override fun encrypt(input: ByteArray) = input
    override fun decrypt(input: ByteArray) = input
}
