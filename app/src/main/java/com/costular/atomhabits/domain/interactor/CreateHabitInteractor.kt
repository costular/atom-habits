package com.costular.atomhabits.domain.interactor

import com.costular.atomhabits.data.habits.HabitsRepository
import com.costular.atomhabits.domain.Interactor
import com.costular.atomhabits.domain.model.Repetition
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateHabitInteractor @Inject constructor(
    private val habitsRepository: HabitsRepository
) : Interactor<CreateHabitInteractor.Params>() {

    data class Params(
        val name: String,
        val repetition: Repetition,
        val reminderEnabled: Boolean,
        val reminderTime: LocalTime?
    )

    override suspend fun doWork(params: Params) {
        habitsRepository.createHabit(
            params.name,
            params.repetition,
            params.reminderEnabled,
            params.reminderTime
        )
    }


}