package it.app.mytrainer.ui.activities.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

/**
 * Class to manage the registration of athletes
 */

class ActivityRegistrationAthlete : AppCompatActivity() {

    private lateinit var tabsName: Array<String>
    private val currUser = FireAuth.getCurrentUserAuth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_athlete)

        //Setting the name of tabs
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

    //Adjust the back press button, to avoid an eventual crash
    override fun onBackPressed() {
        if (viewPagerAthlete.currentItem == 0) {

            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.popup_title_back_login))
                .setMessage(getString(R.string.popup_text_back_login))

                //If cancel has pressed anything happens
                .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
                }
                //If accept button has pressed, the user will be send back to login and all
                //the data will be cancel from the hashmap
                .setPositiveButton(getString(R.string.accept_button)) { _, _ ->
                    MapAthlete.clearHashMap()
                    FireAuth.deleteCurrentUser()
                    FireAuth.signOut()

                    val intent = Intent(this, ActivityLogin::class.java)
                    startActivity(intent)

                    super.onBackPressed()
                    finish()


                }.show()

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


    }

    //Fun used for eventual failed for an already existing email
    private fun resetVisibilityFailedSave() {
        progressBarRegistrationAthlete.visibility = View.INVISIBLE
        floatActionBtnSaveAthlete.visibility = View.VISIBLE
        viewPagerAthlete.visibility = View.VISIBLE
        tabsBarAthlete.visibility = View.VISIBLE
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
                //Adding the obligatory fields, to manage the schedule
                MapAthlete.addFields()

                //If the slider was never touched, the beginner
                //level will be defaulted inserted
                if (!MapAthlete.mapContainsLevel()) {
                    MapAthlete.putLevel(getString(R.string.beginner_athlete))
                }

                //Saving the data of user on firestore
                val fireStore = FireStore()
                fireStore.saveAthlete(currentUserId) { saveOk ->
                    //If is not ok, delete the previous current user on auth and go back in login
                    if (!saveOk) {
                        FireAuth.deleteCurrentUser()

                        Snackbar.make(constraintActivityRegistrationAthlete,
                            getString(R.string.error_creation_user_auth),
                            Snackbar.LENGTH_LONG)
                            .setBackgroundTint(ContextCompat.getColor(this,
                                R.color.app_foreground))
                            .setTextColor(ContextCompat.getColor(this, R.color.white))
                            .show()

                        resetVisibilityFailedSave()
                    } else {
                        //Calling the finish we go back on the login activity, with the created account
                        Toast.makeText(
                            this,
                            getString(R.string.registration_success_toast),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, ActivityLogin::class.java)
                        startActivity(intent)
                        finish()
                        MapAthlete.printHashMap()
                    }
                }

            } else {
                errorCreateAccountNoFB()
            }
        }
    }

    private fun errorCreateAccountNoFB() {

        Snackbar.make(constraintActivityRegistrationAthlete,
            getString(R.string.error_used_email),
            Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this,
                R.color.app_foreground))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()

        resetVisibilityFailedSave()
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

                    Snackbar.make(constraintActivityRegistrationAthlete,
                        getString(R.string.error_creation_user_auth),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()

                    resetVisibilityFailedSave()
                } else {
                    val intent = Intent(this, ActivityHomeAthlete::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            val intent = Intent(this, ActivityHomeAthlete::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun hashMapNotReadyToSave() {

        Snackbar.make(constraintActivityRegistrationAthlete,
            getString(R.string.registration_error_toast),
            Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this,
                R.color.app_foreground))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()

        MapAthlete.printHashMap()
    }
}





