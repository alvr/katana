package dev.alvr.katana.core.preferences.di

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dev.alvr.katana.core.preferences.encrypt.AndroidPreferencesEncrypt
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val aeadModule = module {
    single<Aead> {
        AeadConfig.register()

        AndroidKeysetManager.Builder()
            .withSharedPref(androidApplication(), KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get(TEMPLATE_NAME))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }
}

private val securerModule = module {
    factoryOf(::AndroidPreferencesEncrypt) bind PreferencesEncrypt::class
}

internal actual fun encryptionModule() = module {
    includes(aeadModule, securerModule)
}

private const val KEYSET_NAME = "master_keyset"
private const val PREFERENCE_FILE = "master_key_preference"
private const val MASTER_KEY_URI = "android-keystore://master_key"
private const val TEMPLATE_NAME = "AES256_GCM"
