package it.app.mytrainer.models

import java.io.Serializable

data class ObjExercise(
    var nameExercise: String?,
    var numSeries: String?,
    var numReps: String?,
    var recovery: Int?,
) : Serializable