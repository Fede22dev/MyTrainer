package it.app.mytrainer.models

import java.io.Serializable

/**
 * Class used to create the object for the
 * searching of the exercises
 */

data class ObjSearchExercise(
    var muscle: String?,
    var nameExercise: String?,
) : Serializable