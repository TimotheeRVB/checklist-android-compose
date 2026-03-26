package com.example.checklist.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklist.data.TasksRepository
import com.example.checklist.data.UserPreferencesRepository
import com.example.checklist.domain.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TasksRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState(isLoading = true))
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent: SharedFlow<String> = _uiEvent.asSharedFlow()

    private var allTasks: List<Task> = emptyList()

    init {
        observeTasks()
    }

    fun onQueryChange(query: String) {
        val hideCompleted = _uiState.value.hideCompleted
        _uiState.value = _uiState.value.copy(
            query = query,
            tasks = filterTasks(
                tasks = allTasks,
                query = query,
                hideCompleted = hideCompleted
            )
        )
    }

    private fun observeTasks() {
        viewModelScope.launch {
            combine(
                repository.observeTasks(),
                userPreferencesRepository.observeHideCompleted()
            ) { tasks, hideCompleted ->
                Pair(tasks, hideCompleted)
            }.collect { (tasks, hideCompleted) ->
                allTasks = tasks
                val currentQuery = _uiState.value.query
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    hideCompleted = hideCompleted,
                    tasks = filterTasks(
                        tasks = tasks,
                        query = currentQuery,
                        hideCompleted = hideCompleted
                    )
                )
            }
        }
    }

    fun onHideCompletedChange(hideCompleted: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setHideCompleted(hideCompleted)
        }
    }

    private fun filterTasks(
        tasks: List<Task>,
        query: String,
        hideCompleted: Boolean
    ): List<Task> {
        return tasks
            .filter { task ->
                !hideCompleted || !task.isDone
            }
            .filter { task ->
                task.title.contains(query, ignoreCase = true)
            }
    }
}

data class TasksUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null,
    val query: String = "",
    val hideCompleted: Boolean = false
)