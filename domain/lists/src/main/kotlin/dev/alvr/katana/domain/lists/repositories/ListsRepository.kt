package dev.alvr.katana.domain.lists.repositories

import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    val animeList: Flow<MediaCollection<MediaEntry.Anime>>
    val mangaList: Flow<MediaCollection<MediaEntry.Manga>>
}
