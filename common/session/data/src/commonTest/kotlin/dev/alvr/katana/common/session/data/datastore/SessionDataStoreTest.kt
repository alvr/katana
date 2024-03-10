package dev.alvr.katana.common.session.data.datastore

import androidx.datastore.core.DataStore
import app.cash.turbine.test
import dev.alvr.katana.common.session.data.di.corruptedDataStoreNamed
import dev.alvr.katana.common.session.data.di.dataStoreNamed
import dev.alvr.katana.common.session.data.di.deleteDataStoreFiles
import dev.alvr.katana.common.session.data.di.testDataStoreModule
import dev.alvr.katana.common.session.data.models.Session
import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.core.tests.koinExtension
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.equals.shouldBeEqual
import kotlin.time.Duration.Companion.seconds
import org.koin.test.KoinTest
import org.koin.test.inject

internal class SessionDataStoreTest : FreeSpec(), KoinTest {
    private val dataStore by inject<DataStore<Session>>(dataStoreNamed)
    private val corruptedDataStore by inject<DataStore<Session>>(corruptedDataStoreNamed)

    init {
        "initial session should equal to the Session class" {
            dataStore.data.test(5.seconds) {
                awaitItem() shouldBeEqual Session()
                cancelAndConsumeRemainingEvents()
            }
        }

        "saving a session should return the same values" {
            with(dataStore) {
                updateData { p ->
                    p.copy(
                        anilistToken = AnilistToken("token"),
                        isSessionActive = true,
                    )
                }

                data.test(5.seconds) {
                    awaitItem() shouldBeEqual Session(
                        anilistToken = AnilistToken("token"),
                        isSessionActive = true,
                    )
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        "corrupted dataStore should recreate again the file with initial values" {
            corruptedDataStore.data.test(5.seconds) {
                awaitItem() shouldBeEqual Session(anilistToken = AnilistToken("recreated"))
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        deleteDataStoreFiles()
    }

    override fun extensions() = listOf(koinExtension(testDataStoreModule()))
}
