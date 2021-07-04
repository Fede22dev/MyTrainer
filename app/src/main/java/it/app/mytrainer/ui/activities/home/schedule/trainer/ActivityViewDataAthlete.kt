package it.app.mytrainer.ui.activities.home.schedule.trainer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.ObjAthlete
import it.app.mytrainer.ui.adapter.RecycleViewScheduleAthlete
import kotlinx.android.synthetic.main.activity_view_data_athlete.*
import kotlinx.android.synthetic.main.dialog_choice_type_of_wo.view.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import kotlin.concurrent.thread

class ActivityViewDataAthlete : AppCompatActivity() {

    private lateinit var athlete: ObjAthlete
    private lateinit var fireStore: FireStore
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private val listVisualDay = ArrayList<String>()
    private lateinit var adapter: RecycleViewScheduleAthlete
    private var prefs: SharedPreferences? = null

    @SuppressLint("SetTextI18n")
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

        val trainerId = athlete.idTrainer
        fireStore = FireStore()
        if (trainerId != "") {
            fireStore.getTrainer(trainerId) { trainer ->
                if (trainer != null) {
                    followedTrainerFieldViewDataAthlete.setText(trainer["Name"].toString() + " " + trainer["Surname"].toString())
                }
            }
        } else {
            followedTrainerFieldViewDataAthlete.setText("")
        }

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

        fireStore.getNameDayScheduleAthlete(athlete.idAthlete) { listOfDays ->
            listOfDays.forEach { day ->
                listVisualDay.add(day)
            }
            adapter =
                RecycleViewScheduleAthlete(this, listVisualDay, currentUserId, athlete.idAthlete)
            recyclerViewScheduleViewDataAthlete.adapter = adapter
        }

        addDividerRecycler()
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
                                Snackbar.make(linearLayoutViewDataAthlete,
                                    getString(R.string.invalid_type_workout),
                                    Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(ContextCompat.getColor(this,
                                        R.color.app_foreground))
                                    .setTextColor(ContextCompat.getColor(this, R.color.white))
                                    .show()
                            }
                        }
                        .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
                        }
                        .create()
                        .show()
                } else {
                    Snackbar.make(linearLayoutViewDataAthlete,
                        getString(R.string.id_trainer_mismatch),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()
                }
            }
        }

        topAppBarViewDataAthlete.setNavigationOnClickListener {
            finish()
        }
    }

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

            //Opening the activity from the bottom the first time, to
            //consent to see all the view case
            scrollViewViewDataAthlete.post {
                scrollViewViewDataAthlete.smoothScrollTo(0, Int.MAX_VALUE)
            }

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    GuideView.Builder(this)
                        .setTitle(getString(R.string.viewcase_title_recycle_view_data_athlete))
                        .setContentText(getString(R.string.viewcase_text_recycle_view_data_athlete))
                        .setTitleTextSize(16)
                        .setContentTextSize(14)
                        .setTargetView(textViewInfoScheduleViewDataAthlete)
                        .setDismissType(DismissType.outside)
                        .setGuideListener {


                            scrollViewViewDataAthlete.post {
                                scrollViewViewDataAthlete.smoothScrollTo(0, 0)
                            }

                            prefs!!.edit().putBoolean("FirstRunViewDataAthlete", false)
                                .apply()

                        }
                        .build()
                        .show()
                }, 500)
        }
    }

    private fun startCreationSchedule(typeOfWo: String) {
        val intent = Intent(this, ActivityCreationSchedule::class.java)
        intent.putExtra("TypeWO", typeOfWo)
        intent.putExtra("UserId", athlete.idAthlete)
        startActivity(intent)
    }

    private fun addDividerRecycler() {
        recyclerViewScheduleViewDataAthlete.addItemDecoration(
            HorizontalDividerItemDecoration.Builder(this)
                .color(Color.WHITE)
                .margin(25, 30)
                .size(2)
                .build())
    }
}