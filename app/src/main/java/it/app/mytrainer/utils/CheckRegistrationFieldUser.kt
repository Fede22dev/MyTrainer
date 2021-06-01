package it.app.mytrainer.utils

//Class for the common field
class CheckRegistrationFieldUser {

    companion object {

        //Check for the eventual empty or number in name field
        fun checkName(name: String): Boolean {
            return name.isNotBlank() && !name.contains(Regex("[0-9!-/:-@{-}_]+"))
        }

        //Check for the eventual empty or number in surname field
        fun checkSurname(surname: String): Boolean {
            return surname.isNotBlank() && !surname.contains(Regex("[0-9!-/:-@{-}_]+"))
        }

        //Check for the field
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

        //Check for the Date of birth field
        fun checkDateOfBirth(birthYear: String): Boolean {
            return birthYear <= "2010" && birthYear >= "1920"
        }
    }
}