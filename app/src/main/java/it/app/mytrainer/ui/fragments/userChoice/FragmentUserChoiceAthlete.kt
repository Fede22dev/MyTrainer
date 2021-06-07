package it.app.mytrainer.ui.fragments.userChoice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.ui.activities.registration.ActivityRegistrationAthlete
import kotlinx.android.synthetic.main.fragment_user_choice_athlete.view.*

class FragmentUserChoiceAthlete : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val view = inflater.inflate(R.layout.fragment_user_choice_athlete, container, false)

        view.btnChoiceAthlete.setOnClickListener {
            startRegistrationAthlete()
        }

        return view
    }

    private fun startRegistrationAthlete() {
        val intent = Intent(context, ActivityRegistrationAthlete::class.java)
        startActivity(intent)
        activity?.finish()
    }
}