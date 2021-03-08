package org.inego.multisrs.utils

import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolute


fun workingDir() = Paths.get("").absolute()
