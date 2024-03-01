package dev.alvr.katana.common.session.data.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.none
import arrow.core.right
import arrow.core.some
import dev.alvr.katana.common.session.data.mocks.anilistTokenMock
import dev.alvr.katana.common.session.data.sources.SessionLocalSource
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeNone
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.core.tests.shouldBeSome
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class SessionRepositoryTest : FreeSpec() {
    private val source: SessionLocalSource = mock<SessionLocalSource>()

    private val repo: SessionRepository = SessionRepositoryImpl(source)

    init {
        "observing" - {
            "observing if the session is active" {
                every { source.sessionActive } returns flowOf(
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

                verify { source.sessionActive }
            }
        }

        "getting" - {
            "getting a non-existing token" {
                everySuspend { source.getAnilistToken() } returns none()
                repo.getAnilistToken().shouldBeNone()
                verifySuspend { source.getAnilistToken() }
            }

            "getting an existing token" {
                everySuspend { source.getAnilistToken() } returns anilistTokenMock.some()
                repo.getAnilistToken().shouldBeSome(anilistTokenMock)
                verifySuspend { source.getAnilistToken() }
            }
        }

        "saving" - {
            "saving a session without error" {
                everySuspend { source.saveSession(anilistTokenMock) } returns Unit.right()
                repo.saveSession(anilistTokenMock).shouldBeRight(Unit)
                verifySuspend { source.saveSession(anilistTokenMock) }
            }

            "saving a session with an error" {
                everySuspend { source.saveSession(anilistTokenMock) } returns SessionFailure.SavingSession.left()
                repo.saveSession(anilistTokenMock).shouldBeLeft(SessionFailure.SavingSession)
                verifySuspend { source.saveSession(anilistTokenMock) }
            }
        }

        "deleting" - {
            "deleting a session without error" {
                everySuspend { source.deleteAnilistToken() } returns Unit.right()
                repo.deleteAnilistToken().shouldBeRight(Unit)
                verifySuspend { source.deleteAnilistToken() }
            }

            "deleting a session with an error" {
                everySuspend { source.deleteAnilistToken() } returns SessionFailure.DeletingToken.left()
                repo.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                verifySuspend { source.deleteAnilistToken() }
            }
        }

        "clearing" - {
            "clearing a session without error" {
                everySuspend { source.clearActiveSession() } returns Unit.right()
                repo.clearActiveSession().shouldBeRight(Unit)
                verifySuspend { source.clearActiveSession() }
            }

            "clearing a session with an error" {
                everySuspend { source.clearActiveSession() } returns SessionFailure.ClearingSession.left()
                repo.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                verifySuspend { source.clearActiveSession() }
            }
        }

        "logging out" - {
            "logging out without error" {
                everySuspend { source.logout() } returns Unit.right()
                repo.logout().shouldBeRight(Unit)
                verifySuspend { source.logout() }
            }

            "logging out with an error" {
                everySuspend { source.logout() } returns SessionFailure.LoggingOut.left()
                repo.logout().shouldBeLeft(SessionFailure.LoggingOut)
                verifySuspend { source.logout() }
            }
        }
    }
}
