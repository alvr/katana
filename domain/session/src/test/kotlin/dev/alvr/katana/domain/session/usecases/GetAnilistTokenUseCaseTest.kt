package dev.alvr.katana.domain.session.usecases

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.shouldBeNone
import dev.alvr.katana.common.tests.shouldBeSome
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.anilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetAnilistTokenUseCaseTest : TestBase() {
    @MockK
    private lateinit var repo: SessionRepository

    private lateinit var useCase: GetAnilistTokenUseCase

    override suspend fun beforeEach() {
        useCase = spyk(GetAnilistTokenUseCase(repo))
    }

    @Nested
    @DisplayName("GIVEN a successful repo call")
    inner class SuccessfulExecution {
        @Test
        @DisplayName("WHEN successful getAnilistToken THEN invoke should be some")
        fun `successful getAnilistToken (invoke)`() = runTest {
            // GIVEN
            coEvery { repo.getAnilistToken() } returns anilistToken.some()

            // WHEN
            val result = useCase()

            // THEN
            result.shouldBeSome(anilistToken)
            coVerify(exactly = 1) { repo.getAnilistToken() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 0) { useCase.sync(Unit) }
        }

        @Test
        @DisplayName("WHEN successful deleteAnilistToken THEN sync should be some")
        fun `successful deleteAnilistToken (sync)`() = runTest {
            // GIVEN
            coEvery { repo.getAnilistToken() } returns anilistToken.some()

            // WHEN
            val result = useCase.sync()

            // THEN
            result.shouldBeSome(anilistToken)
            coVerify(exactly = 1) { repo.getAnilistToken() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 1) { useCase.sync(Unit) }
        }
    }

    @Nested
    @DisplayName("GIVEN a failure repo call")
    inner class FailureExecution {
        @Test
        @DisplayName("WHEN failure getting THEN the result should be None")
        fun `failure getAnilistToken (invoke)`() = runTest {
            // GIVEN
            coEvery { repo.getAnilistToken() } returns none()

            // WHEN
            val result = useCase()

            // THEN
            result.shouldBeNone()
            coVerify(exactly = 1) { repo.getAnilistToken() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 0) { useCase.sync(Unit) }
        }

        @Test
        @DisplayName("WHEN failure getting THEN the result should be None")
        fun `failure getAnilistToken (sync)`() = runTest {
            // GIVEN
            coEvery { repo.getAnilistToken() } returns none()

            // WHEN
            val result = useCase.sync()

            // THEN
            result.shouldBeNone()
            coVerify(exactly = 1) { repo.getAnilistToken() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 1) { useCase.sync(Unit) }
        }
    }
}
