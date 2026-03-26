package com.example.checklist

import MainDispatcherTest
import com.example.checklist.FakeUserPreferencesRepository
import com.example.checklist.domain.Task
import com.example.checklist.tasks.TasksViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test


class TasksViewModelTest {
    @get:Rule
    val mainDispatcherTest = MainDispatcherTest()

    @Test
    fun `when repository returns tasks uiState contains tasks`() = runTest {

        // Arrange
        val tasks = listOf(
            Task(id = 1, title = "Task 1", description = "", isDone = false),
            Task(id = 2, title = "Task 2", description = "", isDone = true)
        )

        val fakeTasksRepository = FakeTasksRepository(tasks)
        val fakeUserPreferencesRepository = FakeUserPreferencesRepository(false)

        val viewModel = TasksViewModel(
            repository = fakeTasksRepository,
            userPreferencesRepository = fakeUserPreferencesRepository
        )

        // Act
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(tasks, state.tasks)
        assertEquals(false, state.hideCompleted)
        assertNull(state.errorMessage)
    }

    @Test
    fun `when hide completed is true completed tasks are filtered out`() = runTest {
        // Arrange
        val tasks = listOf(
            Task(id = 1, title = "Task 1", description = "", isDone = false),
            Task(id = 2, title = "Task 2", description = "", isDone = true)
        )

        val fakeTasksRepository = FakeTasksRepository(tasks)
        val fakeUserPreferencesRepository = FakeUserPreferencesRepository(true)

        val viewModel = TasksViewModel(
            repository = fakeTasksRepository,
            userPreferencesRepository = fakeUserPreferencesRepository
        )

        // Act
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(listOf(tasks[0]), state.tasks)
    }

    @Test
    fun `on query change results are filtered`() = runTest {
        // Arrange
        val tasks = listOf(
            Task(id = 1, title = "Couper le bois", description = "", isDone = false),
            Task(id = 2, title = "Faire les courses", description = "", isDone = true)
        )

        val fakeTasksRepository = FakeTasksRepository(tasks)
        val fakeUserPreferencesRepository = FakeUserPreferencesRepository(false)

        val viewModel = TasksViewModel(
            repository = fakeTasksRepository,
            userPreferencesRepository = fakeUserPreferencesRepository
        )

        // Act
        advanceUntilIdle()
        viewModel.onQueryChange("faire")

        val state = viewModel.uiState.value
        assertEquals(listOf(tasks[1]), state.tasks)
    }
}