package dev.alvr.katana.features.home.data.repositories

import dev.alvr.katana.features.home.data.sources.HomeLocalSource
import dev.alvr.katana.features.home.data.sources.HomeRemoteSource
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Suppress("UnusedPrivateProperty")
internal class HomeRepositoryImpl(
    private val localSource: HomeLocalSource,
    @Suppress("Unused") private val remoteSource: HomeRemoteSource,
) : HomeRepository {
    override val welcomeCardVisible = localSource.welcomeCardVisible

    override suspend fun hideWelcomeCard() = localSource.hideWelcomeCard()
}
