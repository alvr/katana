package dev.alvr.katana.core.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

@OptIn(ExperimentalMaterial3Api::class)
internal data class BottomSheetScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val state: ModalBottomSheetState,
    private val properties: ModalBottomSheetProperties,
    private val onBack: () -> Unit,
) : OverlayScene<T> {
    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        val lifecycleOwner = rememberLifecycleOwner()
        val sheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = state.skipPartiallyExpanded,
                confirmValueChange = state.confirmValueChange,
            )

        ModalBottomSheet(
            sheetState = sheetState,
            sheetGesturesEnabled = state.gesturesEnabled,
            onDismissRequest = onBack,
            properties = properties,
        ) {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) { entry.Content() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> internal constructor() : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null

        return lastEntry.metadata[BottomSheetStateKey]?.let { state ->
            lastEntry.metadata[BottomSheetPropertiesKey]?.let { properties ->
                val previousEntries = entries.dropLast(1)

                BottomSheetScene(
                    key = @Suppress("UNCHECKED_CAST") (lastEntry.contentKey as T),
                    previousEntries = previousEntries,
                    overlaidEntries = previousEntries,
                    entry = lastEntry,
                    state = state,
                    properties = properties,
                    onBack = onBack,
                )
            }
        }
    }

    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        fun bottomSheet(
            state: ModalBottomSheetState = ModalBottomSheetState(),
            properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
        ) = metadata {
            put(BottomSheetStateKey, state)
            put(BottomSheetPropertiesKey, properties)
        }
    }
}

@Composable fun <T : Any> rememberBottomSheetSceneStrategy() = remember { BottomSheetSceneStrategy<T>() }

@Immutable
@OptIn(ExperimentalMaterial3Api::class)
class ModalBottomSheetState(
    internal val skipPartiallyExpanded: Boolean = true,
    internal val confirmValueChange: (SheetValue) -> Boolean = { true },
    internal val gesturesEnabled: Boolean = true,
)

@OptIn(ExperimentalMaterial3Api::class) private data object BottomSheetStateKey : NavMetadataKey<ModalBottomSheetState>

@OptIn(ExperimentalMaterial3Api::class)
private data object BottomSheetPropertiesKey : NavMetadataKey<ModalBottomSheetProperties>
