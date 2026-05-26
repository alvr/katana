package dev.alvr.katana.features.lists.domain.usecases

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowEitherUseCase
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListType
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface ObserveListUseCase : KatanaFlowEitherUseCase<MediaListType, MediaCollection<MediaEntry>>

@ContributesBinding(AppScope::class, binding = binding<ObserveListUseCase>())
internal class ObserveListUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: ListsRepository) :
    FlowEitherUseCase<MediaListType, MediaCollection<MediaEntry>>(dispatcher), ObserveListUseCase {

    override fun createFlow(params: MediaListType) =
        when (params) {
            MediaListType.Anime -> repository.animeCollection
            MediaListType.Manga -> repository.mangaCollection
        }
}
