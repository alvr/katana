package dev.alvr.katana.features.login.ui

internal const val LOGIN_DEEP_LINK_TOKEN = "token"
internal const val LOGIN_DEEP_LINK = "katana://app#access_token={$LOGIN_DEEP_LINK_TOKEN}"

internal const val BACKGROUND_ALPHA = .3f
internal const val LOGO_FULL_SIZE = 1f
internal const val LOGO_RESIZED = .6f

internal const val HEADER_ANIMATION_DELAY = 300
internal const val HEADER_ANIMATION_DURATION = 1250

internal const val BOTTOM_ANIM_DELAY = HEADER_ANIMATION_DELAY + HEADER_ANIMATION_DURATION / 2
internal const val BOTTOM_ANIM_DURATION = HEADER_ANIMATION_DURATION
internal const val BOTTOM_ARROW_ANIM_DURATION = 750
internal const val BOTTOM_CROSSFADE_ANIM_DURATION = 800

internal const val ANILIST_LOGIN = "https://anilist.co/api/v2/oauth/authorize?client_id=7275&response_type=token"
internal const val ANILIST_REGISTER = "https://anilist.co/signup"

internal const val GET_STARTED_BUTTON_TAG = "get_started"
