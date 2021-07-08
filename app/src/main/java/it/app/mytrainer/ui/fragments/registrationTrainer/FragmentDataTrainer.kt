package it.app.mytrainer.ui.fragments.registrationTrainer

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
import it.app.mytrainer.models.MapTrainer
import it.app.mytrainer.utils.CheckRegistrationFieldUser
import kotlinx.android.synthetic.main.fragment_data_trainer.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment to manage the data of the
 * trainer (Name, surname, mail, pass, date)
 */

class FragmentDataTrainer : Fragment() {

    private val TAG = "FRAGMENT_TRAINER_DATA"
    private val currentUser = FireAuth.getCurrentUserAuth()
    private var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        prefs = requireContext().getSharedPreferences("it.app.mytrainer",
            AppCompatActivity.MODE_PRIVATE)

        return inflater.inflate(R.layout.fragment_data_trainer, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (currentUser != null) {
            Log.d(
                TAG,
                "${currentUser.displayName} ------ ${currentUser.email}"
            )

            //Setting the field in case of FB registration
            setFieldForFBUser()

        } else {
            //Insert the email in the hashMap of the trainer if valid
            emailFieldTrainer.doOnTextChanged { text, _, _, _ ->
                setEditTextEmail(text)
            }

            //Checking if the pass is valid
            passwordFieldTrainer.doOnTextChanged { text, _, _, _ ->
                setEditTextPass(text)
            }

            //Insert the name in the hashmap of the trainer if valid
            nameFieldTrainer.doOnTextChanged { text, _, _, _ ->
                setEditTextName(text)
            }

            //Insert the surname in the hashmap of trainer if valid
            surnameFieldTrainer.doOnTextChanged { text, _, _, _ ->
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
        dateFieldTrainer.setOnClickListener {
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
                dateFieldTrainer.setText(outputDateFormat.format(selection))
                MapTrainer.putDate(outputDateFormat.format(selection))
            }
        }
    }

    //Setting the field for an FB user
    private fun setFieldForFBUser() {
        //Checking if is an FB user 100%
        if (currentUser != null) {
            //If is an FB user, an user on auth is already created.
            emailFieldTrainer.isEnabled = false
            currentUser.email?.let { MapTrainer.putEmail(it) }
            emailFieldTrainer.setText(currentUser.email)
            layoutTrainerEditTextEmail.endIconMode = TextInputLayout.END_ICON_NONE

            val passwordValid = "123abcA&"
            passwordFieldTrainer.isEnabled = false
            layoutTrainerEditTextPassword.endIconMode = TextInputLayout.END_ICON_NONE
            passwordFieldTrainer.setText(passwordValid)
            MapTrainer.putPass(passwordValid)

            val displayName = currentUser.displayName?.split(" ")
            nameFieldTrainer.isEnabled = false
            layoutTrainerEditTextName.endIconMode = TextInputLayout.END_ICON_NONE
            nameFieldTrainer.setText(displayName?.get(0))
            displayName?.get(0)?.let { MapTrainer.putName(it) }

            surnameFieldTrainer.isEnabled = false
            layoutTrainerEditTextSurname.endIconMode = TextInputLayout.END_ICON_NONE
            surnameFieldTrainer.setText(displayName?.get(1))
            displayName?.get(1)?.let { MapTrainer.putSurname(it) }
        }
    }

    //Fun for the email
    private fun setEditTextEmail(text: CharSequence?) {
        val txt = text.toString().trim()
        //Sending to the check created
        if (CheckRegistrationFieldUser.checkEmail(txt)) {
            MapTrainer.putEmail(txt)
            layoutTrainerEditTextEmail.error = null
            layoutTrainerEditTextEmail.boxStrokeColor = Color.GREEN
        } else {
            MapTrainer.removeEmail()
            layoutTrainerEditTextEmail.error = getString(R.string.invalid_email)
            layoutTrainerEditTextEmail.errorIconDrawable = null
        }
    }

    //Fun for the pass
    private fun setEditTextPass(text: CharSequence?) {
        val txt = text.toString().trim()
        //Sending to the check created
        if (CheckRegistrationFieldUser.checkPass(txt)) {
            MapTrainer.putPass(txt)
            layoutTrainerEditTextPassword.error = null
            layoutTrainerEditTextPassword.boxStrokeColor = Color.GREEN
        } else {
            MapTrainer.removePass()
            layoutTrainerEditTextPassword.error = getString(R.string.invalid_password)
            layoutTrainerEditTextPassword.errorIconDrawable = null
        }
    }

    //Fun for the name
    private fun setEditTextName(text: CharSequence?) {
        val txt = text.toString().trim().capitalize(Locale.ROOT)
        //Sending to the check created
        if (CheckRegistrationFieldUser.checkName(txt)) {
            MapTrainer.putName(txt)
            layoutTrainerEditTextName.error = null
            layoutTrainerEditTextName.boxStrokeColor = Color.GREEN
        } else {
            MapTrainer.removeName()
            layoutTrainerEditTextName.error = getString(R.string.invalid_name)
            layoutTrainerEditTextName.errorIconDrawable = null
        }
    }

    //Fun for the surname
    private fun setEditTextSurname(text: CharSequence?) {
        val txt = text.toString().trim().capitalize(Locale.ROOT)
        //Sending to the check created
        if (CheckRegistrationFieldUser.checkSurname(txt)) {
            MapTrainer.putSurname(txt)
            layoutTrainerEditTextSurname.boxStrokeColor = Color.GREEN
            layoutTrainerEditTextSurname.error = null
        } else {
            MapTrainer.removeSurname()
            layoutTrainerEditTextSurname.error = getString(R.string.invalid_surname)
            layoutTrainerEditTextSurname.errorIconDrawable = null
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunActivityFragmentDataTrainer", true)
        ) {
            GuideView.Builder(requireContext())
                .setTitle(getString(R.string.viewcase_title_select_date_data))
                .setContentText(getString(R.string.viewcase_text_select_date_data))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(dateFieldTrainer)
                .setDismissType(DismissType.outside)
                .setGuideListener {
                    prefs!!.edit()
                        .putBoolean("FirstRunActivityFragmentDataTrainer", false)
                        .apply()

                }
                .build()
                .show()
        }
    }
}
