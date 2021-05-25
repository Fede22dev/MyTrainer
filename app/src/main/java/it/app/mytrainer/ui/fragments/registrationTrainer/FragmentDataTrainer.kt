package it.app.mytrainer.ui.fragments.registrationTrainer

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
import it.app.mytrainer.models.Trainer
import it.app.mytrainer.utils.CheckRegistrationFieldUser
import kotlinx.android.synthetic.main.fragment_data_trainer.*
import kotlinx.android.synthetic.main.fragment_data_trainer.view.*
import java.util.*

class FragmentDataTrainer : Fragment(), DatePickerDialog.OnDateSetListener {

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
        val view = inflater.inflate(R.layout.fragment_data_trainer, container, false)
        getCurrentDataCalendar()
        view.btnSelectDateTrainer.setOnClickListener {
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        //Insert the email in the hashMap of the trainer if valid
        emailFieldTrainer.doOnTextChanged { text, _, _, _ ->
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

        //Checking if the pass is valid
        passwordFieldTrainer.doOnTextChanged { text, _, _, _ ->
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

        //Insert the name in the hashmap of the trainer if valid
        nameFieldTrainer.doOnTextChanged { text, _, _, _ ->
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

        //Insert the surname in the hashmap of trainer if valid
        surnameFieldTrainer.doOnTextChanged { text, _, _, _ ->
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$year/$month/$dayOfMonth"
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
}
