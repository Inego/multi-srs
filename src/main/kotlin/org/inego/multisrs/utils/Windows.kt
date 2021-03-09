package org.inego.multisrs.utils

import androidx.compose.desktop.AppManager
import org.inego.multisrs.ui.home.chooser
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import kotlin.io.path.extension


fun selectSaveFilePath(): Path? {
    val intResult = chooser.showSaveDialog(focusedWindow)

    if (intResult == JFileChooser.APPROVE_OPTION) {
        var selectedPath = chooser.selectedFile.toPath()

        if (selectedPath.extension != "msr") {
            selectedPath = selectedPath.resolveSibling(selectedPath.fileName.toString() + ".msr")
        }

        return selectedPath
    }

    return null
}


fun selectOpenFilePath(): Path? {
    val saveIntResult = chooser.showOpenDialog(focusedWindow)

    if (saveIntResult == JFileChooser.APPROVE_OPTION) {
        return chooser.selectedFile.toPath()
    }

    return null
}


val focusedWindow = AppManager.focusedWindow?.window

fun showError(title: String, message: String) =
    JOptionPane.showMessageDialog(focusedWindow, message, title, JOptionPane.ERROR_MESSAGE)
