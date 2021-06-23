package it.app.mytrainer.ui.activities.home.schedule.trainer

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.ObjDayOfWo
import it.app.mytrainer.models.ObjExercise
import it.app.mytrainer.models.ObjSchedule
import it.app.mytrainer.ui.adapter.CreationSchedulePageAdapter
import kotlinx.android.synthetic.main.activity_creation_schedule.*

class ActivityCreationSchedule : AppCompatActivity() {

    private val MAXPAGE = 13
    private var exerciseCount = 1
    private var prefs: SharedPreferences? = null
    private var schedule = ObjSchedule(ArrayList())
    private lateinit var fireStore: FireStore

    companion object {

        private lateinit var dayOfWo: ObjDayOfWo

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
            prefs?.getBoolean("FirstRun", true), exerciseCount)
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
                prefs?.getBoolean("FirstRun", true), ++exerciseCount)

            viewPagerCreationSchedule.currentItem = exerciseCount
            viewPagerCreationSchedule.offscreenPageLimit = exerciseCount

            wormDotsIndicatorCreationSchedule.setViewPager2(viewPagerCreationSchedule)
        }

        fabDeleteExerciseCreationExercise.setOnClickListener {

            viewPagerCreationSchedule.adapter = CreationSchedulePageAdapter(this,
                prefs?.getBoolean("FirstRun", true), --exerciseCount)

            viewPagerCreationSchedule.currentItem = exerciseCount
            viewPagerCreationSchedule.offscreenPageLimit = exerciseCount

            wormDotsIndicatorCreationSchedule.setViewPager2(viewPagerCreationSchedule)

        }

        topAppBarCreationSchedule.setOnMenuItemClickListener {
            saveSchedule()
            finish()
            true
        }

        topAppBarCreationSchedule.setNavigationOnClickListener {
            finish()
        }
    }

    private fun saveSchedule() {
        dayOfWo.listOfExercise.removeIf { obj -> obj == null }
        schedule.listOfDays.add(dayOfWo)

        fireStore.schedule(intent.getStringExtra("UserId")!!, schedule)
    }

}
