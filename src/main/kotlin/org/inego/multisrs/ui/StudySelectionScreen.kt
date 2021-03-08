package org.inego.multisrs.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mu.KotlinLogging
import org.inego.multisrs.data.Study
import org.inego.multisrs.utils.selectOpenFilePath
import org.inego.multisrs.utils.selectSaveFilePath
import org.inego.multisrs.utils.workingDir
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

private val logger = KotlinLogging.logger {}


val chooser = JFileChooser().apply {
    isAcceptAllFileFilterUsed = false
    currentDirectory = workingDir().toFile()
    addChoosableFileFilter(FileNameExtensionFilter("MSR files", "msr"))
}


@ExperimentalFoundationApi
@Composable
fun StudySelectionScreen(
    studies: List<Study>,
    onDelete: (Study) -> Unit,
    add: (Study) -> Unit,
    onSelect: (Study) -> Unit
) {

    Column {
        Text("Study Selection Screen")

        Row {

            // NEW button
            OutlinedButton(onClick = {
                val saveFilePath = selectSaveFilePath()
                if (saveFilePath != null) {
                    val filePathStr = saveFilePath.toString()
                    add(Study(filePathStr, filePathStr))
                }
            }) {
                Text("New")
            }

            // OPEN button
            OutlinedButton(onClick = {
                val openFilePath = selectOpenFilePath()
                if (openFilePath != null) {
                    val filePathStr = openFilePath.toString()
                    add(Study(filePathStr, filePathStr))
                }
            }) {
                Text("Open")
            }
        }

        Spacer(Modifier)

        val lazyListState = rememberLazyListState()

        Row(Modifier.fillMaxWidth()) {
            LazyColumn(Modifier.weight(1f), state = lazyListState) {
                items(studies) {
                    StudyRow(it,
                        onDelete = { onDelete(it) },
                        onSelect = { onSelect(it) }
                    )
                }
            }

            VerticalScrollbar(
                rememberScrollbarAdapter(lazyListState, studies.size, STUDY_ROW_HEIGHT),
                Modifier.fillMaxHeight()
            )
        }
    }
}
