package dev.alvr.katana.features.account.ui.entities.mappers

import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.features.account.ui.entities.UserInfoUi
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string

internal class UserInfoMapper : ShouldSpec({
    context("map with specific values") {
        should("return the expected value") {
            val result = UserInfo(
                username = "username",
                avatar = "avatar",
                banner = "banner",
            ).toEntity()

            result shouldBe UserInfoUi(
                username = "username",
                avatar = "avatar",
                banner = "banner",
            )
        }
    }

    context("map with random values") {
        should("return the expected value") {
            val model = arbitrary {
                UserInfo(
                    username = Arb.string().next(),
                    avatar = Arb.string().next(),
                    banner = Arb.string().next(),
                )
            }.next()

            val result = model.toEntity()

            result shouldBe UserInfoUi(
                username = model.username,
                avatar = model.avatar,
                banner = model.banner,
            )
        }
    }
})
