package org.inego.multisrs.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun CheckboxWithLabel(text: String, checked: Boolean, setter: (Boolean) -> Unit) {

    Row {
        Checkbox(checked, { setter(it) })
        Text(text, Modifier.clickable { setter(!checked) })
    }
}
