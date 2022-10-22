package dev.alvr.katana.domain.lists.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ObserveMangaListUseCaseTest : FunSpec() {
    private val repo = mockk<ListsRepository>()
    private val useCase = spyk(ObserveMangaListUseCase(repo))

    init {
        context("right manga lists observer") {
            test("invoke should observe the manga lists") {
                every { repo.mangaCollection } returns flowOf(
                    mockk<MediaCollection<MediaEntry.Manga>>().right(),
                )

                useCase()

                useCase.flow.test(5.seconds) {
                    awaitItem().shouldBeRight()
                    cancelAndConsumeRemainingEvents()
                }

                coVerify(exactly = 1) { useCase.invoke(Unit) }
                coVerify(exactly = 1) { repo.mangaCollection }
            }

            test("invoke the use case should call the invoke operator") {
                every { repo.mangaCollection } returns flowOf(
                    mockk<MediaCollection<MediaEntry.Manga>>().right(),
                )

                useCase(Unit)

                useCase.flow.test(5.seconds) {
                    awaitItem().shouldBeRight()
                    cancelAndConsumeRemainingEvents()
                }

                coVerify(exactly = 1) { useCase.invoke(Unit) }
            }
        }

        context("left manga lists observer") {
            test("invoke should observe the manga lists") {
                every { repo.mangaCollection } returns flowOf(mockk<ListsFailure>().left())

                useCase()

                useCase.flow.test(5.seconds) {
                    awaitItem().shouldBeLeft()
                    cancelAndConsumeRemainingEvents()
                }

                coVerify(exactly = 1) { useCase.invoke(Unit) }
                coVerify(exactly = 1) { repo.mangaCollection }
            }

            test("invoke the use case should call the invoke operator") {
                every { repo.mangaCollection } returns flowOf(mockk<ListsFailure>().left())

                useCase(Unit)

                useCase.flow.test(5.seconds) {
                    awaitItem().shouldBeLeft()
                    cancelAndConsumeRemainingEvents()
                }

                coVerify(exactly = 1) { useCase.invoke(Unit) }
            }
        }
    }
}
