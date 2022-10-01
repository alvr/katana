package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.ui.lists.entities.MediaListItem
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import java.time.LocalDate
import java.time.LocalDateTime

internal class MangaListItemMapperTest : WordSpec() {
    init {
        "a random collection of manga" should {
            val times = Arb.int(range = 5..10).next()

            val animeCollection = buildList {
                repeat(times) {
                    add(
                        MediaListGroup(
                            name = Arb.string().next(),
                            entries = buildList {
                                repeat(times) {
                                    add(
                                        MediaListEntry(
                                            list = Arb.bind<MediaList>(
                                                mapOf(
                                                    LocalDate::class to Arb.localDate(),
                                                    LocalDateTime::class to Arb.localDateTime(),
                                                ),
                                            ).next(),
                                            entry = Arb.bind<MediaEntry.Manga>().next(),
                                        ),
                                    )
                                }
                            },
                        ),
                    )
                }
            }

            "have the same size after mapping" {
                animeCollection.toMediaItems().run {
                    first().shouldBeTypeOf<MediaListItem.MangaListItem>()
                    shouldHaveSize(times * times)
                }
            }
        }
    }
}
