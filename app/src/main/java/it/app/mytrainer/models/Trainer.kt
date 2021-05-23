package it.app.mytrainer.models

import android.util.Log

class Trainer {
    companion object {
        private const val TAG = "HASH MAP TRAINER"
        private var hashMapTrainer = HashMap<String, String>()
        private val arrayKey =
            arrayOf("Email", "Password", "Name", "Surname", "BirthDate", "Specialization")

        //Creation of yhe method to fill the hashmap
        fun putEmail(email: String) {
            hashMapTrainer["Email"] = email
        }

        fun putPass(pass: String) {
            hashMapTrainer["Password"] = pass
        }

        fun putName(name: String) {
            hashMapTrainer["Name"] = name
        }

        fun putSurname(surname: String) {
            hashMapTrainer["Surname"] = surname
        }

        fun putDate(dateOfBirth: String) {
            hashMapTrainer["BirthDate"] = dateOfBirth
        }

        fun putGym(gym: String) {
            hashMapTrainer["Gym"] = gym
        }

        fun putSpec(spec: String) {
            hashMapTrainer["Specialization"] = spec
        }

        //Creation the method to remove the element from the hashmap
        fun removeEmail() {
            hashMapTrainer.remove("Email")
        }

        fun removePass() {
            hashMapTrainer.remove("Password")
        }

        fun removeName() {
            hashMapTrainer.remove("Name")
        }

        fun removeSurname() {
            hashMapTrainer.remove("Surname")
        }

        fun removeDate() {
            hashMapTrainer.remove("BirthDate")
        }

        fun removeGym() {
            hashMapTrainer.remove("Gym")
        }

        fun printHashMap() {
            Log.d(TAG, hashMapTrainer.toString())
        }

        fun clearHashMap() {
            hashMapTrainer.clear()
        }

        //Check if all the required field is fill
        fun hashMapReadyToSave(): Boolean {
            var numFieldRequired = 0
            arrayKey.forEach { str ->
                if (hashMapTrainer.contains(str)) {
                    numFieldRequired++
                }
            }
            return numFieldRequired == 6
        }
    }
}