package dev.alvr.katana.ui.lists.entities

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Parcelize
@Serializable
value class UserList(private val data: Pair<String, Int>) : Parcelable {
    operator fun component1() = data.first
    operator fun component2() = data.second
}
