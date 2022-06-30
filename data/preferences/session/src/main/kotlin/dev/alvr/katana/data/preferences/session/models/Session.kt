package dev.alvr.katana.data.preferences.session.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Session(
    @SerialName("anilistToken")
    val anilistToken: String? = null,
)
