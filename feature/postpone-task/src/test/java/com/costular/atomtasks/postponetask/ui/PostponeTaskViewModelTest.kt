package com.costular.atomtasks.postponetask.ui

import com.costular.atomtasks.core.testing.MviViewModelTest
import com.costular.atomtasks.postponetask.domain.GetPostponeChoiceListUseCase
import com.costular.atomtasks.postponetask.domain.PostponeChoice
import com.costular.atomtasks.tasks.interactor.PostponeTaskUseCase
import com.costular.atomtasks.notifications.TaskNotificationManager
import com.costular.atomtasks.postponetask.domain.DefaultPostponeChoiceCalculator
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.Clock
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostponeTaskViewModelTest : MviViewModelTest() {

    private lateinit var sut: PostponeTaskViewModel

    private val getPostponeChoiceListUseCase: GetPostponeChoiceListUseCase = mockk(relaxed = true)
    private val postponeTaskUseCase: PostponeTaskUseCase = mockk(relaxed = true)
    private val taskNotificationManager: TaskNotificationManager = mockk(relaxed = true)

    @Before
    fun setup() {
        sut = PostponeTaskViewModel(
            getPostponeChoiceListUseCase = getPostponeChoiceListUseCase,
            postponeTaskUseCase = postponeTaskUseCase,
            taskNotificationManager = taskNotificationManager,
            postponeChoiceCalculator = DefaultPostponeChoiceCalculator(Clock.systemDefaultZone()),
        )
    }

    @Test
    fun `Should expose postpone choices when usecase return successfully`() = runTest {
        coEvery { getPostponeChoiceListUseCase(Unit) } returns FakeChoices

        sut.initialize(123L)

        assertThat(sut.state.value.postponeChoices).isEqualTo(
            PostponeChoiceListState.Success(
                FakeChoices
            )
        )

        coVerify(exactly = 1) { getPostponeChoiceListUseCase(Unit) }
    }

    @Test
    fun `Should expose loading when retreiving the postpone choice list`() = runTest {
        coEvery { getPostponeChoiceListUseCase(Unit) } coAnswers {
            delay(1000) // Simulate a loading delay
            FakeChoices
        }

        sut.initialize(123)

        assertThat(sut.state.value.postponeChoices).isEqualTo(PostponeChoiceListState.Loading)

        advanceUntilIdle()

        assertThat(sut.state.value.postponeChoices).isEqualTo(
            PostponeChoiceListState.Success(FakeChoices)
        )
    }

    @Test
    fun `Should cancel the notification manager when the task is postponed`() = runTest {
        val taskId = 123L
        coEvery { getPostponeChoiceListUseCase(Unit) } returns FakeChoices

        sut.initialize(taskId)
        sut.onSelectPostponeChoice(PostponeChoice.OneHour)

        coVerify(exactly = 1) { taskNotificationManager.removeTaskNotification(taskId) }
    }

    private companion object {
        val FakeChoices = listOf(PostponeChoice.OneHour, PostponeChoice.Tonight)
    }
}
