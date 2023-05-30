package dev.alvr.katana.common.core.formatters

import java.text.DecimalFormat

internal actual fun Number.format(pattern: String) = DecimalFormat(pattern).format(this)
