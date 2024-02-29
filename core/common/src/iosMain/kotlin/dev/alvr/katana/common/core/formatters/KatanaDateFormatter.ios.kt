package dev.alvr.katana.common.core.formatters

import platform.Foundation.NSLocale
import platform.Foundation.languageCode

internal actual fun currentLanguageCode(): String = NSLocale().languageCode
