package it.app.mytrainer.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import it.app.mytrainer.ui.fragments.homeTrainer.creationSchedule.FragmentCreationExercise

class CreationSchedulePageAdapter(
    fa: FragmentActivity,
    private val firstRun: Boolean?,
    private val exerciseCount: Int,
) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return FragmentCreationExercise(position, firstRun!!)
    }

    override fun getItemCount(): Int {
        return exerciseCount
    }
}