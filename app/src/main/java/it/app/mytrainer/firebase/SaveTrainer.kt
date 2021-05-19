package it.app.mytrainer.firebase


/*class SaveTrainer() {
    //Creation of the map to save data into firesStore
    companion object {
        private var hashMapTrainer: HashMap<String, Any> = HashMap()
    }

    //Creation of the object to call the method for the check
    private val checkField = CheckRegistrationFieldUser()

    //Setting of the email in the hash map
    fun setEmailTrainer(name: String): Boolean {
        val nameOk = checkField.checkName(name)
        return if (nameOk) {
            hashMapTrainer["Email"] = name
            true
        } else {
            false
        }
    }

    //Setting of the password in the hash map
    fun setPassTrainer(pass: String): Boolean {
        val nameOk = checkField.checkName(pass)
        return if (nameOk) {
            hashMapTrainer["Password"] = pass
            true
        } else {
            false
        }
    }

    //Setting of the name in the hash map
    fun setNameTrainer(name: String): Boolean {
        val nameOk = checkField.checkName(name)
        return if (nameOk) {
            hashMapTrainer["Name"] = name
            true
        } else {
            false
        }
    }

    //Setting of the surname in the hash map
    fun setSurnameTrainer(surname: String): Boolean {
        val nameOk = checkField.checkName(surname)
        return if (nameOk) {
            hashMapTrainer["Surname"] = surname
            true
        } else {
            false
        }
    }

    //Setting of the date birth in the hash map
    fun setBirthTrainer(birth: String): Boolean {
        val nameOk = checkField.checkName(birth)
        return if (nameOk) {
            hashMapTrainer["DateOfBirth"] = birth
            true
        } else {
            false
        }
    }

    //Setting of the spec, no control cause the size cannot be 0
    fun setSpec(spec: MutableList<String>) {
        hashMapTrainer["Specialist"] = spec
    }

    //Setting of the gym, no control cause it could be blank
    fun setGym(gym: String) {
        if (gym != "") {
            hashMapTrainer["Gym"] = gym
        }
    }

    //Saving on firestore


}*/