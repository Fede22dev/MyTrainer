package it.app.mytrainer.utils

import java.util.regex.Pattern

class CheckRegistrationField() {

    //Check for the eventual empty name field
    fun checkName(name: String): Boolean{
        return name!=""
    }

    //Check for the eventual empty surname field
    fun checkSurname(surname: String): Boolean{
        return surname!=""
    }

    //Check for the field
    fun checkEmail(email: String): Boolean{
        return (email != "") &&
                android.util.Patterns.EMAIL_ADDRESS.
                matcher(email).matches() //Standard regex for the email
    }

    //Check for the password field
    fun checkPass(pass: String): Boolean{
        val pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +  //min 8 char, 1 upper case
                                        "(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")  //1 lower case, 1 number,
        val matcher = pattern.matcher(pass)                                        //1 special char
        return matcher.matches()
    }

    //Check for the Date of birth field
    fun checkDateOfBirth(birthYear: String): Boolean{
        return birthYear<="2010" && birthYear>="1920"
    }

}