package dev.alvr.katana.ui.lists.navigation

import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer
import dev.alvr.katana.ui.lists.entities.UserList

@NavTypeSerializer
internal class UserListSerializer : DestinationsNavTypeSerializer<UserList> {
    override fun toRouteString(value: UserList): String {
        val (name, count) = value
        return "$name:$count"
    }

    override fun fromRouteString(routeStr: String): UserList {
        val (name, count) = routeStr.split(":")
        return UserList(name to count.toInt())
    }
}
