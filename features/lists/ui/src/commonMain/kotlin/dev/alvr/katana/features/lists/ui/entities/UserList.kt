package dev.alvr.katana.features.lists.ui.entities

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlin.jvm.JvmInline

@JvmInline
@Parcelize
value class UserList(private val data: Pair<String, Int>) : Parcelable {
    operator fun component1() = data.first
    operator fun component2() = data.second
}
