package org.inego.multisrs.ui.note

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import org.inego.multisrs.ui.focus.FocusUtil
import org.inego.multisrs.ui.focus.withFocus


@Composable
fun AddNote() {

    val (question, setQuestion) = remember { mutableStateOf(TextFieldValue("")) }
    val (comment, setComment) = remember { mutableStateOf(TextFieldValue("")) }
    val (comment2, setComment2) = remember { mutableStateOf(TextFieldValue("")) }

    val focusUtil = FocusUtil()

    Column {
        TextField(question,
            onValueChange = { setQuestion(it) },
            label = { Text("Question:") },
            modifier = Modifier.withFocus(focusUtil, 1)
        )

        TextField(comment,
            onValueChange = { setComment(it) },
            label = { Text("Comment:") },
            modifier = Modifier.withFocus(focusUtil, 2)
        )

        TextField(comment2,
            onValueChange = { setComment2(it) },
            label = { Text("Comment 2:") },
            modifier = Modifier.withFocus(focusUtil, 3)
        )
    }

    DisposableEffect(Unit) {
        focusUtil.moveNext(null)
        onDispose {  }
    }
}