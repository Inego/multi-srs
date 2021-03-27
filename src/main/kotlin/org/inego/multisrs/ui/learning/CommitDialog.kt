package org.inego.multisrs.ui.learning

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.inego.multisrs.Note
import org.inego.multisrs.NoteDirection
import org.inego.multisrs.data.Outcome
import org.inego.multisrs.data.directionWithId
import org.inego.multisrs.ui.common.Orange
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel

private val TEXT_PADDING = 10.dp
private val ROW_HEIGHT = 42.dp

val hoverBorder = BorderStroke(1.dp, Color.Black)

@ExperimentalFoundationApi
@Composable
fun CommitDialog(viewModel: StudyDataViewModel, close: () -> Unit) {

    remember { viewModel.clearPressed() }

    if (viewModel.isPressed(Key.Escape)) {
        close()
    }

    val commitRows = remember { viewModel.extractCommitRows() }

    val commitAndClose = {
        viewModel.commit(commitRows)
        close()
    }

    if (viewModel.isPressed(Key.Enter)) {
        commitAndClose()
    }

    val lazyListState = rememberLazyListState()

    Box {

        Row(Modifier.fillMaxWidth()) {

            val hovered = remember { mutableStateOf<CommitRow?>(null) }

            LazyColumn(Modifier.weight(1f), state = lazyListState) {
                items(commitRows) { commitRow ->

                    Surface(
                        modifier = Modifier.pointerMoveFilter {
                            hovered.value = commitRow
                            false
                        },
                        border = if (commitRow == hovered.value) hoverBorder else null,
                    ) {
                        CommitRowComposable(commitRow) { outcome ->
                            commitRow.setOutcome(outcome, viewModel)
                        }
                    }

                }
            }

            VerticalScrollbar(
                rememberScrollbarAdapter(lazyListState, commitRows.size, ROW_HEIGHT),
                Modifier.fillMaxHeight()
            )
        }

        TextButton(
            onClick = { commitAndClose() },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("OK")
        }
    }


}


@Composable
fun CommitRowComposable(commitRow: CommitRow, changeOutcome: (Outcome) -> Unit) {

    Row(
        modifier = Modifier
            .requiredHeight(ROW_HEIGHT)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val outcome = commitRow.outcome.value

        Surface(
            color = if (outcome == Outcome.EASY) Color.Green else Color.Transparent
        ) {
            Box(
                Modifier
                    .requiredHeight(ROW_HEIGHT)
                    .clickable {
                        changeOutcome(Outcome.EASY)
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text("Easy", Modifier.padding(horizontal = TEXT_PADDING))
            }
        }

        Surface(
            color = if (outcome == Outcome.HARD) Orange else Color.Transparent
        ) {
            Box(
                Modifier
                    .requiredHeight(ROW_HEIGHT)
                    .clickable {
                        changeOutcome(Outcome.HARD)
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    "Hard", Modifier
                        .padding(horizontal = TEXT_PADDING)
                )
            }
        }

        Box(
            Modifier
                .weight(1.0F)
                .requiredHeight(ROW_HEIGHT)
                .clickable {
                    changeOutcome(if (outcome == Outcome.NORMAL) Outcome.AGAIN else Outcome.NORMAL)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    commitRow.note.question,
                    color = when (outcome) {
                        Outcome.AGAIN -> Color.Red
                        Outcome.HARD -> Orange
                        Outcome.NORMAL -> Color.Black
                        Outcome.EASY -> Color.Green
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = TEXT_PADDING)
                )
                Text(
                    commitRow.note.questionComment,
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        Text(
            commitRow.span.value.toString(),
            Modifier.padding(horizontal = TEXT_PADDING)
        )
    }
}


class CommitRow(
    val note: Note,
    val noteDirection: NoteDirection,
    outcome: Outcome,
    span: Long
) {
    constructor(
        note: Note,
        noteDirection: NoteDirection,
        outcome: Outcome,
        studyDataViewModel: StudyDataViewModel
    ) : this(
        note, noteDirection, outcome, computeNewSpan(noteDirection, outcome, studyDataViewModel)
    )

    val outcome = mutableStateOf(outcome)
    val span = mutableStateOf(span)

    fun setOutcome(outcome: Outcome, studyDataViewModel: StudyDataViewModel) {
        this.outcome.value = outcome
        span.value = computeNewSpan(noteDirection, outcome, studyDataViewModel)
    }
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
