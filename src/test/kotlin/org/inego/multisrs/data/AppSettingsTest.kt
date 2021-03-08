package org.inego.multisrs.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.inego.multisrs.ui.state.AppScreen


class AppSettingsTest : FunSpec({

    test("Serialization") {

        val appSettings = AppSettings(
            AppScreen.STUDY_SELECTION,
            listOf(
                Study("path/first.msr", "First"),
                Study("path/second.msr", "Second")
            )
        )

        val format = Json {
            prettyPrint = true

        }

        format.encodeToString(appSettings) shouldBe """
            {
                "screen": "STUDY_SELECTION",
                "studies": [
                    {
                        "fileName": "path/first.msr",
                        "name": "First"
                    },
                    {
                        "fileName": "path/second.msr",
                        "name": "Second"
                    }
                ]
            }
        """.trimIndent()
    }
})
