package dev.alvr.katana.data.preferences.session.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.none
import arrow.core.right
import arrow.core.some
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSource
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class SessionRepositoryTest : BehaviorSpec() {
    init {
        given("a SessionRepository") {
            val source = mockk<SessionLocalSource>()
            val repo: SessionRepository = SessionRepositoryImpl(source)

            `when`("observing if the session is active") {
                every { source.sessionActive } returns flowOf(true, true, false)

                then("collecting the flow should get the same items in the same order") {
                    repo.sessionActive.test(5.seconds) {
                        awaitItem().shouldBeTrue()
                        awaitItem().shouldBeTrue()
                        awaitItem().shouldBeFalse()
                        awaitComplete()
                    }
                    verify(exactly = 1) { source.sessionActive }
                }
            }

            `when`("getting a non-existing token") {
                coEvery { source.getAnilistToken() } returns none()
                val token = repo.getAnilistToken()

                then("the token should be none") {
                    token.shouldBeNone()
                    coVerify(exactly = 1) { source.getAnilistToken() }
                }
            }

            `when`("getting an existing token") {
                coEvery { source.getAnilistToken() } returns AnilistToken(SAVED_TOKEN).some()
                val token = repo.getAnilistToken()

                then("the token should be some") {
                    token.shouldBeSome(AnilistToken(SAVED_TOKEN))
                    coVerify(exactly = 1) { source.getAnilistToken() }
                }
            }

            `when`("saving a session without error") {
                coEvery { source.saveSession(any()) } returns Unit.right()
                val result = repo.saveSession(AnilistToken(SAVED_TOKEN))

                then("the result should be right") {
                    result.shouldBeRight()
                    coVerify(exactly = 1) { source.saveSession(AnilistToken(SAVED_TOKEN)) }
                }
            }

            `when`("saving a session with an error") {
                coEvery { source.saveSession(any()) } returns SessionFailure.SavingSession.left()
                val result = repo.saveSession(AnilistToken(SAVED_TOKEN))

                then("the result should be left") {
                    result.shouldBeLeft(SessionFailure.SavingSession)
                    coVerify(exactly = 1) { source.saveSession(AnilistToken(SAVED_TOKEN)) }
                }
            }

            `when`("deleting a session without error") {
                coEvery { source.deleteAnilistToken() } returns Unit.right()
                val result = repo.deleteAnilistToken()

                then("the result should be right") {
                    result.shouldBeRight()
                    coVerify(exactly = 1) { source.deleteAnilistToken() }
                }
            }

            `when`("deleting a session with an error") {
                coEvery { source.deleteAnilistToken() } returns SessionFailure.DeletingToken.left()
                val result = repo.deleteAnilistToken()

                then("the result should be left") {
                    result.shouldBeLeft(SessionFailure.DeletingToken)
                    coVerify(exactly = 1) { source.deleteAnilistToken() }
                }
            }

            `when`("clearing a session without error") {
                coEvery { source.clearActiveSession() } returns Unit.right()
                val result = repo.clearActiveSession()

                then("the result should be right") {
                    result.shouldBeRight()
                    coVerify(exactly = 1) { source.clearActiveSession() }
                }
            }

            `when`("clearing a session with an error") {
                coEvery { source.clearActiveSession() } returns SessionFailure.ClearingSession.left()
                val result = repo.clearActiveSession()

                then("the result should be left") {
                    result.shouldBeLeft(SessionFailure.ClearingSession)
                    coVerify(exactly = 1) { source.clearActiveSession() }
                }
            }
        }
    }

    private companion object {
        const val SAVED_TOKEN = "saved-token"
    }
}
