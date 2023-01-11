package dev.alvr.katana.domain.session.models

import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.tests.TestBase
import io.kotest.assertions.throwables.shouldNotThrowExactlyUnit
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class AnilistTokenTest : TestBase() {
    @Test
    @DisplayName("WHEN the token is empty THEN should throw an exception")
    fun `empty token`() = runTest {
        // THEN
        shouldThrowExactlyUnit<IllegalArgumentException> {

            // GIVEN
            AnilistToken(String.empty)
        } shouldHaveMessage "Token should not be empty."
    }

    @Test
    @DisplayName("WHEN the token is not empty THEN should not throw an exception")
    fun `not empty token`() = runTest {
        // GIVEN
        val token = Arb.string(minSize = 1).next()
        val anilistToken by lazy { AnilistToken(token) }

        // THEN
        shouldNotThrowExactlyUnit<IllegalStateException> {
            anilistToken.token shouldBe token
        }
    }
}
