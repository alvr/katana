package dev.alvr.katana.data.remote.lists.repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaList
import dev.alvr.katana.domain.lists.models.AnimeEntry
import dev.alvr.katana.domain.lists.models.MangaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import dev.alvr.katana.domain.user.managers.UserIdManager
import javax.inject.Inject
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull

internal class ListsRemoteRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val userId: UserIdManager
) : ListsRepository {
    override val animeList = getMediaCollection(MediaType.ANIME).mapNotNull { res ->
        res.data.mediaList<AnimeEntry>()
    }

    override val mangaList = getMediaCollection(MediaType.MANGA).mapNotNull { res ->
        res.data.mediaList<MangaEntry>()
    }

    private fun getMediaCollection(type: MediaType) = flow {
        val response = client
            .query(MediaListCollectionQuery(userId.getId(), type))
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .watch()
        emitAll(response)
    }
}
