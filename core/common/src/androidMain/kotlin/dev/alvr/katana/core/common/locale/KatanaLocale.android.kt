package dev.alvr.katana.core.common.locale

import java.util.Locale as JavaLocale

actual typealias KatanaPlatformLocale = JavaLocale

@JvmInline
actual value class KatanaLocale private actual constructor(internal actual val locale: KatanaPlatformLocale) {
    actual companion object {
        actual fun default(): KatanaLocale = KatanaLocale(JavaLocale.getDefault())
    }
}
