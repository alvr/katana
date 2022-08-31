package dev.alvr.katana.data.preferences.base.di

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object PreferencesBaseModule {
    private const val KEYSET_NAME = "master_keyset"
    private const val PREFERENCE_FILE = "master_key_preference"
    private const val MASTER_KEY_URI = "android-keystore://master_key"
    private const val TEMPLATE_NAME = "AES256_GCM"

    @Provides
    @Singleton
    fun provideEncryptionManager(@ApplicationContext context: Context): Aead {
        AeadConfig.register()

        return AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get(TEMPLATE_NAME))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }
}
