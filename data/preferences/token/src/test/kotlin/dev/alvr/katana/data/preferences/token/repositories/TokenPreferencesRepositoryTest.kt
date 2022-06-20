package dev.alvr.katana.data.preferences.token.repositories

import androidx.datastore.core.DataStore
import dev.alvr.katana.data.preferences.token.models.Token
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.token.failures.TokenPreferencesFailure
import dev.alvr.katana.domain.token.models.AnilistToken
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.flowOf

internal class TokenPreferencesRepositoryTest : BehaviorSpec({
    val savedToken = "saved-token"

    given("a repository") {
        val store = mockk<DataStore<Token>>()
        val repository = TokenPreferencesRepositoryImpl(store)

        `when`("getting a token from datastore for the first time") {
            coEvery { store.data } returns flowOf(Token(null))
            val token = repository.getAnilistToken()

            then("the token object should be None") {
                token.shouldBeNone()
                coVerify(exactly = 1) { store.data }
            }
        }

        `when`("saving a token") {
            coJustRun { store.updateData(any()) }
            repository.saveAnilistToken(AnilistToken(savedToken))

            then("the token should updated in the store") {
                coVerify(exactly = 1) { store.updateData(any()) }
            }

            and("getting the saved token") {
                coEvery { store.data } returns flowOf(Token(savedToken))
                val token = repository.getAnilistToken()

                then("the token should be read from memory") {
                    token.shouldBeSome(AnilistToken(savedToken))
                    coVerify(exactly = 1) { store.data }
                }
            }

            and("deleting the saved token") {
                coEvery { store.data } returns flowOf(Token(null))
                coJustRun { store.updateData(any()) }
                repository.deleteAnilistToken().shouldBeRight()

                then("the token should be null") {
                    repository.getAnilistToken().shouldBeNone()
                }
            }
        }

        `when`("something fails") {
            and("it's the deleting token") {
                and("it's a common Exception") {
                    val update = mockk<(Token) -> Token>()
                    every { update(any()) } throws Exception()
                    coJustRun { store.updateData(update) }

                    then("should be a left of Failure.Unknown") {
                        repository.deleteAnilistToken().shouldBeLeft(Failure.Unknown)
                    }
                }
                and("it's a writing Exception") {
                    coEvery { store.updateData(any()) } throws IOException()

                    then("should be a left of PreferencesTokenFailure.DeletingFailure") {
                        repository.deleteAnilistToken().shouldBeLeft(TokenPreferencesFailure.DeletingFailure)
                    }
                }
            }

            and("it's the saving token") {
                val token = Arb.bind<AnilistToken>().next()

                and("it's a common Exception") {
                    val update = mockk<(Token) -> Token>()
                    every { update(any()) } throws Exception()
                    coJustRun { store.updateData(update) }

                    then("should be a left of Failure.Unknown") {
                        repository.saveAnilistToken(token).shouldBeLeft(Failure.Unknown)
                    }
                }
                and("it's a writing Exception") {
                    coEvery { store.updateData(any()) } throws IOException()

                    then("should be a left of PreferencesTokenFailure.SavingFailure") {
                        repository.saveAnilistToken(token).shouldBeLeft(TokenPreferencesFailure.SavingFailure)
                    }
                }
            }
        }
    }
},)
