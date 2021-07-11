package it.app.mytrainer.ui.activities.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.firebase.storage.Storage
import it.app.mytrainer.ui.activities.starter.ActivityLogin
import kotlinx.android.synthetic.main.activity_home_athlete.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType

/**
 * Class to manage the home of the athlete
 */

class ActivityHomeAthlete : AppCompatActivity() {

    private lateinit var fireStore: FireStore
    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_athlete)

        prefs = getSharedPreferences("it.app.mytrainer", MODE_PRIVATE)

        // Bottom nav
        val navController = findNavController(R.id.navHostFragmentHomeAthlete)
        bottomNavHomeAthlete.setupWithNavController(navController)

        //Setting the fun of item in the appBar
        topAppBarHomeAthlete.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.signOut       -> {
                    //Calling the fun to get out from the account
                    signOut()
                    true
                }

                R.id.deleteAccount -> {
                    //Calling the fun for the pop-up and the eventual deleting of the account
                    deleteAccount()
                    true
                }

                else               -> false
            }
        }
    }

    //Fun calling the sign-out from fireAuth
    private fun signOut() {
        FireAuth.signOut()
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
    }

    //Fun used for the deleting of the account
    private fun deleteAccount() {
        //Dialog to appurate that the user is sure about that
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_account))
            .setMessage(getString(R.string.delete_account_text))

            //If cancel has pressed anything happens
            .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->
            }
            //If accept button has pressed, if is a facebook user all of the data will be canceled
            // else, we need one more step
            .setPositiveButton(getString(R.string.accept_button)) { _, _ ->

                deleteDataAccount()

            }.show()
    }

    private fun deleteDataAccount() {
        // Check if is an FB user or not. If not we go on reauth
        if (FireAuth.getCurrentUserAuth()!!
                .getIdToken(false).result?.signInProvider!! == "password"
        ) {
            reauthenticationDelete()
        } else {
            deleteAllDataOfAccount()
        }
    }

    // Opening the activity for the final check
    private fun reauthenticationDelete() {
        val intent = Intent(this, ActivityReauthDeleteAccount::class.java)
        intent.putExtra("Type", "Athlete")
        startActivity(intent)
        finish()
    }

    //Deleting from firestore, storage and fireAuth
    private fun deleteAllDataOfAccount() {
        fireStore = FireStore()

        val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!

        fireStore.deleteAthlete(currentUserId)
        Storage.deletePhoto(currentUserId)

        FireAuth.deleteCurrentUser()

        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunActivityHomeAthlete", true)
        ) {
            GuideView.Builder(this)
                .setTitle(getString(R.string.viewcase_title_menu_activity_home_trainer))
                .setContentText(getString(R.string.viewcase_text_menu_activity_home_trainer))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(topAppBarHomeAthlete)
                .setDismissType(DismissType.outside)
                .setGuideListener {
                    prefs!!.edit().putBoolean("FirstRunActivityHomeAthlete", false).apply()
                }
                .build()
                .show()
        }
    }
}
