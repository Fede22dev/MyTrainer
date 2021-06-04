package it.app.mytrainer.ui.fragments.homeAthlete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.adapter.RecycleScheduleAdapter
import kotlinx.android.synthetic.main.fragment_schedule_athlete.*

class FragmentScheduleAthlete : Fragment() {

    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private val fireStore = FireStore()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule_athlete, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        val hashMapSchedule = HashMap<String, HashMap<String, ArrayList<Any>>>()

        /*DA SPOSTARE LATO TRAINER
        hashMapSchedule["gambe"] = hashMapOf(Pair("ciao", arrayListOf("Quattro", "MInchia", 4)))
        fireStore.setSchedule(currentUserId, hashMapSchedule)*/

        fireStore.getAthlete(currentUserId) { map ->
            if (map!!["Schedule"] != "") {
                setVisibilityForSchedule()
                //DA CARICARE SU RECYCLE
                val schedule = map["Schedule"] as HashMap<*, *>

                val arrayForRecycle = ArrayList<String>()

                schedule.forEach { (key, _) ->
                    arrayForRecycle.add(key.toString())
                }

                recycleViewSchedule.adapter =
                    RecycleScheduleAdapter(context, arrayForRecycle)

            } else {
                setVisibliityForNoSchedule()
                Toast.makeText(
                    requireContext(),
                    "THERE'S NO SCHEDULE AVAILABLE FOR YOU",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        fabRefreshScheduleAthlete.setOnClickListener {

            fireStore.getAthlete(currentUserId) { map ->
                if (map!!["Schedule"] != "") {
                    setVisibilityForSchedule()
                    //DA CARICARE SU RECYCLE
                    val schedule = map["Schedule"] as HashMap<*, *>

                    val arrayForRecycle = ArrayList<String>()

                    schedule.forEach { (key, _) ->
                        arrayForRecycle.add(key.toString())
                    }

                    recycleViewSchedule.adapter =
                        RecycleScheduleAdapter(context, arrayForRecycle)

                } else {
                    setVisibliityForNoSchedule()
                    Toast.makeText(
                        requireContext(),
                        "THERE'S NO SCHEDULE AVAILABLE FOR YOU",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setVisibilityForSchedule() {
        textViewInfoBox1.visibility = View.INVISIBLE
        textViewInfoBox2.visibility = View.INVISIBLE
        textViewInfoBox3.visibility = View.INVISIBLE
        textViewScheduleReady.visibility = View.VISIBLE
        recycleViewSchedule.visibility = View.VISIBLE
    }

    private fun setVisibliityForNoSchedule() {
        textViewInfoBox1.visibility = View.VISIBLE
        textViewInfoBox2.visibility = View.VISIBLE
        textViewInfoBox3.visibility = View.VISIBLE
        textViewScheduleReady.visibility = View.INVISIBLE
        recycleViewSchedule.visibility = View.INVISIBLE
    }
}