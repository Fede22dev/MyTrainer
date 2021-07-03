package it.app.mytrainer.models

import java.io.Serializable

/**
 * Class used to create the obj athlete, used
 * to add athlete to fireStore
 */

data class ObjAthlete(
    val nameAthlete: String,
    val surnameAthlete: String,
    val dateOfBirth: String,
    val height: String,
    val weight: String,
    val typeOfWO: String,
    val goal: String,
    val level: String,
    val daysOfWO: String,
    val equipment: ArrayList<String>,
    val idAthlete: String,
    val idTrainer: String,
    val urlPhotoAthlete: String,
) : Serializable