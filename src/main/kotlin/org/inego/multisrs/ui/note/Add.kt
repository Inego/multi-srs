package org.inego.multisrs.ui.note

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AddNote(tabPressed: MutableState<Boolean>) {

    val (question, setQuestion) = remember { mutableStateOf(TextFieldValue("")) }
    val (comment, setComment) = remember { mutableStateOf(TextFieldValue("")) }

    val frQuestion = remember { FocusRequester() }
    val frComment = remember { FocusRequester() }

    Column {
        TextField(question,
            onValueChange = { setQuestion(it) },
            label = { Text("Question:") },
            modifier = Modifier
                .focusRequester(frQuestion)
                .focusOrder { next = frComment }
                .onPreviewKeyEvent {
                    println("Preview Key Event = $it")
                    false
                }
        )

        TextField(comment,
            onValueChange = { setComment(it) },
            label = { Text("Comment:") },
            modifier = Modifier
                .focusRequester(frComment)
                .focusOrder { next = frQuestion }
        )
    }

    if (tabPressed.value) {
        tabPressed.value = false
        LocalFocusManager.current.moveFocus(FocusDirection.Next)
    }

    DisposableEffect(Unit) {
        frQuestion.requestFocus()

        onDispose {  }
    }


}
