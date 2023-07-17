package dev.alvr.katana.data.preferences.session.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.none
import arrow.core.right
import arrow.core.some
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeNone
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.common.tests.shouldBeSome
import dev.alvr.katana.data.preferences.session.sources.MockSessionLocalSource
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSource
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(SessionLocalSource::class)
internal class SessionRepositoryTest : FreeSpec() {
    private val mocker = Mocker()
    private val source: SessionLocalSource = MockSessionLocalSource(mocker)
    private val repo: SessionRepository = SessionRepositoryImpl(source)

    private val anilistToken = AnilistToken("TOKEN")

    init {
        "observing" - {
            "observing if the session is active" {
                mocker.every { source.sessionActive } returns flowOf(
                    true.right(),
                    true.right(),
                    false.right(),
                )

                repo.sessionActive.test(5.seconds) {
                    awaitItem().shouldBeRight(true)
                    awaitItem().shouldBeRight(true)
                    awaitItem().shouldBeRight(false)
                    awaitComplete()
                }

                mocker.verify { source.sessionActive }
            }
        }

        "getting" - {
            "getting a non-existing token" {
                mocker.everySuspending { source.getAnilistToken() } returns none()
                repo.getAnilistToken().shouldBeNone()
                mocker.verifyWithSuspend { source.getAnilistToken() }
            }

            "getting an existing token" {
                mocker.everySuspending { source.getAnilistToken() } returns anilistToken.some()
                repo.getAnilistToken().shouldBeSome(anilistToken)
                mocker.verifyWithSuspend { source.getAnilistToken() }
            }
        }

        "saving" - {
            "saving a session without error" {
                mocker.everySuspending { source.saveSession(isAny()) } returns Unit.right()
                repo.saveSession(anilistToken).shouldBeRight(Unit)
                mocker.verifyWithSuspend { source.saveSession(anilistToken) }
            }

            "saving a session with an error" {
                mocker.everySuspending { source.saveSession(isAny()) } returns SessionFailure.SavingSession.left()
                repo.saveSession(anilistToken).shouldBeLeft(SessionFailure.SavingSession)
                mocker.verifyWithSuspend { source.saveSession(anilistToken) }
            }
        }

        "deleting" - {
            "deleting a session without error" {
                mocker.everySuspending { source.deleteAnilistToken() } returns Unit.right()
                repo.deleteAnilistToken().shouldBeRight(Unit)
                mocker.verifyWithSuspend { source.deleteAnilistToken() }
            }

            "deleting a session with an error" {
                mocker.everySuspending { source.deleteAnilistToken() } returns SessionFailure.DeletingToken.left()
                repo.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                mocker.verifyWithSuspend { source.deleteAnilistToken() }
            }
        }

        "clearing" - {
            "clearing a session without error" {
                mocker.everySuspending { source.clearActiveSession() } returns Unit.right()
                repo.clearActiveSession().shouldBeRight(Unit)
                mocker.verifyWithSuspend { source.clearActiveSession() }
            }

            "clearing a session with an error" {
                mocker.everySuspending { source.clearActiveSession() } returns SessionFailure.ClearingSession.left()
                repo.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                mocker.verifyWithSuspend { source.clearActiveSession() }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
