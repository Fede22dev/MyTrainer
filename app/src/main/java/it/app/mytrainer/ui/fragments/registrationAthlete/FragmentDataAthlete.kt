package it.app.mytrainer.ui.fragments.registrationAthlete

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.models.MapAthlete
import it.app.mytrainer.utils.CheckRegistrationFieldUser
import kotlinx.android.synthetic.main.fragment_data_athlete.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentDataAthlete : Fragment() {

    private val TAG = "FRAGMENT_ATHLETE_DATA"
    private val currentUser = FireAuth.getCurrentUserAuth()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_data_athlete, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()

        if (currentUser != null) {
            Log.d(
                TAG,
                "FACEBOOK: ${currentUser.displayName} ------ ${currentUser.email}"
            )

            //Setting the field in case of FB registration
            setFieldForFBUser()

        } else {

            //Insert the email in the hashMap of the Athlete if valid
            emailFieldAthlete.doOnTextChanged { text, _, _, _ ->
                //Setting the edit text for Email
                setEditTextEmail(text)
            }

            //Checking if the pass is valid
            passwordFieldAthlete.doOnTextChanged { text, _, _, _ ->
                //Setting the edit text for password
                setEditTextPass(text)
            }

            //Insert the name in the hashmap of the Athlete if valid
            nameFieldAthlete.doOnTextChanged { text, _, _, _ ->
                //Setting the edit text for name
                setEditTextName(text)
            }

            //Insert the surname in the hashmap of Athlete if valid
            surnameFieldAthlete.doOnTextChanged { text, _, _, _ ->
                //Setting the edit text for surname
                setEditTextSurname(text)
            }
        }

        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val startDate = outputDateFormat.parse("01/01/1920")!!.time
        val endDate = outputDateFormat.parse("31/12/2010")!!.time

        dateFieldAthlete.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(endDate)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setStart(startDate)
                        .setEnd(endDate)
                        .setOpenAt(endDate)
                        .build()
                )
                .build()

            datePicker.show(requireActivity().supportFragmentManager, TAG)

            datePicker.addOnPositiveButtonClickListener { selection ->
                dateFieldAthlete.setText(outputDateFormat.format(selection))
                MapAthlete.putDate(outputDateFormat.format(selection))
            }
        }
    }

    private fun setFieldForFBUser() {
        if (currentUser != null) {
            emailFieldAthlete.isEnabled = false
            currentUser.email?.let { MapAthlete.putEmail(it) }
            emailFieldAthlete.setText(currentUser.email)
            layoutAthleteEditTextEmail.endIconMode = TextInputLayout.END_ICON_NONE

            val passwordValid = "123abcA&"
            passwordFieldAthlete.isEnabled = false
            layoutAthleteEditTextPassword.endIconMode = TextInputLayout.END_ICON_NONE
            passwordFieldAthlete.setText(passwordValid)
            MapAthlete.putPass(passwordValid)

            val displayName = currentUser.displayName?.split(" ")
            nameFieldAthlete.isEnabled = false
            layoutAthleteEditTextName.endIconMode = TextInputLayout.END_ICON_NONE
            nameFieldAthlete.setText(displayName?.get(0))
            displayName?.get(0)?.let { MapAthlete.putName(it) }

            surnameFieldAthlete.isEnabled = false
            layoutAthleteEditTextSurname.endIconMode = TextInputLayout.END_ICON_NONE
            surnameFieldAthlete.setText(displayName?.get(1))
            displayName?.get(1)?.let { MapAthlete.putSurname(it) }
        }
    }

    private fun setEditTextEmail(text: CharSequence?) {
        val txt = text.toString().trim()
        if (CheckRegistrationFieldUser.checkEmail(txt)) {
            MapAthlete.putEmail(txt)
            layoutAthleteEditTextEmail.error = null
            layoutAthleteEditTextEmail.boxStrokeColor = Color.GREEN
        } else {
            MapAthlete.removeEmail()
            layoutAthleteEditTextEmail.error = getString(R.string.invalid_email)
            layoutAthleteEditTextEmail.errorIconDrawable = null
        }
    }

    private fun setEditTextPass(text: CharSequence?) {
        val txt = text.toString().trim()
        if (CheckRegistrationFieldUser.checkPass(txt)) {
            MapAthlete.putPass(txt)
            layoutAthleteEditTextPassword.error = null
            layoutAthleteEditTextPassword.boxStrokeColor = Color.GREEN
        } else {
            MapAthlete.removePass()
            layoutAthleteEditTextPassword.error = getString(R.string.invalid_password)
            layoutAthleteEditTextPassword.errorIconDrawable = null
        }
    }

    private fun setEditTextName(text: CharSequence?) {
        val txt = text.toString().trim().capitalize(Locale.ROOT)
        if (CheckRegistrationFieldUser.checkName(txt)) {
            MapAthlete.putName(txt)
            layoutAthleteEditTextName.error = null
            layoutAthleteEditTextName.boxStrokeColor = Color.GREEN
        } else {
            MapAthlete.removeName()
            layoutAthleteEditTextName.error = getString(R.string.invalid_name)
            layoutAthleteEditTextName.errorIconDrawable = null
        }
    }

    private fun setEditTextSurname(text: CharSequence?) {
        val txt = text.toString().trim().capitalize(Locale.ROOT)
        if (CheckRegistrationFieldUser.checkSurname(txt)) {
            MapAthlete.putSurname(txt)
            layoutAthleteEditTextSurname.boxStrokeColor = Color.GREEN
            layoutAthleteEditTextSurname.error = null
        } else {
            MapAthlete.removeSurname()
            layoutAthleteEditTextSurname.error = getString(R.string.invalid_surname)
            layoutAthleteEditTextSurname.errorIconDrawable = null
        }
    }
}