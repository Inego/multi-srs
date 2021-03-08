package org.inego.multisrs.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import org.inego.multisrs.data.Study


@Composable
fun LearningScreen(study: Study, goHome: () -> Unit) {

    Column {

        TopAppBar(title = {
            Text(study.name)
        }, navigationIcon = {
            IconButton(onClick = goHome) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Select a Study")
            }
        })


        Text("Study")
        Text(study.fileName)
    }

}