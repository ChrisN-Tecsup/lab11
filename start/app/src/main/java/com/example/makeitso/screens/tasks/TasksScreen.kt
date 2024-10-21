/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.makeitso.screens.tasks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.makeitso.EDIT_TASK_SCREEN
import com.example.makeitso.R.drawable as AppIcon
import com.example.makeitso.R.string as AppText
import com.example.makeitso.common.composable.ActionToolbar
import com.example.makeitso.common.ext.smallSpacer
import com.example.makeitso.common.ext.toolbarActions
import com.example.makeitso.model.Task
import com.example.makeitso.theme.MakeItSoTheme

@Composable
@ExperimentalMaterialApi
fun TasksScreen(
  openScreen: (String) -> Unit,
  viewModel: TasksViewModel = hiltViewModel()
) {
  val tasks by viewModel.tasks.collectAsStateWithLifecycle(emptyList())

  TasksScreenContent(
    onAddClick = { openScreen("add_task") },
    onSettingsClick = { openScreen("settings") },
    onTaskCheckChange = viewModel::onTaskCheckChange,
    onTaskActionClick = { action, task -> viewModel.onTaskActionClick(openScreen, task, action) }
    ,
    openScreen = openScreen,
    tasks = tasks
  )
  Button(onClick = { viewModel.onAddClick(openScreen) }) {
    Text("Agregar Tarea")
  }
  LaunchedEffect(viewModel) {
    viewModel.loadTaskOptions()
  }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun TasksScreenContent(
  modifier: Modifier = Modifier,
  onAddClick: (String) -> Unit,
  onSettingsClick: (String) -> Unit,
  onTaskCheckChange: (Task) -> Unit,
  onTaskActionClick: (String, Task) -> Unit,
  openScreen: (String) -> Unit,
  tasks: List<Task>
) {
  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { onAddClick("add_task") },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = modifier.padding(16.dp)
      ) {
        Icon(Icons.Filled.Add, contentDescription = "Add")
      }
    }
  ) {
    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
      ActionToolbar(
        title = AppText.tasks,
        modifier = Modifier.toolbarActions(),
        endActionIcon = AppIcon.ic_settings,
        endAction = { onSettingsClick("settings") }
      )

      Spacer(modifier = Modifier.smallSpacer())

      LazyColumn {
        items(tasks, key = { it.id }) { taskItem ->
          TaskItem(
            task = taskItem,
            options = listOf(),
            onCheckChange = { onTaskCheckChange(taskItem) },
            onActionClick = { action -> onTaskActionClick(action, taskItem) } // Pasa correctamente action y taskItem
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@ExperimentalMaterialApi
@Composable
fun TasksScreenPreview() {
  MakeItSoTheme {
    TasksScreenContent(
      onAddClick = { },
      onSettingsClick = { },
      onTaskCheckChange = { },
      onTaskActionClick = { _, _ -> },
      openScreen = { },
      tasks = listOf()
    )
  }
  fun onAddClick(openScreen: (String) -> Unit) {
    openScreen(EDIT_TASK_SCREEN) // Navega a la pantalla de edición
  }

}
