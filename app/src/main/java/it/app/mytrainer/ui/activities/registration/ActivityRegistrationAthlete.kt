package it.app.mytrainer.ui.activities.registration

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import it.app.mytrainer.R
import it.app.mytrainer.ui.adapter.AthleteRegistrationPageAdapter
import kotlinx.android.synthetic.main.activity_registration_athlete.*

class ActivityRegistrationAthlete : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_athlete)

        tabsBarAthlete.addTab(
            tabsBarAthlete.newTab().setText(getString(R.string.frag1RegistrationAthleteName))
        )
        tabsBarAthlete.addTab(
            tabsBarAthlete.newTab().setText(getString(R.string.frag2RegistrationAthleteName))
        )
        tabsBarAthlete.addTab(
            tabsBarAthlete.newTab().setText(getString(R.string.frag3RegistrationAthleteName))
        )
        tabsBarAthlete.addTab(
            tabsBarAthlete.newTab().setText(getString(R.string.frag4RegistrationAthleteName))
        )

        viewPagerAthlete.adapter = AthleteRegistrationPageAdapter(this, tabsBarAthlete.tabCount)

        tabsBarAthlete.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerAthlete.currentItem = tab.position
                if (tab.position == 3) {
                    floatActionBtnSaveAthlete.visibility = View.VISIBLE
                } else {
                    floatActionBtnSaveAthlete.visibility = View.INVISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        /*floatActionBtnSaveAthlete.setOnClickListener {
            //SALVATAGGIO ATLETA SU FIRESTORE E FIREAUTH
        }*/
    }
}



