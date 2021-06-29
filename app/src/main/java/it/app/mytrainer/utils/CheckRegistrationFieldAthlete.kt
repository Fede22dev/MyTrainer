package it.app.mytrainer.utils

class CheckRegistrationFieldAthlete {

    companion object {

        //Check for the eventual empty name field
        fun checkHeight(height: Int): Boolean {
            return height in 230 downTo 120
        }

        //Check for the eventual empty surname field
        fun checkWeight(weight: Int): Boolean {
            return weight in 250 downTo 30
        }
    }
}