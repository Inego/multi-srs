package org.inego.multisrs.ui.learning

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.inego.multisrs.StudyData
import org.inego.multisrs.data.Study
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readBytes


@Composable
fun LearningScreen(study: Study, goHome: () -> Unit) {

    val filePath = Path.of(study.fileName)

    Column {

        TopAppBar(title = {
            Text(study.name)
        }, navigationIcon = {
            IconButton(onClick = goHome) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Select a Study")
            }
        })

        if (filePath.exists()) {

            val studyData = StudyData.parseFrom(filePath.readBytes())

            Text("Study")
            Text(study.fileName)

            Column(Modifier.fillMaxHeight()) {

                Row(Modifier.fillMaxWidth().weight(1f)) {
                    StudiedNotes(Modifier.weight(1f))
                    NewNotes(Modifier.weight(1f))
                }

                SelectedEntriesArea(Modifier)
            }

        } else {
            Text("The file doesn't exist!", color = MaterialTheme.colors.error)
        }
    }

}
