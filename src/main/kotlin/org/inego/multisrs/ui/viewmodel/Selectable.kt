package org.inego.multisrs.ui.viewmodel

import androidx.compose.runtime.mutableStateOf

class Selectable<T>(
    val value: T,
    selected: Boolean
) {
    private val _selected = mutableStateOf(selected)

    var selected: Boolean
        get() = _selected.value
        set(value) {
            _selected.value = value
        }

    fun toggle(): Boolean {
        selected = !selected
        return selected
    }
}
