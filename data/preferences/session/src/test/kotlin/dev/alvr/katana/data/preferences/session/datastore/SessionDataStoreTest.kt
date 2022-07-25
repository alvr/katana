package dev.alvr.katana.data.preferences.session.datastore

import androidx.datastore.core.DataStore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.alvr.katana.data.preferences.session.models.Session
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
@Config(
    application = HiltTestApplication::class,
    minSdk = 28,
    maxSdk = 32,
)
internal class SessionDataStoreTest {
    @get:Rule
    internal val hiltRule = HiltAndroidRule(this)

    @Inject
    internal lateinit var dataStore: DataStore<Session>

    @Inject
    internal lateinit var scope: TestScope

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `initial session should equal to the Session class`() {
        scope.runTest {
            dataStore.data.first() shouldBeEqualToComparingFields Session()
        }
    }

    @Test
    fun `saving a session should return the same values`() {
        scope.runTest {
            with(dataStore) {
                updateData { p -> p.copy(anilistToken = "token", isSessionActive = true) }

                data.first() shouldBeEqualToComparingFields Session(
                    anilistToken = "token",
                    isSessionActive = true
                )
            }
        }
    }
}
