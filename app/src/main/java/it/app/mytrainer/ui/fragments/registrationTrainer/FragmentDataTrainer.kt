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
            context?.let { it1 ->
                DatePickerDialog(it1, this, year, month, day).show()
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        //Insert the email in the hashMap of the trainer if valid
        emailFieldTrainer.doOnTextChanged { text, start, before, count ->
            if (CheckRegistrationFieldUser.checkEmail(text.toString())) {
                Trainer.putEmail(text.toString())
                emailFieldTrainer.error = null
                layoutTrainerEditTextSurname.boxStrokeColor = Color.GREEN
            } else {
                emailFieldTrainer.error = getString(R.string.invalidEmail)
                layoutTrainerEditTextSurname.boxStrokeColor = Color.RED
            }
        }

        //Checking if the pass is valid
        passwordFieldTrainer.doOnTextChanged { text, start, before, count ->
            if (CheckRegistrationFieldUser.checkPass(text.toString())) {
                passwordFieldTrainer.error = null
                layoutTrainerEditTextSurname.boxStrokeColor = Color.GREEN
            } else {
                passwordFieldTrainer.error = getString(R.string.invalidPassword)
                layoutTrainerEditTextSurname.boxStrokeColor = Color.RED
            }
        }

        //Insert the name in the hashmap of the trainer if valid
        nameFieldTrainer.doOnTextChanged { text, start, before, count ->
            if (CheckRegistrationFieldUser.checkName(text.toString())) {
                Trainer.putName(text.toString())
                nameFieldTrainer.error = null
                layoutTrainerEditTextSurname.boxStrokeColor = Color.GREEN
            } else {
                nameFieldTrainer.error = getString(R.string.invalidName)
                layoutTrainerEditTextSurname.boxStrokeColor = Color.RED
            }
        }

        //Insert the surname in the hashmap of trainer if valid
        surnameFieldTrainer.doOnTextChanged { text, start, before, count ->
            if (CheckRegistrationFieldUser.checkSurname(text.toString())) {
                Trainer.putSurname(text.toString())
                layoutTrainerEditTextSurname.boxStrokeColor = Color.GREEN
                surnameFieldTrainer.error = null
            } else {
                surnameFieldTrainer.error = getString(R.string.invalidSurname)
                layoutTrainerEditTextSurname.boxStrokeColor = Color.RED
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$year/$month/$dayOfMonth"
        dateOfBirthTrainer.text = date
        if (CheckRegistrationFieldUser.checkDateOfBirth(year.toString())) {
            Trainer.putDate(date)
            dateOfBirthTrainer.error = null
        } else {
            dateOfBirthTrainer.error
        }
    }
}
