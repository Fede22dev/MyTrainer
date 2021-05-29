package it.app.mytrainer.ui.activities.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.ui.activities.starter.ActivityLogin
import kotlinx.android.synthetic.main.activity_home_athlete.*
import kotlinx.android.synthetic.main.activity_home_trainer.*

class ActivityHomeAthlete : AppCompatActivity() {
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

                else -> false

            }
        }
    }
}