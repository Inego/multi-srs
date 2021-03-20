package org.inego.multisrs.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.input.key.Key
import org.inego.multisrs.Direction
import org.inego.multisrs.Note
import org.inego.multisrs.StudyData
import org.inego.multisrs.data.directionWithId

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
            refreshStudy()
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
        println("refreshStudy")
        refreshNewNotes()
    }

    private fun refreshNewNotes() {

        val selected = _newNotesView
            .filter { it.selected }
            .map { it.value }
            .toSet()

        val directionId = _directionId.value

        _newNotesView.clear()

        studyData.notesList.filter {
            val noteDirection = it.directionWithId(directionId)
            noteDirection.enabled && noteDirection.span == 0L
        }.mapTo(_newNotesView) {
            Selectable(it, selected.contains(it))
        }
    }
}