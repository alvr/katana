package dev.alvr.katana.features.home.domain.usecases

import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowEitherUseCase
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface ObserveUpcomingAnimeUseCase : KatanaFlowEitherUseCase<Unit, List<CommonMediaEntry>>

@ContributesBinding(AppScope::class, binding = binding<ObserveUpcomingAnimeUseCase>())
internal class ObserveUpcomingAnimeUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: HomeRepository) :
    FlowEitherUseCase<Unit, List<CommonMediaEntry>>(dispatcher), ObserveUpcomingAnimeUseCase {
    override fun createFlow(params: Unit) = repository.upcomingAnime()
}
