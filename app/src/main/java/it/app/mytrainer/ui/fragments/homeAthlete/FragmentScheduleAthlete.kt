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
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_athlete, container, false)
    }

    override fun onStart() {
        super.onStart()
        val hashMapSchedule = HashMap<String, HashMap<String, ArrayList<String>>>()


        /**DA SPOSTARE LATO TRAINER*/
        hashMapSchedule["gambe"] = hashMapOf(Pair("1", arrayListOf("Squat", "4 X 18", "62")),
            Pair("2", arrayListOf("Pressa", "3 X 10", "37")),
            Pair("3", arrayListOf("Salto corda", "9 X 10", "93")))

        hashMapSchedule["petto"] = hashMapOf(Pair("1", arrayListOf("panca", "4x12", "94")),
            Pair("2", arrayListOf("croci", "3x76", "11")),
            Pair("3", arrayListOf("spinte", "7x34", "75")))

        hashMapSchedule["braccia"] = hashMapOf(Pair("1", arrayListOf("hammer", "4x10", "65")),
            Pair("2", arrayListOf("curl", "4x8", "142")),
            Pair("3", arrayListOf("cavi", "4x14", "45")))

        fireStore.setSchedule(currentUserId, hashMapSchedule)


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
                    getString(R.string.no_schedule_available),
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
                        getString(R.string.no_schedule_available),
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