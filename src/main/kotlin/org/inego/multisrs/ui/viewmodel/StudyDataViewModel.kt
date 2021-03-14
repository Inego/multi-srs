package org.inego.multisrs.ui.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.input.key.Key
import org.inego.multisrs.Direction
import org.inego.multisrs.Note
import org.inego.multisrs.StudyData

class StudyDataViewModel(
    var studyData: StudyData,
    val globalKeysPressed: SnapshotStateMap<Key, Boolean>
) {

    val directionsList: List<Direction> = studyData.directionsList

    infix fun press(key: Key) {
        globalKeysPressed[key] = true
    }

    fun ifPressed(key: Key, block: () -> Unit) {
        if (globalKeysPressed[key] == true) {
            globalKeysPressed.remove(key)
            block()
        }
    }

    fun addNote(note: Note) {

    }
}