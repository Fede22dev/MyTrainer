package it.app.mytrainer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_data_athlete.view.*

class FragmentDataTrainer : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_data_trainer, container, false)
        view.btnSelectDate.setOnClickListener{
            val intent = Intent(context, ActivityCalendar::class.java)
            startActivityForResult(intent, 2)
        }
        return view
    }
}