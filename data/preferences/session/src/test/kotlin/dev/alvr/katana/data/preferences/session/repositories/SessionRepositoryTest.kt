package dev.alvr.katana.data.preferences.session.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.none
import arrow.core.right
import arrow.core.some
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeNone
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.common.tests.shouldBeSome
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSource
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SessionRepositoryTest : TestBase() {
    @MockK
    private lateinit var source: SessionLocalSource

    private lateinit var repo: SessionRepository

    override suspend fun beforeEach() {
        repo = SessionRepositoryImpl(source)
    }

    @Test
    @DisplayName(
        """
        WHEN observing if the session is active
        THEN collecting the flow should get the same items in the same order
        """,
    )
    fun `observing if the session is active`() = runTest {
        // GIVEN
        every { source.sessionActive } returns flowOf(
            true.right(),
            true.right(),
            false.right(),
        )

        // WHEN
        repo.sessionActive.test(5.seconds) {
            awaitItem().shouldBeRight(true)
            awaitItem().shouldBeRight(true)
            awaitItem().shouldBeRight(false)
            awaitComplete()
        }

        // THEN
        verify(exactly = 1) { source.sessionActive }
    }

    @Test
    @DisplayName("WHEN getting a non-existing token THEN the token should be none")
    fun `getting a non-existing token`() = runTest {
        // GIVEN
        coEvery { source.getAnilistToken() } returns none()

        // WHEN
        val token = repo.getAnilistToken()

        // THEN
        token.shouldBeNone()
        coVerify(exactly = 1) { source.getAnilistToken() }
    }

    @Test
    @DisplayName("WHEN getting an existing token THEN the token should be some")
    fun `getting an existing token`() = runTest {
        // GIVEN
        coEvery { source.getAnilistToken() } returns AnilistToken(SAVED_TOKEN).some()

        // WHEN
        val token = repo.getAnilistToken()

        // THEN
        token.shouldBeSome(AnilistToken(SAVED_TOKEN))
        coVerify(exactly = 1) { source.getAnilistToken() }
    }

    @Test
    @DisplayName("WHEN saving a session without error THEN the result should be right")
    fun `saving a session without error`() = runTest {
        // GIVEN
        coEitherJustRun { source.saveSession(any()) }

        // WHEN
        val result = repo.saveSession(AnilistToken(SAVED_TOKEN))

        // THEN
        result.shouldBeRight()
        coVerify(exactly = 1) { source.saveSession(AnilistToken(SAVED_TOKEN)) }
    }

    @Test
    @DisplayName("WHEN saving a session with an error THEN the result should be left")
    fun `saving a session with an error`() = runTest {
        // GIVEN
        coEvery { source.saveSession(any()) } returns SessionFailure.SavingSession.left()

        // WHEN
        val result = repo.saveSession(AnilistToken(SAVED_TOKEN))

        // THEN
        result.shouldBeLeft(SessionFailure.SavingSession)
        coVerify(exactly = 1) { source.saveSession(AnilistToken(SAVED_TOKEN)) }
    }

    @Test
    @DisplayName("WHEN deleting a session without error THEN the result should be right")
    fun `deleting a session without error`() = runTest {
        // GIVEN
        coEitherJustRun { source.deleteAnilistToken() }

        // WHEN
        val result = repo.deleteAnilistToken()

        // THEN
        result.shouldBeRight()
        coVerify(exactly = 1) { source.deleteAnilistToken() }
    }

    @Test
    @DisplayName("WHEN deleting a session with an error THEN the result should be left")
    fun `deleting a session with an error`() = runTest {
        // GIVEN
        coEvery { source.deleteAnilistToken() } returns SessionFailure.DeletingToken.left()

        // WHEN
        val result = repo.deleteAnilistToken()

        // THEN
        result.shouldBeLeft(SessionFailure.DeletingToken)
        coVerify(exactly = 1) { source.deleteAnilistToken() }
    }

    @Test
    @DisplayName("WHEN clearing a session without error THEN the result should be right")
    fun `clearing a session without error`() = runTest {
        // GIVEN
        coEitherJustRun { source.clearActiveSession() }

        // WHEN
        val result = repo.clearActiveSession()

        // THEN
        result.shouldBeRight()
        coVerify(exactly = 1) { source.clearActiveSession() }
    }

    @Test
    @DisplayName("WHEN clearing a session with an error THEN the result should be left")
    fun `clearing a session with an error`() = runTest {
        // GIVEN
        coEvery { source.clearActiveSession() } returns SessionFailure.ClearingSession.left()

        // WHEN
        val result = repo.clearActiveSession()

        // THEN
        result.shouldBeLeft(SessionFailure.ClearingSession)
        coVerify(exactly = 1) { source.clearActiveSession() }
    }

    private companion object {
        const val SAVED_TOKEN = "saved-token"
    }
}
