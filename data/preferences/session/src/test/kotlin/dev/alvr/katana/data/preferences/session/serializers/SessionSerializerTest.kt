package dev.alvr.katana.data.preferences.session.serializers

import dev.alvr.katana.data.preferences.session.models.Session
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteExisting
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Suppress("BlockingMethodInNonBlockingContext")
internal class SessionSerializerTest : BehaviorSpec() {
    private val tempFile = createTempFile()

    init {
        afterSpec {
            tempFile.deleteExisting()
        }

        given("a SessionSerializer") {
            val sessionSerializer = SessionSerializer

            `when`("reading from a file") {
                val session = sessionSerializer.readFrom(tempFile.inputStream())

                then("the token should be null") {
                    with(session) {
                        anilistToken.shouldBeNull()
                        isSessionActive.shouldBeFalse()
                    }
                }
            }

            `when`("when writing to a file") {
                val token = Arb.string().next()

                sessionSerializer.writeTo(
                    Session(
                        anilistToken = token,
                        isSessionActive = true,
                    ),
                    tempFile.outputStream(),
                )

                and("reading it later") {
                    val session = sessionSerializer.readFrom(tempFile.inputStream())

                    then("it should return the saved value") {
                        with(session) {
                            anilistToken shouldBe token
                            isSessionActive.shouldBeTrue()
                        }
                    }
                }
            }
        }
    }
}
