package it.app.mytrainer.utils

/**
 * Check for registration (Athlete side)
 */

class CheckRegistrationFieldAthlete {

    companion object {

        //Check to see if the height is in that range
        fun checkHeight(height: Int): Boolean {
            return height in 260 downTo 80
        }

        //Check to see if the weight is in that range
        fun checkWeight(weight: Int): Boolean {
            return weight in 400 downTo 20
        }
    }
}