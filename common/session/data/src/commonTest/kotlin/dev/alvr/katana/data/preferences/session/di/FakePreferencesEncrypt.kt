package dev.alvr.katana.data.preferences.session.di

import dev.alvr.katana.data.preferences.base.encrypt.PreferencesEncrypt

internal class FakePreferencesEncrypt : PreferencesEncrypt {
    override fun encrypt(input: ByteArray) = input
    override fun decrypt(input: ByteArray) = input
}
