package org.inego.multisrs.ui.learning

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import org.inego.multisrs.ui.viewmodel.StudyDataViewModel


@Composable
fun CommitDialog(viewModel: StudyDataViewModel, close: () -> Unit) {

    Text("Commit dialog")

    if (viewModel.isPressed(Key.Escape)) {
        close()
    }
}