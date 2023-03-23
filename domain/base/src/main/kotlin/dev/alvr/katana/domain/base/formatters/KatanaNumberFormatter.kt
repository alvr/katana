package dev.alvr.katana.domain.base.formatters

import java.text.DecimalFormat

sealed class KatanaNumberFormatter(pattern: String) {
    object Score : KatanaNumberFormatter(NO_DECIMAL_IF_ZERO_DECIMAL)

    private val formatter = DecimalFormat(pattern)

    operator fun invoke(value: Number): String = formatter.format(value)

    private companion object {
        const val NO_DECIMAL_IF_ZERO_DECIMAL = "0.#"
    }
}
