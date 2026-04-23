package dev.alvr.katana.features.home.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomePreferences internal constructor(@SerialName("wc") internal val welcomeCardVisible: Boolean = true)
