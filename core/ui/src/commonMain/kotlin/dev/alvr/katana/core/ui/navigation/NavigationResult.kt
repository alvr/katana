package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Composable
inline fun <reified D : KatanaDestination, reified R : Any> NavController.NavigationResult(
    noinline onResult: (R) -> Unit,
) {
    NavigationResult(
        backStackEntry = remember(this) { getBackStackEntry<D>() },
        navController = this,
        serializer = serializer<R>(),
        onResult = onResult,
    )
}

@Composable
@PublishedApi
internal fun <R : Any> NavigationResult(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    serializer: KSerializer<R>,
    onResult: (R) -> Unit,
) {
    val onResultRemembered by rememberUpdatedState(onResult)

    DisposableEffect(navController) {
        val resultKey = serializer.resultKey
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && backStackEntry.savedStateHandle.contains(resultKey)) {
                val result = backStackEntry.savedStateHandle.remove<String>(resultKey)!!
                val decoded = Json.decodeFromString(serializer, result)
                onResultRemembered(decoded)
            }
        }
        backStackEntry.lifecycle.addObserver(observer)

        onDispose { backStackEntry.lifecycle.removeObserver(observer) }
    }
}

inline fun <reified R : Any> NavController.setNavigationResult(data: R) {
    val serializer = serializer<R>()
    val result = Json.encodeToString(serializer, data)
    previousBackStackEntry?.savedStateHandle?.set(serializer.resultKey, result)
}

@PublishedApi
@OptIn(ExperimentalSerializationApi::class)
internal val <R> KSerializer<R>.resultKey: String
    get() = descriptor.serialName + "_result"
