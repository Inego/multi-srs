package org.inego.multisrs

import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.inego.multisrs.data.*
import org.inego.multisrs.ui.learning.LearningScreen
import org.inego.multisrs.ui.home.StudySelectionScreen
import org.inego.multisrs.ui.settings.study.StudySettingsScreen
import org.inego.multisrs.ui.state.AppScreen.*
import org.inego.multisrs.utils.workingDir
import java.lang.IllegalStateException
import java.nio.file.Path
import kotlin.io.path.*


private val logger = KotlinLogging.logger {}


val startDir = workingDir()

val settingsPath: Path = startDir.resolve("settings.json")

val appSettingsOnStart = readAppSettings()

val msAppSettings = mutableStateOf(appSettingsOnStart)

fun setAppSettings(appSettings: AppSettings) {
    msAppSettings.value = appSettings
    saveAppSettings(appSettings)
}

fun saveAppSettings(appSettings: AppSettings) {
    val appSettingsJsonString = Json.encodeToString(appSettings)
    settingsPath.writeText(appSettingsJsonString)
}

val studyDataFileHolder = StudyDataFileHolder()


@ExperimentalFoundationApi
@ExperimentalPathApi
fun main() {

    Window(title = "multi-srs") {

        val appSettings = msAppSettings.value

        when (appSettings.screen) {
            STUDY_SELECTION -> StudySelectionScreen(appSettings.studies, onDelete = {
                val withRemoved = appSettings.studies.toMutableList().apply { remove(it) }
                setAppSettings(appSettings.copy(studies = withRemoved))
            }, add = {
                val withAdded = appSettings.studies.toMutableList().apply { add(it) }
                setAppSettings(appSettings.copy(studies = withAdded))
            }, onSelect = {
                selectStudy(it)
            })

            LEARNING -> {
                val study = appSettings.studies[0]
                val fileResult = studyDataFileHolder.getFileResult(study)
                LearningScreen(study, fileResult, goHome = {
                    setAppSettings(appSettings.copy(screen = STUDY_SELECTION))
                }) {
                    setAppSettings(appSettings.copy(screen = STUDY_SETTINGS))
                }
            }
            BROWSING -> TODO()
            STUDY_SETTINGS -> {
                val study = appSettings.studies[0]
                val fileResult = studyDataFileHolder.getFileResult(study)

                StudySettingsScreen(study, fileResult, goBack = {
                    setAppSettings(appSettings.copy(screen = LEARNING))
                })
            }
            else -> throw IllegalStateException("Whoooops")
        }
    }
}







fun selectStudy(study: Study) {
    // If this study is not at the top of the list, move it to the top, and then proceed to study

    var studies = msAppSettings.value.studies

    if (studies[0] != study) {
        studies = studies.toMutableList()
        studies.remove(study)
        studies.add(0, study)
    }

    setAppSettings(AppSettings(LEARNING, studies))
}


private fun readAppSettings(): AppSettings {

    return if (settingsPath.exists()) {
        val settingsJson = settingsPath.readText()
        try {
            Json.decodeFromString(settingsJson)
        } catch (e: SerializationException) {
            logger.error(e) { "Exception while deserializing program settings" }
            AppSettings.empty()
        }
    } else {
        AppSettings.empty()
    }
}

