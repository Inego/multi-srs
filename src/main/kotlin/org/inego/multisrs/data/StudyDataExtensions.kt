package org.inego.multisrs.data

import org.inego.multisrs.Note
import org.inego.multisrs.NoteDirection

fun Note.directionWithId(id: Int): NoteDirection =
    directionsList.first { it.directionId == id }
