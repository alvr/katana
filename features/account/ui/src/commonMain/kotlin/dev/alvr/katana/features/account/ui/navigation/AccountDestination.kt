package dev.alvr.katana.features.account.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaDestination
import kotlinx.serialization.Serializable

sealed interface AccountDestination : KatanaDestination {

    @Serializable data object Root : AccountDestination

    @Serializable data object Account : AccountDestination
}
