package com.costular.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.costular.atomtasks.core.ui.utils.ofLocalized
import com.costular.atomtasks.core.util.DateTimeFormatters
import com.costular.designsystem.theme.AppTheme
import com.costular.designsystem.theme.AtomTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    selectedDay: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
) {
    val month = remember(selectedDay) { selectedDay.yearMonth }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberCalendarState(
        firstVisibleMonth = month,
        firstDayOfWeek = firstDayOfWeek,
    )

    LaunchedEffect(selectedDay) {
        state.animateScrollToMonth(YearMonth.from(selectedDay))
    }

    HorizontalCalendar(
        state = state,
        dayContent = { day ->
            Day(
                day = day,
                isSelected = selectedDay == day.date,
                onClick = { onDateSelected(it.date) },
            )
        },
        monthHeader = { month ->
            MonthHeader(month)
        },
        modifier = modifier,
    )
}

@Composable
private fun MonthHeader(
    month: CalendarMonth,
) {
    Column {
        MonthHeader(
            month = month.yearMonth.ofLocalized(DateTimeFormatters.monthFormatter)
        )

        Spacer(Modifier.height(AppTheme.dimens.spacingLarge))

        Row(modifier = Modifier.fillMaxWidth()) {
            val mediumEmphasis = MaterialTheme.colorScheme.onSurfaceVariant

            month.weekDays.first().map { it.date.dayOfWeek }.forEach { dayOfWeek ->
                CompositionLocalProvider(LocalContentColor provides mediumEmphasis) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = dayOfWeek.ofLocalized(TextStyle.SHORT),
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun BoxScope.Day(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit,
) {
    val isToday = day.date == LocalDate.now()

    val surfaceElevation = LocalAbsoluteTonalElevation.current.value.dp
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceColorAtElevation(surfaceElevation)
    }
    val borderColor = if (isToday) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        Color.Transparent
    }
    val contentColor = contentColorFor(backgroundColor)
    val contentAlpha =
        if (day.position == DayPosition.MonthDate) 1f else AppTheme.DisabledAlpha

    Box(
        modifier = Modifier
            .padding(AppTheme.dimens.spacingSmall)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape = CircleShape)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = contentColor.copy(alpha = contentAlpha),
        )
    }
}

@Composable
fun MonthHeader(
    month: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = month,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    AtomTheme {
        DatePicker(
            onDateSelected = {},
        )
    }
}
