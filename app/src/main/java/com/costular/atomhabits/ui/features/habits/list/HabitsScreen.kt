package com.costular.atomhabits.ui.features.habits.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.costular.atomhabits.ui.components.HorizontalCalendar
import java.time.LocalDate
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.costular.atomhabits.domain.Async
import com.costular.atomhabits.domain.model.Habit
import com.costular.atomhabits.ui.components.HabitList
import com.costular.atomhabits.ui.components.ScreenHeader
import com.costular.atomhabits.ui.util.DateUtils.dayAsText
import com.costular.atomhabits.ui.util.rememberFlowWithLifecycle

@Composable
fun HabitsScreen(
    onCreateHabit: () -> Unit,
    onOpenHabit: (Habit) -> Unit
) {
    val viewModel: HabitListViewModel = hiltViewModel()
    val state by rememberFlowWithLifecycle(viewModel.state).collectAsState(initial = HabitListState())

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text("Create")
                },
                onClick = onCreateHabit,
                icon = {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                },
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val selectedDayText = dayAsText(state.selectedDay)
                ScreenHeader(
                    text = selectedDayText,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                        .clickable {
                            // TODO: 26/6/21 open calendar
                        }
                )

                IconButton(
                    onClick = {
                        val newDay = state.selectedDay.minusDays(1)
                        viewModel.setSelectedDay(newDay)
                    },
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
                }
                IconButton(
                    onClick = {
                        val newDay = state.selectedDay.plusDays(1)
                        viewModel.setSelectedDay(newDay)
                    },
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                }
            }

            HorizontalCalendar(
                from = LocalDate.now().minusDays(7),
                until = LocalDate.now().plusDays(7),
                selectedDay = state.selectedDay,
                modifier = Modifier.padding(bottom = 24.dp),
                onSelectDay = {
                    viewModel.setSelectedDay(it)
                }
            )

            val habits = state.habits
            when (habits) {
                is Async.Success -> {
                    HabitList(
                        habits = habits.data,
                        onClick = { onOpenHabit(it) },
                        onMarkHabit = { id, isMarked -> viewModel.onMarkHabit(id, !isMarked) },
                        modifier = Modifier.fillMaxSize(),
                        date = state.selectedDay
                    )
                }
            }

        }
    }
}