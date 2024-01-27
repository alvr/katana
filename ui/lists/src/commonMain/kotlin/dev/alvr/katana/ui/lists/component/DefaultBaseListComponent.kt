package dev.alvr.katana.ui.lists.component

import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.ui.base.decompose.AppComponentContext
import dev.alvr.katana.ui.base.decompose.extensions.appChildSlot
import dev.alvr.katana.ui.lists.component.listselector.createListSelectorComponent
import dev.alvr.katana.ui.lists.entities.UserList
import dev.alvr.katana.ui.lists.entities.item.MediaListItem
import dev.alvr.katana.ui.lists.viewmodel.ListsViewModel
import kotlinx.serialization.Serializable

internal sealed class DefaultBaseListComponent<E : MediaEntry, I : MediaListItem>(
    componentContext: AppComponentContext,
) : BaseListComponent<I>, AppComponentContext by componentContext {
    protected abstract val viewModel: ListsViewModel<E, I>

    private val navigation = SlotNavigation<Config>()

    override val userLists get() = viewModel.userLists.toList()

    override val bottomSheet = appChildSlot(
        source = navigation,
        serializer = Config.serializer(),
        childFactory = ::childFactory,
    )

    override fun refreshList() {
        viewModel.refreshList()
    }

    override fun addPlusOne(id: Int) {
        viewModel.addPlusOne(id)
    }

    override fun search(search: String) {
        viewModel.search(search)
    }

    override fun selectList(name: String) {
        viewModel.selectList(name)
    }

    override fun dismissListSelector() {
        navigation.dismiss()
    }

    override fun showListSelectorBottomSheet(lists: List<UserList>, current: String) {
        navigation.activate(Config.BottomSheetListSelector(lists, current))
    }

    private fun childFactory(
        config: Config,
        componentContext: AppComponentContext,
    ) = when (config) {
        is Config.BottomSheetListSelector -> componentContext.listSelectorFactory()
    }

    private fun AppComponentContext.listSelectorFactory() =
        createListSelectorComponent()

    @Serializable
    internal sealed interface Config {
        @Serializable
        class BottomSheetListSelector(val lists: List<UserList>, val current: String) : Config
    }
}
