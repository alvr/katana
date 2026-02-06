package dev.alvr.katana.core.tests

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
val String.Companion.random
    get() = Uuid.random().toString()
