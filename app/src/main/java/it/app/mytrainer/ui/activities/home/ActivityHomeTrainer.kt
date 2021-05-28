package it.app.mytrainer.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.ui.activities.ActivityLogin

class ActivityHomeTrainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_trainer)
    }

    fun onClick(v: View) {
        FireAuth.signOut()
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
    }
}