package dev.alvr.katana.common.user.data.sources

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class) @ContributesBinding(AppScope::class) internal class UserLocalSourceImpl : UserLocalSource
