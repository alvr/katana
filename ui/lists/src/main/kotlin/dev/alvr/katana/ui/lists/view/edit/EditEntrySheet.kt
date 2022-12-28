package dev.alvr.katana.ui.lists.view.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import dev.alvr.katana.ui.base.KatanaDateFormatter
import dev.alvr.katana.ui.base.components.datepicker.KatanaDatePicker
import dev.alvr.katana.ui.base.components.datepicker.KatanaDatePickerParams
import dev.alvr.katana.ui.base.format
import dev.alvr.katana.ui.base.navigation.MultiResultBackNavigator
import dev.alvr.katana.ui.base.navigation.MultiResultRecipient
import dev.alvr.katana.ui.base.navigation.OnBackResult
import dev.alvr.katana.ui.base.navigation.navigateBack
import dev.alvr.katana.ui.base.yearMonth
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.navigation.ListsNavigator
import dev.alvr.katana.ui.lists.view.components.DateSelector
import dev.alvr.katana.ui.lists.view.destinations.CalendarDialogDestination
import java.time.LocalDate
import java.time.YearMonth

@Composable
@Destination(style = DestinationStyle.BottomSheet::class)
internal fun EditEntrySheet(
    listItem: MediaListItem,
    navigator: ListsNavigator,
    resultRecipient: MultiResultRecipient<CalendarDialogDestination, LocalDate>,
    resultNavigator: ResultBackNavigator<MediaListItem>,
) {
    var item by remember { mutableStateOf(listItem) }

    resultRecipient.OnBackResult { code, date ->
        when (code) {
            START_DATE_CALENDAR -> item = item.edit(startedAt = date)
            END_DATE_CALENDAR -> item = item.edit(completedAt = date)
        }
    }

    Column {
        DatesRow(
            startedDate = item.startedAt.format(KatanaDateFormatter.MEDIUM_DATE),
            completedDate = item.completedAt.format(KatanaDateFormatter.MEDIUM_DATE),
            onStartedOpen = {
                navigator.listsOpenDatePicker(
                    START_DATE_CALENDAR,
                    KatanaDatePickerParams(
                        selectedDay = item.startedAt,
                        startMonth = item.startDate.yearMonth,
                        endMonth = item.completedAt.yearMonth,
                        firstVisibleMonth = item.startedAt.yearMonth,
                    ),
                )
            },
            onCompletedOpen = {
                navigator.listsOpenDatePicker(
                    END_DATE_CALENDAR,
                    KatanaDatePickerParams(
                        selectedDay = item.completedAt,
                        startMonth = item.startedAt.yearMonth,
                        endMonth = YearMonth.now(),
                        firstVisibleMonth = item.completedAt.yearMonth,
                    ),
                )
            },
            onStartedClear = { item = item.edit(startedAt = null) },
            onCompletedClear = { item = item.edit(completedAt = null) },
        )

        BottomButtons(
            onCancel = navigator::navigateUp,
            onSave = { resultNavigator.navigateBack(item) },
        )
    }
}

@Composable
private fun DatesRow(
    startedDate: String,
    completedDate: String,
    onStartedOpen: () -> Unit,
    onStartedClear: () -> Unit,
    onCompletedOpen: () -> Unit,
    onCompletedClear: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        EditEntryItem(
            title = stringResource(R.string.lists_edit_entry_started_date),
            modifier = Modifier.weight(1f),
        ) {
            DateSelector(
                date = startedDate,
                onOpen = onStartedOpen,
                onClear = onStartedClear,
            )
        }

        EditEntryItem(
            title = stringResource(R.string.lists_edit_entry_completed_date),
            modifier = Modifier.weight(1f),
        ) {
            DateSelector(
                date = completedDate,
                onOpen = onCompletedOpen,
                onClear = onCompletedClear,
            )
        }
    }
}

@Composable
private fun BottomButtons(onCancel: () -> Unit, onSave: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        OutlinedButton(
            modifier = Modifier.defaultMinSize(minWidth = 120.dp),
            onClick = onCancel,
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.error),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.onError,
            ),
        ) {
            Icon(
                imageVector = Icons.TwoTone.Clear,
                contentDescription = stringResource(R.string.lists_edit_entry_cancel_button),
            )
            Text(text = stringResource(R.string.lists_edit_entry_cancel_button))
        }

        OutlinedButton(
            modifier = Modifier.defaultMinSize(minWidth = 120.dp),
            onClick = onSave,
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.secondary),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.onSecondary,
            ),
        ) {
            Icon(
                imageVector = Icons.TwoTone.Save,
                contentDescription = stringResource(R.string.lists_edit_entry_save_button),
            )
            Text(text = stringResource(R.string.lists_edit_entry_save_button))
        }
    }
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

private const val START_DATE_CALENDAR = 1000
private const val END_DATE_CALENDAR = 1001
