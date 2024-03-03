package dev.alvr.katana.core.common.formatters

import dev.alvr.katana.core.common.noData
import korlibs.time.Date
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.KlockLocale
import korlibs.time.locale.ChineseKlockLocale
import korlibs.time.locale.DutchKlockLocale
import korlibs.time.locale.FrenchKlockLocale
import korlibs.time.locale.GermanKlockLocale
import korlibs.time.locale.ItalianKlockLocale
import korlibs.time.locale.JapaneseKlockLocale
import korlibs.time.locale.KoreanKlockLocale
import korlibs.time.locale.NorwegianKlockLocale
import korlibs.time.locale.PortugueseKlockLocale
import korlibs.time.locale.RussianKlockLocale
import korlibs.time.locale.SpanishKlockLocale
import korlibs.time.locale.SwedishKlockLocale
import korlibs.time.locale.TurkishKlockLocale
import korlibs.time.locale.UkrainianKlockLocale
import korlibs.time.locale.chinese
import korlibs.time.locale.dutch
import korlibs.time.locale.french
import korlibs.time.locale.german
import korlibs.time.locale.italian
import korlibs.time.locale.japanese
import korlibs.time.locale.korean
import korlibs.time.locale.norwegian
import korlibs.time.locale.portuguese
import korlibs.time.locale.russian
import korlibs.time.locale.spanish
import korlibs.time.locale.swedish
import korlibs.time.locale.turkish
import korlibs.time.locale.ukrainian
import kotlin.jvm.JvmInline

@JvmInline
value class KatanaDateFormatter private constructor(private val format: String) {
    operator fun invoke(date: Date?) = date?.format(format) ?: String.noData
    operator fun invoke(datetime: DateTime?) = datetime?.format(format) ?: String.noData
    operator fun invoke(datetime: DateTimeTz?) = datetime?.format(format) ?: String.noData

    companion object {
        val DateWithTime
            get() = with(klockLocale()) {
                KatanaDateFormatter("$formatDateMedium - $formatTimeShort")
            }
    }
}

private val locales = mapOf(
    ChineseKlockLocale.ISO639_1 to KlockLocale.chinese,
    DutchKlockLocale.ISO639_1 to KlockLocale.dutch,
    FrenchKlockLocale.ISO639_1 to KlockLocale.french,
    GermanKlockLocale.ISO639_1 to KlockLocale.german,
    ItalianKlockLocale.ISO639_1 to KlockLocale.italian,
    JapaneseKlockLocale.ISO639_1 to KlockLocale.japanese,
    KoreanKlockLocale.ISO639_1 to KlockLocale.korean,
    NorwegianKlockLocale.ISO639_1 to KlockLocale.norwegian,
    PortugueseKlockLocale.ISO639_1 to KlockLocale.portuguese,
    RussianKlockLocale.ISO639_1 to KlockLocale.russian,
    SpanishKlockLocale.ISO639_1 to KlockLocale.spanish,
    SwedishKlockLocale.ISO639_1 to KlockLocale.swedish,
    TurkishKlockLocale.ISO639_1 to KlockLocale.turkish,
    UkrainianKlockLocale.ISO639_1 to KlockLocale.ukrainian,
)

internal expect fun currentLanguageCode(): String

private fun klockLocale() = locales.getOrElse(currentLanguageCode()) { KlockLocale.english }
