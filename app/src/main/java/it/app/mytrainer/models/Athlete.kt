package it.app.mytrainer.models

import android.view.View
import it.app.mytrainer.utils.CheckRegistrationFieldUser

class Athlete {
    companion object {
        private var hashMap = HashMap<String, Any>()
        private var hashCheck = HashMap<String, Boolean>()

        fun putName(name: String) {
            val nameOk = CheckRegistrationFieldUser.checkName(name)
            if (nameOk) {
                hashMap["Name"] = name
                hashCheck["Name"] = true
            } else {
                hashCheck["Name"] = false
            }
        }

        /*fun saveOnFireStore() {
            if (controllers()) {
                Firestore.saveAthlete(hashMap)
            }
        }*/


        private fun controlHash(v: View) {
            /*hashCheck.forEach { (key, value) ->
                if (key == "Name" && !value) {
                    v.nameAthlete.error =
                }
                if (key == "Surname" && !value) {
                    v.surnameAthlete.error =
                }
                if (key == "Email" && !value) {
                    v.emailFieldRegistAthlete.error =
                }
                if (key == "Password" && !value) {
                    v.passwordFieldRegistAthlete.error =
                }
                if (key == "DateOfBirth" && !value) {
                    v.dateOfBirthAthlete.error =
                }*/
            /*if(key.equals("Goals")&&value==false){
                v..error
            }
            if(key.equals("Height")&&value==false){
                v..error
            }
            if(key.equals("Weigth")&&value==false){
                v.dateOfBirthAthlete.error
            }
            if(key.equals("TypeOfWorhout")&&value==false){
                v..error
            }
            if(key.equals("Times")&&value==false){
                v..error
            }*/
        }
    }
}
