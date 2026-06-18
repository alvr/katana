package dev.alvr.katana.common.media.domain.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.media.domain.di.createMediaDomainTestGraph
import dev.alvr.katana.common.media.domain.failures.MediaFailure
import dev.alvr.katana.common.media.domain.models.MediaCollection
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.common.media.domain.repositories.MediaCollectionRepository
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.flowOf

internal class ObserveMediaCollectionUseCaseTest : FreeSpec() {
    private val repository = mock<MediaCollectionRepository>()

    private lateinit var useCase: ObserveMediaCollectionUseCase

    init {
        listScenarios.forEach { scenario ->
            "successfully observe the ${scenario.name} lists" {
                scenario.mockSuccess()

                useCase(ObserveMediaCollectionUseCase.Params(scenario.type, MediaListStatus.All))

                useCase.flow.test {
                    awaitItem().shouldBeRight(MediaCollection(emptyList()))
                    ensureAllEventsConsumed()
                }

                scenario.verifyCollection()
            }

            "failure observe the ${scenario.name} lists" {
                scenario.mockFailure()

                useCase(ObserveMediaCollectionUseCase.Params(scenario.type, MediaListStatus.All))

                useCase.flow.test {
                    awaitItem().shouldBeLeft(MediaFailure.GetMediaCollection)
                    ensureAllEventsConsumed()
                }

                scenario.verifyCollection()
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = createMediaDomainTestGraph(repository).observeMediaCollectionUseCase
    }

    private fun ListScenario.mockSuccess() {
        when (type) {
            MediaListType.Anime ->
                every { repository.animeCollection(status = MediaListStatus.All) } returns
                    flowOf(MediaCollection<MediaEntry.Anime>(emptyList()).right())
            MediaListType.Manga ->
                every { repository.mangaCollection(status = MediaListStatus.All) } returns
                    flowOf(MediaCollection<MediaEntry.Manga>(emptyList()).right())
        }
    }

    private fun ListScenario.mockFailure() {
        when (type) {
            MediaListType.Anime ->
                every { repository.animeCollection(status = MediaListStatus.All) } returns
                    flowOf(MediaFailure.GetMediaCollection.left())
            MediaListType.Manga ->
                every { repository.mangaCollection(status = MediaListStatus.All) } returns
                    flowOf(MediaFailure.GetMediaCollection.left())
        }
    }

    private fun ListScenario.verifyCollection() {
        when (type) {
            MediaListType.Anime -> verify { repository.animeCollection(status = MediaListStatus.All) }
            MediaListType.Manga -> verify { repository.mangaCollection(status = MediaListStatus.All) }
        }
    }
}

private data class ListScenario(val name: String, val type: MediaListType)

private val listScenarios =
    listOf(
        ListScenario(name = "anime", type = MediaListType.Anime),
        ListScenario(name = "manga", type = MediaListType.Manga),
    )
