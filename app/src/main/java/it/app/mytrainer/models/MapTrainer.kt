package it.app.mytrainer.models

import android.util.Log

/**
 * Class used to manipulate the map for trainers
 */

class MapTrainer {

    companion object {

        private const val TAG = "HASH_MAP_TRAINER"
        private var hashMapTrainer = HashMap<String, Any>()
        private val arrayKey =
            arrayOf("Email", "Password", "Name", "Surname", "BirthDate", "Specialization")

        //Creation of the method to fill the hashmap
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

        fun removeGym() {
            hashMapTrainer.remove("Gym")
        }

        //Get
        fun getEmail(): String {
            return hashMapTrainer["Email"].toString()
        }

        fun getPass(): String {
            return hashMapTrainer["Password"].toString()
        }

        fun getHashMap(): HashMap<String, Any> {
            return hashMapTrainer
        }

        //Utilities
        fun printHashMap() {
            Log.d(TAG, hashMapTrainer.toString())
        }

        fun clearHashMap() {
            hashMapTrainer.clear()
        }

        //Check if all the required field has been filled
        fun hashMapReadyToSave(): Boolean {
            var numFieldRequired = 0
            arrayKey.forEach { str ->
                if (hashMapTrainer.contains(str)) {
                    numFieldRequired++
                }
            }
            return numFieldRequired == arrayKey.size
        }
    }
}