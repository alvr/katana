package dev.alvr.katana.ui.social.component

import dev.alvr.katana.ui.base.decompose.AppComponentContext

internal class DefaultSocialComponent(
    componentContext: AppComponentContext,
) : SocialComponent, AppComponentContext by componentContext
