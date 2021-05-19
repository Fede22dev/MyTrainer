package it.app.mytrainer.ui.activities.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.app.mytrainer.R

class ActivityRegistrationTrainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_trainer)
    }
/*
    fun onClickDataTra(v: View) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FragmentDataTrainer>(R.id.fragmentContainerTra)
        }
    }

    fun onClickInfoTra(v: View) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FragmentInfoTrainer>(R.id.fragmentContainerTra)
        }
    }*/
}