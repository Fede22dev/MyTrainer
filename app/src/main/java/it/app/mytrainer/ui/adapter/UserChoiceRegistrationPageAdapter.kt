package it.app.mytrainer.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import it.app.mytrainer.ui.fragments.userChoice.FragmentUserChoiceAthlete
import it.app.mytrainer.ui.fragments.userChoice.FragmentUserChoiceTrainer

/**
 * Adapter use to manage the user choice,
 * to fill the activity of choice
 */

class UserChoiceRegistrationPageAdapter(fa: FragmentActivity, private val tabCount: Int) :
    FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentUserChoiceAthlete()
            1 -> FragmentUserChoiceTrainer()
            else -> createFragment(position)
        }
    }

    override fun getItemCount(): Int {
        return tabCount
    }
}