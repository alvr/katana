package dev.alvr.katana.features.home.ui.screens.foryou.viewmodel

import androidx.compose.runtime.Stable
import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.common.media.domain.usecases.ObserveMediaCollectionUseCase
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.core.ui.viewmodel.SectionStatus
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObservePopularMediaUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveTrendingMediaUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveUpcomingAnimeUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import dev.alvr.katana.features.home.ui.screens.foryou.entities.mappers.toHomeMediaItems
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Stable
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class ForYouViewModel(
    private val hideWelcomeCardUseCase: HideWelcomeCardUseCase,
    private val observePopularMediaUseCase: ObservePopularMediaUseCase,
    private val observeTrendingMediaUseCase: ObserveTrendingMediaUseCase,
    private val observeUpcomingAnimeUseCase: ObserveUpcomingAnimeUseCase,
    private val observeMediaCollectionUseCase: ObserveMediaCollectionUseCase,
    private val observeWelcomeCardVisibilityUseCase: ObserveWelcomeCardVisibilityUseCase,
) : KatanaViewModel<ForYouState, ForYouEffect, ForYouIntent>(ForYouState()) {

    override fun init() {
        observeWelcomeCardVisibility()
        observeTrending(currentState.trending.selectedType)
        observePopular(currentState.popular.selectedType)
        observeUpcoming()
    }

    override fun intent(intent: ForYouIntent) {
        when (intent) {
            ForYouIntent.CloseWelcomeCard -> handleCloseWelcomeCard()
            is ForYouIntent.SessionChanged -> handleSessionChanged(intent.active)
            is ForYouIntent.SelectTrendingType -> handleSelectTrendingType(intent.type)
            is ForYouIntent.SelectPopularType -> handleSelectPopularType(intent.type)
            ForYouIntent.RetryWatching -> observeWatching()
            ForYouIntent.RetryReading -> observeReading()
            ForYouIntent.RetryTrending -> observeTrending(currentState.trending.selectedType)
            ForYouIntent.RetryPopular -> observePopular(currentState.popular.selectedType)
            ForYouIntent.RetryUpcoming -> observeUpcoming()
            ForYouIntent.NavigateToAnimeLists -> handleNavigateToAnimeLists()
            ForYouIntent.NavigateToMangaLists -> handleNavigateToMangaLists()
            ForYouIntent.NavigateToTrending -> handleNavigateToTrending()
            ForYouIntent.NavigateToPopular -> handleNavigateToPopular()
            ForYouIntent.NavigateToUpcoming -> handleNavigateToUpcoming()
        }
    }

    private fun observeWelcomeCardVisibility() {
        execute(
            useCase = observeWelcomeCardVisibilityUseCase,
            params = Unit,
            onFailure = { state { copy(showWelcomeCard = false) } },
            onSuccess = { isVisible -> state { copy(showWelcomeCard = isVisible) } },
        )
    }

    private fun observeWatching() {
        state { copy(watching = SectionStatus.Loading) }

        execute(
            useCase = observeMediaCollectionUseCase,
            params = ObserveMediaCollectionUseCase.Params(type = MediaListType.Anime, status = MediaListStatus.Current),
            onFailure = { failure -> state { copy(watching = SectionStatus.Error(failure)) } },
            onSuccess = { media -> state { copy(watching = SectionStatus.Success(media.toHomeMediaItems())) } },
        )
    }

    private fun observeReading() {
        state { copy(reading = SectionStatus.Loading) }

        execute(
            useCase = observeMediaCollectionUseCase,
            params = ObserveMediaCollectionUseCase.Params(type = MediaListType.Manga, status = MediaListStatus.Current),
            onFailure = { failure -> state { copy(reading = SectionStatus.Error(failure)) } },
            onSuccess = { media -> state { copy(reading = SectionStatus.Success(media.toHomeMediaItems())) } },
        )
    }

    private fun observeTrending(type: MediaListType) {
        state { copy(trending = trending.copy(selectedType = type, status = SectionStatus.Loading)) }

        execute(
            useCase = observeTrendingMediaUseCase,
            params = type,
            onFailure = { failure -> state { copy(trending = trending.copy(status = SectionStatus.Error(failure))) } },
            onSuccess = { media ->
                state { copy(trending = trending.copy(status = SectionStatus.Success(media.toHomeMediaItems(type)))) }
            },
        )
    }

    private fun observePopular(type: MediaListType) {
        state { copy(popular = popular.copy(selectedType = type, status = SectionStatus.Loading)) }

        execute(
            useCase = observePopularMediaUseCase,
            params = type,
            onFailure = { failure -> state { copy(popular = popular.copy(status = SectionStatus.Error(failure))) } },
            onSuccess = { media ->
                state { copy(popular = popular.copy(status = SectionStatus.Success(media.toHomeMediaItems(type)))) }
            },
        )
    }

    private fun observeUpcoming() {
        state { copy(upcoming = SectionStatus.Loading) }

        execute(
            useCase = observeUpcomingAnimeUseCase,
            params = Unit,
            onFailure = { failure -> state { copy(upcoming = SectionStatus.Error(failure)) } },
            onSuccess = { media ->
                state { copy(upcoming = SectionStatus.Success(media.toHomeMediaItems(MediaListType.Anime))) }
            },
        )
    }

    private fun handleCloseWelcomeCard() {
        execute(
            useCase = hideWelcomeCardUseCase,
            params = Unit,
            onFailure = { state { copy(showWelcomeCard = false) } },
            onSuccess = { state { copy(showWelcomeCard = false) } },
        )
    }

    private fun handleSessionChanged(active: Boolean) {
        if (currentState.sessionActive == active) return

        state { copy(sessionActive = active) }
        observeWatching()
        observeReading()
    }

    private fun handleSelectTrendingType(type: MediaListType) {
        if (currentState.trending.selectedType != type) observeTrending(type)
    }

    private fun handleSelectPopularType(type: MediaListType) {
        if (currentState.popular.selectedType != type) observePopular(type)
    }

    private fun handleNavigateToAnimeLists() {
        effect(ForYouEffect.NavigateToAnimeLists)
    }

    private fun handleNavigateToMangaLists() {
        effect(ForYouEffect.NavigateToMangaLists)
    }

    private fun handleNavigateToTrending() {
        effect(ForYouEffect.NavigateToTrending)
    }

    private fun handleNavigateToPopular() {
        effect(ForYouEffect.NavigateToPopular)
    }

    private fun handleNavigateToUpcoming() {
        effect(ForYouEffect.NavigateToUpcoming)
    }
}
