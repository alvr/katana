package dev.alvr.katana.data.remote.user.managers

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.usecases.GetUserIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class UserIdManagerTest : TestBase() {
    @MockK
    private lateinit var useCase: GetUserIdUseCase

    private lateinit var manager: UserIdManager

    override suspend fun beforeEach() {
        manager = UserIdManagerImpl(useCase)
    }

    @Test
    @DisplayName("WHEN server return viewer is valid THEN it should return the id of the user")
    fun `server return viewer is valid`() = runTest {
        // GIVEN
        coEvery { useCase() } returns UserId(37_384).right()

        // WHEN
        val result = manager.getId()

        // THEN
        result.shouldBeRight(37_384)
        coVerify(exactly = 1) { useCase() }
    }

    @Test
    @DisplayName("WHEN server fails to return something THEN the mapper should throw an exception")
    fun `server fails to return something`() = runTest {
        // GIVEN
        coEvery { useCase() } returns UserFailure.GettingUserId.left()

        // WHEN
        val result = manager.getId()

        // THEN
        result.shouldBeLeft(UserFailure.GettingUserId)
        coVerify(exactly = 1) { useCase() }
    }
}
