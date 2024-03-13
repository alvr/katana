package dev.alvr.katana.common.user.data.di

import dev.alvr.katana.common.user.data.managers.UserIdManagerImpl
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.core.tests.di.coreTestsModule
import org.koin.dsl.module

private val managersModule = module {
    single<UserIdManager> { (repo: UserRepository) ->
        UserIdManagerImpl(GetUserIdUseCase(get(), repo))
    }
}

internal val fakeCommonUserDataModule = module {
    includes(coreTestsModule, managersModule)
}
