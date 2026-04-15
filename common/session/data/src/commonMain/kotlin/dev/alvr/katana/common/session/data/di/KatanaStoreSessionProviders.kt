package dev.alvr.katana.common.session.data.di

import dev.alvr.katana.common.session.data.entities.Session
import dev.alvr.katana.core.common.KatanaFilesPath
import dev.alvr.katana.core.preferences.di.store.KatanaStore
import dev.alvr.katana.core.preferences.di.store.katanaStoreOf
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface KatanaStoreSessionProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun katanaStoreSession(path: KatanaFilesPath): KatanaStore<Session> =
        katanaStoreOf(path = path, name = "session", default = Session())
}
