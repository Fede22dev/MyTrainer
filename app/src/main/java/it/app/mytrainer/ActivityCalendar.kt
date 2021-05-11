package it.app.mytrainer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calendar.*


class ActivityCalendar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // Add Listener in calendar
        calendarView!!.setOnDateChangeListener { view, year, month, dayOfMonth ->
                // In this Listener have one method
                // and in this method we will
                // get the value of DAYS, MONTH, YEARS
                // Store the value of date with
                // format in String type Variable
                // Add 1 in month because month
                // index is start with 0
                val Date = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)
                Log.d("DATAAAAAAAAAAAAA", Date)
                // set this date in TextView for Display

            }

    }
}