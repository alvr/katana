package dev.alvr.katana.common.session.data.sources

import app.cash.turbine.test
import dev.alvr.katana.common.session.data.entities.Session
import dev.alvr.katana.common.session.data.mocks.anilistTokenMock
import dev.alvr.katana.common.session.data.mocks.sessionMock
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.core.preferences.di.store.KatanaStore
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
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.io.IOException

internal class SessionLocalSourceTest : FreeSpec() {
    private val store = mock<KatanaStore<Session>> { every { data } returns emptyFlow() }

    private lateinit var source: SessionLocalSource

    init {
        "successful" -
            {
                every { store.data } returns flowOf(Session(anilistToken = null))

                "getting a token from datastore for the first time" {
                    source.getAnilistToken().shouldBeNone()
                    verify { store.data }
                }

                "saving a session" {
                    everySuspend { store.update(any()) } returns sessionMock
                    source.saveSession(anilistTokenMock).shouldBeRight(Unit)
                    verifySuspend { store.update(any()) }
                }

                "getting the saved token" {
                    every { store.data } returns flowOf(Session(anilistToken = anilistTokenMock, sessionActive = true))

                    source.getAnilistToken().shouldBeSome(anilistTokenMock)
                    verify { store.data }
                }

                "deleting the saved token" {
                    everySuspend { store.update(any()) } returns sessionMock
                    source.deleteAnilistToken().shouldBeRight(Unit)
                    verifySuspend { store.update(any()) }
                }

                "clearing the session" {
                    everySuspend { store.update(any()) } returns sessionMock
                    source.clearActiveSession().shouldBeRight(Unit)
                    verifySuspend { store.update(any()) }
                }

                "logging out" {
                    everySuspend { store.update(any()) } returns sessionMock
                    source.logout().shouldBeRight(Unit)
                    verifySuspend { store.update(any()) }
                }

                listOf(
                        Session(anilistToken = null, sessionActive = false) to false,
                        Session(anilistToken = null, sessionActive = true) to false,
                        Session(anilistToken = anilistTokenMock, sessionActive = false) to false,
                        Session(anilistToken = anilistTokenMock, sessionActive = true) to true,
                    )
                    .forEach { (session, expected) ->
                        every { store.data } returns flowOf(session)

                        "checking session active for ${session.anilistToken} and ${session.sessionActive}" {
                            source.sessionActive.test {
                                awaitItem().shouldBeRight(expected)
                                awaitComplete()
                            }

                            verify { store.data }
                        }
                    }
            }

        "failure" -
            {
                "the clearing the session fails AND it's a common Exception" {
                    everySuspend { store.update(any()) } throws Exception()
                    source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                    verifySuspend { store.update(any()) }
                }

                "the clearing the session fails AND it's a writing Exception" {
                    everySuspend { store.update(any()) } throws IOException("Oops.")
                    source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                    verifySuspend { store.update(any()) }
                }

                "it's the deleting token AND it's a common Exception" {
                    everySuspend { store.update(any()) } throws Exception()
                    source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                    verifySuspend { store.update(any()) }
                }

                "it's the deleting token AND it's a writing Exception" {
                    everySuspend { store.update(any()) } throws IOException("Oops.")
                    source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                    verifySuspend { store.update(any()) }
                }

                "it's the saving token AND it's a common Exception" {
                    everySuspend { store.update(any()) } throws Exception()
                    source.saveSession(anilistTokenMock).shouldBeLeft(SessionFailure.SavingSession)
                    verifySuspend { store.update(any()) }
                }

                "it's the saving token AND it's a writing Exception" {
                    everySuspend { store.update(any()) } throws IOException("Oops.")
                    source.saveSession(anilistTokenMock).shouldBeLeft(SessionFailure.SavingSession)
                    verifySuspend { store.update(any()) }
                }

                "it's logging out AND it's a common Exception" {
                    everySuspend { store.update(any()) } throws Exception()
                    source.logout().shouldBeLeft(SessionFailure.LoggingOut)
                    verifySuspend { store.update(any()) }
                }

                "it's logging out AND it's a writing Exception" {
                    everySuspend { store.update(any()) } throws IOException("Oops.")
                    source.logout().shouldBeLeft(SessionFailure.LoggingOut)
                    verifySuspend { store.update(any()) }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        source = SessionLocalSourceImpl(store)
    }
}
