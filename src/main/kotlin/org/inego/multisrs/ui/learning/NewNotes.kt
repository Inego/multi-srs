package org.inego.multisrs.ui.learning

import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.inego.multisrs.ui.note.AddNote
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel


@Composable
fun NewNotes(modifier: Modifier, viewModel: StudyDataViewModel) {

    val parentComposition = rememberCompositionContext()

    var addWindowHolder by remember { mutableStateOf<AppWindow?>(null) }

    val directionsEnabled = remember {
        val map = mutableStateMapOf<Int, Boolean>()
        viewModel.directionsList.forEach {
            map[it.id] = true // TODO save these flags in StudyData
        }
        map
    }

    val onDismissAddWindow = {
        addWindowHolder = null
    }

    val closeAddWindow = {
        addWindowHolder!!.close()
    }


    val clickAdd = {
        if (addWindowHolder == null) {
            addWindowHolder = AppWindow(
                onDismissRequest = onDismissAddWindow,
                title = "Add a note",
            ).apply {
                keyboard.setShortcut(Key.Escape) {
                    // Close
                    closeAddWindow()
                }

                keyboard.setShortcut(Key.Enter) {
                    viewModel press Key.Enter
                }

                show(parentComposition) {
                    AddNote(viewModel, directionsEnabled)
                }
            }
        }
    }

    viewModel.ifPressed(Key.A) {
        clickAdd()
    }

    Surface(modifier, color = Color.Yellow) {

        Box(Modifier.fillMaxSize()) {
            Text("New notes")

            TextButton(
                modifier = Modifier.align(Alignment.TopEnd),
                enabled = addWindowHolder == null,
                onClick = clickAdd
            ) {
                Text("Add")
            }
        }
    }

}
