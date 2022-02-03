package dev.alvr.katana.data.preferences.token

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Token(
    @SerialName("anilistToken")
    val anilistToken: String? = null
)
