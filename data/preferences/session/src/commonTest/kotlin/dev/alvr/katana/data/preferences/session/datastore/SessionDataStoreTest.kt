package dev.alvr.katana.data.preferences.session.datastore

import androidx.datastore.core.DataStore
import dev.alvr.katana.common.tests.KoinTest4
import dev.alvr.katana.data.preferences.session.di.corruptedDataStoreNamed
import dev.alvr.katana.data.preferences.session.di.dataStoreModule
import dev.alvr.katana.data.preferences.session.di.dataStoreNamed
import dev.alvr.katana.data.preferences.session.models.Session
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Test
import org.koin.core.KoinApplication
import org.koin.test.inject

@ExperimentalCoroutinesApi
internal class SessionDataStoreTest : KoinTest4() {
    private val dataStores by inject<Array<DataStore<Session>>>(dataStoreNamed)
    private val corruptedDataStores by inject<Array<DataStore<Session>>>(corruptedDataStoreNamed)

    override fun KoinApplication.initKoin() {
        modules(dataStoreModule)
    }

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
}
