package org.inego.multisrs.ui.learning

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun SelectedEntriesArea(modifier: Modifier) {

    Surface(modifier, color = Color.Magenta) {

        Box() {
            Text("Selected entries")
        }

    }

}