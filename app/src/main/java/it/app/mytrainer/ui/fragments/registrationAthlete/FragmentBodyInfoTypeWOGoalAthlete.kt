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
import kotlinx.android.synthetic.main.fragment_bodyinfo_typewo_goal_athlete.*
import kotlinx.android.synthetic.main.fragment_bodyinfo_typewo_goal_athlete.view.*


class FragmentBodyInfoTypeWOGoalAthlete : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_bodyinfo_typewo_goal_athlete, container, false)

        view.chip1GoalAthlete.setOnClickListener {
            Athlete.putGoal(view.chip1GoalAthlete.text.toString())
        }

        view.chip2GoalAthlete.setOnClickListener {
            Athlete.putGoal(view.chip2GoalAthlete.text.toString())
        }

        view.chip3GoalAthlete.setOnClickListener {
            Athlete.putGoal(view.chip3GoalAthlete.text.toString())
        }

        view.chip4GoalAthlete.setOnClickListener {
            Athlete.putGoal(view.chip4GoalAthlete.text.toString())
        }

        view.chip5GoalAthlete.setOnClickListener {
            Athlete.putGoal(view.chip5GoalAthlete.text.toString())
        }

        view.chip6GoalAthlete.setOnClickListener {
            Athlete.putGoal(view.chip6GoalAthlete.text.toString())
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        heightFieldAthlete.doOnTextChanged { text, _, _, _ ->
            //Setting the edit text for height
            setEditTextHeight(text)
        }

        weightFieldAthlete.doOnTextChanged { text, _, _, _ ->
            //Setting the edit text for height
            setEditTextWeight(text)
        }

        //Setting the dropdown menu of the type of workout
        setDropDownMenuTypeOfWO()
    }

    private fun setEditTextHeight(text: CharSequence?) {
        if (text != null) {
            if (text.isNotBlank()) {
                val txt = text.toString().trim()
                if (CheckRegistrationFieldAthlete.checkHeight(txt.toInt())) {
                    //Successful data 
                    Athlete.putHeight(txt)
                    layoutAthleteEditTextHeight.error = null
                    layoutAthleteEditTextHeight.boxStrokeColor = Color.GREEN
                } else {
                    //Unsuccessful data 
                    Athlete.removeHeight()
                    layoutAthleteEditTextHeight.error = getString(R.string.invalid_height)
                    layoutAthleteEditTextHeight.errorIconDrawable = null
                }
            } else {
                //Blank field
                Athlete.removeHeight()
                layoutAthleteEditTextHeight.error = getString(R.string.invalid_height)
                layoutAthleteEditTextHeight.errorIconDrawable = null
            }
        }
    }

    private fun setEditTextWeight(text: CharSequence?) {
        if (text != null) {
            if (text.isNotBlank()) {
                val txt = text.toString().trim()
                if (CheckRegistrationFieldAthlete.checkWeight(txt.toInt())) {
                    //Successful data
                    Athlete.putWeight(txt)
                    layoutAthleteEditTextWeight.error = null
                    layoutAthleteEditTextWeight.boxStrokeColor = Color.GREEN
                } else {
                    //Unsuccessful data
                    Athlete.removeWeight()
                    layoutAthleteEditTextWeight.error = getString(R.string.invalid_weight)
                    layoutAthleteEditTextWeight.errorIconDrawable = null
                }
            } else {
                //Blank field
                Athlete.removeHeight()
                layoutAthleteEditTextWeight.error = getString(R.string.invalid_weight)
                layoutAthleteEditTextWeight.errorIconDrawable = null
            }
        }
    }

    private fun setDropDownMenuTypeOfWO() {
        val typeOfWO = arrayOf(
            getString(R.string.weight_workout_registration_athlete),
            getString(R.string.bodyweight_workout_registration_athlete)
        )
        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.drop_menu_item_list,
                typeOfWO
            )
        autoTextViewDropMenuTypeOfWorkAthlete.setAdapter(adapter)
        autoTextViewDropMenuTypeOfWorkAthlete.setOnItemClickListener { _, _, position, _ ->
            Athlete.putTypeOfWO(typeOfWO[position])
        }
    }
}
