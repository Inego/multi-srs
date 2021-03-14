package org.inego.multisrs.ui.settings.study

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.inego.multisrs.data.Study
import org.inego.multisrs.data.StudyFileResult
import org.inego.multisrs.data.StudyReadFailure
import org.inego.multisrs.data.StudyReadSuccess


@Composable
fun StudySettingsScreen(study: Study, studyFileResult: StudyFileResult, goBack: () -> Unit) {

    Column {
        TopAppBar(title = {
            Column {
                Text("Settings")
                Text(study.name, fontSize = 12.sp)
            }
        }, navigationIcon = {
            IconButton(onClick = goBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back to learning")
            }
        })

        when (studyFileResult) {
            is StudyReadFailure -> {
                Text(studyFileResult.exception.message ?: "ERROR", color = MaterialTheme.colors.error)
            }
            is StudyReadSuccess -> {
                val studyData = studyFileResult.studyData

                // TODO

                Surface(border = BorderStroke(1.dp, Color.LightGray)) {
                    Column {
                        studyData.directionsList.forEach {
                            Row {
                                Text(it.id.toString(),
                                    modifier = Modifier.requiredWidth(20.dp),
                                    color = Color.DarkGray,
                                    textAlign = TextAlign.End,
                                    fontSize = 8.sp
                                )
                                Text(it.name, Modifier.padding(start = 5.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
