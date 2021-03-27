package org.inego.multisrs.data

import org.inego.multisrs.Note
import org.inego.multisrs.NoteDirection


data class NoteWithDirection(
    val note: Note,
    val direction: NoteDirection
)
