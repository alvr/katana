package dev.alvr.katana.data.remote.user.mappers.responses

import dev.alvr.katana.data.remote.user.UserIdQuery
import io.kotest.assertions.throwables.shouldNotThrowExactly
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.throwable.shouldHaveMessage

internal class UserIdMapperTest : WordSpec() {
    init {
        "a null UserIdQuery.Data" should {
            val userId: UserIdQuery.Data? = null

            "throw a IllegalStateException" {
                shouldThrowExactly<IllegalStateException> { userId() } shouldHaveMessage "ViewerId is required."
            }
        }

        "a UserIdQuery with null viewer" should {
            val userId = UserIdQuery.Data(viewer = null)

            "throw a IllegalStateException" {
                shouldThrowExactly<IllegalStateException> { userId() } shouldHaveMessage "ViewerId is required."
            }
        }

        "a UserIdQuery with a viewer" should {
            val userId = UserIdQuery.Data(viewer = UserIdQuery.Viewer(37_384))

            "have the same Id returned" {
                userId().id shouldBeExactly 37_384
            }

            "not throw a IllegalStateException" {
                shouldNotThrowExactly<IllegalStateException> { userId() }
            }
        }
    }
}
