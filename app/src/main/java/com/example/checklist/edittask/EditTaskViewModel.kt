package com.example.checklist.edittask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklist.data.TasksRepository
import com.example.checklist.domain.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskUiState())
    val uiState: StateFlow<EditTaskUiState> = _uiState.asStateFlow()

    fun onTitleChange(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title
        )
    }

    fun onDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description
        )
    }

    fun onIsDoneChange(isDone: Boolean) {
        _uiState.value = _uiState.value.copy(isDone = isDone)
    }

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

    fun saveTask(taskId: Long?) {
        viewModelScope.launch {
            if (taskId == null)
                tasksRepository.addTask(
                    Task(
                        id = 0,
                        title = uiState.value.title,
                        description = uiState.value.description,
                        isDone = uiState.value.isDone
                    )
                )
            else
                tasksRepository.updateTask(
                    Task(
                        id = taskId.toInt(),
                        title = uiState.value.title,
                        description = uiState.value.description,
                        isDone = uiState.value.isDone
                    )
                )
        }
    }
}


data class EditTaskUiState(
    val title: String = "",
    val description: String = "",
    val isDone: Boolean = false,
    val isLoading: Boolean = false
)
