package dev.alvr.katana.core.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination

@OptIn(ExperimentalMaterial3Api::class)
internal data class BottomSheetScene(
    override val key: KatanaDestination,
    override val previousEntries: List<NavEntry<KatanaDestination>>,
    override val overlaidEntries: List<NavEntry<KatanaDestination>>,
    private val entry: NavEntry<KatanaDestination>,
    private val modalBottomSheetProperties: ModalBottomSheetProperties,
    private val onBack: () -> Unit,
) : OverlayScene<KatanaDestination> {
    override val entries: List<NavEntry<KatanaDestination>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        val lifecycleOwner = rememberLifecycleOwner()

        ModalBottomSheet(onDismissRequest = onBack, properties = modalBottomSheetProperties) {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) { entry.Content() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy internal constructor() : SceneStrategy<KatanaDestination> {
    override fun SceneStrategyScope<KatanaDestination>.calculateScene(
        entries: List<NavEntry<KatanaDestination>>
    ): Scene<KatanaDestination>? {
        val lastEntry = entries.lastOrNull() ?: return null
        return lastEntry.metadata[BottomSheetKey]?.let { bottomSheetProperties ->
            BottomSheetScene(
                key = lastEntry.contentKey as KatanaDestination,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                modalBottomSheetProperties = bottomSheetProperties,
                onBack = onBack,
            )
        }
    }

    companion object {
        fun bottomSheet(modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties()) =
            metadata {
                put(BottomSheetKey, modalBottomSheetProperties)
            }

        object BottomSheetKey : NavMetadataKey<ModalBottomSheetProperties>
    }
}

@Composable fun rememberBottomSheetSceneStrategy() = remember { BottomSheetSceneStrategy() }
