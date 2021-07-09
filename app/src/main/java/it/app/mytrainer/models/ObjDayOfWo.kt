package it.app.mytrainer.models

import java.io.Serializable

/**
 * Class used to create the objDayOfWO
 */

data class ObjDayOfWo(
    val nameOfDay: String,
    var listOfExercise: ArrayList<ObjExercise?> = ArrayList(12),
) : Serializable