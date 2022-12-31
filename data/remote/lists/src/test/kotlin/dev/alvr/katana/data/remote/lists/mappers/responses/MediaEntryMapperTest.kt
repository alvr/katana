package dev.alvr.katana.data.remote.lists.mappers.responses

import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import dev.alvr.katana.data.remote.lists.fragment.MediaEntry as MediaEntryFragment

@ExperimentalCoroutinesApi
internal class MediaEntryMapperTest : TestBase() {
    @Test
    @DisplayName("WHEN the format is known THEN the format should not be UNKNOWN")
    fun `when the format is known`() = runTest {
        // GIVEN
        val formats = MediaFormat.knownValues().map { format ->
            MediaEntryFragment(
                id = Int.zero,
                title = null,
                episodes = null,
                chapters = null,
                volumes = null,
                format = format,
                coverImage = null,
                nextAiringEpisode = null,
                startDate = null,
                endDate = null,
            ).mediaEntry().format
        }

        // THEN
        formats.shouldNotContain(CommonMediaEntry.Format.UNKNOWN)
    }

    @ParameterizedTest(name = "WHEN is {0} THEN the format should be UNKNOWN")
    @MethodSource("unknownArgs")
    fun `is unknown`(format: MediaFormat?) = runTest {
        // GIVEN
        val entryFormat = MediaEntryFragment(
            id = Int.zero,
            title = null,
            episodes = null,
            chapters = null,
            volumes = null,
            format = format,
            coverImage = null,
            nextAiringEpisode = null,
            startDate = null,
            endDate = null,
        ).mediaEntry().format

        // THEN
        entryFormat shouldBe CommonMediaEntry.Format.UNKNOWN
    }

    private companion object {
        @JvmStatic
        fun unknownArgs() = listOf(
            Arguments.of(MediaFormat.UNKNOWN__),
            Arguments.of(null),
        )
    }
}
