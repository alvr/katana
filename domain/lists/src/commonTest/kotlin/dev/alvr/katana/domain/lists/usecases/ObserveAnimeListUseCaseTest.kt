package dev.alvr.katana.domain.lists.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ObserveAnimeListUseCaseTest : FreeSpec() {
    private val repo = mock<ListsRepository>()

    private val useCase = ObserveAnimeListUseCase(repo)

    init {
        "successfully observe the anime lists" {
            every { repo.animeCollection } returns flowOf(
                MediaCollection<MediaEntry.Anime>(emptyList()).right(),
            )

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeRight(MediaCollection(emptyList()))
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.animeCollection }
        }

        "failure observe the anime lists" {
            every { repo.animeCollection } returns flowOf(
                ListsFailure.GetMediaCollection.left(),
            )

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.animeCollection }
        }
    }
}
