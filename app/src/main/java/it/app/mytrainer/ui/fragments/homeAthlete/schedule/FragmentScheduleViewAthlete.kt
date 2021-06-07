package it.app.mytrainer.ui.fragments.homeAthlete.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import kotlinx.android.synthetic.main.fragment_schedule_view_athlete.*
import java.util.*

class FragmentScheduleViewAthlete(
    private val exercise: ArrayList<String>,
    private val position: Int,
) : Fragment() {

    private val TAG = "FRAGMENT_SCHEDULE_VIEW_ATHLETE"
    private var timerCountDown: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_view_athlete, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewNumExercises.text = "${getString(R.string.exercise)} ${position + 1}"
        textViewNameExercise.text = exercise[0]
        textViewSeriesAthlete.text = exercise[1]

        val recoveryTimeMin = exercise[2].toInt() / 60 % 60
        textViewRecoveryTimeMinutes.text = recoveryTimeMin.toString()

        val recoveryTimeSec = exercise[2].toInt() % 60
        if (recoveryTimeSec < 10) {
            textViewRecoveryTimeSeconds.text = "0$recoveryTimeSec"
        } else {
            textViewRecoveryTimeSeconds.text = recoveryTimeSec.toString()
        }

        btnTimer.setOnClickListener {
            btnTimer.visibility = View.INVISIBLE

            var turn = 0

            timerCountDown = object : CountDownTimer(exercise[2].toLong() * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    textViewMinSecSeparator.text = ":"
                    textViewRecoveryTimeSeconds.visibility = View.VISIBLE
                    textViewRecoveryTimeMinutes.visibility = View.VISIBLE

                    turn = if (turn == 0) {
                        textViewRecoveryTimeMinutes.setTextColor(requireContext().getColor(R.color.green_button))
                        textViewMinSecSeparator.setTextColor(requireContext().getColor(R.color.green_button))
                        textViewRecoveryTimeSeconds.setTextColor(requireContext().getColor(R.color.green_button))
                        1
                    } else {
                        textViewRecoveryTimeMinutes.setTextColor(requireContext().getColor(R.color.white))
                        textViewMinSecSeparator.setTextColor(requireContext().getColor(R.color.white))
                        textViewRecoveryTimeSeconds.setTextColor(requireContext().getColor(R.color.white))
                        0
                    }

                    val sec = ((millisUntilFinished / 1000) % 60)
                    if (sec < 10) {
                        textViewRecoveryTimeSeconds.text = "0$sec"
                    } else {
                        textViewRecoveryTimeSeconds.text = sec.toString()
                    }

                    textViewRecoveryTimeMinutes.text =
                        ((millisUntilFinished / (1000 * 60)) % 60).toString()
                }

                override fun onFinish() {
                    textViewMinSecSeparator.text = getString(R.string.finish)
                    textViewMinSecSeparator.setTextColor(requireContext().getColor(R.color.green_button))
                    textViewRecoveryTimeSeconds.visibility = View.INVISIBLE
                    textViewRecoveryTimeMinutes.visibility = View.INVISIBLE
                    btnTimer.visibility = View.VISIBLE
                }
            }.start()
        }
    }

    override fun onDetach() {
        super.onDetach()
        timerCountDown?.cancel()
        Log.d(TAG, "Timer countdown stopped")
    }
}