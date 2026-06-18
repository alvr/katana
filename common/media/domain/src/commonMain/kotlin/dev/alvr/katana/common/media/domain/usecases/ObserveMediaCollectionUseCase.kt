package dev.alvr.katana.common.media.domain.usecases

import dev.alvr.katana.common.media.domain.models.MediaCollection
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.common.media.domain.repositories.MediaCollectionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface ObserveMediaCollectionUseCase :
    KatanaFlowEitherUseCase<ObserveMediaCollectionUseCase.Params, MediaCollection<MediaEntry>> {
    data class Params(val type: MediaListType, val status: MediaListStatus)
}

@ContributesBinding(AppScope::class, binding = binding<ObserveMediaCollectionUseCase>())
internal class ObserveMediaCollectionUseCaseImpl(
    dispatcher: KatanaDispatcher,
    private val repository: MediaCollectionRepository,
) :
    FlowEitherUseCase<ObserveMediaCollectionUseCase.Params, MediaCollection<MediaEntry>>(dispatcher),
    ObserveMediaCollectionUseCase {
    override fun createFlow(params: ObserveMediaCollectionUseCase.Params) =
        when (params.type) {
            MediaListType.Anime -> repository.animeCollection(params.status)
            MediaListType.Manga -> repository.mangaCollection(params.status)
        }
}
