package it.app.mytrainer

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_data_athlete.*
import kotlinx.android.synthetic.main.fragment_data_athlete.view.*

class FragmentDataAthlete : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_data_athlete, container, false)
        view.btnSelectDate.setOnClickListener{
            val intent = Intent(context, ActivityCalendar::class.java)
            startActivityForResult(intent, 1)
        }
        return view

    }
}