package dev.alvr.katana.data.preferences.token.serializers

import dev.alvr.katana.data.preferences.token.models.Token
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteExisting
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Suppress("BlockingMethodInNonBlockingContext")
internal class TokenSerializerTest : BehaviorSpec() {
    private val tempFile = createTempFile()

    init {
        afterSpec {
            tempFile.deleteExisting()
        }

        given("a TokenSerializer") {
            val tokenSerializer = TokenSerializer

            `when`("reading from a file") {
                val token = tokenSerializer.readFrom(tempFile.inputStream())

                then("the token should be null") {
                    token.anilistToken.shouldBeNull()
                }
            }

            `when`("when writing to a file") {
                tokenSerializer.writeTo(Token(anilistToken = "saved-token"), tempFile.outputStream())

                and("reading it later") {
                    val token = tokenSerializer.readFrom(tempFile.inputStream())

                    then("it should return the saved value") {
                        token.anilistToken shouldBe "saved-token"
                    }
                }
            }
        }
    }
}
