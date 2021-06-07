package it.app.mytrainer.ui.activities.home.schedule

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.adapter.ViewScheduleAthleteAdapter
import kotlinx.android.synthetic.main.activity_schedule_view_athlete.*

class ActivityScheduleViewAthlete : AppCompatActivity() {

    private val TAG = "ACTIVITY_SCHEDULE_VIEW_ATHLETE"
    private lateinit var fireStore: FireStore
    private val currentUserId = FireAuth.getCurrentUserAuth()!!.uid
    private lateinit var schedule: HashMap<*, *>
    private lateinit var choice: String
    private var listOfExercises = ArrayList<ArrayList<String>>()
    private var listOfExerciseInfo = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_view_athlete)

        fireStore = FireStore()
        choice = intent.getStringExtra("TypeWO").toString()

        fireStore.getAthlete(currentUserId) { map ->
            schedule = map?.get("Schedule") as HashMap<*, *>

            schedule.forEach { (typeWo, exercises) ->
                //Bringing the right tyoe of wo
                if (typeWo == choice) {
                    exercises as HashMap<String, ArrayList<String>>

                    //Starting to scroll the nested map (External level)
                    exercises.forEach { (_, infoExercise) ->

                        //Scrolling the nested array list with the info of whole ex
                        infoExercise.forEach { info ->
                            listOfExerciseInfo.add(info)
                        }

                        //Adding to the list which is going on the fragment the name and the info
                        listOfExercises.add(listOfExerciseInfo.clone() as ArrayList<String>)
                        listOfExerciseInfo.clear()
                    }
                }
            }

            Log.d(TAG, "$listOfExercises")

            //Passing the list with all the info
            viewPagerScheduleAthlete.adapter =
                ViewScheduleAthleteAdapter(this, listOfExercises)
            viewPagerScheduleAthlete.offscreenPageLimit = listOfExercises.size

            //Implementing the dot, scrolling over the fragment
            wormDotsIndicatorScheduleAthlete.setViewPager2(viewPagerScheduleAthlete)
        }

        topAppBarViewScheduleAthlete.setOnMenuItemClickListener {
            finish()
            true
        }

    }
}