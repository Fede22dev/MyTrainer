package it.app.mytrainer.models

import java.io.Serializable

/**
 * Class used to create the object used to fill
 * the objDayOfWO
 */

data class ObjExercise(
    var nameExercise: String?,
    var numSeries: String?,
    var numReps: String?,
    var recovery: Int?,
) : Serializable