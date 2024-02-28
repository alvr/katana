package dev.alvr.katana.data.remote.user

import dev.alvr.katana.domain.user.models.UserId
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt

internal val userIdMock = UserId(Arb.positiveInt().next())
