package it.app.mytrainer.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import it.app.mytrainer.models.ObjExercise
import it.app.mytrainer.ui.fragments.homeAthlete.schedule.FragmentScheduleViewAthlete

/**
 * Adapter used for the schedule,
 * to see all the exercise
 */

class ViewScheduleAthletePageAdapter(
    fa: FragmentActivity,
    private val listExercises: ArrayList<ObjExercise>,
) :
    FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        //Returning the fragment with right exercise
        return FragmentScheduleViewAthlete(listExercises[position], position)
    }

    override fun getItemCount(): Int {
        return listExercises.size
    }
}
