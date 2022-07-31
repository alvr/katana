package dev.alvr.katana.domain.lists.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import java.time.LocalDate
import java.time.LocalDateTime

internal class UpdateListUseCaseTest : FunSpec({
    val repo = mockk<ListsRepository>()
    val useCase = spyk(UpdateListUseCase(repo))

    val mediaList = Arb.bind<MediaList>(
        mapOf(
            LocalDate::class to Arb.localDate(),
            LocalDateTime::class to Arb.localDateTime(),
        ),
    ).next()

    context("successful updating") {
        coEvery { repo.updateList(any()) } returns Unit.right()

        test("invoke should update the list") {
            useCase(mediaList).shouldBeRight()
            coVerify(exactly = 1) { repo.updateList(mediaList) }
        }

        test("sync should update the list") {
            useCase.sync(mediaList).shouldBeRight()
            coVerify(exactly = 1) { repo.updateList(mediaList) }
        }
    }

    context("failure updating") {
        context("is a ListsFailure.UpdatingList") {
            coEvery { repo.updateList(any()) } returns ListsFailure.UpdatingList.left()

            test("invoke should return failure") {
                useCase(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                coVerify(exactly = 1) { repo.updateList(mediaList) }
            }

            test("sync should return failure") {
                useCase.sync(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                coVerify(exactly = 1) { repo.updateList(mediaList) }
            }
        }

        context("is a Failure.Unknown") {
            coEvery { repo.updateList(any()) } returns Failure.Unknown.left()

            test("invoke should return failure") {
                useCase(mediaList).shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.updateList(mediaList) }
            }

            test("sync should return failure") {
                useCase.sync(mediaList).shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.updateList(mediaList) }
            }
        }
    }

    test("invoke the use case should call the invoke operator") {
        coEvery { repo.updateList(any()) } returns mockk()

        useCase(mediaList)

        coVerify(exactly = 1) { useCase.invoke(mediaList) }
    }

    test("sync the use case should call the invoke operator") {
        coEvery { repo.updateList(any()) } returns mockk()

        useCase.sync(mediaList)

        coVerify(exactly = 1) { useCase.sync(mediaList) }
    }
},)
