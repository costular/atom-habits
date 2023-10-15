package com.costular.atomtasks.ui.features.edittask

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.costular.atomtasks.core.ui.mvi.MviViewModel
import com.costular.atomtasks.tasks.interactor.GetTaskByIdInteractor
import com.costular.atomtasks.tasks.interactor.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val getTaskByIdInteractor: GetTaskByIdInteractor,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : MviViewModel<EditTaskState>(EditTaskState.Empty) {

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            getTaskByIdInteractor(GetTaskByIdInteractor.Params(taskId))
            getTaskByIdInteractor.flow
                .onStart {
                    setState { copy(taskState = TaskState.Loading) }
                }
                .collect { task ->
                    setState {
                        copy(
                            taskState = TaskState.Success(
                                taskId = task.id,
                                name = task.name,
                                date = task.day,
                                reminder = task.reminder?.time,
                            ),
                        )
                    }
                }
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun editTask(
        name: String,
        date: LocalDate,
        reminder: LocalTime?,
    ) {
        viewModelScope.launch {
            val state = state.value
            if (state.taskState !is TaskState.Success) {
                return@launch
            }

            try {
                updateTaskUseCase(
                    UpdateTaskUseCase.Params(
                        taskId = state.taskState.taskId,
                        name = name,
                        date = date,
                        reminderTime = reminder,
                    ),
                )
                setState { copy(savingTask = SavingState.Success) }
            } catch (e: Exception) {
                setState { copy(savingTask = SavingState.Failure) }
            }
        }
    }
}

data class EditTaskState(
    val taskState: TaskState = TaskState.Idle,
    val savingTask: SavingState = SavingState.Uninitialized,
) {

    companion object {
        val Empty = EditTaskState()
    }
}

sealed class TaskState {

    object Idle : TaskState()

    object Loading : TaskState()

    data class Success(
        val taskId: Long,
        val name: String,
        val date: LocalDate,
        val reminder: LocalTime?,
    ) : TaskState()
}

@Stable
sealed interface SavingState {

    data object Uninitialized : SavingState
    data object Saving : SavingState
    data object Failure : SavingState
    data object Success : SavingState
}
