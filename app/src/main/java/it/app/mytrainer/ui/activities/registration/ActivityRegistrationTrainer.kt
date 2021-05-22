package it.app.mytrainer.ui.activities.registration

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import it.app.mytrainer.R
import it.app.mytrainer.models.Trainer
import it.app.mytrainer.ui.adapter.AthleteRegistrationPageAdapter
import it.app.mytrainer.ui.adapter.TrainerRegistrationPageAdapter
import kotlinx.android.synthetic.main.activity_registration_athlete.*
import kotlinx.android.synthetic.main.activity_registration_trainer.*

class ActivityRegistrationTrainer : AppCompatActivity() { override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_trainer)

        val tabsName = arrayOf(
            getString(R.string.frag1RegistrationTrainerName),
            getString(R.string.frag2RegistrationTrainerName)
        )

        tabsBarTrainer.addTab(
            tabsBarTrainer.newTab().setText(tabsName[0])
        )
        tabsBarTrainer.addTab(
            tabsBarTrainer.newTab().setText(tabsName[1])
        )

        viewPagerTrainer.adapter = TrainerRegistrationPageAdapter(this, tabsBarTrainer.tabCount)

        TabLayoutMediator(tabsBarTrainer, viewPagerTrainer) {tab, position ->
            tab.text=tabsName[position]
            tabsBarTrainer.selectTab(tab)
        }.attach()

        tabsBarTrainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerTrainer.currentItem = tab.position
                //To show the save button at the last page
                if (tab.position == 1) {
                    floatActionBtnSaveTrainer.visibility = View.VISIBLE
                } else {
                    floatActionBtnSaveTrainer.visibility = View.INVISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

}