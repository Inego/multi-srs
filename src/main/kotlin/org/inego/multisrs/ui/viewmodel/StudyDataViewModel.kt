package org.inego.multisrs.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.input.key.Key
import org.inego.multisrs.Direction
import org.inego.multisrs.Note
import org.inego.multisrs.StudyData
import org.inego.multisrs.data.NoteWithDirection
import org.inego.multisrs.data.Outcome
import org.inego.multisrs.data.directionWithId
import org.inego.multisrs.ui.learning.CommitRow
import java.time.Instant


class StudyDataViewModel(
    var studyData: StudyData,
    private val globalKeysPressed: SnapshotStateMap<Key, Boolean>
) {
    private var _newNotesView = mutableStateOf(listOf<Selectable<NoteWithDirection>>())
    val newNotesView: List<Selectable<NoteWithDirection>>
        get() = _newNotesView.value

    private val _studiedNotesView = mutableStateOf(listOf<Selectable<NoteWithDirection>>())
    val studiedNotesView: List<Selectable<NoteWithDirection>>
        get() = _studiedNotesView.value

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

    val currentDirectionId: Int
        get() = _directionId.value

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

    fun refreshStudy(keepSelected: Boolean = false) {
        if (!keepSelected) {
            _selectedNotesView.value = listOf()
        }
        refreshStudiedNotes(keepSelected)
        refreshNewNotes(keepSelected)
    }


    private fun refreshNewNotes(keepSelected: Boolean = false) {

        val selected = selectedSet(keepSelected, _newNotesView.value)

        val directionId = _directionId.value

        val newList = studyData.notesList
            .map {
                val noteDirection = it.directionWithId(directionId)
                NoteWithDirection(it, noteDirection)
            }
            .filter {
                it.direction.enabled && it.direction.due == 0L
            }.map {
                Selectable(it, selected.contains(it.note))
            }

        _newNotesView.value = newList
    }

    private fun selectedSet(keepSelected: Boolean, view: List<Selectable<NoteWithDirection>>) =
        if (keepSelected)
            view
                .filter { it.selected }
                .map { it.value.note }
                .toSet()
        else setOf()


    private fun refreshStudiedNotes(keepSelected: Boolean = false) {

        val selected = selectedSet(keepSelected, _studiedNotesView.value)

        val directionId = _directionId.value

        _studiedNotesView.value = studyData.notesList
            .map { NoteWithDirection(it, it.directionWithId(directionId)) }
            .filter {
                it.direction.enabled && it.direction.due > 0
            }
            .sortedBy { it.direction.due }
            .map {
                Selectable(it, selected.contains(it.note))
            }
    }

    private val _selectedNotesView = mutableStateOf(listOf<Note>())
    val selectedNotesView: List<Note> = _selectedNotesView.value


    fun toggleNote(note: Note) {

        // Look for it in studied and new notes

        val selectable = _newNotesView.value.find { it.value.note == note }
            ?: _studiedNotesView.value.find { it.value.note == note }
            ?: return

        selectable.toggle()

        if (selectable.selected) {
            _selectedNotesView.value = _selectedNotesView.value.toMutableList().also { it.add(note) }
        } else {
            _selectedNotesView.value = _selectedNotesView.value.toMutableList().also { it.remove(note) }
        }
    }

    val canCommit: Boolean
        get() = _selectedNotesView.value.isNotEmpty()


    /**
     * Ease for the specified outcome (percent from current span)
     */
    fun easeFor(outcome: Outcome): Int {
        // TODO store in study data (settings block)
        return when (outcome) {
            Outcome.AGAIN -> 0
            Outcome.HARD -> 130
            Outcome.NORMAL -> 150
            Outcome.EASY -> 200
        }
    }

    fun getNewSpan(outcome: Outcome): Long {
        // TODO from study data settings
        return when (outcome) {
            Outcome.AGAIN -> 0
            Outcome.HARD -> 30
            Outcome.NORMAL -> 300
            Outcome.EASY -> 2000
        }
    }

    fun clearPressed() {
        globalKeysPressed.clear()
    }


    fun commit(rows: List<CommitRow>) {

        val now = Instant.now()
        val nowEpochSecond = now.epochSecond

        val committedNotes = rows.associateBy { it.note }

        val studyDataBuilder = studyData.toBuilder()

        // TODO can be optimized (keep note index in commit row?)

        for (i in 0 until studyData.notesCount) {

            val note = studyData.getNotes(i)
            val commitRow = committedNotes[note]
            if (commitRow != null) {

                val committedDirection = commitRow.noteDirection

                val noteCopy = note.toBuilder()

                for (directionBuilder in noteCopy.directionsBuilderList) {
                    if (directionBuilder.directionId == committedDirection.directionId) {
                        val span = commitRow.span.value
                        directionBuilder.span = span
                        directionBuilder.due = nowEpochSecond + span
                        break
                    }
                }

                studyDataBuilder.setNotes(i, noteCopy)
            }
        }

        studyData = studyDataBuilder.build()

        refreshStudy()
    }
}
