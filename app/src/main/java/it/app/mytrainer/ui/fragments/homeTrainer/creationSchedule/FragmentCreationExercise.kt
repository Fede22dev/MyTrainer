package it.app.mytrainer.ui.fragments.homeTrainer.creationSchedule

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.models.ObjExercise
import it.app.mytrainer.ui.activities.home.schedule.trainer.ActivityCreationSchedule
import it.app.mytrainer.ui.activities.home.schedule.trainer.ActivitySearchExercise
import kotlinx.android.synthetic.main.fragment_creation_exercise.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType

class FragmentCreationExercise(private val position: Int) :
    Fragment() {

    private val TAG = "FRAGMENT_CREATION_EXERCISE"
    private lateinit var exercise: ObjExercise
    private var recovery = 0
    private var editMin = 0
    private var editSec = 0
    private var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        prefs = requireActivity().getSharedPreferences("it.app.mytrainer",
            AppCompatActivity.MODE_PRIVATE)

        exercise = ObjExercise(null, null, null, null)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creation_exercise, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        val restoreExercise = ActivityCreationSchedule.getExercise(position)
        if (restoreExercise != null) {
            editTextNameExerciseCreationExercise.setText(restoreExercise.nameExercise)
            editTextSeriesCreationExercise.setText(restoreExercise.numSeries)
            editTextRepsCreationExercise.setText(restoreExercise.numReps)
            editTextRecoveryMinutCreationExercise.setText((restoreExercise.recovery!! / 60 % 60).toString())

            val sec = restoreExercise.recovery!! % 60
            if (sec < 10) {
                editTextRecoverySecondCreationExercise.setText("0$sec")
            } else {
                editTextRecoverySecondCreationExercise.setText(sec.toString())
            }

            exercise.nameExercise = restoreExercise.nameExercise
            exercise.numSeries = restoreExercise.numSeries
            exercise.numReps = restoreExercise.numReps
            exercise.recovery = restoreExercise.recovery
        }

        textViewNumExercisesCreationExercise.text =
            "${getString(R.string.exercise)} ${position + 1}"

        editTextNameExerciseCreationExercise.doAfterTextChanged { text ->
            exercise.nameExercise = text.toString().trim()
            exerciseManager()
        }

        editTextSeriesCreationExercise.doAfterTextChanged { text ->
            exercise.numSeries = text.toString().trim()
            exerciseManager()
        }

        editTextRepsCreationExercise.doAfterTextChanged { text ->
            exercise.numReps = text.toString().trim()
            exerciseManager()
        }

        editTextRecoveryMinutCreationExercise.doAfterTextChanged { text ->
            val txt = text.toString().trim()
            if (txt != "") {
                recoveryCalculate(txt.toInt(), "Min")
            }
            exerciseManager()
        }

        editTextRecoverySecondCreationExercise.doAfterTextChanged { text ->
            val txt = text.toString().trim()
            if (txt != "") {
                recoveryCalculate(txt.toInt(), "Sec")
            }
            exerciseManager()
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunFragmentCreationExercise", true)
        ) {
            GuideView.Builder(requireContext())
                .setTitle(getString(R.string.viewcase_title_fab_fragment_creation_exercises))
                .setContentText(getString(R.string.viewcase_text_fab_fragment_creation_exercise))
                .setTargetView(fabSearchExerciseCreationExercise)
                .setDismissType(DismissType.outside)
                .setGuideListener {
                    // prefs!!.edit().putBoolean("FirstRunFragmentCreationExercise", false).apply()
                }
                .build()
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // There are no request codes
                    editTextNameExerciseCreationExercise.setText(result.data?.getStringExtra("NameExercise"))
                }
            }

        fabSearchExerciseCreationExercise.setOnClickListener {
            val intent = Intent(requireContext(), ActivitySearchExercise::class.java)
            resultLauncher.launch(intent)
        }
    }

    //Checking if all the fields are null or not
    private fun exerciseManager() {
        if (exercise.nameExercise != null && exercise.numSeries != null && exercise.numReps != null && exercise.recovery != null) {
            ActivityCreationSchedule.removeExercise(position)
            ActivityCreationSchedule.addExercise(position, exercise)
        }
    }

    private fun recoveryCalculate(time: Int, unity: String) {

        if (unity == "Min" && time in 0..99) {
            if (editMin == 0) {
                recovery += time * 60
                exercise.recovery = recovery
                editMin = time * 60
                editTextRecoveryMinutCreationExercise.error = null
                editTextRecoverySecondCreationExercise.error = null
            } else {
                recovery -= editMin
                recovery += time * 60
                exercise.recovery = recovery
                editMin = time * 60
                editTextRecoveryMinutCreationExercise.error = null
                editTextRecoverySecondCreationExercise.error = null
            }

        } else if (unity == "Sec" && time in 0..59) {
            if (editSec == 0) {
                recovery += time
                exercise.recovery = recovery
                editSec = time
                editTextRecoveryMinutCreationExercise.error = null
                editTextRecoverySecondCreationExercise.error = null
            } else {
                recovery -= editSec
                recovery += time
                exercise.recovery = recovery
                editSec = time
                editTextRecoveryMinutCreationExercise.error = null
                editTextRecoverySecondCreationExercise.error = null
            }

        } else {
            exercise.recovery = null
            editTextRecoveryMinutCreationExercise.error = ""
            editTextRecoverySecondCreationExercise.error = ""
        }
    }
}