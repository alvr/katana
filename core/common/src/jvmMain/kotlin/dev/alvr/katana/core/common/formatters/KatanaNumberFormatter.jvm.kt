package dev.alvr.katana.core.common.formatters

import java.text.DecimalFormat

internal actual fun Number.format(pattern: String) = DecimalFormat(pattern).format(this)
