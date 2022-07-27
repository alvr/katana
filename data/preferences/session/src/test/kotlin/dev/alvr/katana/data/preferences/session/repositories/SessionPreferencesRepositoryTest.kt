package dev.alvr.katana.data.preferences.session.repositories

import androidx.datastore.core.DataStore
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.failures.SessionPreferencesFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

internal class SessionPreferencesRepositoryTest : BehaviorSpec({
    val savedToken = "saved-token"

    given("a repository") {
        val store = mockk<DataStore<Session>>()
        val repository = SessionPreferencesRepositoryImpl(store)

        `when`("getting a token from datastore for the first time") {
            coEvery { store.data } returns flowOf(Session(anilistToken = null))
            val token = repository.getAnilistToken()

            then("the token object should be None") {
                token.shouldBeNone()
                coVerify(exactly = 1) { store.data }
            }
        }

        `when`("saving a session") {
            coJustRun { store.updateData(any()) }
            repository.saveSession(AnilistToken(savedToken))

            then("the token should updated in the store") {
                coVerify(exactly = 1) { store.updateData(any()) }
            }
        }

        `when`("getting the saved token") {
            coEvery { store.data } returns flowOf(
                Session(anilistToken = savedToken, isSessionActive = true),
            )
            val token = repository.getAnilistToken()

            then("the token should be read from memory") {
                token.shouldBeSome(AnilistToken(savedToken))
                coVerify(exactly = 1) { store.data }
            }
        }

        `when`("deleting the saved token") {
            coEvery { store.data } returns flowOf(
                Session(anilistToken = null, isSessionActive = true),
            )
            coJustRun { store.updateData(any()) }
            repository.deleteAnilistToken().shouldBeRight()

            then("the token should be none") {
                repository.getAnilistToken().shouldBeNone()
            }

            then("the session should not be valid") {
                repository.isSessionActive().first().shouldBeFalse()
            }
        }

        `when`("clearing the session") {
            coEvery { store.data } returns flowOf(
                Session(anilistToken = null, isSessionActive = false),
            )
            coJustRun { store.updateData(any()) }
            repository.clearActiveSession().shouldBeRight()

            then("the session should be valid") {
                repository.isSessionActive().first().shouldBeTrue()
            }
        }

        `when`("something fails") {
            and("it's the clearing the session") {
                and("it's a common Exception") {
                    val update = mockk<(Session) -> Session>()
                    every { update(any()) } throws Exception()
                    coJustRun { store.updateData(update) }

                    then("should be a left of Failure.Unknown") {
                        repository.clearActiveSession().shouldBeLeft(Failure.Unknown)
                    }
                }
                and("it's a writing Exception") {
                    coEvery { store.updateData(any()) } throws IOException()

                    then("should be a left of PreferencesTokenFailure.ClearingSessionFailure") {
                        repository.clearActiveSession().shouldBeLeft(SessionPreferencesFailure.ClearingSessionFailure)
                    }
                }
            }

            and("it's the deleting token") {
                and("it's a common Exception") {
                    val update = mockk<(Session) -> Session>()
                    every { update(any()) } throws Exception()
                    coJustRun { store.updateData(update) }

                    then("should be a left of Failure.Unknown") {
                        repository.deleteAnilistToken().shouldBeLeft(Failure.Unknown)
                    }
                }
                and("it's a writing Exception") {
                    coEvery { store.updateData(any()) } throws IOException()

                    then("should be a left of PreferencesTokenFailure.DeletingFailure") {
                        repository.deleteAnilistToken().shouldBeLeft(SessionPreferencesFailure.DeletingTokenFailure)
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
                        repository.saveSession(token).shouldBeLeft(Failure.Unknown)
                    }
                }
                and("it's a writing Exception") {
                    coEvery { store.updateData(any()) } throws IOException()

                    then("should be a left of PreferencesTokenFailure.SavingFailure") {
                        repository.saveSession(token).shouldBeLeft(SessionPreferencesFailure.SavingFailure)
                    }
                }
            }
        }
    }
},)
