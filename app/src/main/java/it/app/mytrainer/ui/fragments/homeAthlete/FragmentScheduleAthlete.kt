package it.app.mytrainer.ui.fragments.homeAthlete

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.adapter.RecyclerScheduleAdapter
import kotlinx.android.synthetic.main.fragment_schedule_athlete.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType

class FragmentScheduleAthlete : Fragment() {

    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private val fireStore = FireStore()
    private var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        prefs = requireContext().getSharedPreferences("it.app.mytrainer", MODE_PRIVATE)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_athlete, container, false)
    }

    override fun onStart() {
        super.onStart()

        fireStore.getNameDayScheduleAthlete(currentUserId) { listOfDays ->
            if (listOfDays.isNotEmpty()) {
                setVisibilityForSchedule()

                recycleViewSchedule.adapter =
                    RecyclerScheduleAdapter(requireContext(), listOfDays)

            } else {
                setVisibilityForNoSchedule()

                Snackbar.make(constraintFragmentScheduleAthlete,
                    getString(R.string.no_schedule_available),
                    Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(),
                        R.color.app_foreground))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            }
        }

        addDividerRecycler()
    }

    override fun onResume() {
        super.onResume()

        fireStore.setListenerNameDayScheduleChange(currentUserId) { listOfDays ->
            if (listOfDays.isNotEmpty()) {
                setVisibilityForSchedule()

                recycleViewSchedule.adapter =
                    RecyclerScheduleAdapter(requireContext(), listOfDays)

            } else {
                setVisibilityForNoSchedule()

                Snackbar.make(constraintFragmentScheduleAthlete,
                    getString(R.string.no_schedule_available),
                    Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(),
                        R.color.app_foreground))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            }
        }

        if (prefs!!.getBoolean("FirstRunFragmentScheduleAthlete", true)
        ) {
            GuideView.Builder(requireContext())
                .setTitle(getString(R.string.viewcase_title_recycle_fragment_schedule_athlete))
                .setContentText(getString(R.string.viewcase_text_recycle_fragment_schedule_athlete))
                .setTargetView(textViewScheduleReady)
                .setDismissType(DismissType.outside)
                .setGuideListener {
                    prefs!!.edit().putBoolean("FirstRunFragmentScheduleAthlete", false)
                        .apply()
                }
                .build()
                .show()
        }
    }

    override fun onPause() {
        super.onPause()
        fireStore.removeListenerNameDayScheduleChange()
    }

    private fun setVisibilityForSchedule() {
        textViewInfoBox1.visibility = View.INVISIBLE
        textViewInfoBox2.visibility = View.INVISIBLE
        textViewScheduleReady.visibility = View.VISIBLE
        recycleViewSchedule.visibility = View.VISIBLE
    }

    private fun setVisibilityForNoSchedule() {
        textViewInfoBox1.visibility = View.VISIBLE
        textViewInfoBox2.visibility = View.VISIBLE
        textViewScheduleReady.visibility = View.INVISIBLE
        recycleViewSchedule.visibility = View.INVISIBLE
    }

    private fun addDividerRecycler() {
        recycleViewSchedule.addItemDecoration(
            HorizontalDividerItemDecoration.Builder(requireContext())
                .color(Color.WHITE)
                .margin(25, 30)
                .size(2)
                .build())
    }
}