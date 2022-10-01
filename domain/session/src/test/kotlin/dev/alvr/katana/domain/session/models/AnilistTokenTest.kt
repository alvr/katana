package dev.alvr.katana.domain.session.models

import dev.alvr.katana.common.core.empty
import io.kotest.assertions.throwables.shouldNotThrowExactlyUnit
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string

internal class AnilistTokenTest : WordSpec() {
    init {
        "if the token is empty" should {
            "throw an exception" {
                shouldThrowExactly<IllegalArgumentException> {
                    AnilistToken(String.empty)
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
    }
}
