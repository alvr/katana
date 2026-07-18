package dev.alvr.katana.features.home.domain.usecases

import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowEitherUseCase
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface ObservePopularMediaUseCase : KatanaFlowEitherUseCase<MediaListType, List<CommonMediaEntry>>

@ContributesBinding(AppScope::class, binding = binding<ObservePopularMediaUseCase>())
internal class ObservePopularMediaUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: HomeRepository) :
    FlowEitherUseCase<MediaListType, List<CommonMediaEntry>>(dispatcher), ObservePopularMediaUseCase {
    override fun createFlow(params: MediaListType) = repository.popularMedia(params)
}
