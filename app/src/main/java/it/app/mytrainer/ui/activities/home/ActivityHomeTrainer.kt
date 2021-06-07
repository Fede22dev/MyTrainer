package it.app.mytrainer.ui.activities.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.activities.starter.ActivityLogin
import kotlinx.android.synthetic.main.activity_home_trainer.*

class ActivityHomeTrainer : AppCompatActivity() {

    private lateinit var fireStore: FireStore
    private lateinit var storage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_trainer)

        val navController = findNavController(R.id.navHostFragmentHomeTrainer)
        bottomNavHomeTrainer.setupWithNavController(navController)

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

                else -> false
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
        intent.putExtra("Type", "Trainer")
        startActivity(intent)
        finish()
    }

    private fun deleteAllDataOfAccount() {
        storage = Firebase.storage.reference
        fireStore = FireStore()

        val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!

        fireStore.deleteTrainer(currentUserId)
        storage.child("Photos").child(currentUserId).delete()

        FireAuth.deleteCurrentUser()

        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
    }
}