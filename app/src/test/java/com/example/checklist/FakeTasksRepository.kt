package com.example.checklist

import com.example.checklist.data.TasksRepository
import com.example.checklist.domain.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTasksRepository(
    initialTasks: List<Task> = emptyList()
) : TasksRepository {

    private val tasksFlow = MutableStateFlow(initialTasks)

    override fun observeTasks(): Flow<List<Task>> {
        return tasksFlow
    }

    override suspend fun getTaskById(taskId: Int): Task {
        return tasksFlow.value.first{ it.id == taskId}
    }

    override suspend fun addTask(task: Task) {
        tasksFlow.value += task
    }

    override suspend fun deleteTask(task: Task) {
        tasksFlow.value -= task
    }

    override suspend fun updateTask(task: Task) {
        tasksFlow.value = tasksFlow.value.map {
            if (it.id == task.id) task else it
        }
    }
}