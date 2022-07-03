package com.costular.atomtasks.ui.home

import androidx.lifecycle.viewModelScope
import com.costular.atomtasks.domain.interactor.GetThemeUseCase
import com.costular.atomtasks.domain.model.Theme
import com.costular.atomtasks.core_ui.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
) : com.costular.atomtasks.core_ui.mvi.MviViewModel<AppState>(AppState.Empty) {

    init {
        getTheme()
    }

    private fun getTheme() {
        viewModelScope.launch {
            getThemeUseCase(Unit)
            getThemeUseCase.observe()
                .collect { theme ->
                    setState { copy(theme = theme) }
                }
        }
    }
}

data class AppState(
    val theme: Theme = Theme.System,
) {
    companion object {
        val Empty = AppState()
    }
}
