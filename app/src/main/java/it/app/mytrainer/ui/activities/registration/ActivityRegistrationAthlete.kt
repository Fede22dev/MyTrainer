package it.app.mytrainer.ui.activities.registration

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import it.app.mytrainer.R
import it.app.mytrainer.ui.fragments.registrationAthlete.FragmentDataAthlete
import it.app.mytrainer.ui.fragments.registrationAthlete.FragmentGoalsAthlete

class ActivityRegistrationAthlete : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_athlete)
    }

    fun onClickDataAthlete(v: View) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FragmentDataAthlete>(R.id.fragmentContainerAthl)
        }
    }

    fun onClickGoalsAthlete(v: View) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FragmentGoalsAthlete>(R.id.fragmentContainerAthl)
        }
    }
}
