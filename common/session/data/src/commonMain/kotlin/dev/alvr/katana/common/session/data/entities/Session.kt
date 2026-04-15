package dev.alvr.katana.common.session.data.entities

import dev.alvr.katana.common.session.data.serializers.AnilistTokenSerializer
import dev.alvr.katana.common.session.domain.models.AnilistToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session
internal constructor(
    @SerialName("at")
    @Serializable(with = AnilistTokenSerializer::class)
    internal val anilistToken: AnilistToken? = null,
    @SerialName("sa") internal val sessionActive: Boolean = false,
)
