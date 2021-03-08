package org.inego.multisrs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import org.inego.multisrs.data.Study

val STUDY_ROW_HEIGHT = 24.dp

@Composable
fun StudyRow(study: Study, onDelete: () -> Unit, onSelect: () -> Unit) {

    val hovered = remember { mutableStateOf(false) }

    Row(
        Modifier
            .height(STUDY_ROW_HEIGHT)
            .pointerMoveFilter(onEnter = {
                hovered.value = true
                true
            }, onExit = {
                hovered.value = false
                true
            })
            .fillMaxWidth()
            .background(
                if (hovered.value) MaterialTheme.colors.secondary else MaterialTheme.colors.background
            )
    ) {
        Text(study.fileName, Modifier
            .weight(1f)
            .clickable { onSelect() }
        )
        IconButton(onClick = onDelete) {
            if (hovered.value) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
    }

}