package dev.alvr.katana.ui.account.viewmodel

internal data class AccountState(
    val username: String = "alvr",
    val avatar: String = "https://s4.anilist.co/file/anilistcdn/user/avatar/large/37384-2t1Zp3LBpzUZ.jpg",
    val background: String = "https://s4.anilist.co/file/anilistcdn/user/banner/37384-jtds8dpQIGVG.jpg",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
