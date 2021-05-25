package it.app.mytrainer.models

import android.util.Log

class Athlete {
    companion object {
        private const val TAG = "HASH MAP ATHLETE"
        private var hashMapAthlete = HashMap<String, Any>()
        private val arrayKey =
            arrayOf(
                "Email",
                "Password",
                "Name",
                "Surname",
                "BirthDate",
                "Height",
                "Weight",
                "TypeOfWO",
                "Goal",
                "Level",
                "DaysOfWorkout",
                "Equipment"
            )

        //Creation of the method to fill the hashmap (First Fragment)
        fun putEmail(email: String) {
            hashMapAthlete["Email"] = email
        }

        fun putPass(pass: String) {
            hashMapAthlete["Password"] = pass
        }

        fun putName(name: String) {
            hashMapAthlete["Name"] = name
        }

        fun putSurname(surname: String) {
            hashMapAthlete["Surname"] = surname
        }

        fun putDate(dateOfBirth: String) {
            hashMapAthlete["BirthDate"] = dateOfBirth
        }

        //Creation of the method to fill the hashmap (Second Fragment)
        fun putHeight(height: String) {
            hashMapAthlete["Height"] = height
        }

        fun putWeight(weight: String) {
            hashMapAthlete["Weight"] = weight
        }

        fun putTypeOfWO(type: String) {
            hashMapAthlete["TypeOfWO"] = type
        }

        fun putGoal(goal: String) {
            hashMapAthlete["Goal"] = goal
        }

        //Creation of the method to fill the hashmap (Third Fragment)
        fun putLevel(level: String) {
            hashMapAthlete["Level"] = level
        }

        fun putNumberOfWOWeek(days: String) {
            hashMapAthlete["DaysOfWorkout"] = days
        }

        fun putEquipment(list: MutableList<String>) {
            hashMapAthlete["Equipment"] = list
        }

        //Creation the method to remove the element from the hashmap (First Fragment)
        fun removeEmail() {
            hashMapAthlete.remove("Email")
        }

        fun removePass() {
            hashMapAthlete.remove("Password")
        }

        fun removeName() {
            hashMapAthlete.remove("Name")
        }

        fun removeSurname() {
            hashMapAthlete.remove("Surname")
        }

        fun removeDate() {
            hashMapAthlete.remove("BirthDate")
        }

        //Creation the method to remove the element from the hashmap (Second Fragment)
        fun removeHeight() {
            hashMapAthlete.remove("Height")
        }

        fun removeWeight() {
            hashMapAthlete.remove("Weight")
        }

        fun removeEquipment() {
            hashMapAthlete.remove("Equipment")
        }

        //Get
        fun getEmail(): String {
            return hashMapAthlete["Email"].toString()
        }

        fun getPass(): String {
            return hashMapAthlete["Password"].toString()
        }

        //Utilities
        fun printHashMap() {
            Log.d(TAG, hashMapAthlete.toString())
        }

        fun clearHashMap() {
            hashMapAthlete.clear()
        }

        //Check if all the required field is fill
        fun hashMapReadyToSave(): Boolean {
            var numFieldRequired = 0
            arrayKey.forEach { str ->
                if (hashMapAthlete.contains(str)) {
                    numFieldRequired++
                }
            }
            return numFieldRequired == 12
        }
    }
}
