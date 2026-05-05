package dev.alvr.katana.features.lists.ui.navigation

import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.alvr.katana.features.lists.ui.entities.UserList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

internal sealed interface ListsDestination : KatanaDestination {

    @Serializable data class Selector(val selected: String, val lists: ImmutableList<UserList>) : ListsDestination
}
