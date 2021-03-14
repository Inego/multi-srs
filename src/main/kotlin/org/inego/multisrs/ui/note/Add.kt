package org.inego.multisrs.ui.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import org.inego.multisrs.Direction
import org.inego.multisrs.ui.common.CheckboxWithLabel
import org.inego.multisrs.ui.focus.FocusUtil
import org.inego.multisrs.ui.focus.withFocus


@Composable
fun AddNote(directions: List<Direction>, directionsEnabled: SnapshotStateMap<Int, Boolean>) {

    val (question, setQuestion) = remember { mutableStateOf(TextFieldValue("")) }
    val (comment, setComment) = remember { mutableStateOf(TextFieldValue("")) }

    val focusUtil = FocusUtil()



    Column {

        Row {
            directions.forEach {
                val checked = directionsEnabled[it.id]!!
                CheckboxWithLabel(it.name, checked) { b -> directionsEnabled[it.id] = b }
            }
        }

        TextField(question,
            onValueChange = { setQuestion(it) },
            label = { Text("Question:") },
            modifier = Modifier.withFocus(focusUtil, 1),
            singleLine = true
        )

        TextField(comment,
            onValueChange = { setComment(it) },
            label = { Text("Comment:") },
            modifier = Modifier.withFocus(focusUtil, 2),
            singleLine = true
        )

        TextButton({}) { Text("Add") }
    }

    DisposableEffect(Unit) {
        focusUtil.moveNext(null)
        onDispose {  }
    }
}
