package it.app.mytrainer.ui.fragments.userChoice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.ui.activities.registration.ActivityRegistrationTrainer
import kotlinx.android.synthetic.main.fragment_user_choice_trainer.view.*


class FragmentUserChoiceTrainer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_user_choice_trainer, container, false)

        view.btnChoiceTrainer.setOnClickListener {
            val intent = Intent(context, ActivityRegistrationTrainer::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return view
    }
}