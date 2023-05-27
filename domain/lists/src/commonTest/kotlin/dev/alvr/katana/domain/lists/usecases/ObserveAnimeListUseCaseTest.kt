package dev.alvr.katana.domain.lists.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import dev.alvr.katana.domain.lists.repositories.MockListsRepository
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(ListsRepository::class)
internal class ObserveAnimeListUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockListsRepository(mocker)

    private val useCase = ObserveAnimeListUseCase(repo)

    init {
        "successfully observe the anime lists" {
            mocker.every { repo.animeCollection } returns flowOf(
                MediaCollection<MediaEntry.Anime>(emptyList()).right(),
            )

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeRight(MediaCollection(emptyList()))
                cancelAndConsumeRemainingEvents()
            }

            mocker.verify { repo.animeCollection }
        }

        "failure observe the anime lists" {
            mocker.every { repo.animeCollection } returns flowOf(
                ListsFailure.GetMediaCollection.left(),
            )

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                cancelAndConsumeRemainingEvents()
            }

            mocker.verify { repo.animeCollection }
        }
    }

    override fun extensions() = listOf(mocker())
}
