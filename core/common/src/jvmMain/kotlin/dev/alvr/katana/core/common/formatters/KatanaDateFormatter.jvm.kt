package dev.alvr.katana.core.common.formatters

import java.util.Locale

internal actual fun currentLanguageCode() = Locale.getDefault().language
