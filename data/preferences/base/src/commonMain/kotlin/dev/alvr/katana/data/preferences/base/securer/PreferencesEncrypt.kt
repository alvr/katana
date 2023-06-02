package dev.alvr.katana.data.preferences.base.securer

interface PreferencesEncrypt {
    fun encrypt(input: ByteArray): ByteArray
    fun decrypt(input: ByteArray): ByteArray
}
