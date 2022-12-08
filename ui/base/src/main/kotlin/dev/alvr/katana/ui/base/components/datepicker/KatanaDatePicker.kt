package dev.alvr.katana.ui.base.components.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import dev.alvr.katana.ui.base.currentLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

private val now by lazy { LocalDate.now() }

@Composable
fun KatanaDatePicker(
    params: KatanaDatePickerParams,
    onSelectDate: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (date, selectDate) = remember { mutableStateOf(params.selectedDay ?: now) }

    Column(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(size = 16.dp),
            ),
    ) {
        Column(
            Modifier
                .defaultMinSize(minHeight = 72.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                )
                .padding(16.dp),
        ) {
            if (params.title != null) {
                Text(
                    text = params.title.uppercase(currentLocale()),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary,
                )

                Spacer(modifier = Modifier.size(12.dp))
            }

            Text(
                text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onPrimary,
            )
        }

        KatanaCalendar(params = params.copy(selectedDay = date), onSelectDate = selectDate)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 16.dp, end = 16.dp),
        ) {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = stringResource(android.R.string.cancel),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onPrimary,
                )
            }

            TextButton(onClick = { onSelectDate(date) }) {
                Text(
                    text = stringResource(android.R.string.ok),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun KatanaCalendar(
    params: KatanaDatePickerParams,
    onSelectDate: (LocalDate) -> Unit,
) {
    var selectedDate by remember { mutableStateOf(params.selectedDay) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val daysOfWeek = remember { daysOfWeek(firstDayOfWeek).toPersistentList() }

    val state = rememberCalendarState(
        startMonth = params.startMonth,
        endMonth = params.endMonth,
        firstVisibleMonth = params.firstVisibleMonth,
        firstDayOfWeek = firstDayOfWeek,
    )

    HorizontalCalendar(
        state = state,
        monthHeader = { calendarMonth ->
            val (currentMonth, currentYear) = with(calendarMonth.yearMonth) {
                month.getDisplayName(TextStyle.FULL, currentLocale()) to year
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$currentMonth $currentYear",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.h6,
            )

            Spacer(modifier = Modifier.height(24.dp))

            CalendarDaysOfWeekTitle(daysOfWeek = daysOfWeek)
        },
        dayContent = { day ->
            CalendarDay(day = day, selected = selectedDate == day.date) {
                selectedDate = day.date
                selectedDate?.let { date -> onSelectDate(date) }
            }
        },
    )
}

@Composable
private fun CalendarDaysOfWeekTitle(daysOfWeek: ImmutableList<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, currentLocale()),
            )
        }
    }
}

@Composable
private fun CalendarDay(
    day: CalendarDay,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val dayBackground = if (selected) {
        MaterialTheme.colors.secondaryVariant
    } else {
        Color.Transparent
    }

    val dayTextColor = if (selected) {
        contentColorFor(dayBackground)
    } else {
        MaterialTheme.colors.onSurface
    }

    val dayEnabled = day.position == DayPosition.MonthDate && day.date <= now

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(color = dayBackground)
            .clickable(
                enabled = dayEnabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (dayEnabled) dayTextColor else Color.Gray,
        )
    }
}
