package it.app.mytrainer.ui.fragments.registrationTrainer

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
import it.app.mytrainer.models.MapTrainer
import it.app.mytrainer.utils.CheckRegistrationFieldUser
import kotlinx.android.synthetic.main.fragment_data_trainer.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentDataTrainer : Fragment() {

    private val TAG = "FRAGMENT_TRAINER_DATA"
    private val currentUser = FireAuth.getCurrentUserAuth()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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

        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val startDate = outputDateFormat.parse("01/01/1920")!!.time
        val endDate = outputDateFormat.parse("31/12/2010")!!.time

        dateFieldTrainer.setOnClickListener {
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
                dateFieldTrainer.setText(outputDateFormat.format(selection))
                MapTrainer.putDate(outputDateFormat.format(selection))
            }
        }
    }

    private fun setFieldForFBUser() {
        if (currentUser != null) {
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

    private fun setEditTextEmail(text: CharSequence?) {
        val txt = text.toString().trim()
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

    private fun setEditTextPass(text: CharSequence?) {
        val txt = text.toString().trim()
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

    private fun setEditTextName(text: CharSequence?) {
        val txt = text.toString().trim().capitalize(Locale.ROOT)
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

    private fun setEditTextSurname(text: CharSequence?) {
        val txt = text.toString().trim().capitalize(Locale.ROOT)
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
}
