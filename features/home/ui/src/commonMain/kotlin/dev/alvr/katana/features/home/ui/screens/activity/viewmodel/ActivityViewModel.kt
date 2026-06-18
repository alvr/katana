package dev.alvr.katana.features.home.ui.screens.activity.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Stable
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class ActivityViewModel : KatanaViewModel<ActivityState, ActivityEffect, ActivityIntent>(ActivityState)
