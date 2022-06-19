package dev.alvr.katana.data.preferences.token

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.alvr.katana.domain.token.models.AnilistToken
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class TokenDataStoreTest {
    @get:Rule
    internal val hiltRule = HiltAndroidRule(this)

    @Inject
    internal lateinit var repository: TokenPreferencesRepository

    @Inject
    internal lateinit var scope: TestScope

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_initialToken_shouldBeNone() {
        scope.runTest {
            repository.getAnilistToken().shouldBeNone()
        }
    }

    @Test
    fun test_savingEmptyToken_shouldThrowException() {
        scope.runTest {
            shouldThrowExactly<IllegalArgumentException> {
                repository.saveAnilistToken(AnilistToken(""))
            }
        }
    }

    @Test
    fun test_savingBlankToken_shouldThrowException() {
        scope.runTest {
            shouldThrowExactly<IllegalArgumentException> {
                repository.saveAnilistToken(AnilistToken("    "))
            }
        }
    }

    @Test
    fun test_savingAndGettingToken_shouldReturnTheSameValue() {
        scope.runTest {
            val token = AnilistToken("saved-token")

            repository.saveAnilistToken(token)
            repository.getAnilistToken().asClue { t ->
                t.shouldBeSome().token shouldBeEqualToComparingFields token
            }
        }
    }

    @Test
    fun test_deletingTokenAndGetting_shouldBeNone() {
        scope.runTest {
            repository.deleteAnilistToken()
            repository.getAnilistToken().shouldBeNone()
        }
    }
}
