package dev.alvr.katana.core.common.formatters

import kotlin.jvm.JvmInline

@JvmInline
value class KatanaNumberFormatter private constructor(private val pattern: String) {
    operator fun invoke(number: Number) = number.format(pattern)

    companion object {
        private const val NO_DECIMAL_IF_ZERO_DECIMAL = "0.#"

        val Score get() = KatanaNumberFormatter(NO_DECIMAL_IF_ZERO_DECIMAL)
    }
}

internal expect fun Number.format(pattern: String): String
