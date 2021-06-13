package it.app.mytrainer.ui.activities.home.schedule.viewDataAthlete

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.app.mytrainer.R
import it.app.mytrainer.models.ObjAthlete
import kotlinx.android.synthetic.main.activity_view_data_athlete.*

class ActivityViewDataAthlete : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data_athlete)

        val athlete = intent.getSerializableExtra("Athlete") as ObjAthlete

        nameFieldViewDataAthlete.setText(athlete.nameAthlete)
        surnameFieldViewDataAthlete.setText(athlete.surnameAthlete)
        textViewDateViewDatathlete.text = athlete.dateOfBirth
        heightFieldViewDataAthlete.setText(athlete.height)
        weightFieldViewDataAthlete.setText(athlete.weight)
        autoTextViewDropMenuTypeOfWOViewDataAthlete.setText(athlete.typeOfWO)
        autoTextViewDropMenuGoalViewDataAthlete.setText(athlete.goal)
        autoTextViewDropMenuLevelViewDataAthlete.setText(athlete.level)
        autoTextViewDropMenuNumOfWOViewDataAthlete.setText(athlete.daysOfWO)

        athlete.equipment.forEach { equip ->
            when (equip) {

                "Dumbells", "Manubri" -> {
                    checkbox1ViewDataAthlete.isChecked = true
                }

                "Rope", "Corde" -> {
                    checkbox2ViewDataAthlete.isChecked = true
                }

                "Rack" -> {
                    checkbox3ViewDataAthlete.isChecked = true
                }

                "Benches", "Panca" -> {
                    checkbox4ViewDataAthlete.isChecked = true
                }

                "Pull-up bar" -> {
                    checkbox5ViewDataAthlete.isChecked = true
                }

                "Dip station" -> {
                    checkbox6ViewDataAthlete.isChecked = true
                }

                "Barbell and discs", "Bilanciere e dischi" -> {
                    checkbox7ViewDataAthlete.isChecked = true
                }

                "Gym membership", "Abbonamento palestra" -> {
                    checkbox8ViewDataAthlete.isChecked = true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        
        fabAddScheduleViewDataAthlete.setOnClickListener { 
            
        }
    }
    
}



    


