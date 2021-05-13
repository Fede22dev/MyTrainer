package it.app.mytrainer.ui.activities.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import it.app.mytrainer.R

class ActivityUserChoice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_choice)
    }

    fun onClickAthlete(v: View) {
        val intent = Intent(this, ActivityRegistrationAthlete::class.java)
        startActivity(intent)
        finish()
    }

    fun onClickTrainer(v: View) {
        val intent = Intent(this, ActivityRegistrationTrainer::class.java)
        startActivity(intent)
        finish()
    }
}