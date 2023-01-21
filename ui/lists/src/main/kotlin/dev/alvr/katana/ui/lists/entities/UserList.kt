package dev.alvr.katana.ui.lists.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@JvmInline
@Parcelize
value class UserList(private val data: Pair<String, Int>) : Parcelable {
    operator fun component1() = data.first
    operator fun component2() = data.second
}
