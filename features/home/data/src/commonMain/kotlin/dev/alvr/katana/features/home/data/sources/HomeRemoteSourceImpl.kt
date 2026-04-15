package dev.alvr.katana.features.home.data.sources

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class) @ContributesBinding(AppScope::class) internal class HomeRemoteSourceImpl : HomeRemoteSource
