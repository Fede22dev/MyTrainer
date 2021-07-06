package it.app.mytrainer.ui.fragments.homeAthlete.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.models.ObjExercise
import kotlinx.android.synthetic.main.fragment_schedule_view_athlete.*
import kotlin.concurrent.thread

class FragmentScheduleViewAthlete(
    private val exercise: ObjExercise,
    private val position: Int,
) : Fragment() {

    private val TAG = "FRAGMENT_SCHEDULE_VIEW_ATHLETE"
    private var timerCountDown: CountDownTimer? = null
    private var seriesDone = 0
    private var threadRestoreTimer: Thread? = null
    private var timerStop = false
    private var timeRemained = 0L

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
        textViewNameExercise.text = exercise.nameExercise
        textViewSeriesAthlete.text = "${exercise.numSeries} X ${exercise.numReps}"

        var recoveryTimeMin = exercise.recovery!! / 60 % 60
        textViewRecoveryTimeMinutes.text = recoveryTimeMin.toString()

        var recoveryTimeSec = exercise.recovery!! % 60
        if (recoveryTimeSec < 10) {
            textViewRecoveryTimeSeconds.text = "0$recoveryTimeSec"
        } else {
            textViewRecoveryTimeSeconds.text = recoveryTimeSec.toString()
        }

        timeRemained = exercise.recovery!!.toLong()

        btnStartTimer.setOnClickListener {
            threadRestoreTimer?.interrupt()

            btnStartTimer.visibility = View.INVISIBLE
            btnStopTimer.visibility = View.VISIBLE

            if (!timerStop) {
                textViewSeriesDone.text = (++seriesDone).toString()
                timeRemained = exercise.recovery!!.toLong()
            }

            var turn = 0

            timerCountDown = object : CountDownTimer(timeRemained * 1000, 1000) {
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

                    timeRemained -= 1
                }

                override fun onFinish() {
                    textViewMinSecSeparator.text = getString(R.string.finish)
                    textViewMinSecSeparator.setTextColor(requireContext().getColor(R.color.green_button))
                    textViewRecoveryTimeSeconds.visibility = View.INVISIBLE
                    textViewRecoveryTimeMinutes.visibility = View.INVISIBLE

                    textViewRecoveryTimeSeconds.setTextColor(requireContext().getColor(R.color.white))
                    textViewRecoveryTimeMinutes.setTextColor(requireContext().getColor(R.color.white))

                    btnStartTimer.visibility = View.VISIBLE
                    btnStopTimer.visibility = View.INVISIBLE
                    timerStop = false

                    threadRestoreTimer = thread {
                        try {
                            Thread.sleep(10000)

                            Handler(Looper.getMainLooper()).post {

                                textViewMinSecSeparator.text = ":"
                                textViewMinSecSeparator.setTextColor(requireContext().getColor(R.color.white))
                                textViewRecoveryTimeSeconds.visibility = View.VISIBLE
                                textViewRecoveryTimeMinutes.visibility = View.VISIBLE
                                textViewRecoveryTimeSeconds.setTextColor(requireContext().getColor(R.color.white))
                                textViewRecoveryTimeMinutes.setTextColor(requireContext().getColor(R.color.white))
                                recoveryTimeMin = exercise.recovery!! / 60 % 60
                                textViewRecoveryTimeMinutes.text = recoveryTimeMin.toString()

                                recoveryTimeSec = exercise.recovery!! % 60
                                if (recoveryTimeSec < 10) {
                                    textViewRecoveryTimeSeconds.text = "0$recoveryTimeSec"
                                } else {
                                    textViewRecoveryTimeSeconds.text =
                                        recoveryTimeSec.toString()
                                }
                            }
                        } catch (e: InterruptedException) {
                        }
                    }
                }
            }.start()
        }

        btnStopTimer.setOnClickListener {
            btnStartTimer.visibility = View.VISIBLE
            btnStopTimer.visibility = View.INVISIBLE

            timerStop = true

            timerCountDown?.cancel()

            textViewRecoveryTimeMinutes.setTextColor(requireContext().getColor(R.color.red_button))
            textViewMinSecSeparator.setTextColor(requireContext().getColor(R.color.red_button))
            textViewRecoveryTimeSeconds.setTextColor(requireContext().getColor(R.color.red_button))
        }
    }

    override fun onDetach() {
        super.onDetach()
        timerCountDown?.cancel()
        threadRestoreTimer?.interrupt()
        Log.d(TAG, "Timer countdown stopped")
    }
}