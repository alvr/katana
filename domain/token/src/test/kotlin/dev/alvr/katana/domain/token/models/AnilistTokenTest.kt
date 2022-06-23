package dev.alvr.katana.domain.token.models

import io.kotest.assertions.throwables.shouldNotThrowExactlyUnit
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string

internal class AnilistTokenTest : WordSpec({
    "if the token is empty" should {
        "throw an exception" {
            shouldThrowExactly<IllegalArgumentException> {
                AnilistToken("")
            } shouldHaveMessage "Token should not be empty."
        }
    }

    "if the token is not empty" should {
        val token = Arb.string(minSize = 1).next()

        "not throw an exception" {
            shouldNotThrowExactlyUnit<IllegalStateException> {
                val anilistToken = AnilistToken(token)
                anilistToken.token shouldBe token
            }
        }
    }
},)
