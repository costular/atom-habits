package com.costular.atomtasks.tasks.interactor

import com.costular.atomtasks.tasks.model.Reminder
import com.costular.atomtasks.tasks.model.Task
import com.costular.atomtasks.tasks.manager.TaskReminderManager
import com.costular.core.toError
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostponeTaskUseCaseTest {

    lateinit var sut: PostponeTaskUseCase

    private val getTaskByIdInteractor: GetTaskByIdInteractor = mockk(relaxed = true)
    private val updateTaskReminderInteractor: UpdateTaskReminderInteractor = mockk(relaxed = true)
    private val taskReminderManager: TaskReminderManager = mockk(relaxed = true)

    @Before
    fun setUp() {
        sut = PostponeTaskUseCase(
            getTaskByIdInteractor = getTaskByIdInteractor,
            updateTaskReminderInteractor = updateTaskReminderInteractor,
            taskReminderManager = taskReminderManager,
        )
    }

    @Test
    fun `Should return error when postpone a task given the task has no reminder`() = runTest {
        givenTaskWithoutReminder()

        val result = sut.invoke(
            PostponeTaskUseCase.Params(
                taskId = 1L,
                day = LocalDate.now(),
                time = LocalTime.now().plusHours(1),
            )
        )

        Truth.assertThat(result).isEqualTo(PostponeTaskFailure.MissingReminder.toError())
    }

    @Test
    fun `Should update task reminder when postpone task given the task has reminder`() = runTest {
        givenTaskWithReminder()

        val taskId = 10L
        val day = LocalDate.now().plusDays(1)
        val time = LocalTime.of(21, 0)

        sut.invoke(
            PostponeTaskUseCase.Params(
                taskId = taskId,
                day = day,
                time = time,
            )
        )

        coEvery {
            updateTaskReminderInteractor.invoke(
                UpdateTaskReminderInteractor.Params(
                    taskId = taskId,
                    time = time,
                    date = day,
                )
            )
        }
    }

    @Test
    fun `Should set the new reminder when postpone a task given it has a remidner`() = runTest {
        givenTaskWithReminder()

        val taskId = 10L
        val day = LocalDate.now().plusDays(1)
        val time = LocalTime.of(21, 0)

        sut.invoke(
            PostponeTaskUseCase.Params(
                taskId = taskId,
                day = day,
                time = time,
            )
        )

        coEvery {
            taskReminderManager.set(
                taskId = taskId,
                localDateTime = day.atTime(time),
            )
        }
    }

    private fun givenTaskWithoutReminder() {
        every { getTaskByIdInteractor.flow } returns flowOf(
            Task(
                id = 1L,
                name = "Whatever",
                createdAt = LocalDate.now(),
                day = LocalDate.now(),
                reminder = null,
                isDone = false,
                position = 0,
            )
        )
    }

    private fun givenTaskWithReminder() {
        every { getTaskByIdInteractor.flow } returns flowOf(
            Task(
                id = 1L,
                name = "Whatever",
                createdAt = LocalDate.now(),
                day = LocalDate.now(),
                reminder = Reminder(
                    id = 1L,
                    time = LocalTime.of(9, 0),
                    date = LocalDate.now().plusDays(1),
                ),
                isDone = false,
                position = 0,
            )
        )
    }
}
