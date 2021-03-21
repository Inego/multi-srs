package org.inego.multisrs.ui.learning

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
                        Text(note.question)
                    }
                }
            }
        }
    }
}