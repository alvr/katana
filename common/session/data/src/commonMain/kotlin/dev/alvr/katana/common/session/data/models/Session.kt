package dev.alvr.katana.common.session.data.models

import dev.alvr.katana.common.session.data.serializers.AnilistTokenSerializer
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import dev.alvr.katana.core.preferences.encrypted
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
        fun serializer(securer: PreferencesEncrypt) = serializer().encrypted(
            defaultValue = Session(),
            securer = securer,
        )
    }
}
