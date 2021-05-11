package it.app.mytrainer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_data_athlete.*

class ActivityRegistrationAthlete: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_athlete)

        btnSelectDate.setOnClickListener {
            val intent = Intent(this, ActivityCalendar::class.java)
            startActivity(intent)
        }
    }



}
