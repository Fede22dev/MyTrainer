package it.app.mytrainer.ui.activities.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.MapAthlete
import it.app.mytrainer.ui.activities.home.ActivityHomeAthlete
import it.app.mytrainer.ui.activities.starter.ActivityLogin
import it.app.mytrainer.ui.adapter.AthleteRegistrationPageAdapter
import kotlinx.android.synthetic.main.activity_registration_athlete.*

class ActivityRegistrationAthlete : AppCompatActivity() {

    private lateinit var tabsName: Array<String>
    private val currUser = FireAuth.getCurrentUserAuth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_athlete)

        //Setting the name
        tabsName = arrayOf(
            getString(R.string.frag_1_registration_athlete_name),
            getString(R.string.frag_2_registration_athlete_name),
            getString(R.string.frag_3_registration_athlete_name),
        )

        //Creating the bar
        setTabBar()

        //Calling the fun for the setting of the view pager
        setViewPager()

        //Setting the action of the saving on FS and turning visible the progress bar
        floatActionBtnSaveAthlete.setOnClickListener {
            if (MapAthlete.hashMapReadyToSave()) {

                //Fun for set the visibility of multiple view
                setVisibilityForSave()

                if (currUser == null) {
                    //Method for creation of the account NO FB
                    successCreationAccountNoFB()
                } else {
                    //Registration with FB, no need to save on auth
                    successCreateAccountFB()
                }

            } else {
                //Hashmap not ready, some field are uncorrected
                hashMapNotReadyToSave()
            }
        }
    }

    //Adjust the back press button
    override fun onBackPressed() {
        if (viewPagerAthlete.currentItem == 0) {
            //Delete all trace and go to login
            MapAthlete.clearHashMap()
            FireAuth.deleteCurrentUser()
            FireAuth.signOut()

            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)

            super.onBackPressed()
            finish()
        } else {
            viewPagerAthlete.currentItem = viewPagerAthlete.currentItem - 1
        }
    }

    private fun setTabBar() {
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

            //Method not necessary but mandatory override
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            //Method not necessary but mandatory override
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setViewPager() {
        //Set adapter of view pager
        viewPagerAthlete.adapter = AthleteRegistrationPageAdapter(this, tabsBarAthlete.tabCount)

        //To avoid the problem on the tab while switching manually the fragment
        TabLayoutMediator(tabsBarAthlete, viewPagerAthlete) { tab, position ->
            tab.text = tabsName[position]
            tabsBarAthlete.selectTab(tab)
        }.attach()
    }

    private fun setVisibilityForSave() {
        floatActionBtnSaveAthlete.visibility = View.INVISIBLE
        viewPagerAthlete.visibility = View.INVISIBLE
        tabsBarAthlete.visibility = View.INVISIBLE
        progressBarRegistrationAthlete.visibility = View.VISIBLE

        Toast.makeText(
            this,
            getString(R.string.registration_success_toast),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun successCreationAccountNoFB() {
        //Creation of the auth account
        FireAuth.createAccount(
            MapAthlete.getEmail(),
            MapAthlete.getPass(),
            this
        ) { result, currentUserId ->
            if (result) {
                MapAthlete.removePass()
                MapAthlete.printHashMap()
                //Adding the obligatory fields
                MapAthlete.addFields()

                //Saving the data of user on firestore
                val fireStore = FireStore()
                fireStore.saveAthlete(currentUserId) { saveOk ->
                    //If is not ok, delete the previous current user on auth and go back in login
                    if (!saveOk) {
                        FireAuth.deleteCurrentUser()
                        Toast.makeText(
                            this,
                            getString(R.string.error_creation_user_auth),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } else {
                errorCreateAccountNoFB()
            }
        }
        //Calling the finish we go back on the login activity, with the created account
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
        MapAthlete.printHashMap()
    }

    private fun errorCreateAccountNoFB() {
        Toast.makeText(
            this,
            getString(R.string.error_creation_user_auth),
            Toast.LENGTH_LONG
        ).show()
        MapAthlete.clearHashMap()
    }

    private fun successCreateAccountFB() {
        MapAthlete.removePass()
        MapAthlete.printHashMap()
        //Adding the obligatory fields
        MapAthlete.addFields()

        val fireStore = FireStore()
        if (currUser != null) {
            fireStore.saveAthlete(currUser.uid) { saveOk ->
                //If is not ok, delete the previous current user on auth and go back in login
                if (!saveOk) {
                    FireAuth.deleteCurrentUser()
                    Toast.makeText(
                        this,
                        getString(R.string.error_creation_user_auth),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        val intent = Intent(this, ActivityHomeAthlete::class.java)
        startActivity(intent)
        finish()
    }

    private fun hashMapNotReadyToSave() {
        Toast.makeText(
            this,
            getString(R.string.registration_error_toast),
            Toast.LENGTH_LONG
        ).show()
        MapAthlete.printHashMap()
    }
}





