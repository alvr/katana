package dev.alvr.katana.common.tests

import java.io.InputStream
import java.io.OutputStream
import java.security.Key
import java.security.KeyStore
import java.security.KeyStoreSpi
import java.security.Provider
import java.security.SecureRandom
import java.security.Security
import java.security.cert.Certificate
import java.security.spec.AlgorithmParameterSpec
import java.util.Collections
import java.util.Date
import java.util.Enumeration
import javax.crypto.KeyGenerator
import javax.crypto.KeyGeneratorSpi
import javax.crypto.SecretKey

internal object RobolectricKeyStore {
    private const val AES = "AES"
    private const val AKS = "AndroidKeyStore"
    private const val BC = "BC"
    private const val BKS = "BKS"

    val setup by lazy {
        Security.removeProvider(AKS)
        Security.addProvider(AndroidKeyStoreProvider())
    }

    private class AndroidKeyStoreProvider : Provider(AKS, Double.NaN, "Fake $AKS") {
        init {
            put("KeyStore.$AKS", AndroidKeyStore::class.java.name)
            put("KeyGenerator.$AES", AesKeyGenerator::class.java.name)
        }

        class AndroidKeyStore : KeyStoreSpi() {
            override fun engineIsKeyEntry(alias: String?): Boolean = wrapped.isKeyEntry(alias)
            override fun engineIsCertificateEntry(alias: String?): Boolean =
                wrapped.isCertificateEntry(alias)

            override fun engineGetCertificate(alias: String?): Certificate =
                wrapped.getCertificate(alias)

            override fun engineGetCreationDate(alias: String?): Date =
                wrapped.getCreationDate(alias)

            override fun engineDeleteEntry(alias: String?) {
                storedKeys.remove(alias)
            }

            override fun engineSetKeyEntry(
                alias: String?,
                key: Key?,
                password: CharArray?,
                chain: Array<out Certificate>?,
            ) = wrapped.setKeyEntry(alias, key, password, chain)

            override fun engineSetKeyEntry(
                alias: String?,
                key: ByteArray?,
                chain: Array<out Certificate>?,
            ) = wrapped.setKeyEntry(alias, key, chain)

            override fun engineStore(stream: OutputStream?, password: CharArray?) =
                wrapped.store(stream, password)

            override fun engineSize(): Int = wrapped.size()
            override fun engineAliases(): Enumeration<String> =
                Collections.enumeration(storedKeys.keys)

            override fun engineContainsAlias(alias: String?): Boolean =
                storedKeys.containsKey(alias)

            override fun engineLoad(stream: InputStream?, password: CharArray?) =
                wrapped.load(stream, password)

            override fun engineGetCertificateChain(alias: String?): Array<Certificate>? =
                wrapped.getCertificateChain(alias)

            override fun engineSetCertificateEntry(alias: String?, cert: Certificate?) =
                wrapped.setCertificateEntry(alias, cert)

            override fun engineGetCertificateAlias(cert: Certificate?): String? =
                wrapped.getCertificateAlias(cert)

            override fun engineGetKey(alias: String?, password: CharArray?): Key? =
                (storedKeys[alias] as? KeyStore.SecretKeyEntry)?.secretKey

            override fun engineGetEntry(
                p0: String,
                p1: KeyStore.ProtectionParameter?,
            ): KeyStore.Entry? = storedKeys[p0]

            override fun engineSetEntry(
                p0: String,
                p1: KeyStore.Entry,
                p2: KeyStore.ProtectionParameter?,
            ) {
                storedKeys[p0] = p1
            }

            override fun engineLoad(p0: KeyStore.LoadStoreParameter?) = wrapped.load(p0)
            override fun engineStore(p0: KeyStore.LoadStoreParameter?) = wrapped.store(p0)
            override fun engineEntryInstanceOf(p0: String?, p1: Class<out KeyStore.Entry>?) =
                wrapped.entryInstanceOf(p0, p1)

            companion object {
                private val wrapped = KeyStore.getInstance(BKS, BC)
                internal val storedKeys = mutableMapOf<String, KeyStore.Entry>()
            }
        }

        private class AesKeyGenerator : KeyGeneratorSpi() {
            private val wrapped = KeyGenerator.getInstance(AES, BC)
            private var lastSpec: AlgorithmParameterSpec? = null

            override fun engineInit(random: SecureRandom?) = wrapped.init(random)
            override fun engineInit(params: AlgorithmParameterSpec?, random: SecureRandom?) =
                wrapped.init(random).also { lastSpec = params }

            override fun engineInit(keysize: Int, random: SecureRandom?) =
                wrapped.init(keysize, random)

            override fun engineGenerateKey(): SecretKey = wrapped.generateKey().also {
                AndroidKeyStore.storedKeys[lastSpec!!.keystoreAlias] = KeyStore.SecretKeyEntry(it)
            }
        }

        companion object {
            private val AlgorithmParameterSpec.keystoreAlias: String
                get() = this::class.java.getDeclaredMethod("getKeystoreAlias")
                    .invoke(this) as String
        }
    }
}
