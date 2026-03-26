package com.example.checklist.edittask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
fun EditTaskRoute(
    taskId: Long?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: EditTaskViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId)
        }
    }

    EditTaskScreen(
        uiState = uiState,
        taskId = taskId,
        onBackClick = onBackClick,
        onSaveClick = {
            viewModel.saveTask(taskId)
            onBackClick()
        },
        onTitleChange = { viewModel.onTitleChange(it) },
        onDescriptionChange = { viewModel.onDescriptionChange(it) },
        onIsDoneChange = viewModel::onIsDoneChange,
        modifier = modifier
    )
}

@Composable
fun EditTaskScreen(
    uiState: EditTaskUiState,
    modifier: Modifier = Modifier,
    taskId: Long?,
    onSaveClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIsDoneChange: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {


    EditTaskContent(
        modifier = modifier,
        taskId = taskId,
        uiState = uiState,
        onSaveClick = onSaveClick,
        onTitleChange = onTitleChange,
        onDescriptionChange = onDescriptionChange,
        onIsDoneChange = onIsDoneChange,
        onBackClick = onBackClick
    )
}

@Composable
fun EditTaskContent(
    modifier: Modifier = Modifier,
    uiState: EditTaskUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIsDoneChange: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    taskId: Long?,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            if (taskId == null) "Créer une tâche"
            else "Éditer la tâche $taskId",
            style = MaterialTheme.typography.titleLarge
        )
        OutlinedTextField(
            value = uiState.title,
            onValueChange = onTitleChange,
            label = { Text("Titre") }
        )
        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = uiState.isDone,
                onCheckedChange = onIsDoneChange
            )
            Text("Tâche terminée")
        }
        Button(onClick = onSaveClick) {
            Text("Enregistrer")
        }
        Button(onClick = onBackClick) {
            Text("Retour")
        }
    }
}

@Preview
@Composable
private fun PreviewEditTaskScreen() {
    CheckListTheme() {
        EditTaskContent(
            taskId = null,
            onSaveClick = {},
            onBackClick = {},
            onDescriptionChange = {},
            onTitleChange = {},
            onIsDoneChange = {},
            uiState = EditTaskUiState(

            )
        )
    }
}
