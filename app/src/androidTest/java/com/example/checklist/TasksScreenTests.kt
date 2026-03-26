package com.example.checklist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.checklist.domain.Task
import com.example.checklist.tasks.TasksContent
import com.example.checklist.tasks.TasksUiState
import com.example.checklist.ui.theme.CheckListTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tasks_are_displayed() {
        // Arrange
        val tasks = listOf(
            Task(id = 1, title = "Couper le bois", description = "", isDone = false),
            Task(id = 2, title = "Faire les courses", description = "", isDone = true)
        )

        val uiState = TasksUiState(
            isLoading = false,
            tasks = tasks,
            errorMessage = null,
            query = "",
            hideCompleted = false
        )

        // Act
        composeTestRule.setContent {
            CheckListTheme {
                TasksContent(
                    uiState = uiState,
                    onHideCompletedChange = {},
                    onAddTask = {},
                    onQueryChange = {},
                    onTaskClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Couper le bois")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Faire les courses")
            .assertIsDisplayed()
    }

    @Test
    fun error_is_displayed() {
        // Arrange

        val uiState = TasksUiState(
            isLoading = false,
            tasks = emptyList(),
            errorMessage = "",
            query = "",
            hideCompleted = false
        )

        // Act
        composeTestRule.setContent {
            CheckListTheme {
                TasksContent(
                    uiState = uiState,
                    onHideCompletedChange = {},
                    onAddTask = {},
                    onQueryChange = {},
                    onTaskClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("error_state")
            .assertIsDisplayed()
    }

    @Test
    fun empty_state_is_displayed() {
        // Arrange

        val uiState = TasksUiState(
            isLoading = false,
            tasks = emptyList(),
            errorMessage = null,
            query = "",
            hideCompleted = false
        )

        // Act
        composeTestRule.setContent {
            CheckListTheme {
                TasksContent(
                    uiState = uiState,
                    onHideCompletedChange = {},
                    onAddTask = {},
                    onQueryChange = {},
                    onTaskClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("empty_state").assertIsDisplayed()
    }
}