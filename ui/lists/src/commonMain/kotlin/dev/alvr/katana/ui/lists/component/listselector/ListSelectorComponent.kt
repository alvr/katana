package dev.alvr.katana.ui.lists.component.listselector

import dev.alvr.katana.ui.base.decompose.AppComponentContext

sealed interface ListSelectorComponent

fun AppComponentContext.createListSelectorComponent(): ListSelectorComponent = DefaultListSelectorComponent(
    componentContext = this,
)
