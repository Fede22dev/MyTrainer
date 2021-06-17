package it.app.mytrainer.models

import java.io.Serializable

data class ObjDayOfWo(
    val nameOfDay: String,
    val listOfExercise: ArrayList<ObjExercise?> = ArrayList(12),
) : Serializable