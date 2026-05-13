package dev.alvr.katana.core.ui.navigation.deeplink

private const val ANILIST_HOST = "anilist.co"
private const val OAUTH_PIN_PATH = "/api/v2/oauth/pin"
private const val ACCESS_TOKEN_FRAGMENT_KEY = "access_token"

object KatanaDeepLinkHandler {

    fun parse(url: String): KatanaDeepLink {
        val (path, fragment) = splitUrl(url)

        if (isAnilistOAuthPin(path)) {
            val token = parseFragmentParam(fragment, ACCESS_TOKEN_FRAGMENT_KEY)
            if (!token.isNullOrBlank()) return KatanaDeepLink.Login(token)
        }

        val segments = path
            .removePrefix("/")
            .removeSuffix("/")
            .split("/")
            .filter { it.isNotBlank() }

        if (segments.size >= 2) {
            val type = segments[0]
            val id = segments[1].toIntOrNull()
            if (id != null) {
                return when (type) {
                    "anime" -> KatanaDeepLink.AnimeDetail(id)
                    "manga" -> KatanaDeepLink.MangaDetail(id)
                    else -> KatanaDeepLink.Home
                }
            }
        }

        return KatanaDeepLink.Home
    }

    private fun splitUrl(url: String): Pair<String, String?> {
        val withoutScheme = url.substringAfter("://")
        val withoutHost = withoutScheme.substringAfter(ANILIST_HOST).ifEmpty { withoutScheme }
        val hashIndex = withoutHost.indexOf('#')
        return if (hashIndex == -1) {
            withoutHost to null
        } else {
            withoutHost.substring(0, hashIndex) to withoutHost.substring(hashIndex + 1)
        }
    }

    private fun isAnilistOAuthPin(path: String): Boolean = path.trimEnd('/') == OAUTH_PIN_PATH

    private fun parseFragmentParam(fragment: String?, key: String): String? {
        if (fragment.isNullOrBlank()) return null
        return fragment
            .split("&")
            .firstOrNull { it.startsWith("$key=") }
            ?.substringAfter("$key=")
            ?.takeIf { it.isNotBlank() }
    }
}
