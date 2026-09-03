package dev.alvr.katana.shared

/** Called from Swift `onOpenURL` with the incoming deep link URI. */
fun onDeepLink(uri: String) {
    appGraph.deepLinkState.onDeepLink(uri)
}
