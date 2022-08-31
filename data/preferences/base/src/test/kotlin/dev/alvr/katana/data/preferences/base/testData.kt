package dev.alvr.katana.data.preferences.base

import dev.alvr.katana.data.preferences.base.serializers.PreferencesSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@ExperimentalSerializationApi
@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable(with = ColorSerializer::class)
internal data class Color(val rgb: Int = 0x000000)

@ExperimentalSerializationApi
internal object ColorSerializer : KSerializer<Color>, PreferencesSerializer<Color> {
    override val serializer = this
    override val defaultValue = Color()
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        val string = value.rgb.toString(16).padStart(6, '0')
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): Color {
        val string = decoder.decodeString()
        return Color(string.toInt(16))
    }
}
