package dev.alvr.katana.features.explore.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.core.ui.viewmodel.EmptyState
import org.orbitmvi.orbit.container

internal class ExploreViewModel : BaseViewModel<EmptyState, EmptyEffect>() {
    override val container = viewModelScope.container<EmptyState, EmptyEffect>(EmptyState)
}
