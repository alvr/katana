package dev.alvr.katana.common.user.data.mappers.responses

import dev.alvr.katana.common.user.data.UserIdQuery
import dev.alvr.katana.core.tests.random
import io.kotest.assertions.throwables.shouldNotThrowExactlyUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeExactly

internal class UserIdMapperTest :
    FreeSpec({
        "an UserIdQuery with a viewer should have same id" {
            shouldNotThrowExactlyUnit<IllegalStateException> {
                UserIdQuery.Data(viewer = UserIdQuery.Viewer(__typename = String.random, id = 37_384))
                    .invoke()
                    .id shouldBeExactly 37_384
            }
        }
    })
