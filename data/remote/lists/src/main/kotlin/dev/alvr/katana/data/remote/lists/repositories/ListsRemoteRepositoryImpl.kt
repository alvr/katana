package dev.alvr.katana.data.remote.lists.repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import dev.alvr.katana.data.remote.base.extensions.optional
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaList
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import dev.alvr.katana.domain.user.managers.UserIdManager
import javax.inject.Inject
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class ListsRemoteRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val userId: UserIdManager,
) : ListsRepository {
    override val animeList = getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
    override val mangaList = getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)

    private inline fun <reified T : MediaEntry> getMediaCollection(type: MediaType) = flow {
        val response = client
            .query(MediaListCollectionQuery(userId.getId().optional(), type))
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .watch()
            .distinctUntilChanged()
            .map { res -> MediaCollection(res.data?.mediaList<T>().orEmpty()) }
            .distinctUntilChanged()
        emitAll(response)
    }
}
