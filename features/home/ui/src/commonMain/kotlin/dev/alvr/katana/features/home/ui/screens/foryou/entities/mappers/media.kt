package dev.alvr.katana.features.home.ui.screens.foryou.entities.mappers

import dev.alvr.katana.common.media.domain.models.MediaCollection
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.features.home.ui.screens.foryou.entities.HomeMediaItem
import kotlinx.collections.immutable.toImmutableList

internal fun Iterable<CommonMediaEntry>.toHomeMediaItems(type: MediaListType) =
    map { entry -> HomeMediaItem(id = entry.id, title = entry.title, cover = entry.coverImage, type = type) }
        .toImmutableList()

internal fun MediaCollection<MediaEntry>.toHomeMediaItems() =
    lists
        .flatMap { group -> group.entries }
        .map { entry ->
            when (val media = entry.entry) {
                is MediaEntry.Anime ->
                    HomeMediaItem(
                        id = media.id,
                        title = media.title,
                        cover = media.coverImage,
                        type = MediaListType.Anime,
                        progress = entry.list.progress,
                        total = media.episodes,
                    )
                is MediaEntry.Manga ->
                    HomeMediaItem(
                        id = media.id,
                        title = media.title,
                        cover = media.coverImage,
                        type = MediaListType.Manga,
                        progress = entry.list.progress,
                        total = media.chapters,
                    )
            }
        }
        .toImmutableList()
