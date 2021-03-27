package org.inego.multisrs.ui.learning

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import org.inego.multisrs.Note
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel
import java.time.Instant


private val rowHeight = 24.dp


@ExperimentalFoundationApi
@Composable
fun StudiedNotes(modifier: Modifier, viewModel: StudyDataViewModel) {

    Surface(modifier, color = Color.LightGray) {

        Column(Modifier.fillMaxSize()) {

            if (viewModel.studiedNotesView.isEmpty()) {
                Text("Studied notes", fontStyle = FontStyle.Italic)
            }

            Row(Modifier.fillMaxWidth()) {

                val lazyListState = rememberLazyListState()

                val hovered = remember { mutableStateOf<Note?>(null) }

                LazyColumn(Modifier.weight(1f), state = lazyListState) {

                    val nowSeconds = Instant.now().epochSecond

                    items(viewModel.studiedNotesView, key = { it.value.note } ) {
                        Surface(
                            color = if (it.selected) Color.White else Color.Transparent,
                            border = if (hovered.value == it.value.note) BorderStroke(1.dp, Color.Black) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.toggleNote(it.value.note)
                                }.pointerMoveFilter { hovered.value = it.value.note; false }
                        ) {
                            Row {
                                Text(
                                    it.value.note.question,
                                    modifier = Modifier
                                        .requiredHeight(rowHeight).weight(1F),
                                )

                                Text(
                                    (it.value.direction.due - nowSeconds).toString(),
                                    modifier = Modifier.requiredHeight(rowHeight),
                                )
                            }
                        }
                    }
                }

                VerticalScrollbar(
                    rememberScrollbarAdapter(lazyListState, viewModel.studiedNotesView.size, rowHeight),
                    Modifier.fillMaxHeight()
                )
            }
        }
    }
}
