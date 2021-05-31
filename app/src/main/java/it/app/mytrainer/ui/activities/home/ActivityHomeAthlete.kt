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
import kotlinx.android.synthetic.main.activity_home_athlete.*

class ActivityHomeAthlete : AppCompatActivity() {

    lateinit var fireStore: FireStore
    lateinit var storage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_athlete)

        val navController = findNavController(R.id.navHostFragmentHomeAthlete)
        bottomNavHomeAthlete.setupWithNavController(navController)

        topAppBarHomeAthlete.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.signOut -> {
                    FireAuth.signOut()
                    val intent = Intent(this, ActivityLogin::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.deleteAccount -> {

                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.delete_account))
                        .setMessage(getString(R.string.delete_account_text))

                        //If cancel has pressed anything happens
                        .setNegativeButton(getString(R.string.cancel_button)) { _, _ ->

                        }

                        //If accept button has pressed, the current id will be delte from auth, store and storage
                        .setPositiveButton(getString(R.string.accept_button)) { _, _ ->

                            storage = Firebase.storage.reference
                            fireStore = FireStore()

                            val currentUserId = FireAuth.getCurrentUserAuth()?.uid
                            FireAuth.deleteCurrentUser()

                            if (currentUserId != null) {
                                fireStore.deleteAthlete(currentUserId)
                                storage.child("Photos").child(currentUserId).delete()
                            }


                            val intent = Intent(this, ActivityLogin::class.java)
                            startActivity(intent)
                            finish()
                        }.show()

                    true
                }
                else -> false
            }

        }
    }
}
