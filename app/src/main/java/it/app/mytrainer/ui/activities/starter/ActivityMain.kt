package it.app.mytrainer.ui.activities.starter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.ui.activities.home.ActivityHomeAthlete
import it.app.mytrainer.ui.activities.home.ActivityHomeTrainer
import kotlin.concurrent.thread

class ActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        thread {
            Thread.sleep(750)
            FireAuth.getCurrentUser { type ->
                // Check if the user is already logged in
                when (type) {
                    0 -> {
                        val intent = Intent(this, ActivityHomeTrainer::class.java)
                        Toast.makeText(
                            this, getString(R.string.welcome_back_trainer_login),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent)
                        finish()
                    }

                    1 -> {
                        val intent = Intent(this, ActivityHomeAthlete::class.java)
                        Toast.makeText(
                            this, getString(R.string.welcome_back_athlete_login),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent)
                        finish()
                    }

                    -1 -> {
                        val intent = Intent(this, ActivityLogin::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}