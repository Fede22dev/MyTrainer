package it.app.mytrainer.ui.fragments.registrationAthlete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.models.Athlete
import kotlinx.android.synthetic.main.fragment_extra_info_athlete.*
import kotlinx.android.synthetic.main.fragment_extra_info_athlete.view.*

class FragmentExtraInfoAthlete : Fragment() {

    private val listToSave = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_extra_info_athlete, container, false)

        //Listener to manage the slider and change text view level
        view.sliderLevelAthlete.addOnChangeListener { _, value, _ ->
            when (value) {
                1f -> {
                    val textBeginner = getString(R.string.beginner_athlete)
                    textViewLevelAthlete.text = textBeginner
                    Athlete.putLevel(textBeginner)
                }
                2f -> {
                    val textIntermediate = getString(R.string.intermediate_athlete)
                    textViewLevelAthlete.text = textIntermediate
                    Athlete.putLevel(textIntermediate)
                }
                3f -> {
                    val textAdvanced = getString(R.string.advanced_athlete)
                    textViewLevelAthlete.text = textAdvanced
                    Athlete.putLevel(textAdvanced)
                }
                4f -> {
                    val textVeteran = getString(R.string.veteran_athlete)
                    textViewLevelAthlete.text = textVeteran
                    Athlete.putLevel(textVeteran)
                }
            }
        }

        view.checkbox1Athlete.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to checkbox being checked/unchecked
            managerList(buttonView, isChecked)
        }

        view.checkbox2Athlete.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to checkbox being checked/unchecked
            managerList(buttonView, isChecked)
        }

        view.checkbox3Athlete.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to checkbox being checked/unchecked
            managerList(buttonView, isChecked)
        }

        view.checkbox4Athlete.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to checkbox being checked/unchecked
            managerList(buttonView, isChecked)
        }

        view.checkbox5Athlete.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to checkbox being checked/unchecked
            managerList(buttonView, isChecked)
        }

        view.checkbox6Athlete.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to checkbox being checked/unchecked
            managerList(buttonView, isChecked)
        }

        view.checkbox7Athlete.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to checkbox being checked/unchecked
            managerList(buttonView, isChecked)
        }

        view.checkbox8Athlete.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to checkbox being checked/unchecked
            managerList(buttonView, isChecked)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        //DROPDOWN MENU DAYS
        val days = arrayOf("1", "2", "3", "4", "5", "6", "7")
        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.drop_menu_item_list,
                days
            )
        autoTextViewDropMenuDaysOfWorkOut.setAdapter(adapter)
        autoTextViewDropMenuDaysOfWorkOut.setOnItemClickListener { _, _, position, _ ->
            Athlete.putNumberOfWOWeek(days[position])
        }
    }

    private fun managerList(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            listToSave.add(buttonView.text.toString())
            Athlete.putEquipment(listToSave)
        } else {
            listToSave.remove(buttonView.text.toString())
            if (listToSave.size > 0) {
                Athlete.putEquipment(listToSave)
            } else {
                Athlete.removeEquipment()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_toast_equipment_athlete),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}