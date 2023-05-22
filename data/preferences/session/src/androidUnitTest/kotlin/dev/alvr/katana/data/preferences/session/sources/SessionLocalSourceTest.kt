package dev.alvr.katana.data.preferences.session.sources

import androidx.datastore.core.DataStore
import app.cash.turbine.test
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeNone
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.common.tests.shouldBeSome
import dev.alvr.katana.data.preferences.session.anilistToken
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SessionLocalSourceTest : TestBase() {
    @MockK
    private lateinit var store: DataStore<Session>
    @SpyK
    private var session = Session()

    private lateinit var source: SessionLocalSource

    override suspend fun beforeEach() {
        source = SessionLocalSourceImpl(store)
    }

    @Nested
    @DisplayName("GIVEN a SessionLocalSource that succeed")
    inner class SuccessfulExecution {
        @Test
        @DisplayName("WHEN getting a token from datastore for the first time THEN the token should be none")
        fun `getting a token from datastore for the first time`() = runTest {
            // GIVEN
            every { store.data } returns flowOf(Session(anilistToken = null))

            // WHEN
            val token = source.getAnilistToken()

            // THEN
            token.shouldBeNone()
            verify(exactly = 1) { store.data }
        }

        @Test
        @DisplayName("WHEN saving a session THEN the token should updated in the store")
        fun `saving a session`() = runTest {
            // GIVEN
            coJustRun { store.updateData(any()) }

            // WHEN
            val result = source.saveSession(AnilistToken(SAVED_TOKEN))

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName("WHEN getting the saved token THEN the token should be read from memory")
        fun `getting the saved token`() = runTest {
            // GIVEN
            every { store.data } returns flowOf(
                Session(anilistToken = AnilistToken(SAVED_TOKEN), isSessionActive = true),
            )

            // WHEN
            val token = source.getAnilistToken()

            // THEN
            token.shouldBeSome(AnilistToken(SAVED_TOKEN))
            verify(exactly = 1) { store.data }
        }

        @Test
        @DisplayName("WHEN deleting the saved token THEN the token should be none")
        fun `deleting the saved token`() = runTest {
            // GIVEN
            coJustRun { store.updateData(any()) }

            // WHEN
            val result = source.deleteAnilistToken()

            // THEN
            result.shouldBeRight()
            verify(exactly = 0) { store.data }
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName("WHEN clearing the session THEN the session should be valid")
        fun `clearing the session`() = runTest {
            // GIVEN
            coJustRun { store.updateData(any()) }

            // WHEN
            val result = source.clearActiveSession()

            // THEN
            result.shouldBeRight()
            verify(exactly = 0) { store.data }
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName("WHEN checking all conditions for active session THEN the token should be read from memory")
        fun `checking all conditions for active session`() = runTest {
            listOf(
                Session(anilistToken = null, isSessionActive = false),
                Session(anilistToken = null, isSessionActive = true),
                Session(anilistToken = AnilistToken("token"), isSessionActive = false),
                Session(anilistToken = AnilistToken("token"), isSessionActive = true),
            ).forEach { session ->
                // GIVEN
                every { store.data } returns flowOf(session)

                // WHEN
                source.sessionActive.test(5.seconds) {
                    awaitItem().shouldBeRight((session.anilistToken == null && session.isSessionActive).not())
                    cancelAndIgnoreRemainingEvents()
                }

                // THEN
                verify(exactly = 1) { store.data }

                clearAllMocks()
            }
        }
    }

    @Nested
    @DisplayName("GIVEN a SessionLocalSource that fails")
    inner class FailureExecution {
        @Test
        @DisplayName(
            """
            WHEN the clearing the session fails
                AND it's a common Exception
            THEN should be a left of SessionFailure.ClearingSession
            """,
        )
        fun `the clearing the session fails AND it's a common Exception`() = runTest {
            // GIVEN
            coEvery { store.updateData(any()) } throws Exception()

            // WHEN
            val result = source.clearActiveSession()

            // THEN
            result.shouldBeLeft(SessionFailure.ClearingSession)
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName(
            """
            WHEN the clearing the session fails
                AND it's a writing Exception
            THEN should be a left of PreferencesTokenFailure.ClearingSessionFailure
            """,
        )
        fun `the clearing the session fails AND it's a writing Exception`() = runTest {
            // GIVEN
            coEvery { store.updateData(any()) } throws IOException()

            // WHEN
            val result = source.clearActiveSession()

            // THEN
            result.shouldBeLeft(SessionFailure.ClearingSession)
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName(
            """
            WHEN it's the deleting token
                AND it's a common Exception
            THEN should be a left of SessionFailure.DeletingToken
            """,
        )
        fun `it's the deleting token AND it's a common Exception`() = runTest {
            // GIVEN
            coEvery { store.updateData(any()) } throws Exception()

            // WHEN
            val result = source.deleteAnilistToken()

            // THEN
            result.shouldBeLeft(SessionFailure.DeletingToken)
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName(
            """
            WHEN it's the deleting token
                AND it's a writing Exception
            THEN should be a left of PreferencesTokenFailure.DeletingFailure
            """,
        )
        fun `it's the deleting token AND it's a writing Exception`() = runTest {
            // GIVEN
            coEvery { store.updateData(any()) } throws IOException()

            // WHEN
            val result = source.deleteAnilistToken()

            // THEN
            result.shouldBeLeft(SessionFailure.DeletingToken)
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName(
            """
            WHEN it's the saving token
                AND it's a common Exception
            THEN should be a left of SessionFailure.SavingSession
            """,
        )
        fun `it's the saving token AND it's a common Exception`() = runTest {
            // GIVEN
            coEvery { store.updateData(any()) } throws Exception()

            // WHEN
            val result = source.saveSession(anilistToken)

            // THEN
            result.shouldBeLeft(SessionFailure.SavingSession)
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName(
            """
            WHEN it's the saving token
                AND it's a writing Exception
            THEN should be a left of PreferencesTokenFailure.SavingSession
            """,
        )
        fun `it's the saving token AND it's a writing Exception`() = runTest {
            // GIVEN
            coEvery { store.updateData(any()) } throws IOException()

            // WHEN
            val result = source.saveSession(anilistToken)

            // THEN
            result.shouldBeLeft(SessionFailure.SavingSession)
            coVerify(exactly = 1) { store.updateData(any()) }
        }

        @Test
        @DisplayName("WHEN it's getting if the session is active THEN should be a left of Failure.Unknown")
        fun `it's getting if the session is active`() = runTest {
            // GIVEN
            every { store.data } returns flowOf(session)
            every { session.anilistToken } throws mockk<Exception>()

            // WHEN
            source.sessionActive.test(5.seconds) {
                awaitItem().shouldBeLeft(SessionFailure.CheckingActiveSession)
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            verify(exactly = 1) { session.anilistToken }
            verify(exactly = 1) { store.data }
        }

        @Test
        @DisplayName("WHEN it's getting the token THEN should be a left of PreferencesTokenFailure.SavingSession")
        fun `it's getting the token`() = runTest {
            // GIVEN
            every { store.data } returns flowOf(session)
            every { session.anilistToken } throws mockk<Exception>()

            // WHEN
            val result = source.getAnilistToken()

            // THEN
            result.shouldBeNone()
            verify(exactly = 1) { session.anilistToken }
            verify(exactly = 1) { store.data }
        }
    }

    private companion object {
        const val SAVED_TOKEN = "saved-token"
    }
}
