package org.inego.multisrs.ui.learning

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import org.inego.multisrs.Note
import org.inego.multisrs.NoteDirection
import org.inego.multisrs.data.Outcome
import org.inego.multisrs.data.directionWithId
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel


private val rowHeight = 36.dp

@ExperimentalFoundationApi
@Composable
fun CommitDialog(viewModel: StudyDataViewModel, close: () -> Unit) {

    if (viewModel.isPressed(Key.Escape)) {
        close()
    }

    val commitRows = remember { viewModel.extractCommitRows() }

    val lazyListState = rememberLazyListState()

    Row(Modifier.fillMaxWidth()) {

        LazyColumn(Modifier.weight(1f), state = lazyListState) {
            items(commitRows) {
                CommitRowComposable(it)
            }
        }

        VerticalScrollbar(
            rememberScrollbarAdapter(lazyListState, commitRows.size, rowHeight),
            Modifier.fillMaxHeight()
        )
    }
}

val BUTTON_PADDING = 5.dp
val TEXT_PADDING = 10.dp

@Composable
fun CommitRowComposable(commitRow: CommitRow) {

    Row(modifier = Modifier.requiredHeight(rowHeight).fillMaxWidth()) {
        Text("Easy", Modifier.padding(BUTTON_PADDING))
        Text("Hard", Modifier.padding(BUTTON_PADDING))
        Text(commitRow.note.question, Modifier.weight(1.0F).padding(horizontal = TEXT_PADDING, vertical = BUTTON_PADDING))
        Text(commitRow.span.toString(), Modifier.padding(BUTTON_PADDING))
    }
}


class CommitRow(
    val note: Note,
    val noteDirection: NoteDirection,
    var outcome: Outcome,
    var span: Long
) {
    constructor(
        note: Note,
        noteDirection: NoteDirection,
        outcome: Outcome,
        studyDataViewModel: StudyDataViewModel
    ) : this(
        note, noteDirection, outcome, computeNewSpan(noteDirection, outcome, studyDataViewModel)
    )
}

fun computeNewSpan(noteDirection: NoteDirection, outcome: Outcome, studyDataViewModel: StudyDataViewModel): Long {
    val span = noteDirection.span
    if (span == 0L) {
        return studyDataViewModel.getNewSpan(outcome)
    }
    return (span * (studyDataViewModel.easeFor(outcome).toDouble() / 100)).toLong()
}

fun StudyDataViewModel.extractCommitRows(): List<CommitRow> {

    val directionId = currentDirectionId

    return selectedNotesView.map { note ->
        val noteDirection = note.directionWithId(directionId)
        CommitRow(
            note,
            noteDirection,
            if (noteDirection.due == 0L)
                Outcome.AGAIN
            else Outcome.NORMAL,
            this
        )
    }
}
