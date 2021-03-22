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
            clearSelectedNotes()
            refreshStudy()
        }

    val directions: List<Direction> = studyData.directionsList

    infix fun press(key: Key) {
        globalKeysPressed[key] = true
    }

    fun isPressed(key: Key): Boolean {
        if (globalKeysPressed[key] == true) {
            globalKeysPressed.remove(key)
            return true
        }
        return false
    }

    fun addNote(note: Note) {
        studyData = studyData.toBuilder().addNotes(0, note).build()
    }

    fun refreshStudy() {
        println("refreshStudy")
        refreshNewNotes()
    }

    private fun clearSelectedNotes() {
        _newNotesView.forEach { it.selected = false }
        _selectedNotesView.clear()
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

    private val _selectedNotesView = mutableStateListOf<Note>()
    val selectedNotesView: List<Note> = _selectedNotesView


    fun toggleNote(note: Note) {

        // Look for it in studied and new notes

        val selectable = _newNotesView.find { it.value == note }
            ?: return

        selectable.toggle()

        if (selectable.selected) {
            _selectedNotesView.add(note)
        } else {
            _selectedNotesView.remove(note)
        }
    }

    val canCommit: Boolean
        get() = _selectedNotesView.isNotEmpty()
}