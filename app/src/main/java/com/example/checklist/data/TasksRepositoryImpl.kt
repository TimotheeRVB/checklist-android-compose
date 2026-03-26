package com.example.checklist.data

import com.example.checklist.data.local.TaskDao
import com.example.checklist.data.local.toDomain
import com.example.checklist.data.local.toEntity
import com.example.checklist.domain.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


interface TasksRepository {
    fun observeTasks(): Flow<List<Task>>
    suspend fun getTaskById(taskId: Int): Task
    suspend fun addTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(task: Task)
}

class TasksRepositoryImpl(
    private val taskDao: TaskDao
) : TasksRepository {

    override fun observeTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(taskId: Int): Task {
        return taskDao.getTask(taskId).toDomain()
    }

    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }
}
