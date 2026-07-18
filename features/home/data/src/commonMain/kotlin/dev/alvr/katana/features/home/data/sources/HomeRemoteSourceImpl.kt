package dev.alvr.katana.features.home.data.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.remote.toFailure
import dev.alvr.katana.core.remote.watchFiltered
import dev.alvr.katana.features.home.data.PopularMediaQuery
import dev.alvr.katana.features.home.data.TrendingMediaQuery
import dev.alvr.katana.features.home.data.UpcomingAnimeQuery
import dev.alvr.katana.features.home.data.mappers.requests.toRemote
import dev.alvr.katana.features.home.data.mappers.responses.toMediaEntry
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class HomeRemoteSourceImpl(private val client: ApolloClient) : HomeRemoteSource {
    override fun trendingMedia(type: MediaListType) =
        client
            .query(TrendingMediaQuery(type.toRemote()))
            .watchFiltered()
            .distinctUntilChanged { old, new -> old.data == new.data }
            .map { res ->
                res.dataAssertNoErrors.page
                    ?.media
                    .orEmpty()
                    .mapNotNull { media -> media?.homeMediaEntry?.toMediaEntry() }
                    .right()
            }
            .catchFailure(HomeFailure.GettingTrendingMedia)

    override fun popularMedia(type: MediaListType) =
        client
            .query(PopularMediaQuery(type.toRemote()))
            .watchFiltered()
            .distinctUntilChanged { old, new -> old.data == new.data }
            .map { res ->
                res.dataAssertNoErrors.page
                    ?.media
                    .orEmpty()
                    .mapNotNull { media -> media?.homeMediaEntry?.toMediaEntry() }
                    .right()
            }
            .catchFailure(HomeFailure.GettingPopularMedia)

    override fun upcomingAnime() =
        client
            .query(UpcomingAnimeQuery(Clock.System.now().epochSeconds.toInt()))
            .watchFiltered()
            .distinctUntilChanged { old, new -> old.data == new.data }
            .map { res ->
                res.dataAssertNoErrors.page
                    ?.airingSchedules
                    .orEmpty()
                    .mapNotNull { schedule -> schedule?.media?.homeMediaEntry?.toMediaEntry() }
                    .right()
            }
            .catchFailure(HomeFailure.GettingUpcomingMedia)

    private fun Flow<Either<Failure, List<CommonMediaEntry>>>.catchFailure(failure: HomeFailure) = catch { error ->
        Logger.e(error) { "There was an error collecting home media" }
        emit(error.toFailure(network = failure, response = failure).left())
    }
}
