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
import it.app.mytrainer.models.MapTrainer
import it.app.mytrainer.ui.activities.home.ActivityHomeTrainer
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
            MapTrainer.clearHashMap()
            FireAuth.deleteCurrentUser()
            FireAuth.signOut()

            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)

            super.onBackPressed()
            finish()
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

        Toast.makeText(
            this,
            getString(R.string.registration_success_toast),
            Toast.LENGTH_SHORT
        ).show()
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
                        Toast.makeText(
                            this,
                            getString(R.string.error_creation_user_auth),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        //Calling the finish we go back on the login activity, with the created account
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
        Toast.makeText(
            this,
            getString(R.string.error_creation_user_auth),
            Toast.LENGTH_SHORT
        ).show()
        MapTrainer.clearHashMap()
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
                    Toast.makeText(
                        this,
                        getString(R.string.error_creation_user_auth),
                        Toast.LENGTH_LONG
                    ).show()
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
        Toast.makeText(
            this,
            getString(R.string.registration_error_toast),
            Toast.LENGTH_SHORT
        ).show()
        MapTrainer.printHashMap()
    }
}
