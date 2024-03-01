package dev.alvr.katana.common.session.data.serializers

import dev.alvr.katana.common.session.domain.models.AnilistToken
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object AnilistTokenSerializer : KSerializer<AnilistToken> {
    override val descriptor = PrimitiveSerialDescriptor("AnilistToken", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = AnilistToken(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: AnilistToken) {
        encoder.encodeString(value.token)
    }
}
