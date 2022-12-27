package dev.alvr.katana.domain.lists.usecases

import arrow.core.Either
import arrow.core.left
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Stream
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource

@ExperimentalCoroutinesApi
internal class UpdateListUseCaseTest : TestBase() {
    @MockK
    private lateinit var repo: ListsRepository

    private lateinit var useCase: UpdateListUseCase

    private val mediaList = Arb.bind<MediaList>(
        mapOf(
            LocalDate::class to Arb.localDate(),
            LocalDateTime::class to Arb.localDateTime(),
        ),
    ).next()

    override suspend fun beforeEach() {
        useCase = spyk(UpdateListUseCase(repo))
    }

    @Nested
    @DisplayName("GIVEN a successful execution")
    inner class SuccessfulExecution {
        @Test
        @DisplayName("WHEN successful updateList THEN invoke should be right")
        fun `successful updateList (invoke)`() = runTest {
            // GIVEN
            coEitherJustRun { repo.updateList(any()) }

            // WHEN
            val result = useCase(mediaList)

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { repo.updateList(mediaList) }
            coVerify(exactly = 1) { useCase.invoke(mediaList) }
            verify(exactly = 0) { useCase.sync(mediaList) }
        }

        @Test
        @DisplayName("WHEN successful updateList THEN invoke should be right")
        fun `successful updateList (sync)`() = runTest {
            // GIVEN
            coEitherJustRun { repo.updateList(any()) }

            // WHEN
            val result = useCase.sync(mediaList)

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { repo.updateList(mediaList) }
            coVerify(exactly = 1) { useCase.invoke(mediaList) }
            verify(exactly = 1) { useCase.sync(mediaList) }
        }
    }

    @Nested
    @DisplayName("GIVEN a failure execution")
    inner class FailureExecution {
        @ArgumentsSource(FailuresArguments::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure updateList (invoke)`(failure: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { repo.updateList(any()) } returns expected

            // WHEN
            val result = useCase(mediaList)

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.updateList(mediaList) }
            coVerify(exactly = 1) { useCase.invoke(mediaList) }
        }

        @ArgumentsSource(FailuresArguments::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure updateList (sync)`(failure: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { repo.updateList(any()) } returns expected

            // WHEN
            val result = useCase.sync(mediaList)

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.updateList(mediaList) }
            verify(exactly = 1) { useCase.sync(mediaList) }
        }
    }

    private class FailuresArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
            Stream.of(
                Arguments.of(ListsFailure.UpdatingList, ListsFailure.UpdatingList.left()),
                Arguments.of(Failure.Unknown, Failure.Unknown.left()),
            )
    }
}
