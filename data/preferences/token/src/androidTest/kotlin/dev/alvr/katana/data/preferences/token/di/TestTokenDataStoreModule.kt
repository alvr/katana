package dev.alvr.katana.data.preferences.token.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.alvr.katana.data.preferences.base.encoded
import dev.alvr.katana.data.preferences.token.models.Token
import dev.alvr.katana.data.preferences.token.serializers.TokenSerializer
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.serialization.ExperimentalSerializationApi

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TokenDataStoreModule::class],
)
@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
internal object TestTokenDataStoreModule {

    private const val DATASTORE_FILE = "test_token.pb"

    @Provides
    @Singleton
    fun provideScope() = TestScope()

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
        scope: TestScope
    ): DataStore<Token> = DataStoreFactory.create(
        produceFile = { context.dataStoreFile(DATASTORE_FILE) },
        scope = scope,
        serializer = TokenSerializer.encoded(),
    )
}
