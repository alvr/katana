package dev.alvr.katana.features.lists.ui.entities

import androidx.compose.runtime.Immutable
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Immutable
@JvmInline
@Serializable
internal value class UserList(private val data: Pair<String, Int>) {
    operator fun component1() = data.first

    operator fun component2() = data.second
}
