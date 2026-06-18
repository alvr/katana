package dev.alvr.katana.common.session.data.di

import dev.alvr.katana.core.common.KatanaStorage
import dev.alvr.katana.core.common.di.AppContext
import dev.alvr.katana.core.common.di.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import eu.anifantakis.lib.ksafe.KSafe

@BindingContainer
@ContributesTo(AppScope::class)
actual object SessionPreferencesContainer {

    @Provides
    @SessionPreferences
    @SingleIn(AppScope::class)
    actual fun sessionPreferences(@AppContext context: PlatformContext, storage: KatanaStorage): KSafe =
        KSafe(context = context, fileName = SafeFileName, baseDir = storage.files.toFile())
}
