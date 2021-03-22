package org.inego.multisrs.ui.learning

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import org.inego.multisrs.data.Study
import org.inego.multisrs.data.StudyFileResult
import org.inego.multisrs.data.StudyReadFailure
import org.inego.multisrs.data.StudyReadSuccess
import org.inego.multisrs.ui.common.MyToggleButton
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel
import java.io.File
import java.time.Instant


@ExperimentalFoundationApi
@Composable
fun LearningScreen(
    study: Study,
    studyFileResult: StudyFileResult,
    goHome: () -> Unit,
    openSettings: () -> Unit
) {
    val actionsMenuShown = remember { mutableStateOf(false) }
    val keysPressed = remember { mutableStateMapOf<Key, Boolean>() }
    val topFocusRequester = FocusRequester()

    Column(
        Modifier
            .focusRequester(topFocusRequester)
            .focusable()
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    keysPressed[it.key] = true
                }
                true
            }
    ) {

        TopAppBar(title = {
            Text(study.name)
        }, navigationIcon = {
            IconButton(onClick = goHome) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Select a Study")
            }
        }, actions = {
            Box {
                IconButton(onClick = {
                    actionsMenuShown.value = true
                }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Actions")
                }

                DropdownMenu(actionsMenuShown.value, {
                    actionsMenuShown.value = false
                }) {
                    DropdownMenuItem({ openSettings() }) {
                        Text("Settings")
                    }
                }
            }
        })

        when (studyFileResult) {

            is StudyReadSuccess -> {

                val viewModel = remember {
                    StudyDataViewModel(studyFileResult.studyData, keysPressed).apply {
                        refreshStudy()
                    }
                }

                val commitDialogShown = remember { mutableStateOf(false) }

                if (viewModel.isPressed(Key.Spacebar) && !commitDialogShown.value && viewModel.canCommit) {
                    commitDialogShown.value = true
                }

                if (commitDialogShown.value) {
                    CommitDialog(viewModel, close = { commitDialogShown.value = false })
                }
                else Column {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("Study")
                            Text(study.fileName)
                        }
                        TextButton(onClick = {
                            File(study.fileName).writeBytes(viewModel.studyData.toByteArray())
                            println("${Instant.now()} - ${study.fileName} saved successfully.")

                        }, modifier = Modifier.align(Alignment.CenterEnd)) {
                            Text("Save")
                        }
                    }

                    MyToggleButton(viewModel.directions, viewModel.currentDirection, {
                        viewModel.currentDirection = it
                    }) { element, isCurrent ->
                        Text(
                            element.name,
                            color = if (isCurrent) Color.Unspecified else Color.LightGray
                        )
                    }

                    Column(Modifier.fillMaxHeight()) {

                        Row(Modifier.fillMaxWidth().weight(1f)) {
                            StudiedNotes(Modifier.weight(1f))
                            NewNotes(Modifier.weight(1f), viewModel)
                        }

                        SelectedEntriesArea(Modifier, viewModel)
                    }
                }
            }

            is StudyReadFailure -> {
                Text(studyFileResult.exception.message ?: "ERROR", color = MaterialTheme.colors.error)
            }
        }
    }

    DisposableEffect(Unit) {
        topFocusRequester.requestFocus()
        onDispose { }
    }
}
