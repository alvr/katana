package dev.alvr.katana.data.preferences.session.sources

import androidx.datastore.core.DataStore
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

internal class SessionLocalSourceTest : BehaviorSpec() {
    init {
        given("a SessionLocalSource") {
            val store = mockk<DataStore<Session>>()
            val source = SessionLocalSource(store)

            `when`("getting a token from datastore for the first time") {
                every { store.data } returns flowOf(Session(anilistToken = null))
                val token = source.getAnilistToken()

                then("the token should be none") {
                    token.shouldBeNone()
                    verify(exactly = 1) { store.data }
                }
            }

            `when`("saving a session") {
                coJustRun { store.updateData(any()) }
                source.saveSession(AnilistToken(SAVED_TOKEN))

                then("the token should updated in the store") {
                    coVerify(exactly = 1) { store.updateData(any()) }
                }
            }

            `when`("getting the saved token") {
                every { store.data } returns flowOf(
                    Session(anilistToken = SAVED_TOKEN, isSessionActive = true),
                )
                val token = source.getAnilistToken()

                then("the token should be read from memory") {
                    token.shouldBeSome(AnilistToken(SAVED_TOKEN))
                    verify(exactly = 1) { store.data }
                }
            }

            `when`("deleting the saved token") {
                every { store.data } returns flowOf(
                    Session(anilistToken = null, isSessionActive = true),
                )
                coJustRun { store.updateData(any()) }
                source.deleteAnilistToken().shouldBeRight()

                then("the token should be none") {
                    source.getAnilistToken().shouldBeNone()
                }

                then("the session should not be valid") {
                    source.sessionActive.first().shouldBeRight(false)
                }
            }

            `when`("clearing the session") {
                every { store.data } returns flowOf(
                    Session(anilistToken = null, isSessionActive = false),
                )
                coJustRun { store.updateData(any()) }
                source.clearActiveSession().shouldBeRight()

                then("the session should be valid") {
                    source.sessionActive.first().shouldBeRight(true)
                }
            }

            `when`("something fails") {
                and("it's the clearing the session") {
                    and("it's a common Exception") {
                        val update = mockk<(Session) -> Session>()
                        every { update(any()) } throws Exception()
                        coJustRun { store.updateData(update) }

                        then("should be a left of Failure.Unknown") {
                            source.clearActiveSession().shouldBeLeft(Failure.Unknown)
                        }
                    }
                    and("it's a writing Exception") {
                        coEvery { store.updateData(any()) } throws IOException()

                        then("should be a left of PreferencesTokenFailure.ClearingSessionFailure") {
                            source.clearActiveSession().shouldBeLeft(SessionFailure.ClearingSession)
                        }
                    }
                }

                and("it's the deleting token") {
                    and("it's a common Exception") {
                        val update = mockk<(Session) -> Session>()
                        every { update(any()) } throws Exception()
                        coJustRun { store.updateData(update) }

                        then("should be a left of Failure.Unknown") {
                            source.deleteAnilistToken().shouldBeLeft(Failure.Unknown)
                        }
                    }
                    and("it's a writing Exception") {
                        coEvery { store.updateData(any()) } throws IOException()

                        then("should be a left of PreferencesTokenFailure.DeletingFailure") {
                            source.deleteAnilistToken().shouldBeLeft(SessionFailure.DeletingToken)
                        }
                    }
                }

                and("it's the saving token") {
                    val token = valueMockk<AnilistToken>()

                    and("it's a common Exception") {
                        val update = mockk<(Session) -> Session>()
                        every { update(any()) } throws Exception()
                        coJustRun { store.updateData(update) }

                        then("should be a left of Failure.Unknown") {
                            source.saveSession(token).shouldBeLeft(Failure.Unknown)
                        }
                    }
                    and("it's a writing Exception") {
                        coEvery { store.updateData(any()) } throws IOException()

                        then("should be a left of PreferencesTokenFailure.SavingFailure") {
                            source.saveSession(token).shouldBeLeft(SessionFailure.SavingSession)
                        }
                    }
                }
            }
        }
    }

    private companion object {
        const val SAVED_TOKEN = "saved-token"
    }
}
