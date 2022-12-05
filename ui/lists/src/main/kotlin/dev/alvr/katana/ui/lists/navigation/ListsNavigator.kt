package dev.alvr.katana.ui.lists.navigation

import dev.alvr.katana.ui.base.components.datepicker.KatanaDatePickerParams
import dev.alvr.katana.ui.base.navigation.BaseNavigator
import dev.alvr.katana.ui.lists.entities.UserList
import dev.alvr.katana.ui.lists.entities.MediaListItem

interface ListsNavigator : BaseNavigator {
    fun listsEditEntry(entry: MediaListItem)
    fun listsEntryDetails(id: Int)
    fun listSelector(lists: Array<UserList>, selectedList: String)
    fun listsOpenDatePicker(params: KatanaDatePickerParams)
}
