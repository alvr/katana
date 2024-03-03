package dev.alvr.katana.common.user.domain

import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.common.user.domain.models.UserInfo
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string

internal val userIdMock = UserId(Arb.int().next())

internal val userInfoMock = UserInfo(
    username = Arb.string().next(),
    avatar = Arb.string().next(),
    banner = Arb.string().next(),
)
