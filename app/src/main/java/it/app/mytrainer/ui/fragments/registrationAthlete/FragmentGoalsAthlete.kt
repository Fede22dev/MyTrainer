package it.app.mytrainer.ui.fragments.registrationAthlete

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.models.Athlete
import it.app.mytrainer.utils.CheckRegistrationFieldAthlete
import kotlinx.android.synthetic.main.fragment_goals_athlete.*
import kotlinx.android.synthetic.main.fragment_goals_athlete.view.*

class FragmentGoalsAthlete : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals_athlete, container, false)

        view.chip1GoalsAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Athlete.putGoal(buttonView.text.toString())
            } else {
                Athlete.removeGoal()
            }
        }

        view.chip2GoalsAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Athlete.putGoal(buttonView.text.toString())
            } else {
                Athlete.removeGoal()
            }
        }

        view.chip3GoalsAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Athlete.putGoal(buttonView.text.toString())
            } else {
                Athlete.removeGoal()
            }
        }

        view.chip3GoalsAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Athlete.putGoal(buttonView.text.toString())
            } else {
                Athlete.removeGoal()
            }
        }

        view.chip4GoalsAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Athlete.putGoal(buttonView.text.toString())
            } else {
                Athlete.removeGoal()
            }
        }

        view.chip5GoalsAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Athlete.putGoal(buttonView.text.toString())
            } else {
                Athlete.removeGoal()
            }
        }

        view.chip6GoalsAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Athlete.putGoal(buttonView.text.toString())
            } else {
                Athlete.removeGoal()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        heightFieldAthlete.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotBlank()) {
                    if (CheckRegistrationFieldAthlete.checkHeight(text.toString().toInt())) {
                        Athlete.putHeight(text.toString())
                        layoutAthleteEditTextHeight.error = null
                        layoutAthleteEditTextHeight.boxStrokeColor = Color.GREEN
                    } else {
                        Athlete.removeHeight()
                        layoutAthleteEditTextHeight.error = "Invalid height"
                        layoutAthleteEditTextHeight.errorIconDrawable = null
                    }
                } else {
                    Athlete.removeHeight()
                    layoutAthleteEditTextHeight.error = "Invalid height"
                    layoutAthleteEditTextHeight.errorIconDrawable = null
                }
            }
        }

        weightFieldAthlete.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotBlank()) {
                    if (CheckRegistrationFieldAthlete.checkWeight(text.toString().toInt())) {
                        Athlete.putWeight(text.toString())
                        layoutAthleteEditTextWeight.error = null
                        layoutAthleteEditTextWeight.boxStrokeColor = Color.GREEN
                    } else {
                        Athlete.removeWeight()
                        layoutAthleteEditTextWeight.error = "Invalid weight"
                        layoutAthleteEditTextWeight.errorIconDrawable = null
                    }
                } else {
                    Athlete.removeHeight()
                    layoutAthleteEditTextWeight.error = "Invalid weight"
                    layoutAthleteEditTextWeight.errorIconDrawable = null
                }
            }
        }

        val items = listOf("Material", "Design", "Components", "Android")
        val adapter = context?.let { ArrayAdapter(it, R.layout.list_item, items) }
        autoTextViewDropMenuTypeOfWorkAthlete.setAdapter(adapter)
    }


}
