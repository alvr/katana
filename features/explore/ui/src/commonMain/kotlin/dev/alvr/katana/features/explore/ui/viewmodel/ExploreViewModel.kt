package dev.alvr.katana.features.explore.ui.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.core.ui.viewmodel.EmptyIntent
import dev.alvr.katana.core.ui.viewmodel.EmptyState
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel

@Stable internal class ExploreViewModel : KatanaViewModel<EmptyState, EmptyEffect, EmptyIntent>(EmptyState)
