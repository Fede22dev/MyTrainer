package it.app.mytrainer.firestore

import it.app.mytrainer.utils.CheckRegistrationField

class SaveAthlete() {

    //Creation of the map to save data into firesStore
    companion object {
        private var hashMapAthlete: HashMap<String, Any> = HashMap()
    }

    //Creation of the object to call the method for the check
    private val checkField = CheckRegistrationField()

    //Setting of the email in the hash map
    fun setEmailAthlete(email: String): Boolean {
        val emailOk = checkField.checkName(email)
        return if (emailOk) {
            hashMapAthlete["Email"] = email
            true
        } else {
            false
        }
    }

    //Setting of the password in the hash map
    fun setPassAthlete(pass: String): Boolean {
        val passOk = checkField.checkName(pass)
        return if (passOk) {
            hashMapAthlete["Password"] = pass
            true
        } else {
            false
        }
    }

    //Setting of the name in the hash map
    fun setNameAthlete(name: String): Boolean {
        val nameOk = checkField.checkName(name)
        return if (nameOk) {
            hashMapAthlete["Name"] = name
            true
        } else {
            false
        }
    }

    //Setting of the surname in the hash map
    fun setSurnameAthlete(surname: String): Boolean {
        val surnameOk = checkField.checkName(surname)
        return if (surnameOk) {
            hashMapAthlete["Surname"] = surname
            true
        } else {
            false
        }
    }

    //Setting of the date birth in the hash map
    fun setBirthAthlete(birth: String): Boolean {
        val birthOk = checkField.checkName(birth)
        return if (birthOk) {
            hashMapAthlete["DateOfBirth"] = birth
            true
        } else {
            false
        }
    }

    //Setting of the goals, no control cause the size cannot be 0
    fun setGoals(goals: MutableList<String>) {
        hashMapAthlete["Goals"] = goals
    }

    //Saving on firestore
}