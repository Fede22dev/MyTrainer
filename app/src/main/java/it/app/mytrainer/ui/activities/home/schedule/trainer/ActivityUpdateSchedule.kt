package it.app.mytrainer.ui.activities.home.schedule.trainer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.ObjDayOfWo
import it.app.mytrainer.models.ObjExercise
import it.app.mytrainer.ui.adapter.UpdateSchedulePageAdapter
import kotlinx.android.synthetic.main.activity_creation_schedule.*

class ActivityUpdateSchedule : AppCompatActivity() {

    private val MAXPAGE = 13
    private var exerciseCount = 1
    private var listExerciseCount = 0
    private lateinit var fireStore: FireStore
    private var firstExercise = true

    // We declare this method static cause they are unique for all the fragment
    companion object {

        private lateinit var dayOfWo: ObjDayOfWo
        // To make sure that all the field created for each exercise are ok
        private var fieldOk = true

        fun addExercise(position: Int, exercise: ObjExercise) {
            dayOfWo.listOfExercise[position] = exercise
            Log.d("ACTIVITY_CREATION_SCHEDULE", "ADD: $dayOfWo")
        }

        fun removeExercise(position: Int) {
            dayOfWo.listOfExercise[position] = null
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

        topAppBarCreationSchedule.title = getString(R.string.update_schedule)

        val typeOfWo = intent.getStringExtra("TypeWO")
        dayOfWo = ObjDayOfWo(typeOfWo!!)

        for (i in 0..12) {
            dayOfWo.listOfExercise.add(null)
        }

        fireStore = FireStore()
        fireStore.getScheduleExercise(intent.getStringExtra("UserId")!!, typeOfWo) { listExercise ->

            if (!firstExercise) {
                ++exerciseCount
            }

            firstExercise = false

            dayOfWo.listOfExercise[listExerciseCount] = listExercise[listExercise.size - 1]

            viewPagerCreationSchedule.adapter = UpdateSchedulePageAdapter(this,
                exerciseCount)
            viewPagerCreationSchedule.offscreenPageLimit = exerciseCount

            wormDotsIndicatorCreationSchedule.setViewPager2(viewPagerCreationSchedule)

            listExerciseCount++
        }
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

                if (position == exerciseCount - 1 && position != 0) {
                    fabDeleteExerciseCreationExercise.visibility = View.VISIBLE
                } else {
                    fabDeleteExerciseCreationExercise.visibility = View.INVISIBLE
                }
            }
        })

        fabAddExerciseCreationExercise.setOnClickListener {

            viewPagerCreationSchedule.adapter = UpdateSchedulePageAdapter(this,
                ++exerciseCount)

            viewPagerCreationSchedule.currentItem = exerciseCount
            viewPagerCreationSchedule.offscreenPageLimit = exerciseCount

            wormDotsIndicatorCreationSchedule.setViewPager2(viewPagerCreationSchedule)
        }

        fabDeleteExerciseCreationExercise.setOnClickListener {

            dayOfWo.listOfExercise[exerciseCount - 1] = null

            viewPagerCreationSchedule.adapter = UpdateSchedulePageAdapter(this,
                --exerciseCount)

            viewPagerCreationSchedule.currentItem = exerciseCount
            viewPagerCreationSchedule.offscreenPageLimit = exerciseCount

            wormDotsIndicatorCreationSchedule.setViewPager2(viewPagerCreationSchedule)

        }

        topAppBarCreationSchedule.setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.popup_title_save_creation_schedule))
                .setMessage(getString(R.string.popup_text_save_creation_schedule))

                //If cancel has pressed anything happens
                .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
                }
                //If accept button has pressed,the user go back to the home
                .setPositiveButton(getString(R.string.accept_button)) { _, _ ->
                    saveSchedule()
                }.show()
            true
        }

        topAppBarCreationSchedule.setNavigationOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.popup_title_back_info_creation_schedule))
                .setMessage(getString(R.string.popup_text_back_info_creation_schedule))

                //If cancel has pressed anything happens
                .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
                }
                //If accept button has pressed,the user go back to the home
                .setPositiveButton(getString(R.string.accept_button)) { _, _ ->
                    finish()
                }.show()
        }
    }

    private fun saveSchedule() {
        dayOfWo.listOfExercise.removeIf { obj -> obj == null }
        if (dayOfWo.listOfExercise.isNotEmpty() && fieldOk) {
            fireStore.updateExistingSchedule(intent.getStringExtra("UserId")!!, dayOfWo)
            finish()
        } else {
            //If there's a problem we reset the previous element to null again
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

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.popup_title_back_info_creation_schedule))
            .setMessage(getString(R.string.popup_text_back_info_creation_schedule))

            //If cancel has pressed anything happens
            .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
            }
            //If accept button has pressed,the user go back to the home
            .setPositiveButton(getString(R.string.accept_button)) { _, _ ->
                finish()
            }.show()
    }
}