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
import kotlinx.android.synthetic.main.activity_home_trainer.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType

/**
 * Class to manage the home of the trainer
 */

class ActivityHomeTrainer : AppCompatActivity() {

    private lateinit var fireStore: FireStore
    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_trainer)

        prefs = getSharedPreferences("it.app.mytrainer", MODE_PRIVATE)

        val navController = findNavController(R.id.navHostFragmentHomeTrainer)
        bottomNavHomeTrainer.setupWithNavController(navController)

        //Setting the fun of item in the appBar
        topAppBarHomeTrainer.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.signOut -> {
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
            //If accept button has pressed, the current id will be delete from auth, store and storage
            .setPositiveButton(getString(R.string.accept_button)) { _, _ ->

                deleteDataAccount()

            }.show()
    }

    private fun deleteDataAccount() {
        //Appuring the reauth has been alright
        if (FireAuth.getCurrentUserAuth()!!
                .getIdToken(false).result?.signInProvider!! == "password"
        ) {
            reauthenticationDelete()
        } else {
            deleteAllDataOfAccount()
        }
    }

    private fun reauthenticationDelete() {
        val intent = Intent(this, ActivityReauthDeleteAccount::class.java)
        intent.putExtra("Type", "Trainer")
        startActivity(intent)
        finish()
    }

    //Deleting from firestore and storage and auth
    private fun deleteAllDataOfAccount() {
        fireStore = FireStore()

        val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!

        fireStore.deleteReferencesTrainer(currentUserId) { result ->
            if (result) {
                fireStore.deleteTrainer(currentUserId)

                Storage.deletePhoto(currentUserId)

                FireAuth.deleteCurrentUser()

                val intent = Intent(this, ActivityLogin::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunActivityHomeTrainer", true)
        ) {
            GuideView.Builder(this)
                .setTitle(getString(R.string.viewcase_title_menu_activity_home_trainer))
                .setContentText(getString(R.string.viewcase_text_menu_activity_home_trainer))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(topAppBarHomeTrainer)
                .setDismissType(DismissType.outside)
                .setGuideListener {
                    prefs!!.edit().putBoolean("FirstRunActivityHomeTrainer", false).apply()
                }
                .build()
                .show()
        }
    }
}