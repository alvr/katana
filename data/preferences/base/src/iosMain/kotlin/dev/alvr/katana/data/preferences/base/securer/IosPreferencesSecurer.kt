package dev.alvr.katana.data.preferences.base.securer

internal class IosPreferencesSecurer : PreferencesSecurer {
    override fun fromSecured(input: ByteArray) = input // TODO
    override fun toSecured(input: ByteArray) = input // TODO
}
