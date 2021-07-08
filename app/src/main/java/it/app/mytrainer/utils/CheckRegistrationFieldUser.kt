package it.app.mytrainer.utils

/**
 * Check for registration (Athlete and trainer)
 */

class CheckRegistrationFieldUser {

    companion object {

        //Check for eventual empty or number in name field
        fun checkName(name: String): Boolean {
            return name.isNotBlank() && !name.contains(Regex("[0-9!-/:-@{-}_]+"))
        }

        //Check for eventual empty or number in surname field
        fun checkSurname(surname: String): Boolean {
            return surname.isNotBlank() && !surname.contains(Regex("[0-9!-/:-@{-}_]+"))
        }

        //Check for email field
        fun checkEmail(email: String): Boolean {
            return email.isNotBlank() && email.matches(android.util.Patterns.EMAIL_ADDRESS.toRegex())  //Standard regex for the email
        }

        //Check for the password field
        fun checkPass(pass: String): Boolean {  //min 8 char, 1 upper case, 1 lower case, 1 number, 1 special char
            return pass.matches(
                Regex(
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
                            "(?=.*[@$!%*?&.:;,-])[A-Za-z\\d@$!%*?&.:;,-]{8,}$"
                )
            )
        }
    }
}