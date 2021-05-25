package it.app.mytrainer.ui.activities.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.models.Athlete
import it.app.mytrainer.ui.adapter.AthleteRegistrationPageAdapter
import kotlinx.android.synthetic.main.activity_registration_athlete.*

class ActivityRegistrationAthlete : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_athlete)

        //Creating the name of the tab
        val tabsName = arrayOf(
            getString(R.string.frag_1_registration_athlete_name),
            getString(R.string.frag_2_registration_athlete_name),
            getString(R.string.frag_3_registration_athlete_name),
        )

        //Setting the name of the tab according to the fragment
        tabsBarAthlete.addTab(
            tabsBarAthlete.newTab().setText(tabsName[0])
        )
        tabsBarAthlete.addTab(
            tabsBarAthlete.newTab().setText(tabsName[1])
        )
        tabsBarAthlete.addTab(
            tabsBarAthlete.newTab().setText(tabsName[2])
        )

        //Insert
        viewPagerAthlete.adapter = AthleteRegistrationPageAdapter(this, tabsBarAthlete.tabCount)

        //To avoid the problem on the tab while switching manually the fragment
        TabLayoutMediator(tabsBarAthlete, viewPagerAthlete) { tab, position ->
            tab.text = tabsName[position]
            tabsBarAthlete.selectTab(tab)
        }.attach()

        tabsBarAthlete.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerAthlete.currentItem = tab.position
                //To show the save button at the last page
                if (tab.position == tabsName.size - 1) {
                    floatActionBtnSaveAthlete.visibility = View.VISIBLE
                } else {
                    floatActionBtnSaveAthlete.visibility = View.INVISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //Setting the action of the saving on FS and turning visible the progress bar
        floatActionBtnSaveAthlete.setOnClickListener {
            if (Athlete.hashMapReadyToSave()) {

                floatActionBtnSaveAthlete.visibility = View.INVISIBLE
                viewPagerAthlete.visibility = View.INVISIBLE
                tabsBarAthlete.visibility = View.INVISIBLE
                progressBarRegistrationAthlete.visibility = View.VISIBLE

                Toast.makeText(
                    this,
                    getString(R.string.registration_success_toast),
                    Toast.LENGTH_LONG
                ).show()

                //CREA ACCOUNT AUTH
                FireAuth.createAccount(
                    Athlete.getEmail(),
                    Athlete.getPass(),
                    this
                ) { result, currentUserId ->
                    if (result) {
                        Athlete.removePass()
                        Athlete.printHashMap()

                        //SALVARE SU FIRESTORE


                        //FINITO TUTTO TORNO ALLA LOGIN
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.error_creation_user_auth),
                            Toast.LENGTH_LONG
                        ).show()
                        Athlete.clearHashMap()
                        finish()
                    }
                }

                Athlete.printHashMap()

            } else {
                Toast.makeText(
                    this,
                    getString(R.string.registration_error_toast),
                    Toast.LENGTH_LONG
                ).show()
                Athlete.printHashMap()
            }
        }
    }

    override fun onBackPressed() {
        if (viewPagerAthlete.currentItem == 0) {
            Athlete.clearHashMap()
            super.onBackPressed()
        } else {
            viewPagerAthlete.currentItem = viewPagerAthlete.currentItem - 1
        }
    }
}



