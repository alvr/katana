package dev.alvr.katana.common.user.data.mappers.responses

import dev.alvr.katana.common.user.data.UserIdQuery
import io.kotest.assertions.throwables.shouldNotThrowExactlyUnit
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.throwable.shouldHaveMessage

internal class UserIdMapperTest : FreeSpec({
    "null UserIdQuery Data" {
        shouldThrowExactlyUnit<IllegalStateException> {
            val userId: UserIdQuery.Data? = null
            userId()
        } shouldHaveMessage "ViewerId is required."
    }

    "UserIdQuery with null viewer" {
        shouldThrowExactlyUnit<IllegalStateException> {
            val userId = UserIdQuery.Data(viewer = null)
            userId()
        } shouldHaveMessage "ViewerId is required."
    }

    "an UserIdQuery with a viewer should have same id" {
        shouldNotThrowExactlyUnit<IllegalStateException> {
            UserIdQuery.Data(viewer = UserIdQuery.Viewer(37_384))
                .invoke().id shouldBeExactly 37_384
        }
    }
})
