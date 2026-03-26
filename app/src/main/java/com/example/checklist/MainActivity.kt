package com.example.checklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.checklist.edittask.EditTaskRoute
import com.example.checklist.taskdetail.TaskDetailRoute
import com.example.checklist.tasks.TasksRoute
import com.example.checklist.ui.theme.CheckListTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CheckListTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "tasks",
                    modifier = Modifier
                ) {
                    composable("tasks") {
                        TasksRoute(
                            onTaskClick = { task ->
                                navController.navigate("task/${task.id}")
                            },
                            onAddTask = {
                                navController.navigate("edit")
                            })
                    }

                    composable(
                        route = "task/{taskId}",
                        arguments = listOf(
                            navArgument("taskId") {
                                type = NavType.LongType
                            }
                        )
                    ) { backStackEntry ->
                        val taskId =
                            backStackEntry.arguments?.getLong("taskId") ?: return@composable

                        TaskDetailRoute(
                            taskId = taskId,
                            onEditClick = {
                                navController.navigate("edit?taskId=$taskId")
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = "edit?taskId={taskId}",
                        arguments = listOf(
                            navArgument("taskId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            }
                        )
                    ) { backStackEntry ->
                        val rawTaskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
                        val taskId = rawTaskId.takeIf { it != -1L }

                        EditTaskRoute(
                            taskId = taskId,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

            }
        }
    }
}