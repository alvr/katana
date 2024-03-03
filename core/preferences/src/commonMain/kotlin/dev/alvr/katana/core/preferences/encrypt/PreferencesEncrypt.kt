package dev.alvr.katana.core.preferences.encrypt

interface PreferencesEncrypt {
    fun encrypt(input: ByteArray): ByteArray
    fun decrypt(input: ByteArray): ByteArray
}
