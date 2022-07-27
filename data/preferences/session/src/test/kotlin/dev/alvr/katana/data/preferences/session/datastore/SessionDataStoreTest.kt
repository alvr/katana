package dev.alvr.katana.data.preferences.session.datastore

import androidx.datastore.core.DataStore
import dagger.hilt.android.testing.HiltAndroidTest
import dev.alvr.katana.common.tests.HiltTest
import dev.alvr.katana.data.preferences.session.models.Session
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Test

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionDataStoreTest : HiltTest() {

    @Inject
    internal lateinit var dataStore: DataStore<Session>

    @Test
    fun `initial session should equal to the Session class`() {
        runTest {
            dataStore.data.first() shouldBeEqualToComparingFields Session()
        }
    }

    @Test
    fun `saving a session should return the same values`() {
        runTest {
            with(dataStore) {
                updateData { p -> p.copy(anilistToken = "token", isSessionActive = true) }

                data.first() shouldBeEqualToComparingFields Session(
                    anilistToken = "token",
                    isSessionActive = true,
                )
            }
        }
    }
}
