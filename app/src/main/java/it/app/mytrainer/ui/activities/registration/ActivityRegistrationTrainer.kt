package it.app.mytrainer.ui.activities.registration

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import it.app.mytrainer.R
import it.app.mytrainer.ui.fragments.registrationTrainer.FragmentDataTrainer
import it.app.mytrainer.ui.fragments.registrationTrainer.FragmentInfoTrainer

class ActivityRegistrationTrainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_trainer)
    }

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
    }
}