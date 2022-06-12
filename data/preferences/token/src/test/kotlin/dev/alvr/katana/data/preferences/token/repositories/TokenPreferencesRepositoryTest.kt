package dev.alvr.katana.data.preferences.token.repositories

import androidx.datastore.core.DataStore
import dev.alvr.katana.data.preferences.token.models.Token
import dev.alvr.katana.domain.token.models.AnilistToken
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

internal class TokenPreferencesRepositoryTest : BehaviorSpec({
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
            repository.saveAnilistToken(AnilistToken("saved-token"))

            then("the token should updated in the store") {
                coVerify(exactly = 1) { store.updateData(any()) }
            }

            `when`("getting the saved token") {
                val token = repository.getAnilistToken()

                then("the token should be read from memory") {
                    token.shouldBeSome(AnilistToken("saved-token"))
                    coVerify(exactly = 0) { store.data }
                }
            }

            `when`("deleting the saved token") {
                coJustRun { store.updateData(any()) }
                repository.deleteAnilistToken()

                then("the token should be null") {
                    repository.getAnilistToken().shouldBeNull()
                    coVerify(exactly = 1) { store.updateData(any()) }
                }
            }
        }
    }
},)
