package it.app.mytrainer.ui.fragments.registrationAthlete

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment to manage the data of the
 * athlete (Name, surname, mail, pass, date)
 */

class FragmentDataAthlete : Fragment() {

    private val TAG = "FRAGMENT_ATHLETE_DATA"
    private val currentUser = FireAuth.getCurrentUserAuth()
    private var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        prefs = requireContext().getSharedPreferences("it.app.mytrainer",
            AppCompatActivity.MODE_PRIVATE)

        return inflater.inflate(R.layout.fragment_data_athlete, container, false)
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

        //Setting the format of the date
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        // Crating the variable for the constraint of the calendar
        val startDate = outputDateFormat.parse("01/01/1920")!!.time
        val endDate = outputDateFormat.parse("31/12/2010")!!.time
        val openDate = outputDateFormat.parse("01/01/1999")!!.time

        // Creating the calendar
        dateFieldAthlete.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.viewcase_title_select_date_data))
                .setSelection(openDate)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setStart(startDate)
                        .setEnd(endDate)
                        .setOpenAt(openDate)
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

    //Setting the field for an FB user
    private fun setFieldForFBUser() {
        //Checking if is an FB user 100%
        if (currentUser != null) {
            //If is an FB user, an user on auth is already created.
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

    //Fun for the email
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

    //Fun for the pass
    private fun setEditTextPass(text: CharSequence?) {
        val txt = text.toString().trim()
        //Sending to the check created
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

    //Fun for the name
    private fun setEditTextName(text: CharSequence?) {
        val txt = text.toString().trim()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        //Sending to the check created
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

    //Fun for the surname
    private fun setEditTextSurname(text: CharSequence?) {
        val txt = text.toString().trim()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        //Sending to the check created
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

    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunActivityFragmentDataAthlete", true)
        ) {
            GuideView.Builder(requireContext())
                .setTitle(getString(R.string.viewcase_title_select_date_data))
                .setContentText(getString(R.string.viewcase_text_select_date_data))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(dateFieldAthlete)
                .setDismissType(DismissType.outside)
                .setGuideListener {
                    prefs!!.edit()
                        .putBoolean("FirstRunActivityFragmentDataAthlete", false)
                        .apply()

                }
                .build()
                .show()
        }
    }
}