package dev.alvr.katana.data.preferences.session.datastore

import androidx.datastore.core.DataStore
import dagger.hilt.android.testing.HiltAndroidTest
import dev.alvr.katana.common.tests.HiltTest
import dev.alvr.katana.common.tests.RobolectricKeyStore
import dev.alvr.katana.data.preferences.session.di.TestSessionDataStoreModule.CORRUPTED_DATASTORE
import dev.alvr.katana.data.preferences.session.di.TestSessionDataStoreModule.DATASTORE
import dev.alvr.katana.data.preferences.session.models.Session
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.BeforeClass
import org.junit.Test

@HiltAndroidTest
@ExperimentalCoroutinesApi
internal class SessionDataStoreTest : HiltTest() {
    @Inject
    @Named(DATASTORE)
    internal lateinit var dataStores: Array<DataStore<Session>>

    @Inject
    @Named(CORRUPTED_DATASTORE)
    internal lateinit var corruptedDataStores: Array<DataStore<Session>>

    @Test
    fun `initial session should equal to the Session class`() = runTest {
        dataStores.forEach { dataStore ->
            dataStore.data.first() shouldBeEqualToComparingFields Session()
        }
    }

    @Test
    fun `saving a session should return the same values`() = runTest {
        dataStores.forEach { dataStore ->
            with(dataStore) {
                updateData { p -> p.copy(anilistToken = "token", isSessionActive = true) }

                data.first() shouldBeEqualToComparingFields Session(
                    anilistToken = "token",
                    isSessionActive = true,
                )
            }
        }
    }

    @Test
    fun `corrupted dataStore should recreate again the file with initial values`() = runTest {
        corruptedDataStores.forEach { corruptedDataStore ->
            corruptedDataStore.data.first() shouldBeEqualToComparingFields Session(anilistToken = "recreated")
        }
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun setUpClass() {
            RobolectricKeyStore.setup
        }
    }
}
