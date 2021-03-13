package org.inego.multisrs.ui.learning

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.inego.multisrs.StudyData
import org.inego.multisrs.data.Study
import org.inego.multisrs.data.StudyFileResult
import org.inego.multisrs.data.StudyReadFailure
import org.inego.multisrs.data.StudyReadSuccess
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readBytes


@Composable
fun LearningScreen(study: Study, studyFileResult: StudyFileResult, goHome: () -> Unit, openSettings: () -> Unit) {

    val actionsMenuShown = remember { mutableStateOf(false) }

    Column {

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

                val studyData = studyFileResult.studyData

                Text("Study")
                Text(study.fileName)

                Column(Modifier.fillMaxHeight()) {

                    Row(Modifier.fillMaxWidth().weight(1f)) {
                        StudiedNotes(Modifier.weight(1f))
                        NewNotes(Modifier.weight(1f))
                    }

                    SelectedEntriesArea(Modifier)
                }

            }

            is StudyReadFailure -> {
                Text(studyFileResult.exception.message ?: "ERROR", color = MaterialTheme.colors.error)
            }
        }
    }

}
