package dev.alvr.katana.common.core.formatters

import platform.Foundation.NSLocale

internal actual fun currentLanguageCode(): String = NSLocale().languageCode
