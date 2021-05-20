package it.app.mytrainer.ui.fragments.registrationAthlete

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.models.Athlete
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$year/$month/$dayOfMonth"
        dateOfBirthAthlete.text = date
        /*val birthOk = saveAthlete.setBirthAthlete(date)
        if (!birthOk) {
            dateOfBirthAthlete.error = "Date of birth not valid"
        } else {
            dateOfBirthAthlete.text = date
        }*/
    }


}