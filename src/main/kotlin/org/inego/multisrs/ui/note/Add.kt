package org.inego.multisrs.ui.note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.protobuf.Timestamp
import org.inego.multisrs.Note
import org.inego.multisrs.NoteDirection
import org.inego.multisrs.ui.common.CheckboxWithLabel
import org.inego.multisrs.ui.focus.FocusUtil
import org.inego.multisrs.ui.focus.withFocus
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel
import java.time.Instant


val addedRowHeight = 16.dp


@ExperimentalFoundationApi
@Composable
fun AddNote(
    viewModel: StudyDataViewModel,
    directionsEnabled: SnapshotStateMap<Int, Boolean>,
    closeAddWindow: () -> Unit
) {
    var addedCount by remember { mutableStateOf(1) }

    var question by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }

    val addedNotes = remember { mutableStateOf(listOf<Note>()) }

    val focusUtil = remember { FocusUtil() }

    fun checkCanAdd() = question.text.isNotEmpty() && directionsEnabled.any { it.value }


    fun handleAdd() {

        val now = Instant.now()
        val nowTimestamp = Timestamp.newBuilder()
            .setSeconds(now.epochSecond)
            .setNanos(now.nano)

        val builder = Note.newBuilder()
            .setQuestion(question.text)
            .setQuestionComment(comment.text)
            .setAdded(nowTimestamp)

        directionsEnabled.forEach { (idx, enabled) ->
            builder.addDirections(NoteDirection.newBuilder()
                .setDirectionId(idx)
                .setEnabled(enabled)
            )
        }

        val note = builder.build()

        viewModel.addNote(note)

        addedNotes.value = addedNotes.value.toMutableList().also { it.add(0, note) }

        question = TextFieldValue("")
        comment = TextFieldValue("")

        addedCount += 1
    }

    if (viewModel.isPressed(Key.Enter)) {
        if (checkCanAdd()) {
            handleAdd()
        }
    }

    focusUtil.startRecomposition()

    Row {

        Column {

            Row {
                viewModel.directions.forEach {
                    val checked = directionsEnabled[it.id]!!
                    CheckboxWithLabel(it.name, checked) { b -> directionsEnabled[it.id] = b }
                }
            }

            TextField(question,
                onValueChange = { question = it },
                label = { Text("Question:") },
                modifier = Modifier.withFocus(focusUtil, 1),
                singleLine = true
            )

            TextField(comment,
                onValueChange = { comment = it },
                label = { Text("Comment:") },
                modifier = Modifier.withFocus(focusUtil, 2),
                singleLine = true
            )

            Row {
                TextButton(::handleAdd, enabled = checkCanAdd()) { Text("Add") }
                TextButton(closeAddWindow) { Text("Close") }
            }
        }

        val addedListState = rememberLazyListState()

        Column {
            Text("Added notes:")

            Row(Modifier.fillMaxWidth()) {

                LazyColumn(Modifier.weight(1f), state = addedListState) {
                    items(addedNotes.value) {
                        Text(it.question, modifier = Modifier.requiredHeight(addedRowHeight))
                    }
                }

                VerticalScrollbar(
                    rememberScrollbarAdapter(addedListState, addedNotes.value.size, addedRowHeight),
                    Modifier.fillMaxHeight()
                )
            }
        }

    }

    DisposableEffect(addedCount) {
        focusUtil.focusFirst()
        onDispose {  }
    }
}


