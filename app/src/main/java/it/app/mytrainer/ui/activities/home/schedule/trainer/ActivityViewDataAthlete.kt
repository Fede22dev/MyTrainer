package it.app.mytrainer.ui.activities.home.schedule.trainer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.app.mytrainer.R
import it.app.mytrainer.models.ObjAthlete
import kotlinx.android.synthetic.main.activity_view_data_athlete.*
import kotlinx.android.synthetic.main.dialog_choice_type_of_wo.view.*

class ActivityViewDataAthlete : AppCompatActivity() {

    private lateinit var athlete: ObjAthlete

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data_athlete)

        athlete = intent.getSerializableExtra("Athlete") as ObjAthlete

        if (athlete.urlPhotoAthlete.isNotBlank()) {
            Glide.with(this).load(athlete.urlPhotoAthlete).into(imageViewViewDataAthlete)
        }

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

            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_choice_type_of_wo, null)
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.type_workout))
                .setMessage(getString(R.string.explain_insert_dialog_type_workout))
                .setView(dialogView)
                .setPositiveButton(getString(R.string.accept_button)) { _, _ ->

                    val typeOfWO = dialogView.editTextTypeOfWO.text.toString()
                    if (typeOfWO.isNotBlank()) {
                        startCreationSchedule(typeOfWO)
                    } else {
                        Toast.makeText(this,
                            getString(R.string.invalid_type_workout),
                            Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
                }
                .create()
                .show()
        }

        topAppBarViewDataAthlete.setOnMenuItemClickListener {
            finish()
            true
        }
    }

    private fun startCreationSchedule(typeOfWo: String) {
        val intent = Intent(this, ActivityCreationSchedule::class.java)
        intent.putExtra("TypeWO", typeOfWo)
        intent.putExtra("UserId", athlete.idAthlete)
        startActivity(intent)
    }
}