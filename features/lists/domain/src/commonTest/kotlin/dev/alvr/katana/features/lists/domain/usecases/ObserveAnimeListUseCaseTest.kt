package dev.alvr.katana.features.lists.domain.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
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
