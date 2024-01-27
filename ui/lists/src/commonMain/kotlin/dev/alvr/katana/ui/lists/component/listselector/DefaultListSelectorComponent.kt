package dev.alvr.katana.ui.lists.component.listselector

import dev.alvr.katana.ui.base.decompose.AppComponentContext

internal class DefaultListSelectorComponent(
    componentContext: AppComponentContext
) : ListSelectorComponent, AppComponentContext by componentContext
