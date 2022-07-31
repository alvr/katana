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

internal class ObserveMangaListUseCaseTest : FunSpec({
    val repo = mockk<ListsRepository>()
    val useCase = spyk(ObserveMangaListUseCase(repo))

    context("manga lists observer") {
        coEvery { repo.mangaList } returns flowOf(mockk())
        useCase()

        test("invoke should observe the manga lists") {
            useCase.flow.testIn(this).run {
                awaitItem()
                cancelAndConsumeRemainingEvents()
            }
            coVerify(exactly = 1) { repo.mangaList }
        }
    }

    test("invoke the use case should call the invoke operator") {
        coEvery { repo.mangaList } returns mockk()

        useCase(Unit)

        coVerify(exactly = 1) { useCase.invoke(Unit) }
    }
},)
