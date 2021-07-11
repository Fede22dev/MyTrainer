package it.app.mytrainer.ui.activities.starter

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.ui.activities.home.ActivityHomeAthlete
import it.app.mytrainer.ui.activities.home.trainer.ActivityHomeTrainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

/**
 * Class used to send to the right place user
 * already logged
 */

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        //Check for internet connection
        if (checkNetworkStatus()) {
            startRightActivity()
        } else {
            thread {
                Thread.sleep(1000)
                Snackbar.make(constraintActivityMain,
                    getString(R.string.no_connection),
                    Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                    .setTextColor(ContextCompat.getColor(this, R.color.white))
                    .show()

                Thread.sleep(3250)
                finishAndRemoveTask()
            }
        }
    }

    private fun checkNetworkStatus(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false

        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)     -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else                                                               -> false
        }
    }

    private fun startRightActivity() {
        thread {
            Thread.sleep(500)

            FireAuth.getCurrentUserType { type ->
                // Check if the user is already logged in
                when (type) {
                    0  -> {
                        startHomeTrainer()
                    }

                    1  -> {
                        startHomeAthlete()
                    }

                    -1 -> {
                        startLogin()
                    }
                }
            }
        }
    }

    private fun startHomeTrainer() {
        val intent = Intent(this, ActivityHomeTrainer::class.java)
        Toast.makeText(
            this, getString(R.string.welcome_back_trainer_login),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(intent)
        finish()
    }

    private fun startHomeAthlete() {
        val intent = Intent(this, ActivityHomeAthlete::class.java)
        Toast.makeText(
            this, getString(R.string.welcome_back_athlete_login),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(intent)
        finish()
    }

    private fun startLogin() {
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
    }
}