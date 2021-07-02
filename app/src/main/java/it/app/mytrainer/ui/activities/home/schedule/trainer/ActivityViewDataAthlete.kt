package it.app.mytrainer.ui.activities.home.schedule.trainer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.ObjAthlete
import it.app.mytrainer.ui.adapter.RecycleViewScheduleAthlete
import kotlinx.android.synthetic.main.activity_view_data_athlete.*
import kotlinx.android.synthetic.main.dialog_choice_type_of_wo.view.*
import kotlin.concurrent.thread

class ActivityViewDataAthlete : AppCompatActivity() {

    private lateinit var athlete: ObjAthlete
    private lateinit var fireStore: FireStore
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private val listVisualDay = ArrayList<String>()
    private lateinit var adapter: RecycleViewScheduleAthlete
    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data_athlete)

        prefs = getSharedPreferences("it.app.mytrainer", MODE_PRIVATE)

        athlete = intent.getSerializableExtra("Athlete") as ObjAthlete

        if (athlete.urlPhotoAthlete.isNotBlank()) {
            Glide.with(this).load(athlete.urlPhotoAthlete).into(imageViewViewDataAthlete)
        }

        nameFieldViewDataAthlete.setText(athlete.nameAthlete)
        surnameFieldViewDataAthlete.setText(athlete.surnameAthlete)
        textViewDateViewDataAthlete.setText(athlete.dateOfBirth)
        heightFieldViewDataAthlete.setText(athlete.height)
        weightFieldViewDataAthlete.setText(athlete.weight)
        autoTextViewDropMenuTypeOfWOViewDataAthlete.setText(athlete.typeOfWO)
        autoTextViewDropMenuGoalViewDataAthlete.setText(athlete.goal)
        autoTextViewDropMenuLevelViewDataAthlete.setText(athlete.level)
        autoTextViewDropMenuNumOfWOViewDataAthlete.setText(athlete.daysOfWO)

        athlete.equipment.forEach { equip ->
            when (equip) {

                "Dumbbells", "Manubri" -> {
                    checkbox1ViewDataAthlete.isChecked = true
                }

                "Ropes", "Corde" -> {
                    checkbox2ViewDataAthlete.isChecked = true
                }

                "Rack" -> {
                    checkbox3ViewDataAthlete.isChecked = true
                }

                "Bench", "Panca" -> {
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

        fireStore = FireStore()

        fireStore.getNameDayScheduleAthlete(athlete.idAthlete) { listOfDays ->
            listOfDays.forEach { day ->
                listVisualDay.add(day)
            }
            adapter =
                RecycleViewScheduleAthlete(this, listVisualDay, currentUserId, athlete.idAthlete)
            recyclerViewScheduleViewDataAthlete.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()

        fabAddScheduleViewDataAthlete.setOnClickListener {

            fireStore.checkIdTrainer(athlete.idAthlete, currentUserId) { permission ->
                if (permission) {
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
                } else {
                    Toast.makeText(this,
                        getString(R.string.id_trainer_mismatch),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        topAppBarViewDataAthlete.setOnMenuItemClickListener {
            finish()
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onResume() {
        super.onResume()
        thread {
            Thread.sleep(1000)
            fireStore.getNameDayScheduleAthlete(athlete.idAthlete) { listOfDays ->
                listVisualDay.clear()
                listOfDays.forEach { day ->
                    listVisualDay.add(day)
                }
                adapter.notifyDataSetChanged()
            }
        }

        if (prefs!!.getBoolean("FirstRunViewDataAthlete", true)
        ) {
            scrollViewViewDataAthlete.fullScroll(View.FOCUS_DOWN)
            /*GuideView.Builder(this)
                .setTitle(getString(R.string.viewcase_title_search_exercise_filter))
                .setContentText(getString(R.string.viewcase_text_search_exercise_filter))
                .setTargetView(recyclerViewScheduleViewDataAthlete)
                .setDismissType(DismissType.outside)
                .setGuideListener {

                    GuideView.Builder(this)
                        .setTitle(getString(R.string.viewcase_titlekeyboard_search_exercise_filter))
                        .setContentText(getString(R.string.viewcase_textkeyboard_search_exercise_filter))
                        .setTitleTextSize(16)
                        .setContentTextSize(14)
                        .setTargetView(fabAddScheduleViewDataAthlete)
                        .setDismissType(DismissType.outside)
                        .setGuideListener {
                           // prefs!!.edit().putBoolean("FirstRunViewDataAthlete", false).apply()
                        }
                        .build()
                        .show()

                }
                .build()
                .show()*/
        }
    }

    private fun startCreationSchedule(typeOfWo: String) {
        val intent = Intent(this, ActivityCreationSchedule::class.java)
        intent.putExtra("TypeWO", typeOfWo)
        intent.putExtra("UserId", athlete.idAthlete)
        startActivity(intent)
    }
}