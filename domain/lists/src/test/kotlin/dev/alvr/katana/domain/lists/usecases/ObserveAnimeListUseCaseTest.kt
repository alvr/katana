package dev.alvr.katana.domain.lists.usecases

import app.cash.turbine.test
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.kotest.core.spec.style.FunSpec
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ObserveAnimeListUseCaseTest : FunSpec({
    val repo = mockk<ListsRepository>()
    val useCase = spyk(ObserveAnimeListUseCase(repo))

    context("anime lists observer") {
        test("invoke should observe the anime lists") {
            every { repo.animeCollection } returns flowOf(mockk())

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem()
                cancelAndConsumeRemainingEvents()
            }

            coVerify(exactly = 1) { useCase.invoke(Unit) }
            coVerify(exactly = 1) { repo.animeCollection }
        }

        test("invoke the use case should call the invoke operator") {
            every { repo.animeCollection } returns flowOf(mockk())

            useCase(Unit)

            useCase.flow.test(5.seconds) {
                awaitItem()
                cancelAndConsumeRemainingEvents()
            }

            coVerify(exactly = 1) { useCase.invoke(Unit) }
        }
    }
},)
