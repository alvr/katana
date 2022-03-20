package dev.alvr.katana.domain.lists.repositories

import dev.alvr.katana.domain.lists.models.AnimeEntry
import dev.alvr.katana.domain.lists.models.MangaEntry
import dev.alvr.katana.domain.lists.models.MediaCollection
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    val animeList: Flow<List<MediaCollection<AnimeEntry>>>
    val mangaList: Flow<List<MediaCollection<MangaEntry>>>
}
