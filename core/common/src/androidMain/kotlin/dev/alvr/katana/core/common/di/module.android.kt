package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.KatanaFilesPath
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal actual fun katanaPathModule() = module {
    single<KatanaCachePath> { KatanaCachePath(androidApplication().cacheDir.toOkioPath()) }
    single<KatanaFilesPath> { KatanaFilesPath(androidApplication().filesDir.toOkioPath()) }
}
