package dev.alvr.katana.domain.session.models

import kotlin.jvm.JvmInline

@JvmInline
value class AnilistToken(val token: String) {
    init {
        require(token.isNotBlank()) { "Token should not be empty." }
    }
}
