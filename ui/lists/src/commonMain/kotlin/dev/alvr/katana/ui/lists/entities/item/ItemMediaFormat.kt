package dev.alvr.katana.ui.lists.entities.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.alvr.katana.ui.lists.strings.LocalListsStrings

@Immutable
@Parcelize
sealed interface ItemMediaFormat : Parcelable {
    val value
        @Composable get() = with(LocalListsStrings.current) {
            when (this@ItemMediaFormat) {
                Tv -> entryFormatTv
                TvShort -> entryFormatTvShort
                Movie -> entryFormatMovie
                Special -> entryFormatSpecial
                Ova -> entryFormatOva
                Ona -> entryFormatOna
                Music -> entryFormatMusic
                Manga -> entryFormatManga
                Novel -> entryFormatNovel
                OneShot -> entryFormatOneShot
                Unknown -> entryFormatUnknown
            }
        }
}

@Immutable internal data object Tv : ItemMediaFormat
@Immutable internal data object TvShort : ItemMediaFormat
@Immutable internal data object Movie : ItemMediaFormat
@Immutable internal data object Special : ItemMediaFormat
@Immutable internal data object Ova : ItemMediaFormat
@Immutable internal data object Ona : ItemMediaFormat
@Immutable internal data object Music : ItemMediaFormat
@Immutable internal data object Manga : ItemMediaFormat
@Immutable internal data object Novel : ItemMediaFormat
@Immutable internal data object OneShot : ItemMediaFormat
@Immutable internal data object Unknown : ItemMediaFormat
