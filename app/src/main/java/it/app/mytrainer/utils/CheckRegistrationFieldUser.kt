package it.app.mytrainer.utils

import java.util.regex.Pattern

//Class for the common field
class CheckRegistrationFieldUser {
    companion object {
        //Check for the eventual empty name field
        fun checkName(name: String): Boolean {
            return name.isNotBlank()
        }

        //Check for the eventual empty surname field
        fun checkSurname(surname: String): Boolean {
            return surname.isNotBlank()
        }

        //Check for the field
        fun checkEmail(email: String): Boolean {
            return (email.isNotBlank()) &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches() //Standard regex for the email
        }

        //Check for the password field
        fun checkPass(pass: String): Boolean {
            val pattern = Pattern.compile(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
                        "(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            )  //min 8 char, 1 upper case, 1 lower case, 1 number, 1 special char
            val matcher = pattern.matcher(pass)
            return matcher.matches()
        }

        //Check for the Date of birth field
        fun checkDateOfBirth(birthYear: String): Boolean {
            return birthYear <= "2010" && birthYear >= "1920"
        }
    }

}