package dev.alvr.katana.features.home.data.repositories

import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.features.home.data.sources.HomeLocalSource
import dev.alvr.katana.features.home.data.sources.HomeRemoteSource
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class HomeRepositoryImpl(
    private val localSource: HomeLocalSource,
    private val remoteSource: HomeRemoteSource,
) : HomeRepository {
    override val welcomeCardVisible = localSource.welcomeCardVisible

    override fun trendingMedia(type: MediaListType) = remoteSource.trendingMedia(type)

    override fun popularMedia(type: MediaListType) = remoteSource.popularMedia(type)

    override fun upcomingAnime() = remoteSource.upcomingAnime()

    override suspend fun hideWelcomeCard() = localSource.hideWelcomeCard()
}
