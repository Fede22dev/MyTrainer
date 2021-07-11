package it.app.mytrainer.utils

/**
 * Check for registration (Athlete side)
 */

class CheckRegistrationFieldAthlete {

    companion object {

        //Check to see if the height is in that range
        fun checkHeight(height: Int): Boolean {
            return height in 250 downTo 120
        }

        //Check to see if the weight is in that range
        fun checkWeight(weight: Int): Boolean {
            return weight in 300 downTo 25
        }
    }
}