package it.app.mytrainer.ui.fragments.registrationAthlete

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.models.Athlete
import it.app.mytrainer.utils.CheckRegistrationFieldUser
import kotlinx.android.synthetic.main.fragment_data_athlete.*
import kotlinx.android.synthetic.main.fragment_data_athlete.view.*
import java.util.*

class FragmentDataAthlete : Fragment(), DatePickerDialog.OnDateSetListener {

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
            context?.let { it1 ->
                DatePickerDialog(it1, this, year, month, day).show()
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        //Insert the email in the hashMap of the Athlete if valid
        emailFieldAthlete.doOnTextChanged { text, _, _, _ ->
            if (CheckRegistrationFieldUser.checkEmail(text.toString())) {
                Athlete.putEmail(text.toString())
                layoutAthleteEditTextEmail.error = null
                layoutAthleteEditTextEmail.boxStrokeColor = Color.GREEN
            } else {
                Athlete.removeEmail()
                layoutAthleteEditTextEmail.error = getString(R.string.invalidEmail)
                layoutAthleteEditTextEmail.errorIconDrawable = null
            }
        }

        //Checking if the pass is valid
        passwordFieldAthlete.doOnTextChanged { text, _, _, _ ->
            if (CheckRegistrationFieldUser.checkPass(text.toString())) {
                Athlete.putPass(text.toString())
                layoutAthleteEditTextPassword.error = null
                layoutAthleteEditTextPassword.boxStrokeColor = Color.GREEN
            } else {
                Athlete.removePass()
                layoutAthleteEditTextPassword.error = getString(R.string.invalidPassword)
                layoutAthleteEditTextPassword.errorIconDrawable = null
            }
        }

        //Insert the name in the hashmap of the Athlete if valid
        nameFieldAthlete.doOnTextChanged { text, _, _, _ ->
            if (CheckRegistrationFieldUser.checkName(text.toString())) {
                Athlete.putName(text.toString())
                layoutAthleteEditTextName.error = null
                layoutAthleteEditTextName.boxStrokeColor = Color.GREEN
            } else {
                Athlete.removeName()
                layoutAthleteEditTextName.error = getString(R.string.invalidName)
                layoutAthleteEditTextName.errorIconDrawable = null
            }
        }

        //Insert the surname in the hashmap of Athlete if valid
        surnameFieldAthlete.doOnTextChanged { text, _, _, _ ->
            if (CheckRegistrationFieldUser.checkSurname(text.toString())) {
                Athlete.putSurname(text.toString())
                layoutAthleteEditTextSurname.boxStrokeColor = Color.GREEN
                layoutAthleteEditTextSurname.error = null
            } else {
                Athlete.removeSurname()
                layoutAthleteEditTextSurname.error = getString(R.string.invalidSurname)
                layoutAthleteEditTextSurname.errorIconDrawable = null
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$year/$month/$dayOfMonth"
        dateOfBirthAthlete.text = date
        if (CheckRegistrationFieldUser.checkDateOfBirth(year.toString())) {
            Athlete.putDate(date)
            dateOfBirthAthlete.error = null
        } else {
            Athlete.removeDate()
            dateOfBirthAthlete.error
        }
    }


}