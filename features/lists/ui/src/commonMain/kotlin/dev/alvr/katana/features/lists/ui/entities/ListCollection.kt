package dev.alvr.katana.features.lists.ui.entities

import dev.alvr.katana.common.media.domain.models.ItemEntryId
import kotlinx.collections.immutable.ImmutableMap

internal typealias ListEntries<T> = ImmutableMap<ItemEntryId, T>

internal typealias ListsCollection<T> = ImmutableMap<String, ListEntries<T>>
