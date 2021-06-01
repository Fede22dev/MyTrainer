package it.app.mytrainer.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import it.app.mytrainer.ui.fragments.registrationAthlete.FragmentBodyInfoTypeWOGoalAthlete
import it.app.mytrainer.ui.fragments.registrationAthlete.FragmentDataAthlete
import it.app.mytrainer.ui.fragments.registrationAthlete.FragmentExtraInfoAthlete

class AthleteRegistrationPageAdapter(fa: FragmentActivity, private val tabCount: Int) :
    FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentDataAthlete()
            1 -> FragmentBodyInfoTypeWOGoalAthlete()
            2 -> FragmentExtraInfoAthlete()
            else -> createFragment(position)
        }
    }

    override fun getItemCount(): Int {
        return tabCount
    }
}


