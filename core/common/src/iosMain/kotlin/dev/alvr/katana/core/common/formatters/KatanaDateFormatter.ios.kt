package dev.alvr.katana.core.common.formatters

import platform.Foundation.NSLocale
import platform.Foundation.languageCode

internal actual fun currentLanguageCode(): String = NSLocale().languageCode
