package dev.alvr.katana.domain.session.models

import dev.alvr.katana.common.core.empty
import io.kotest.assertions.throwables.shouldNotThrowExactlyUnit
import io.kotest.assertions.throwables.shouldThrowUnitWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string

internal class AnilistTokenTest : FreeSpec({
    "an empty token should throw an exception" {
        shouldThrowUnitWithMessage<IllegalArgumentException>("Token should not be empty.") {
            AnilistToken(String.empty)
        }
    }

    "a blank token should throw an exception" {
        shouldThrowUnitWithMessage<IllegalArgumentException>("Token should not be empty.") {
            AnilistToken(" ".repeat(5))
        }
    }

    "a token with value should not throw an exception" {
        val token = Arb.string(minSize = 1).next()

        shouldNotThrowExactlyUnit<IllegalStateException> {
            AnilistToken(token).token shouldBe token
        }
    }
})
