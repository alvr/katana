package dev.alvr.katana.common.core.formatters

import java.util.Locale

internal actual fun currentLanguageCode() = Locale.getDefault().language
