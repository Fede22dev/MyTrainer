package it.app.mytrainer.ui.activities.home

import android.content.Intent
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

class ActivityHomeAthlete : AppCompatActivity() {

    private lateinit var fireStore: FireStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_athlete)

        val navController = findNavController(R.id.navHostFragmentHomeAthlete)
        bottomNavHomeAthlete.setupWithNavController(navController)

        topAppBarHomeAthlete.setOnMenuItemClickListener { menuItem ->
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

    private fun signOut() {
        FireAuth.signOut()
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
    }

    private fun deleteAccount() {
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
        intent.putExtra("Type", "Athlete")
        startActivity(intent)
        finish()
    }

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
}
