package org.inego.multisrs.ui.learning

import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import org.inego.multisrs.Note
import org.inego.multisrs.ui.note.AddNote
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel


private val rowHeight = 40.dp


@ExperimentalFoundationApi
@Composable
fun NewNotes(modifier: Modifier, viewModel: StudyDataViewModel) {

    val parentComposition = rememberCompositionContext()

    var addWindowHolder by remember { mutableStateOf<AppWindow?>(null) }

    val directionsEnabled = remember {
        val map = mutableStateMapOf<Int, Boolean>()
        viewModel.directions.forEach {
            map[it.id] = true // TODO save these flags in StudyData
        }
        map
    }

    val onDismissAddWindow = {
        addWindowHolder = null
        viewModel.refreshStudy(true)
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
                    AddNote(viewModel, directionsEnabled, closeAddWindow)
                }
            }
        }
    }

    if (viewModel.isPressed(Key.A)) {
        clickAdd()
    }

    Surface(modifier, color = Color.Yellow) {

        Box(Modifier.fillMaxSize()) {

            Column {
                if (viewModel.newNotesView.isEmpty()) {
                    Text("New notes", fontStyle = FontStyle.Italic)
                }

                val lazyListState = rememberLazyListState()

                val hovered = remember { mutableStateOf<Note?>(null) }

                Row(Modifier.fillMaxWidth()) {

                    LazyColumn(Modifier.weight(1f), state = lazyListState) {
                        items(viewModel.newNotesView) {
                            Surface(
                                color = if (it.selected) Color.White else Color.Transparent,
                                border = if (hovered.value == it.value.note) BorderStroke(1.dp, Color.Black) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.toggleNote(it.value.note)
                                    }
                                    .pointerMoveFilter {
                                        hovered.value = it.value.note
                                        false
                                    }
                            ) {
                                Column(Modifier.requiredHeight(rowHeight)) {
                                    Text(
                                        it.value.note.question,
                                        modifier = Modifier
                                    )
                                }

                            }
                        }
                    }

                    VerticalScrollbar(
                        rememberScrollbarAdapter(lazyListState, viewModel.newNotesView.size, rowHeight),
                        Modifier.fillMaxHeight()
                    )
                }
            }

            Surface(
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                TextButton(
                    enabled = addWindowHolder == null,
                    onClick = clickAdd
                ) {
                    Text("Add")
                }
            }
        }
    }
}
