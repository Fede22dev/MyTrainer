package it.app.mytrainer.models

import java.io.Serializable

data class ObjSchedule(
    var listOfDays: ArrayList<ObjDayOfWo>,
) : Serializable