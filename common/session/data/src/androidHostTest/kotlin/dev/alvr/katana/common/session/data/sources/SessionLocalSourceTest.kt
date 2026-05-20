package dev.alvr.katana.common.session.data.sources

import app.cash.turbine.test
import dev.alvr.katana.common.session.data.di.createSessionLocalSourceTestGraph
import dev.alvr.katana.common.session.data.mocks.anilistTokenMock
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.core.preferences.utils.flow
import dev.alvr.katana.core.preferences.utils.get
import dev.alvr.katana.core.preferences.utils.set
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeNone
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.core.tests.shouldBeSome
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.io.IOException

@Suppress("UnusedFlow")
internal class SessionLocalSourceTest : FreeSpec() {
    private val safe = mockk<KSafe>()

    private lateinit var source: SessionLocalSource

    init {
        "successful" -
            {
                "getting a token from datastore for the first time" {
                    every { safe[AnilistTokenPrefKey, anyNullable()] } returns null
                    source.getAnilistToken().shouldBeNone()
                    verify { safe.getDirect<String?>(AnilistTokenPrefKey.toString(), null) }
                }

                "saving a session" {
                    justRun { safe[AnilistTokenPrefKey] = anyNullable() }
                    justRun { safe[SessionActivePrefKey] = any() }

                    source.saveSession(anilistTokenMock).shouldBeRight(Unit)

                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), anilistTokenMock.token) }
                    verify { safe.putDirect<Boolean>(SessionActivePrefKey.toString(), true) }
                }

                "getting the saved token" {
                    every { safe[AnilistTokenPrefKey, anyNullable()] } returns anilistTokenMock.token
                    source.getAnilistToken() shouldBeSome anilistTokenMock
                    verify { safe.getDirect<String?>(AnilistTokenPrefKey.toString(), null) }
                }

                "deleting the saved token" {
                    justRun { safe[AnilistTokenPrefKey] = anyNullable() }
                    source.deleteAnilistToken().shouldBeRight(Unit)
                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), null) }
                }

                "clearing the session" {
                    justRun { safe[SessionActivePrefKey] = any() }
                    source.clearActiveSession().shouldBeRight(Unit)
                    verify { safe.putDirect<Boolean>(SessionActivePrefKey.toString(), false) }
                }

                "logging out" {
                    justRun { safe[AnilistTokenPrefKey] = anyNullable() }
                    justRun { safe[SessionActivePrefKey] = any() }

                    source.logout().shouldBeRight(Unit)

                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), null) }
                    verify { safe.putDirect<Boolean>(SessionActivePrefKey.toString(), false) }
                }

                listOf(
                        Triple(null as String?, false, false),
                        Triple(null as String?, true, false),
                        Triple(anilistTokenMock.token, false, false),
                        Triple(anilistTokenMock.token, true, true),
                    )
                    .forEach { (token, active, expected) ->
                        "checking session active for token=$token and active=$active" {
                            every { safe.flow(AnilistTokenPrefKey, anyNullable()) } returns flowOf(token)
                            every { safe.flow(SessionActivePrefKey, any()) } returns flowOf(active)
                            source = createSessionLocalSourceTestGraph(safe).sessionLocalSource

                            source.sessionActive.test {
                                awaitItem().shouldBeRight(expected)
                                awaitComplete()
                            }

                            verify { safe.getFlow<String?>(AnilistTokenPrefKey.toString(), null as String?) }
                            verify { safe.getFlow<Boolean>(SessionActivePrefKey.toString(), false) }
                        }
                    }
            }

        "failure" -
            {
                "observing the session fails AND it's a common Exception" {
                    every { safe.flow(AnilistTokenPrefKey, anyNullable()) } returns flowOf(null)
                    every { safe.flow(SessionActivePrefKey, any()) } returns flow { throw IllegalStateException() }
                    source = createSessionLocalSourceTestGraph(safe).sessionLocalSource

                    source.sessionActive.test {
                        awaitItem().shouldBeLeft(SessionFailure.CheckingActiveSession)
                        awaitComplete()
                    }

                    verify { safe.getFlow<String?>(AnilistTokenPrefKey.toString(), null) }
                    verify { safe.getFlow<Boolean>(SessionActivePrefKey.toString(), false) }
                }

                "observing the session fails AND it's a reading Exception" {
                    every { safe.flow(AnilistTokenPrefKey, anyNullable()) } returns flowOf(null)
                    every { safe.flow(SessionActivePrefKey, any()) } returns flow { throw IOException("Oops.") }
                    source = createSessionLocalSourceTestGraph(safe).sessionLocalSource

                    source.sessionActive.test {
                        awaitItem().shouldBeLeft(SessionFailure.CheckingActiveSession)
                        awaitComplete()
                    }

                    verify { safe.getFlow<String?>(AnilistTokenPrefKey.toString(), null) }
                    verify { safe.getFlow<Boolean>(SessionActivePrefKey.toString(), false) }
                }

                "the clearing the session fails AND it's a common Exception" {
                    every { safe[SessionActivePrefKey] = any() } throws Exception()
                    source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                    verify { safe.putDirect<Boolean>(SessionActivePrefKey.toString(), false) }
                }

                "the clearing the session fails AND it's a writing Exception" {
                    every { safe[SessionActivePrefKey] = any() } throws IOException("Oops.")
                    source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                    verify { safe.putDirect<Boolean>(SessionActivePrefKey.toString(), false) }
                }

                "it's the deleting token AND it's a common Exception" {
                    every { safe[AnilistTokenPrefKey] = anyNullable() } throws Exception()
                    source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), null) }
                }

                "it's the deleting token AND it's a writing Exception" {
                    every { safe[AnilistTokenPrefKey] = anyNullable() } throws IOException("Oops.")
                    source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), null) }
                }

                "it's the saving token AND it's a common Exception" {
                    every { safe[AnilistTokenPrefKey] = anilistTokenMock.token } throws Exception()
                    source.saveSession(anilistTokenMock).shouldBeLeft(SessionFailure.SavingSession)
                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), anilistTokenMock.token) }
                }

                "it's the saving token AND it's a writing Exception" {
                    every { safe[AnilistTokenPrefKey] = anilistTokenMock.token } throws IOException("Oops.")
                    source.saveSession(anilistTokenMock).shouldBeLeft(SessionFailure.SavingSession)
                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), anilistTokenMock.token) }
                }

                "it's logging out AND it's a common Exception" {
                    every { safe[AnilistTokenPrefKey] = anyNullable() } throws Exception()
                    source.logout().shouldBeLeft(SessionFailure.LoggingOut)
                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), null) }
                }

                "it's logging out AND it's a writing Exception" {
                    every { safe[AnilistTokenPrefKey] = anyNullable() } throws IOException("Oops.")
                    source.logout().shouldBeLeft(SessionFailure.LoggingOut)
                    verify { safe.putDirect<String?>(AnilistTokenPrefKey.toString(), null) }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        every { safe.flow(AnilistTokenPrefKey, anyNullable()) } returns flowOf(null)
        every { safe.flow(SessionActivePrefKey, any()) } returns flowOf(false)
        source = createSessionLocalSourceTestGraph(safe).sessionLocalSource
    }
}
