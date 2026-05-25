package dev.alvr.katana.features.lists.domain.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.lists.domain.di.createListsDomainTestGraph
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListType
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.flowOf

internal class ObserveListUseCaseTest : FreeSpec() {
    private val repo = mock<ListsRepository>()

    private lateinit var useCase: ObserveListUseCase

    init {
        listScenarios.forEach { scenario ->
            "successfully observe the ${scenario.name} lists" {
                scenario.mockSuccess()

                useCase(scenario.type)

                useCase.flow.test {
                    awaitItem().shouldBeRight(MediaCollection(emptyList()))
                    ensureAllEventsConsumed()
                }

                scenario.verifyCollection()
            }

            "failure observe the ${scenario.name} lists" {
                scenario.mockFailure()

                useCase(scenario.type)

                useCase.flow.test {
                    awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                    ensureAllEventsConsumed()
                }

                scenario.verifyCollection()
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = createListsDomainTestGraph(repo).observeListUseCase
    }

    private fun ListScenario.mockSuccess() {
        when (type) {
            MediaListType.Anime ->
                every { repo.animeCollection } returns flowOf(MediaCollection<MediaEntry.Anime>(emptyList()).right())
            MediaListType.Manga ->
                every { repo.mangaCollection } returns flowOf(MediaCollection<MediaEntry.Manga>(emptyList()).right())
        }
    }

    private fun ListScenario.mockFailure() {
        when (type) {
            MediaListType.Anime -> every { repo.animeCollection } returns flowOf(ListsFailure.GetMediaCollection.left())
            MediaListType.Manga -> every { repo.mangaCollection } returns flowOf(ListsFailure.GetMediaCollection.left())
        }
    }

    private fun ListScenario.verifyCollection() {
        when (type) {
            MediaListType.Anime -> verify { repo.animeCollection }
            MediaListType.Manga -> verify { repo.mangaCollection }
        }
    }
}

private data class ListScenario(val name: String, val type: MediaListType)

private val listScenarios =
    listOf(
        ListScenario(name = "anime", type = MediaListType.Anime),
        ListScenario(name = "manga", type = MediaListType.Manga),
    )
