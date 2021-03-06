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
import it.app.mytrainer.models.MapTrainer
import it.app.mytrainer.ui.activities.home.trainer.ActivityHomeTrainer
import it.app.mytrainer.ui.activities.starter.ActivityLogin
import it.app.mytrainer.ui.adapter.TrainerRegistrationPageAdapter
import kotlinx.android.synthetic.main.activity_registration_trainer.*

/**
 * Class to manage the registration of trainers
 */

class ActivityRegistrationTrainer : AppCompatActivity() {

    private lateinit var tabsName: Array<String>
    private val currUser = FireAuth.getCurrentUserAuth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_trainer)

        //Setting the name of tabs
        tabsName = arrayOf(
            getString(R.string.frag_1_registration_trainer_name),
            getString(R.string.frag_2_registration_trainer_name)
        )

        //Creating the bar
        setTabBar()

        //Calling the fun for the setting of the view pager
        setViewPager()

        //Setting the action of the saving on FS and turning visible the progress bar
        floatActionBtnSaveTrainer.setOnClickListener {
            if (MapTrainer.hashMapReadyToSave()) {

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
        if (viewPagerTrainer.currentItem == 0) {
            //Delete all trace and go to login
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.popup_title_back_login))
                .setMessage(getString(R.string.popup_text_back_login))

                //If cancel has pressed anything happens
                .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
                }
                //If accept button has pressed, the user will be send back to login and all
                //the data will be cancel from the hashmap
                .setPositiveButton(getString(R.string.accept_button)) { _, _ ->
                    MapTrainer.clearHashMap()
                    FireAuth.deleteCurrentUser()
                    FireAuth.signOut()

                    val intent = Intent(this, ActivityLogin::class.java)
                    startActivity(intent)

                    super.onBackPressed()
                    finish()


                }.show()
        } else {
            viewPagerTrainer.currentItem = viewPagerTrainer.currentItem - 1
        }
    }

    private fun setTabBar() {
        //Setting the name of the tab according to the fragment
        tabsBarTrainer.addTab(
            tabsBarTrainer.newTab().setText(tabsName[0])
        )
        tabsBarTrainer.addTab(
            tabsBarTrainer.newTab().setText(tabsName[1])
        )

        tabsBarTrainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerTrainer.currentItem = tab.position
                //To show the save button at the last page
                if (tab.position == tabsName.size - 1) {
                    floatActionBtnSaveTrainer.visibility = View.VISIBLE
                } else {
                    floatActionBtnSaveTrainer.visibility = View.INVISIBLE
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
        viewPagerTrainer.adapter = TrainerRegistrationPageAdapter(this, tabsBarTrainer.tabCount)

        //To avoid the problem on the tab while switching manually the fragment
        TabLayoutMediator(tabsBarTrainer, viewPagerTrainer) { tab, position ->
            tab.text = tabsName[position]
            tabsBarTrainer.selectTab(tab)
        }.attach()
    }

    private fun setVisibilityForSave() {
        floatActionBtnSaveTrainer.visibility = View.INVISIBLE
        viewPagerTrainer.visibility = View.INVISIBLE
        tabsBarTrainer.visibility = View.INVISIBLE
        progressBarRegistrationTrainer.visibility = View.VISIBLE
    }

    //Fun used for eventual failed for an already existing email
    private fun resetVisibilityFailedSave() {
        progressBarRegistrationTrainer.visibility = View.INVISIBLE
        floatActionBtnSaveTrainer.visibility = View.VISIBLE
        viewPagerTrainer.visibility = View.VISIBLE
        tabsBarTrainer.visibility = View.VISIBLE
    }

    private fun successCreationAccountNoFB() {
        //Creation of the auth account
        FireAuth.createAccount(
            MapTrainer.getEmail(),
            MapTrainer.getPass(),
            this
        ) { result, currentUserId ->

            if (result) {
                MapTrainer.removePass()
                MapTrainer.printHashMap()

                //Saving the data of user on firestore
                val fireStore = FireStore()

                fireStore.saveTrainer(currentUserId) { saveOk ->
                    //If is not ok, delete the previous current user on auth and go back in login
                    if (!saveOk) {
                        FireAuth.deleteCurrentUser()

                        Snackbar.make(constraintActivityRegistrationTrainer,
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
                        MapTrainer.printHashMap()
                    }
                }

            } else {
                errorCreateAccountNoFB()
            }
        }
    }

    private fun errorCreateAccountNoFB() {

        Snackbar.make(constraintActivityRegistrationTrainer,
            getString(R.string.error_used_email),
            Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this,
                R.color.app_foreground))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()

        resetVisibilityFailedSave()
    }

    private fun successCreateAccountFB() {
        MapTrainer.removePass()
        MapTrainer.printHashMap()

        val fireStore = FireStore()
        if (currUser != null) {
            fireStore.saveTrainer(currUser.uid) { saveOk ->
                //If is not ok, delete the previous current user on auth and go back in login
                if (!saveOk) {
                    FireAuth.deleteCurrentUser()

                    Snackbar.make(constraintActivityRegistrationTrainer,
                        getString(R.string.error_creation_user_auth),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()

                    resetVisibilityFailedSave()
                } else {
                    val intent = Intent(this, ActivityHomeTrainer::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            val intent = Intent(this, ActivityHomeTrainer::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun hashMapNotReadyToSave() {

        Snackbar.make(constraintActivityRegistrationTrainer,
            getString(R.string.registration_error_toast),
            Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this,
                R.color.app_foreground))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()

        MapTrainer.printHashMap()
    }
}
