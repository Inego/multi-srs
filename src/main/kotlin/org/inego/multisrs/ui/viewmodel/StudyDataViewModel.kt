package org.inego.multisrs.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.input.key.Key
import org.inego.multisrs.Direction
import org.inego.multisrs.Note
import org.inego.multisrs.StudyData

class StudyDataViewModel(
    var studyData: StudyData,
    private val globalKeysPressed: SnapshotStateMap<Key, Boolean>
) {
    private val _newNotesView = mutableStateListOf<Selectable<Note>>()
    val newNotesView: List<Selectable<Note>> = _newNotesView

    private val _directionId = mutableStateOf(1) // TODO store/retrieve in StudyData
    var currentDirection: Direction
        get() {
            val lookingFor = _directionId.value
            return studyData.directionsList.find { it.id == lookingFor } ?: throw IllegalStateException()
        }
        set(value) {
            _directionId.value = value.id
        }

    val directions: List<Direction> = studyData.directionsList

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
        studyData = studyData.toBuilder().addNotes(note).build()
    }

    fun refreshStudy() {


    }
}