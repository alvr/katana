package dev.alvr.katana.common.session.data.sources

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import app.cash.turbine.test
import dev.alvr.katana.common.session.data.mocks.anilistTokenMock
import dev.alvr.katana.common.session.data.mocks.sessionMock
import dev.alvr.katana.common.session.data.models.Session
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeNone
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.core.tests.shouldBeSome
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class SessionLocalSourceTest : FreeSpec() {
    private val store = mock<DataStore<Session>>()

    private val source: SessionLocalSource = SessionLocalSourceImpl(store)

    init {
        "successful" - {
            "getting a token from datastore for the first time" {
                every { store.data } returns flowOf(Session(anilistToken = null))
                source.getAnilistToken().shouldBeNone()
                verify { store.data }
            }

            "saving a session" {
                everySuspend { store.updateData(any()) } returns sessionMock
                source.saveSession(anilistTokenMock).shouldBeRight(Unit)
                verifySuspend { store.updateData(any()) }
            }

            "getting the saved token" {
                every { store.data } returns flowOf(
                    Session(
                        anilistToken = anilistTokenMock,
                        isSessionActive = true,
                    ),
                )
                source.getAnilistToken().shouldBeSome(anilistTokenMock)
                verify { store.data }
            }

            "deleting the saved token" {
                everySuspend { store.updateData(any()) } returns sessionMock
                source.deleteAnilistToken().shouldBeRight(Unit)
                verifySuspend { store.updateData(any()) }
            }

            "clearing the session" {
                everySuspend { store.updateData(any()) } returns sessionMock
                source.clearActiveSession().shouldBeRight(Unit)
                verifySuspend { store.updateData(any()) }
            }

            "logging out" {
                everySuspend { store.updateData(any()) } returns sessionMock
                source.logout().shouldBeRight(Unit)
                verifySuspend { store.updateData(any()) }
            }

            listOf(
                Session(anilistToken = null, isSessionActive = false),
                Session(anilistToken = null, isSessionActive = true),
                Session(anilistToken = anilistTokenMock, isSessionActive = false),
                Session(anilistToken = anilistTokenMock, isSessionActive = true),
            ).forEach { session ->
                "checking session active for ${session.anilistToken} and ${session.isSessionActive}" {
                    every { store.data } returns flowOf(session)

                    source.sessionActive.test(5.seconds) {
                        awaitItem().shouldBeRight((session.anilistToken == null && session.isSessionActive).not())
                        cancelAndIgnoreRemainingEvents()
                    }

                    verify { store.data }
                }
            }
        }

        "failure" - {
            "the clearing the session fails AND it's a common Exception" {
                everySuspend { store.updateData(any()) } throws Exception()
                source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                verifySuspend { store.updateData(any()) }
            }

            "the clearing the session fails AND it's a writing Exception" {
                everySuspend { store.updateData(any()) } throws IOException("Oops.")
                source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                verifySuspend { store.updateData(any()) }
            }

            "it's the deleting token AND it's a common Exception" {
                everySuspend { store.updateData(any()) } throws Exception()
                source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                verifySuspend { store.updateData(any()) }
            }

            "it's the deleting token AND it's a writing Exception" {
                everySuspend { store.updateData(any()) } throws IOException("Oops.")
                source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                verifySuspend { store.updateData(any()) }
            }

            "it's the saving token AND it's a common Exception" {
                everySuspend { store.updateData(any()) } throws Exception()
                source.saveSession(anilistTokenMock).shouldBeLeft(SessionFailure.SavingSession)
                verifySuspend { store.updateData(any()) }
            }

            "it's the saving token AND it's a writing Exception" {
                everySuspend { store.updateData(any()) } throws IOException("Oops.")
                source.saveSession(anilistTokenMock).shouldBeLeft(SessionFailure.SavingSession)
                verifySuspend { store.updateData(any()) }
            }

            "it's logging out AND it's a common Exception" {
                everySuspend { store.updateData(any()) } throws Exception()
                source.logout().shouldBeLeft(SessionFailure.LoggingOut)
                verifySuspend { store.updateData(any()) }
            }

            "it's logging out AND it's a writing Exception" {
                everySuspend { store.updateData(any()) } throws IOException("Oops.")
                source.logout().shouldBeLeft(SessionFailure.LoggingOut)
                verifySuspend { store.updateData(any()) }
            }
        }
    }
}
