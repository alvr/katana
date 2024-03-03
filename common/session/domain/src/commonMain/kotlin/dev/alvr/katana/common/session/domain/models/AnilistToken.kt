package dev.alvr.katana.common.session.domain.models

import kotlin.jvm.JvmInline

@JvmInline
value class AnilistToken(val token: String) {
    init {
        require(token.isNotBlank()) { "Token should not be empty." }
    }
}
