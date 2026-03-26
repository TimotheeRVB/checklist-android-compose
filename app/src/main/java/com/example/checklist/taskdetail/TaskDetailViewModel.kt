package com.example.checklist.taskdetail

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklist.data.TasksRepository
import com.example.checklist.tasks.TasksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailUiState(isLoading = true))

    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            val task = tasksRepository.getTaskById(taskId.toInt())
            _uiState.value = _uiState.value.copy(
                title = task.title,
                description = task.description,
                isDone = task.isDone,
                isLoading = false
            )
        }
    }

}

data class TaskDetailUiState(
    val title: String = "",
    val description: String = "",
    val isDone: Boolean = false,
    val isLoading: Boolean = false
)