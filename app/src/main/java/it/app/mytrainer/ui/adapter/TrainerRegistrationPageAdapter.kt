package it.app.mytrainer.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import it.app.mytrainer.ui.fragments.registrationTrainer.FragmentDataTrainer
import it.app.mytrainer.ui.fragments.registrationTrainer.FragmentExtraInfoTrainer

/**
 * Adapter use to manage the registration of
 * the trainer
 */

class TrainerRegistrationPageAdapter(fa: FragmentActivity, private val tabCount: Int) :
    FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentDataTrainer()
            1 -> FragmentExtraInfoTrainer()
            else -> createFragment(position)
        }
    }

    override fun getItemCount(): Int {
        return tabCount
    }
}
