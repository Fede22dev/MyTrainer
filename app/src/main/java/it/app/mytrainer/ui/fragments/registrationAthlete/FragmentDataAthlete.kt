package it.app.mytrainer.ui.fragments.registrationAthlete

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.models.Athlete
import it.app.mytrainer.utils.CheckRegistrationFieldUser
import kotlinx.android.synthetic.main.fragment_data_athlete.*
import kotlinx.android.synthetic.main.fragment_data_athlete.view.*
import java.util.*

class FragmentDataAthlete : Fragment(), DatePickerDialog.OnDateSetListener {

    private val TAG = "FRAGMENT ATHLETE DATA"

    private var day = 0
    private var month = 0
    private var year = 0

    private fun getCurrentDataCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_data_athlete, container, false)
        getCurrentDataCalendar()
        view.btnSelectDateAthlete.setOnClickListener {
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
        return view
    }

    override fun onStart() {
        super.onStart()

        val currentUser = FireAuth.getCurrentUserAuth()
        if (currentUser != null) {
            Log.d(
                TAG,
                "${currentUser.displayName} ------ ${currentUser.email} ------ ${currentUser.photoUrl}"
            )

            emailFieldAthlete.isEnabled = false
            currentUser.email?.let { Athlete.putEmail(it) }
            emailFieldAthlete.setText(currentUser.email)
            layoutAthleteEditTextEmail.endIconMode = TextInputLayout.END_ICON_NONE

            val passwordValid = "123abcA&"
            passwordFieldAthlete.isEnabled = false
            layoutAthleteEditTextPassword.endIconMode = TextInputLayout.END_ICON_NONE
            passwordFieldAthlete.setText(passwordValid)
            Athlete.putPass(passwordValid)

            val displayName = currentUser.displayName?.split(" ")
            nameFieldAthlete.isEnabled = false
            layoutAthleteEditTextName.endIconMode = TextInputLayout.END_ICON_NONE
            nameFieldAthlete.setText(displayName?.get(0))
            displayName?.get(0)?.let { Athlete.putName(it) }

            surnameFieldAthlete.isEnabled = false
            layoutAthleteEditTextSurname.endIconMode = TextInputLayout.END_ICON_NONE
            surnameFieldAthlete.setText(displayName?.get(1))
            displayName?.get(1)?.let { Athlete.putSurname(it) }

        } else {

            //Insert the email in the hashMap of the Athlete if valid
            emailFieldAthlete.doOnTextChanged { text, _, _, _ ->
                val txt = text.toString().trim()
                if (CheckRegistrationFieldUser.checkEmail(txt)) {
                    Athlete.putEmail(txt)
                    layoutAthleteEditTextEmail.error = null
                    layoutAthleteEditTextEmail.boxStrokeColor = Color.GREEN
                } else {
                    Athlete.removeEmail()
                    layoutAthleteEditTextEmail.error = getString(R.string.invalid_email)
                    layoutAthleteEditTextEmail.errorIconDrawable = null
                }
            }

            //Checking if the pass is valid
            passwordFieldAthlete.doOnTextChanged { text, _, _, _ ->
                val txt = text.toString().trim()
                if (CheckRegistrationFieldUser.checkPass(txt)) {
                    Athlete.putPass(txt)
                    layoutAthleteEditTextPassword.error = null
                    layoutAthleteEditTextPassword.boxStrokeColor = Color.GREEN
                } else {
                    Athlete.removePass()
                    layoutAthleteEditTextPassword.error = getString(R.string.invalid_password)
                    layoutAthleteEditTextPassword.errorIconDrawable = null
                }
            }

            //Insert the name in the hashmap of the Athlete if valid
            nameFieldAthlete.doOnTextChanged { text, _, _, _ ->
                val txt = text.toString().trim()
                if (CheckRegistrationFieldUser.checkName(txt)) {
                    Athlete.putName(txt)
                    layoutAthleteEditTextName.error = null
                    layoutAthleteEditTextName.boxStrokeColor = Color.GREEN
                } else {
                    Athlete.removeName()
                    layoutAthleteEditTextName.error = getString(R.string.invalid_name)
                    layoutAthleteEditTextName.errorIconDrawable = null
                }
            }

            //Insert the surname in the hashmap of Athlete if valid
            surnameFieldAthlete.doOnTextChanged { text, _, _, _ ->
                val txt = text.toString().trim()
                if (CheckRegistrationFieldUser.checkSurname(txt)) {
                    Athlete.putSurname(txt)
                    layoutAthleteEditTextSurname.boxStrokeColor = Color.GREEN
                    layoutAthleteEditTextSurname.error = null
                } else {
                    Athlete.removeSurname()
                    layoutAthleteEditTextSurname.error = getString(R.string.invalid_surname)
                    layoutAthleteEditTextSurname.errorIconDrawable = null
                }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$year/$month/$dayOfMonth"
        if (CheckRegistrationFieldUser.checkDateOfBirth(year.toString())) {
            dateOfBirthAthlete.text = date
            dateOfBirthAthlete.setTextColor(Color.WHITE)
            Athlete.putDate(date)
        } else {
            Athlete.removeDate()
            dateOfBirthAthlete.text = getString(R.string.error_invalid_date)
            dateOfBirthAthlete.setTextColor(Color.RED)
        }
    }
}