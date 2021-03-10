package org.inego.multisrs.ui.learning

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun StudiedNotes(modifier: Modifier) {

    Surface(modifier, color = Color.LightGray) {

        Box(Modifier.fillMaxSize()) {
            Text("Studied notes")
        }
    }
}
