package it.app.mytrainer.ui.fragments.registrationTrainer

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
import it.app.mytrainer.models.Trainer
import it.app.mytrainer.utils.CheckRegistrationFieldUser
import kotlinx.android.synthetic.main.fragment_data_trainer.*
import kotlinx.android.synthetic.main.fragment_data_trainer.view.*
import java.util.*

class FragmentDataTrainer : Fragment(), DatePickerDialog.OnDateSetListener {

    private val TAG = "FRAGMENT TRAINER DATA"

    private var day = 0
    private var month = 0
    private var year = 0

    private val currentUser = FireAuth.getCurrentUserAuth()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_data_trainer, container, false)
        getCurrentDataCalendar()
        view.btnSelectDateTrainer.setOnClickListener {
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
        return view
    }

    private fun getCurrentDataCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$year/$month/$dayOfMonth"
        //Setting the date
        dateSet(date, year)
    }

    private fun dateSet(date: String, year: Int) {
        if (CheckRegistrationFieldUser.checkDateOfBirth(year.toString())) {
            dateOfBirthTrainer.text = date
            dateOfBirthTrainer.setTextColor(Color.WHITE)
            Trainer.putDate(date)
        } else {
            Trainer.removeDate()
            dateOfBirthTrainer.text = getString(R.string.error_invalid_date)
            dateOfBirthTrainer.setTextColor(Color.RED)
        }
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
    }

    private fun setFieldForFBUser() {
        if (currentUser != null) {
            emailFieldTrainer.isEnabled = false
            currentUser.email?.let { Trainer.putEmail(it) }
            emailFieldTrainer.setText(currentUser.email)
            layoutTrainerEditTextEmail.endIconMode = TextInputLayout.END_ICON_NONE

            val passwordValid = "123abcA&"
            passwordFieldTrainer.isEnabled = false
            layoutTrainerEditTextPassword.endIconMode = TextInputLayout.END_ICON_NONE
            passwordFieldTrainer.setText(passwordValid)
            Trainer.putPass(passwordValid)

            val displayName = currentUser.displayName?.split(" ")
            nameFieldTrainer.isEnabled = false
            layoutTrainerEditTextName.endIconMode = TextInputLayout.END_ICON_NONE
            nameFieldTrainer.setText(displayName?.get(0))
            displayName?.get(0)?.let { Trainer.putName(it) }

            surnameFieldTrainer.isEnabled = false
            layoutTrainerEditTextSurname.endIconMode = TextInputLayout.END_ICON_NONE
            surnameFieldTrainer.setText(displayName?.get(1))
            displayName?.get(1)?.let { Trainer.putSurname(it) }
        }
    }

    private fun setEditTextEmail(text: CharSequence?) {
        val txt = text.toString().trim()
        if (CheckRegistrationFieldUser.checkEmail(txt)) {
            Trainer.putEmail(txt)
            layoutTrainerEditTextEmail.error = null
            layoutTrainerEditTextEmail.boxStrokeColor = Color.GREEN
        } else {
            Trainer.removeEmail()
            layoutTrainerEditTextEmail.error = getString(R.string.invalid_email)
            layoutTrainerEditTextEmail.errorIconDrawable = null
        }
    }

    private fun setEditTextPass(text: CharSequence?) {
        val txt = text.toString().trim()
        if (CheckRegistrationFieldUser.checkPass(txt)) {
            Trainer.putPass(txt)
            layoutTrainerEditTextPassword.error = null
            layoutTrainerEditTextPassword.boxStrokeColor = Color.GREEN
        } else {
            Trainer.removePass()
            layoutTrainerEditTextPassword.error = getString(R.string.invalid_password)
            layoutTrainerEditTextPassword.errorIconDrawable = null
        }
    }

    private fun setEditTextName(text: CharSequence?) {
        val txt = text.toString().trim()
        if (CheckRegistrationFieldUser.checkName(txt)) {
            Trainer.putName(txt)
            layoutTrainerEditTextName.error = null
            layoutTrainerEditTextName.boxStrokeColor = Color.GREEN
        } else {
            Trainer.removeName()
            layoutTrainerEditTextName.error = getString(R.string.invalid_name)
            layoutTrainerEditTextName.errorIconDrawable = null
        }
    }

    private fun setEditTextSurname(text: CharSequence?) {
        val txt = text.toString().trim()
        if (CheckRegistrationFieldUser.checkSurname(txt)) {
            Trainer.putSurname(txt)
            layoutTrainerEditTextSurname.boxStrokeColor = Color.GREEN
            layoutTrainerEditTextSurname.error = null
        } else {
            Trainer.removeSurname()
            layoutTrainerEditTextSurname.error = getString(R.string.invalid_surname)
            layoutTrainerEditTextSurname.errorIconDrawable = null
        }
    }
}
