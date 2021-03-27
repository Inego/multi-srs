package org.inego.multisrs.ui.learning

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.inego.multisrs.ui.common.FlowLayout
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel


@Composable
fun SelectedEntriesArea(modifier: Modifier, viewModel: StudyDataViewModel) {

    Surface(modifier, color = Color.Magenta) {

        FlowLayout {

            for (note in viewModel.selectedNotesView) {
                Box(Modifier.padding(2.dp).clickable {
                    viewModel.toggleNote(note)
                }) {
                    Surface(color = Color.White) {
                        Column {
                            Text(note.question)
                            Text(
                                note.questionComment,
                                modifier = Modifier.padding(start = 5.dp),
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}