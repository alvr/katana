package dev.alvr.katana.features.home.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class HomePreferences(
    @SerialName("wc")
    val welcomeCardVisible: Boolean = true,
)
