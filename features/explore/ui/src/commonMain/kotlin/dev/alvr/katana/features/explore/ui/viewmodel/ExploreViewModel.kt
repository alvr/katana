package dev.alvr.katana.features.explore.ui.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.core.ui.viewmodel.EmptyIntent
import dev.alvr.katana.core.ui.viewmodel.EmptyState
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Stable
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class ExploreViewModel(dispatcher: KatanaDispatcher) :
    KatanaViewModel<EmptyState, EmptyEffect, EmptyIntent>(dispatcher, EmptyState)
