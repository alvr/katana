package dev.alvr.katana.ui.lists.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.alvr.katana.ui.base.utils.Locales
import dev.alvr.katana.ui.base.utils.rememberKatanaStrings
import kotlinx.collections.immutable.persistentMapOf

@Immutable
@Suppress("LongParameterList", "UseDataClass")
class ListsStrings internal constructor(
    // Common
    internal val animeToolbar: String = "Anime",
    internal val mangaToolbar: String = "Manga",
    internal val entryProgress: (Int, Any) -> String = { current, total -> "$current/$total" },
    internal val entryNextEpisode: (Int, String) -> String = { number, date -> "Ep. $number: $date" },
    internal val entryNextEpisodeSeparator: String = "›",
    internal val entryPlusOne: (String) -> String = { progress -> "$progress +" },

    // Specific
    internal val animeToolbarSearchPlaceholder: String,
    internal val mangaToolbarSearchPlaceholder: String,

    internal val emptyAnimeList: String,
    internal val emptyMangaList: String,

    internal val errorMessage: String,

    internal val entryFormatTv: String,
    internal val entryFormatTvShort: String,
    internal val entryFormatMovie: String,
    internal val entryFormatSpecial: String,
    internal val entryFormatOva: String,
    internal val entryFormatOna: String,
    internal val entryFormatMusic: String,
    internal val entryFormatManga: String,
    internal val entryFormatNovel: String,
    internal val entryFormatOneShot: String,
    internal val entryFormatUnknown: String,

    internal val changeListButton: String,
)

internal val Strings = persistentMapOf(
    Locales.EN to enListsStrings,
    Locales.ES to esListsStrings,
)

val LocalListsStrings = compositionLocalOf { enListsStrings }

@Composable
fun rememberListsStrings() = rememberKatanaStrings(Strings)
