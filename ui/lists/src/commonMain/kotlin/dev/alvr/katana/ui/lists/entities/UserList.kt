package dev.alvr.katana.ui.lists.entities

import kotlin.jvm.JvmInline

@JvmInline
value class UserList(private val data: Pair<String, Int>) {
    operator fun component1() = data.first
    operator fun component2() = data.second
}
