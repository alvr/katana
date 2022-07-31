package dev.alvr.katana.domain.lists.usecases

import app.cash.turbine.testIn
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flowOf

internal class ObserveAnimeListUseCaseTest : FunSpec({
    val repo = mockk<ListsRepository>()
    val useCase = spyk(ObserveAnimeListUseCase(repo))

    context("anime lists observer") {
        coEvery { repo.animeList } returns flowOf(mockk())
        useCase()

        test("invoke should observe the anime lists") {
            useCase.flow.testIn(this).run {
                awaitItem()
                cancelAndConsumeRemainingEvents()
            }
            coVerify(exactly = 1) { repo.animeList }
        }
    }

    test("invoke the use case should call the invoke operator") {
        coEvery { repo.animeList } returns mockk()

        useCase(Unit)

        coVerify(exactly = 1) { useCase.invoke(Unit) }
    }
},)
