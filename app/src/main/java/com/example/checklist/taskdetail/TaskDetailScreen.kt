package com.example.checklist.taskdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checklist.ui.theme.CheckListTheme

@Composable
fun TaskDetailRoute(
    taskId: Long,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: TaskDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    TaskDetailScreen(
        uiState = uiState,
        onEditClick = onEditClick,
        onBackClick = onBackClick,
        modifier = modifier
    )

}

@Composable
fun TaskDetailScreen(
    uiState: TaskDetailUiState,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TaskDetailContent(
        uiState = uiState,
        onEditClick = onEditClick,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun TaskDetailContent(
    uiState: TaskDetailUiState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = uiState.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Statut",
            style = MaterialTheme.typography.labelLarge
        )
        Text(text = if (uiState.isDone) "Done" else "Open")

        Text(
            text = "Date",
            style = MaterialTheme.typography.labelLarge
        )

        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Modifier")
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retour")
        }
    }
}

@Preview
@Composable
private fun PreviewTaskScreen() {
    CheckListTheme() {
        TaskDetailContent(
            TaskDetailUiState(
                title = "Préparer réunion",
                description = "Relire les notes",
                isDone = true,
            ),
            onEditClick = {},
            onBackClick = {}
        )
    }
}