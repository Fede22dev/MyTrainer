package it.app.mytrainer.ui.fragments.homeTrainer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.adapter.RecycleListClientTrainerAdapter
import kotlinx.android.synthetic.main.fragment_list_client_trainer.*

class FragmentListClientTrainer : Fragment() {

    private lateinit var fireStore: FireStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_client_trainer, container, false)
    }

    override fun onStart() {
        super.onStart()

        fireStore = FireStore()

        fireStore.getAllAthlete { listAthlete, result ->
            if (result) {
                textViewTrainerInfoBox1.visibility = View.INVISIBLE
                textViewTrainerInfoBox2.visibility = View.INVISIBLE
                textViewListClientReady.visibility = View.VISIBLE
                recycleViewListClient.visibility = View.VISIBLE
                Log.d("---------------", listAthlete.toString())
                recycleViewListClient.adapter =
                    RecycleListClientTrainerAdapter(requireContext(), listAthlete)
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.no_athlete_list_client),
                    Toast.LENGTH_SHORT).show()
            }
        }

        fabRefreshListClient.setOnClickListener {
            setVisibilityForRefresh()
            fireStore.getAllAthlete { listAthlete, result ->
                if (result) {
                    recycleViewListClient.adapter =
                        RecycleListClientTrainerAdapter(requireContext(), listAthlete)
                } else {
                    Toast.makeText(requireContext(),
                        getString(R.string.no_athlete_list_client),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setVisibilityForRefresh() {

    }


}