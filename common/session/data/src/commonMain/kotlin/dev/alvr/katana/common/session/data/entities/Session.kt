package dev.alvr.katana.common.session.data.entities

import dev.alvr.katana.common.session.data.serializers.AnilistTokenSerializer
import dev.alvr.katana.common.session.domain.models.AnilistToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Session(
    @SerialName("at")
    @Serializable(with = AnilistTokenSerializer::class)
    val anilistToken: AnilistToken? = null,
    @SerialName("sa")
    val sessionActive: Boolean = false,
)
