package dev.alvr.katana.data.preferences.session.mocks

import dev.alvr.katana.data.preferences.session.models.Session
import dev.alvr.katana.domain.session.models.AnilistToken
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.next

internal val anilistTokenMock = AnilistToken("TOKEN")

internal val sessionMock = Session(
    anilistToken = anilistTokenMock,
    isSessionActive = Arb.boolean().next(),
)
