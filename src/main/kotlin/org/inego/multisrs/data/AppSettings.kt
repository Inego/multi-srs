package org.inego.multisrs.data

import kotlinx.serialization.Serializable
import org.inego.multisrs.ui.state.AppScreen

@Serializable
data class AppSettings(
    val screen: AppScreen,
    val studies: List<Study>
) {
    companion object {
        fun empty() = AppSettings(AppScreen.STUDY_SELECTION, listOf())
    }
}


@Serializable
data class Study(
    val fileName: String,
    val name: String
)
