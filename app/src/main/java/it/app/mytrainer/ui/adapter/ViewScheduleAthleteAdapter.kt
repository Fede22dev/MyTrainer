package it.app.mytrainer.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import it.app.mytrainer.ui.fragments.homeAthlete.schedule.FragmentScheduleViewAthlete

class ViewScheduleAthleteAdapter(
    fa: FragmentActivity,
    private val listExercises: ArrayList<ArrayList<String>>,
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
