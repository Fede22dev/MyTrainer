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
import it.app.mytrainer.models.Trainer
import it.app.mytrainer.ui.activities.home.ActivityHomeTrainer
import it.app.mytrainer.ui.activities.starter.ActivityLogin
import it.app.mytrainer.ui.adapter.TrainerRegistrationPageAdapter
import kotlinx.android.synthetic.main.activity_registration_trainer.*

class ActivityRegistrationTrainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_trainer)

        //Creating the name of the tab
        val tabsName = arrayOf(
            getString(R.string.frag_1_registration_trainer_name),
            getString(R.string.frag_2_registration_trainer_name)
        )

        //Setting the name of the tab according to the fragment
        tabsBarTrainer.addTab(
            tabsBarTrainer.newTab().setText(tabsName[0])
        )
        tabsBarTrainer.addTab(
            tabsBarTrainer.newTab().setText(tabsName[1])
        )

        //Insert
        viewPagerTrainer.adapter = TrainerRegistrationPageAdapter(this, tabsBarTrainer.tabCount)

        //To avoid the problem on the tab while switching manually the fragment
        TabLayoutMediator(tabsBarTrainer, viewPagerTrainer) { tab, position ->
            tab.text = tabsName[position]
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

        //Setting the action of the saving on FS and turning visible the progress bar
        floatActionBtnSaveTrainer.setOnClickListener {
            if (Trainer.hashMapReadyToSave()) {

                floatActionBtnSaveTrainer.visibility = View.INVISIBLE
                viewPagerTrainer.visibility = View.INVISIBLE
                tabsBarTrainer.visibility = View.INVISIBLE
                progressBarRegistrationTrainer.visibility = View.VISIBLE

                Toast.makeText(
                    this,
                    getString(R.string.registration_success_toast),
                    Toast.LENGTH_LONG
                ).show()

                val currUser = FireAuth.getCurrentUserAuth()
                if (currUser == null) {

                    //Creation of the auth account
                    FireAuth.createAccount(
                        Trainer.getEmail(),
                        Trainer.getPass(),
                        this
                    ) { result, currentUserId ->
                        if (result) {
                            Trainer.removePass()
                            Trainer.printHashMap()

                            //Saving the data of user on firestore
                            val fireStore = FireStore()
                            fireStore.saveTrainer(currentUserId) { saveOk ->
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

                            //Calling the finish we go back on the login activity, with the created account
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                getString(R.string.error_creation_user_auth),
                                Toast.LENGTH_LONG
                            ).show()
                            Trainer.clearHashMap()
                            finish()
                        }
                    }

                    Trainer.printHashMap()

                } else {
                    Trainer.removePass()
                    Trainer.printHashMap()

                    val fireStore = FireStore()
                    fireStore.saveTrainer(currUser.uid) { saveOk ->
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

                    val intent = Intent(this, ActivityHomeTrainer::class.java)
                    startActivity(intent)
                    finish()
                }

            } else {
                Toast.makeText(
                    this,
                    getString(R.string.registration_error_toast),
                    Toast.LENGTH_LONG
                ).show()
                Trainer.printHashMap()
            }
        }
    }

    //Adjust the back press button
    override fun onBackPressed() {
        if (viewPagerTrainer.currentItem == 0) {
            Trainer.clearHashMap()
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
}