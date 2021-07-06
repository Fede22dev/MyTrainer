package it.app.mytrainer.ui.activities.home.schedule.athlete

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.adapter.ViewScheduleAthleteAdapter
import kotlinx.android.synthetic.main.activity_schedule_view_athlete.*

class ActivityScheduleViewAthlete : AppCompatActivity() {

    private val TAG = "ACTIVITY_SCHEDULE_VIEW_ATHLETE"
    private lateinit var fireStore: FireStore
    private val currentUserId = FireAuth.getCurrentUserAuth()!!.uid
    private lateinit var choice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_view_athlete)

        fireStore = FireStore()
        choice = intent.getStringExtra("TypeWO").toString()

        fireStore.getScheduleExercise(currentUserId, choice) { arrayExercise ->

            Log.d(TAG, "$arrayExercise")

            //Passing the list with all the info
            viewPagerScheduleAthlete.adapter =
                ViewScheduleAthleteAdapter(this, arrayExercise)
            viewPagerScheduleAthlete.offscreenPageLimit = arrayExercise.size

            //Implementing the dot, scrolling over the fragment
            wormDotsIndicatorScheduleAthlete.setViewPager2(viewPagerScheduleAthlete)
        }

        topAppBarViewScheduleAthlete.setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.popup_title_go_back_home_view_schedule))
                .setMessage(getString(R.string.popup_text_go_back_home_view_schedule))

                //If cancel has pressed anything happens
                .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
                }
                //If accept button has pressed,the user go back to the home
                .setPositiveButton(getString(R.string.accept_button)) { _, _ ->
                    finish()
                }.show()
            true
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.popup_title_go_back_home_view_schedule))
            .setMessage(getString(R.string.popup_text_go_back_home_view_schedule))

            //If cancel has pressed anything happens
            .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
            }
            //If accept button has pressed,the user go back to the home
            .setPositiveButton(getString(R.string.accept_button)) { _, _ ->
                finish()
            }.show()
    }
}