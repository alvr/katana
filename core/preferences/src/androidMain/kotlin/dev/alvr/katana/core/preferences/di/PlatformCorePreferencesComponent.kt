package dev.alvr.katana.core.preferences.di

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dev.alvr.katana.core.common.di.ApplicationScope
import dev.alvr.katana.core.preferences.encrypt.AndroidPreferencesEncrypt
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import me.tatarka.inject.annotations.Provides

actual sealed interface PlatformCorePreferencesComponent {

    @Provides
    @ApplicationScope
    fun provideAead(context: Context): Aead {
        AeadConfig.register()

        return AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get(TEMPLATE_NAME))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    @Provides
    @ApplicationScope
    fun providePreferencesEncrypt(impl: AndroidPreferencesEncrypt): PreferencesEncrypt = impl
}

private const val KEYSET_NAME = "master_keyset"
private const val PREFERENCE_FILE = "master_key_preference"
private const val MASTER_KEY_URI = "android-keystore://master_key"
private const val TEMPLATE_NAME = "AES256_GCM"
