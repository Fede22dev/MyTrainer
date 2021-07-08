package it.app.mytrainer.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.firebase.storage.Storage
import it.app.mytrainer.ui.activities.home.trainer.ActivityHomeTrainer
import it.app.mytrainer.ui.activities.starter.ActivityLogin
import kotlinx.android.synthetic.main.activity_reauth_delete_account.*

/**
 * Class to manage the reauth, to delete all the data
 */

class ActivityReauthDeleteAccount : AppCompatActivity() {

    private lateinit var fireStore: FireStore
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reauth_delete_account)

        fireStore = FireStore()

        reauthBtnConfirmation.setOnClickListener {
            setVisibilityForDelete()

            FireAuth.userReauthenticate(passwordFieldReauth.text.toString()) { result ->
                //If the pass is ok all the data from fireAuth are gonna be cancelled
                if (result) {
                    deleteAllDataAccount()

                    val intent = Intent(this, ActivityLogin::class.java)
                    startActivity(intent)

                    finish()
                } else {
                    resetVisibilityForDelete()

                    passwordFieldReauth.setText("")

                    YoYo.with(Techniques.Shake)
                        .playOn(layoutReauthEditTextPassword)

                    Snackbar.make(constraintActivityReauth,
                        getString(R.string.not_valid_password),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()
                }
            }
        }

        //If the user decide to avoid the delete he will be send to the right home
        reauthBtnCancel.setOnClickListener {
            if (intent.getStringExtra("Type") == "Athlete") {
                val intent = Intent(this, ActivityHomeAthlete::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, ActivityHomeTrainer::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setVisibilityForDelete() {
        progressBarReauth.visibility = View.VISIBLE
        titleAppReauth.visibility = View.INVISIBLE
        textViewInfoDelete.visibility = View.INVISIBLE
        layoutReauthEditTextPassword.visibility = View.INVISIBLE
        reauthBtnConfirmation.visibility = View.INVISIBLE
        reauthBtnCancel.visibility = View.INVISIBLE
    }

    private fun resetVisibilityForDelete() {
        progressBarReauth.visibility = View.INVISIBLE
        titleAppReauth.visibility = View.VISIBLE
        textViewInfoDelete.visibility = View.VISIBLE
        layoutReauthEditTextPassword.visibility = View.VISIBLE
        reauthBtnConfirmation.visibility = View.VISIBLE
        reauthBtnCancel.visibility = View.VISIBLE
    }

    //Deleting everything from fireAuth, fireStore and storage
    private fun deleteAllDataAccount() {
        if (intent.getStringExtra("Type") == "Athlete") {
            fireStore.deleteAthlete(currentUserId)
            Storage.deletePhoto(currentUserId)
            FireAuth.deleteCurrentUser()
        } else {
            fireStore.deleteReferencesTrainer(currentUserId) { result ->
                if (result) {
                    fireStore.deleteTrainer(currentUserId)
                    Storage.deletePhoto(currentUserId)
                    FireAuth.deleteCurrentUser()
                }
            }
        }
    }

    //Managing the back pressed, to avoid the crash
    override fun onBackPressed() {
        if (intent.getStringExtra("Type") == "Athlete") {
            val intent = Intent(this, ActivityHomeAthlete::class.java)
            startActivity(intent)
            super.onBackPressed()
            finish()
        } else {
            val intent = Intent(this, ActivityHomeTrainer::class.java)
            startActivity(intent)
            super.onBackPressed()
            finish()
        }
    }
}