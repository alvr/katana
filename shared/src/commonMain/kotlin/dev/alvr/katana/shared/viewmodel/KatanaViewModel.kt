package dev.alvr.katana.shared.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.core.ui.viewmodel.EmptyIntent
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.shared.navigation.mainNavigationBarItems
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.ExposeImplBinding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.toImmutableList

@Stable
@ViewModelKey
@ExposeImplBinding
@ContributesIntoMap(AppScope::class)
internal class KatanaViewModel(private val observeActiveSessionUseCase: ObserveActiveSessionUseCase) :
    KatanaViewModel<KatanaState, EmptyEffect, EmptyIntent>(KatanaState()) {

    override fun init() {
        observeActiveSession()
    }

    private fun observeActiveSession() {
        execute(
            useCase = observeActiveSessionUseCase,
            params = Unit,
            onFailure = {
                state { copy(loading = false, sessionActive = false, navigationBarItems = navigationBarItems()) }
            },
            onSuccess = { isActive ->
                state {
                    copy(loading = false, sessionActive = isActive, navigationBarItems = navigationBarItems(isActive))
                }
            },
        )
    }

    private fun navigationBarItems(sessionActive: Boolean = false) =
        mainNavigationBarItems.filterNot { it.requireSession && !sessionActive }.toImmutableList()
}
