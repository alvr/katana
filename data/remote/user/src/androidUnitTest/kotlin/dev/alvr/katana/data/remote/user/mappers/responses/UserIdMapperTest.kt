package dev.alvr.katana.data.remote.user.mappers.responses

import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.user.UserIdQuery
import io.kotest.assertions.throwables.shouldNotThrowExactlyUnit
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class UserIdMapperTest : TestBase() {
    @Test
    @DisplayName("WHEN a null UserIdQuery.Data THEN throw an IllegalStateException")
    fun `null UserIdQuery Data`() = runTest {
        // GIVEN
        val userId: UserIdQuery.Data? = null

        // THEN
        shouldThrowExactlyUnit<IllegalStateException> {

            // WHEN
            userId()
        } shouldHaveMessage "ViewerId is required."
    }

    @Test
    @DisplayName("WHEN an UserIdQuery with null viewer THEN should throw an IllegalStateException")
    fun `UserIdQuery with null viewer`() = runTest {
        // GIVEN
        val userId = UserIdQuery.Data(viewer = null)

        // THEN
        shouldThrowExactlyUnit<IllegalStateException> {

            // WHEN
            userId()
        } shouldHaveMessage "ViewerId is required."
    }

    @Test
    @DisplayName("WHEN an UserIdQuery with a viewer THEN should have the same Id returned")
    fun `an UserIdQuery with a viewer should have same id`() = runTest {
        // GIVEN
        val userId = UserIdQuery.Data(viewer = UserIdQuery.Viewer(37_384))

        // WHEN
        val result = userId().id

        // THEN
        result shouldBeExactly 37_384
    }

    @Test
    @DisplayName("WHEN an UserIdQuery with a viewer THEN should not throw a IllegalStateException")
    fun `an UserIdQuery with a viewer should not throw`() = runTest {
        // GIVEN
        val userId = UserIdQuery.Data(viewer = UserIdQuery.Viewer(37_384))

        // THEN
        shouldNotThrowExactlyUnit<IllegalStateException> {

            // WHEN
            userId()
        }
    }
}
