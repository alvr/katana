package dev.alvr.katana.ui.lists.view.edit

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle
import dev.alvr.katana.ui.base.components.datepicker.KatanaDatePicker
import dev.alvr.katana.ui.base.components.datepicker.KatanaDatePickerParams
import dev.alvr.katana.ui.base.navigation.MultiResultBackNavigator
import dev.alvr.katana.ui.base.navigation.MultiResultRecipient
import dev.alvr.katana.ui.base.navigation.navigateBack
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.destinations.CalendarDialogDestination
import java.time.LocalDate

@Composable
@Destination(style = DestinationStyle.BottomSheet::class)
internal fun EditEntrySheet(
    listItem: MediaListItem,
    navigator: ListsNavigator,
    resultRecipient: MultiResultRecipient<CalendarDialogDestination, LocalDate>,
) {
    Text(text = "EditEntrySheet")
}

@Composable
@Destination(style = DestinationStyle.Dialog::class)
internal fun CalendarDialog(
    code: Int,
    params: KatanaDatePickerParams,
    resultNavigator: MultiResultBackNavigator<LocalDate>,
) {
    KatanaDatePicker(
        params = params,
        onSelectDate = { date -> resultNavigator.navigateBack(code, date) },
        onDismissRequest = resultNavigator::navigateBack,
    )
}
