package com.costular.atomtasks.agenda

import com.costular.atomtasks.agenda.AgendaViewModel.Companion.DaysAfter
import com.costular.atomtasks.agenda.AgendaViewModel.Companion.DaysBefore
import com.costular.atomtasks.tasks.Task
import com.costular.core.Async
import java.time.LocalDate

data class AgendaState(
    val selectedDay: LocalDate = LocalDate.now(),
    val tasks: Async<List<com.costular.atomtasks.tasks.Task>> = Async.Uninitialized,
    val taskAction: com.costular.atomtasks.tasks.Task? = null,
    val deleteTaskAction: DeleteTaskAction = DeleteTaskAction.Hidden,
) {
    val calendarFromDate: LocalDate = LocalDate.now().minusDays(DaysBefore.toLong())
    val calendarUntilDate: LocalDate = LocalDate.now().plusDays(DaysAfter.toLong())

    val isPreviousDaySelected get() = calendarFromDate.isBefore(selectedDay)
    val isNextDaySelected get() = calendarUntilDate.isAfter(selectedDay)

    companion object {
        val Empty = AgendaState()
    }
}

sealed class DeleteTaskAction {

    object Hidden : DeleteTaskAction()

    data class Shown(
        val taskId: Long,
    ) : DeleteTaskAction()
}
