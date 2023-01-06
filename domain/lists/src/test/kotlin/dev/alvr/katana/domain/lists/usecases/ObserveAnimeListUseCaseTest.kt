package dev.alvr.katana.domain.lists.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class ObserveAnimeListUseCaseTest : TestBase() {
    @MockK
    private lateinit var repo: ListsRepository

    private lateinit var useCase: ObserveAnimeListUseCase

    override suspend fun beforeEach() {
        useCase = spyk(ObserveAnimeListUseCase(repo))
    }

    @Test
    @DisplayName("WHEN invoking THEN should observe the anime lists")
    fun `invoking the useCase (success)`() = runTest {
        // GIVEN
        every { repo.animeCollection } returns flowOf(
            MediaCollection<MediaEntry.Anime>(emptyList()).right(),
        )

        // WHEN
        useCase()

        // THEN
        useCase.flow.test(5.seconds) {
            awaitItem().shouldBeRight()
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { useCase.invoke(Unit) }
        verify(exactly = 1) { useCase.flow }
        verify(exactly = 1) { repo.animeCollection }
    }

    @Test
    @DisplayName("WHEN invoking THEN should observe the anime lists")
    fun `invoking the useCase (failure)`() = runTest {
        // GIVEN
        every { repo.animeCollection } returns flowOf(mockk<ListsFailure>().left())

        // WHEN
        useCase()

        // THEN
        useCase.flow.test(5.seconds) {
            awaitItem().shouldBeLeft()
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { useCase.invoke(Unit) }
        verify(exactly = 1) { useCase.flow }
        verify(exactly = 1) { repo.animeCollection }
    }
}
