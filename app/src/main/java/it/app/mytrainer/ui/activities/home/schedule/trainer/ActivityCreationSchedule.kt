package it.app.mytrainer.ui.activities.home.schedule.trainer

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.ObjDayOfWo
import it.app.mytrainer.models.ObjExercise
import it.app.mytrainer.ui.adapter.CreationSchedulePageAdapter
import kotlinx.android.synthetic.main.activity_creation_schedule.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType

class ActivityCreationSchedule : AppCompatActivity() {

    private val MAXPAGE = 13
    private var exerciseCount = 1
    private var prefs: SharedPreferences? = null
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private lateinit var fireStore: FireStore


    companion object {

        private lateinit var dayOfWo: ObjDayOfWo
        private var fieldOk = true

        fun addExercise(position: Int, exercise: ObjExercise) {
            dayOfWo.listOfExercise.add(position, exercise)
            Log.d("ACTIVITY_CREATION_SCHEDULE", "ADD: $dayOfWo")
        }

        fun removeExercise(position: Int) {
            dayOfWo.listOfExercise.removeAt(position)
            Log.d("ACTIVITY_CREATION_SCHEDULE", "REMOVE: $dayOfWo")
        }

        fun getExercise(position: Int): ObjExercise? {
            return dayOfWo.listOfExercise[position]
        }

        fun setFieldOk(ok: Boolean) {
            fieldOk = ok
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_schedule)

        val typeOfWo = intent.getStringExtra("TypeWO")
        dayOfWo = ObjDayOfWo(typeOfWo!!)

        for (i in 0..12) {
            dayOfWo.listOfExercise.add(null)
        }

        prefs = getSharedPreferences("it.app.mytrainer", MODE_PRIVATE)
        prefs!!.edit().putBoolean("FirstRun", true).apply()

        viewPagerCreationSchedule.adapter = CreationSchedulePageAdapter(this,
            exerciseCount)
        viewPagerCreationSchedule.offscreenPageLimit = exerciseCount

        wormDotsIndicatorCreationSchedule.setViewPager2(viewPagerCreationSchedule)

        fireStore = FireStore()
    }

    override fun onStart() {
        super.onStart()

        viewPagerCreationSchedule.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (exerciseCount >= MAXPAGE) {
                    fabAddExerciseCreationExercise.visibility = View.INVISIBLE
                } else {
                    fabAddExerciseCreationExercise.visibility = View.VISIBLE
                }

                if (position > 0) {
                    fabDeleteExerciseCreationExercise.visibility = View.VISIBLE
                } else {
                    fabDeleteExerciseCreationExercise.visibility = View.INVISIBLE
                }
            }
        })

        fabAddExerciseCreationExercise.setOnClickListener {

            viewPagerCreationSchedule.adapter = CreationSchedulePageAdapter(this,
                ++exerciseCount)

            viewPagerCreationSchedule.currentItem = exerciseCount
            viewPagerCreationSchedule.offscreenPageLimit = exerciseCount

            wormDotsIndicatorCreationSchedule.setViewPager2(viewPagerCreationSchedule)
        }

        fabDeleteExerciseCreationExercise.setOnClickListener {

            viewPagerCreationSchedule.adapter = CreationSchedulePageAdapter(this,
                --exerciseCount)

            viewPagerCreationSchedule.currentItem = exerciseCount
            viewPagerCreationSchedule.offscreenPageLimit = exerciseCount

            wormDotsIndicatorCreationSchedule.setViewPager2(viewPagerCreationSchedule)

        }

        topAppBarCreationSchedule.setOnMenuItemClickListener {
            saveSchedule()
            true
        }

        topAppBarCreationSchedule.setNavigationOnClickListener {
            finish()
        }
    }

    private fun saveSchedule() {
        dayOfWo.listOfExercise.removeIf { obj -> obj == null }
        if (dayOfWo.listOfExercise.isNotEmpty() && fieldOk) {
            fireStore.updateSchedule(intent.getStringExtra("UserId")!!, currentUserId, dayOfWo)
            finish()
        } else {

            for (i in dayOfWo.listOfExercise.size..12) {
                dayOfWo.listOfExercise.add(null)
            }

            Snackbar.make(constraintActivityCreationSchedule,
                getString(R.string.error_empty_schedule),
                Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(this,
                    R.color.app_foreground))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunActivityCreationSchedule", true)
        ) {
            GuideView.Builder(this)
                .setTitle(getString(R.string.viewcase_title_save_activity_creation_schedule))
                .setContentText(getString(R.string.viewcase_text_save_activity_creation_schedule))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(findViewById(R.id.saveSchedule))
                .setDismissType(DismissType.outside)
                .setGuideListener {

                    GuideView.Builder(this)
                        .setTitle(getString(R.string.viewcase_title_fab_delete_activity_creation_schedule))
                        .setContentText(getString(R.string.viewcase_text_fab_delete_activity_creation_schedule))
                        .setTitleTextSize(16)
                        .setContentTextSize(14)
                        .setTargetView(fabDeleteExerciseCreationExercise)
                        .setDismissType(DismissType.outside)
                        .setGuideListener {

                            GuideView.Builder(this)
                                .setTitle(getString(R.string.viewcase_title_fab_add_activity_creation_schedule))
                                .setContentText(getString(R.string.viewcase_text_fab_add_activity_creation_schedule))
                                .setTitleTextSize(16)
                                .setContentTextSize(14)
                                .setTargetView(fabAddExerciseCreationExercise)
                                .setDismissType(DismissType.outside)
                                .setGuideListener {
                                    prefs!!.edit()
                                        .putBoolean("FirstRunActivityCreationSchedule", false)
                                        .apply()
                                }
                                .build()
                                .show()
                        }
                        .build()
                        .show()

                }
                .build()
                .show()
        }
    }
}
