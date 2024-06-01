package dev.alvr.katana.features.social.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dev.alvr.katana.core.ui.viewmodel.BaseViewModel
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.core.ui.viewmodel.EmptyState
import org.orbitmvi.orbit.container

internal class SocialViewModel : BaseViewModel<EmptyState, EmptyEffect>() {
    override val container = viewModelScope.container<EmptyState, EmptyEffect>(EmptyState)
}
