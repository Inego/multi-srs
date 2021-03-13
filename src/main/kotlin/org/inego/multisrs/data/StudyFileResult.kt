package org.inego.multisrs.data

import org.inego.multisrs.StudyData
import java.lang.RuntimeException


sealed class StudyFileResult


class StudyReadSuccess(
    val studyData: StudyData
) : StudyFileResult()


class StudyReadFailure(
    val exception: Exception
) : StudyFileResult()
