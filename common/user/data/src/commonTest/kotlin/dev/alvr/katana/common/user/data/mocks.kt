package dev.alvr.katana.common.user.data

import dev.alvr.katana.common.user.domain.models.UserId
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt

internal val userIdMock = UserId(Arb.positiveInt().next())
