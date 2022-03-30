package dev.alvr.katana.domain.lists.repositories

import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.AnimeEntry
import dev.alvr.katana.domain.lists.models.entries.MangaEntry
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    val animeList: Flow<MediaCollection<AnimeEntry>>
    val mangaList: Flow<MediaCollection<MangaEntry>>
}
