package dev.alvr.katana.data.preferences.session.sources

import androidx.datastore.core.IOException
import app.cash.turbine.test
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeNone
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.common.tests.shouldBeSome
import dev.alvr.katana.data.preferences.session.mocks.MockSessionDataStore
import dev.alvr.katana.data.preferences.session.mocks.SessionDataStore
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.data.preferences.session.models.fakeSession
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks

@UsesFakes(Session::class)
@UsesMocks(SessionDataStore::class)
@Suppress("TooGenericExceptionThrown")
internal class SessionLocalSourceTest : FreeSpec() {
    private val mocker = Mocker()
    private val store: SessionDataStore = MockSessionDataStore(mocker)
    private val source: SessionLocalSource = SessionLocalSourceImpl(store)

    private val anilistToken = AnilistToken("TOKEN")

    init {
        "successful" - {
            "getting a token from datastore for the first time" {
                mocker.every { store.data } returns flowOf(Session(anilistToken = null))
                source.getAnilistToken().shouldBeNone()
                mocker.verify { store.data }
            }

            "saving a session" {
                mocker.everySuspending { store.updateData(isAny()) } returns fakeSession()
                source.saveSession(anilistToken).shouldBeRight(Unit)
                mocker.verifyWithSuspend { store.updateData(isAny()) }
            }

            "getting the saved token" {
                mocker.every { store.data } returns flowOf(
                    Session(
                        anilistToken = anilistToken,
                        isSessionActive = true,
                    ),
                )
                source.getAnilistToken().shouldBeSome(anilistToken)
                mocker.verify { store.data }
            }

            "deleting the saved token" {
                mocker.everySuspending { store.updateData(isAny()) } returns fakeSession()
                source.deleteAnilistToken().shouldBeRight(Unit)
                mocker.verifyWithSuspend { store.updateData(isAny()) }
            }

            "clearing the session" {
                mocker.everySuspending { store.updateData(isAny()) } returns fakeSession()
                source.clearActiveSession().shouldBeRight(Unit)
                mocker.verifyWithSuspend { store.updateData(isAny()) }
            }

            listOf(
                Session(anilistToken = null, isSessionActive = false),
                Session(anilistToken = null, isSessionActive = true),
                Session(anilistToken = anilistToken, isSessionActive = false),
                Session(anilistToken = anilistToken, isSessionActive = true),
            ).forEach { session ->
                "checking session active for ${session.anilistToken} and ${session.isSessionActive}" {
                    mocker.every { store.data } returns flowOf(session)

                    source.sessionActive.test(5.seconds) {
                        awaitItem().shouldBeRight((session.anilistToken == null && session.isSessionActive).not())
                        cancelAndIgnoreRemainingEvents()
                    }

                    mocker.verify { store.data }
                }
            }
        }

        "failure" - {
            "the clearing the session fails AND it's a common Exception" {
                mocker.everySuspending { store.updateData(isAny()) } runs { throw Exception() }
                source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                mocker.verifyWithSuspend { called { store.updateData(isAny()) } }
            }

            "the clearing the session fails AND it's a writing Exception" {
                mocker.everySuspending { store.updateData(isAny()) } runs { throw IOException("Oops.") }
                source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                mocker.verifyWithSuspend { called { store.updateData(isAny()) } }
            }

            "it's the deleting token AND it's a common Exception" {
                mocker.everySuspending { store.updateData(isAny()) } runs { throw Exception() }
                source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                mocker.verifyWithSuspend { called { store.updateData(isAny()) } }
            }

            "it's the deleting token AND it's a writing Exception" {
                mocker.everySuspending { store.updateData(isAny()) } runs { throw IOException("Oops.") }
                source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                mocker.verifyWithSuspend { called { store.updateData(isAny()) } }
            }

            "it's the saving token AND it's a common Exception" {
                mocker.everySuspending { store.updateData(isAny()) } runs { throw Exception() }
                source.saveSession(anilistToken).shouldBeLeft(SessionFailure.SavingSession)
                mocker.verifyWithSuspend { called { store.updateData(isAny()) } }
            }

            "it's the saving token AND it's a writing Exception" {
                mocker.everySuspending { store.updateData(isAny()) } runs { throw IOException("Oops.") }
                source.saveSession(anilistToken).shouldBeLeft(SessionFailure.SavingSession)
                mocker.verifyWithSuspend { called { store.updateData(isAny()) } }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
