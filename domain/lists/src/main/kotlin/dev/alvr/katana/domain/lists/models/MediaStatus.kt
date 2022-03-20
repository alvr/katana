package dev.alvr.katana.domain.lists.models

enum class MediaStatus(val value: String) {
    CURRENT("current"),
    PLANNING("planning"),
    COMPLETED("completed"),
    DROPPED("dropped"),
    PAUSED("paused"),
    REPEATING("repeating"),
    UNKNOWN("unknown");

    companion object {
        fun of(value: String?) = values().find { it.value == value?.lowercase() } ?: UNKNOWN
    }
}
