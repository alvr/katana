package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.alvr.katana.core.ui.navigation.destinations.TopLevelDestination
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class KatanaNavState
internal constructor(
    private val backStacks: ImmutableMap<TopLevelDestination, SnapshotStateList<KatanaDestination>>,
    private val defaultBackStack: SnapshotStateList<KatanaDestination>,
    internal val currentBackStack: SnapshotStateList<KatanaDestination>,
    internal val primaryTopLevelDestination: TopLevelDestination,
) {
    var bottomBarDestination: TopLevelDestination? = primaryTopLevelDestination
        get() = currentBackStack.firstOrNull() as? TopLevelDestination
        internal set(value) {
            (field?.let { backStacks.getValue(it) } ?: defaultBackStack).apply {
                clear()
                addAll(currentBackStack)
            }

            (value?.let { backStacks.getValue(it) } ?: defaultBackStack).apply {
                currentBackStack.clear()
                currentBackStack.addAll(this)
            }

            field = value
        }

    @Composable
    internal fun toDecoratedEntries(
        entryProviders: ImmutableSet<KatanaEntryProviderInstaller>
    ): List<NavEntry<KatanaDestination>> {
        val decorators =
            listOf<NavEntryDecorator<KatanaDestination>>(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
                rememberResultEventBusNavEntryDecorator(),
            )

        val entryProvider = remember { entryProvider { entryProviders.forEach { provider -> provider() } } }

        val topLevelEntries =
            backStacks
                .mapValues { (route, stack) ->
                    rememberDecoratedNavEntries(
                        backStack = if (route == bottomBarDestination) currentBackStack else stack,
                        entryDecorators = decorators,
                        entryProvider = entryProvider,
                    )
                }
                .withDefault { emptyList() }

        val defaultEntries =
            rememberDecoratedNavEntries(
                backStack = if (bottomBarDestination == null) currentBackStack else defaultBackStack,
                entryDecorators = decorators,
                entryProvider = entryProvider,
            )

        return remember(bottomBarDestination, topLevelEntries, defaultEntries) {
            when (val topRoute = bottomBarDestination) {
                null -> defaultEntries
                primaryTopLevelDestination -> topLevelEntries.getValue(primaryTopLevelDestination)
                else -> topLevelEntries.getValue(primaryTopLevelDestination) + topLevelEntries.getValue(topRoute)
            }.toMutableStateList()
        }
    }
}

@Composable
fun rememberNavState(
    startDestination: KatanaDestination,
    primaryTopLevelDestination: TopLevelDestination,
    topLevelDestinations: ImmutableSet<TopLevelDestination>,
): KatanaNavState {
    val configuration = rememberSavedStateConfiguration()
    val backStackSerializer = remember { SnapshotStateListSerializer(PolymorphicSerializer(KatanaDestination::class)) }

    val backStacks = topLevelDestinations.associateWith { route ->
        rememberSerializable(serializer = backStackSerializer, configuration = configuration) {
            mutableStateListOf(route)
        }
    }

    val defaultBackstack =
        rememberSerializable(serializer = backStackSerializer, configuration = configuration) {
            if (startDestination is TopLevelDestination) {
                mutableStateListOf()
            } else {
                mutableStateListOf(startDestination)
            }
        }

    val currentBackStack =
        rememberSerializable(serializer = backStackSerializer, configuration = configuration) {
            val backStack =
                if (startDestination is TopLevelDestination) {
                    backStacks.getValue(startDestination)
                } else {
                    defaultBackstack
                }

            backStack.toMutableStateList()
        }

    return remember(startDestination, topLevelDestinations) {
        KatanaNavState(
            backStacks = backStacks.toImmutableMap(),
            defaultBackStack = defaultBackstack,
            currentBackStack = currentBackStack,
            primaryTopLevelDestination = primaryTopLevelDestination,
        )
    }
}

@Composable
private fun rememberSavedStateConfiguration() = remember {
    SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(KatanaDestination::class) {
                subclass(TopLevelDestination.Home::class, TopLevelDestination.Home.serializer())
                subclass(TopLevelDestination.Anime::class, TopLevelDestination.Anime.serializer())
                subclass(TopLevelDestination.Manga::class, TopLevelDestination.Manga.serializer())
                subclass(TopLevelDestination.Explore::class, TopLevelDestination.Explore.serializer())
                subclass(TopLevelDestination.Account::class, TopLevelDestination.Account.serializer())
            }
        }
    }
}
