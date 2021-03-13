package org.inego.multisrs.data

import org.inego.multisrs.Direction
import org.inego.multisrs.StudyData
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readBytes


class StudyDataFileHolder {

    private var studyFileResult: StudyFileResult? = null
    private var study: Study? = null

    fun getFileResult(study: Study): StudyFileResult {

        if (this.study != study) {
            this.study = study
            studyFileResult = null
        }

        var studyFileResultCopy = studyFileResult
        if (studyFileResultCopy == null) {
            studyFileResultCopy = readStudyData(study)
            studyFileResult = studyFileResultCopy
        }
        return studyFileResultCopy
    }
}


fun readStudyData(study: Study): StudyFileResult {

    val filePath = Path.of(study.fileName)

    return if (filePath.exists()) {
        try {
            var studyData = StudyData.parseFrom(filePath.readBytes())

            if (studyData.directionsCount == 0) {
                studyData = studyData.toBuilder()
                    .addAllDirections(listOf(
                        Direction.newBuilder().setName("Listen").build(),
                        Direction.newBuilder().setId(1).setName("Speak").build()
                    ))
                    .build()
            }

            StudyReadSuccess(studyData)
        } catch (e: Exception) {
            StudyReadFailure(e)
        }
    } else {
        StudyReadFailure(RuntimeException("File doesn't exist"))
    }
}