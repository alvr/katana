package dev.alvr.katana.ui.social.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

sealed interface SocialComponent

fun AppComponentContext.createSocialComponent(): SocialComponent = DefaultSocialComponent(
    componentContext = this,
)
