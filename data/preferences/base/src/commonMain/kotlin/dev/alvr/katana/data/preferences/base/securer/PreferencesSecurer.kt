package dev.alvr.katana.data.preferences.base.securer

interface PreferencesSecurer {
    fun toSecured(input: ByteArray): ByteArray
    fun fromSecured(input: ByteArray): ByteArray
}
