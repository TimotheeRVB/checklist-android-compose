package com.example.checklist.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checklist.domain.Task
import com.example.checklist.ui.theme.CheckListTheme


@Composable
fun TasksRoute(
    onTaskClick: (Task) -> Unit,
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: TasksViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            snackBarHostState.showSnackbar(message)
        }
    }

    TasksScreen(
        snackBarHostState = snackBarHostState,
        uiState = uiState,
        onTaskClick = onTaskClick,
        onHideCompletedChange = { viewModel.onHideCompletedChange(it) },
        onAddTask = onAddTask,
        onQueryChange = { viewModel.onQueryChange(it) },
        modifier = modifier
    )
}

@Composable
fun TasksScreen(
    snackBarHostState: SnackbarHostState,
    uiState: TasksUiState,
    onTaskClick: (Task) -> Unit,
    onHideCompletedChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {


    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        TasksContent(
            uiState = uiState,
            onHideCompletedChange = onHideCompletedChange,
            onAddTask = onAddTask,
            onQueryChange = onQueryChange,
            onTaskClick = onTaskClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TasksContent(
    uiState: TasksUiState,
    onHideCompletedChange: (Boolean) -> Unit,
    onAddTask: () -> Unit,
    onQueryChange: (String) -> Unit,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Text(
            text = "Mes tâches",
            style = MaterialTheme.typography.headlineMedium
        )
        TopSearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = uiState.query,
            onQueryChange = onQueryChange
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = uiState.hideCompleted,
                onCheckedChange = onHideCompletedChange
            )
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Right,
                text = "Cacher les tâches terminées"
            )
        }
        if (uiState.isLoading) {
            LoadingState()
        } else if (uiState.errorMessage != null) {
            ErrorState()
        } else {
            if (uiState.tasks.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("tasks_list"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.tasks) { task ->
                        TaskRow(
                            task = task,
                            onTaskClick = onTaskClick
                        )
                    }
                }
            }
        }
        Button(
            onClick = onAddTask,
            modifier = Modifier.testTag("add_task_button")
        ) { Text("Ajouter une tâche") }
    }
}

@Composable
fun LoadingState() {
    CircularProgressIndicator(
        modifier = Modifier.testTag("loading_indicator")
    )
}

@Composable
fun ErrorState() {
    Text(
        modifier = Modifier.testTag("error_state"),
        text = "Erreur lors du chargement des tâches",
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun EmptyState() {
    Text(
        modifier = Modifier.testTag("empty_state"),
        text = "Aucune tâche disponible",
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun TaskRow(
    task: Task,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onTaskClick(task)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = if (task.isDone) "Done" else "Open"
        )
    }
}

@Composable
fun TopSearchBar(
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    query: String
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        label = { Text("Rechercher") },
        modifier = modifier
    )
}

@Preview
@Composable
private fun PreviewTasksScreen() {
    CheckListTheme() {
        TasksContent(
            uiState = TasksUiState(
                tasks = listOf(
                    Task(0, "Préparer réunion", "Relire les notes", true),
                    Task(1, "Envoyer mail", "Répondre au client", false),
                    Task(2, "Corriger bug", "Fix crash login", false)
                ),
                isLoading = false,
            ),
            onAddTask = {},
            onQueryChange = {},
            onTaskClick = { _ -> },
            onHideCompletedChange = { }
        )
    }
}

@Preview
@Composable
private fun PreviewTasksScreenError() {
    CheckListTheme() {
        TasksContent(
            uiState = TasksUiState(
                errorMessage = "",
                isLoading = false,
                tasks = emptyList()
            ),
            onAddTask = {},
            onQueryChange = { },
            onTaskClick = { _ -> },
            onHideCompletedChange = { }
        )
    }
}

@Preview
@Composable
private fun PreviewTasksScreenEmpty() {
    CheckListTheme() {
        TasksContent(
            uiState = TasksUiState(
                errorMessage = null,
                isLoading = false,
                tasks = emptyList()
            ),
            onAddTask = {},
            onQueryChange = { },
            onTaskClick = { _ -> },
            onHideCompletedChange = { }
        )
    }
}

