package dev.alvr.katana.data.preferences.session.models

import dev.alvr.katana.data.preferences.base.encrypted
import dev.alvr.katana.data.preferences.base.securer.PreferencesSecurer
import dev.alvr.katana.data.preferences.session.serializers.AnilistTokenSerializer
import dev.alvr.katana.domain.session.models.AnilistToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Session(
    @SerialName("at")
    @Serializable(with = AnilistTokenSerializer::class)
    val anilistToken: AnilistToken? = null,
    @SerialName("sa")
    val isSessionActive: Boolean = false,
) {
    internal companion object {
        fun serializer(securer: PreferencesSecurer) = serializer().encrypted(
            defaultValue = Session(),
            securer = securer,
        )
    }
}
