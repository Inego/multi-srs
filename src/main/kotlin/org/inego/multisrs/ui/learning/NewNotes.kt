package org.inego.multisrs.ui.learning

import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import org.inego.multisrs.ui.note.AddNote


@Composable
fun NewNotes(modifier: Modifier) {

    val parentComposition = rememberCompositionContext()

    val addWindowHolder = remember { mutableStateOf<AppWindow?>(null) }

    val onDismissAddWindow = {
        println("On dismiss")
        addWindowHolder.value = null
    }

    val closeAddWindow = {
        println("Close!")
        addWindowHolder.value!!.close()
    }

    Surface(modifier, color = Color.Yellow) {

        Box(Modifier.fillMaxSize()) {
            Text("New notes")
            TextButton(
                modifier = Modifier.align(Alignment.TopEnd),
                enabled = addWindowHolder.value == null,
                onClick = {
                    if (addWindowHolder.value == null) {
                        addWindowHolder.value = AppWindow(
                            onDismissRequest = onDismissAddWindow,
                            title = "Add a note",
                        ).apply {
                            keyboard.setShortcut(Key.Escape) {
                                // Close
                                closeAddWindow()
                            }

                            show(parentComposition) {
                                AddNote()
                            }
                        }
                    }

                }) {
                Text("Add")
            }
        }
    }

}
