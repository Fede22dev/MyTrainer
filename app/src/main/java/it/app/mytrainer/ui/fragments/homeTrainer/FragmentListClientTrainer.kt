package it.app.mytrainer.ui.fragments.homeTrainer

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.activities.home.trainer.ActivityViewAllAthleteRegistered
import it.app.mytrainer.ui.adapter.RecyclerListClientTrainerAdapter
import kotlinx.android.synthetic.main.fragment_list_client_trainer.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import java.util.*

class FragmentListClientTrainer : Fragment() {

    private lateinit var fireStore: FireStore
    private var prefs: SharedPreferences? = null
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        fireStore = FireStore()

        prefs = requireActivity().getSharedPreferences("it.app.mytrainer",
            AppCompatActivity.MODE_PRIVATE)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_client_trainer, container, false)
    }

    override fun onStart() {
        super.onStart()

        fireStore.getAllAthleteFollowedByTrainer(currentUserId) { listAthlete, result ->
            if (result) {
                if (listAthlete.size > 0) {
                    textViewTrainerInfoBox1.visibility = View.INVISIBLE
                    textViewTrainerInfoBox2.visibility = View.INVISIBLE
                    textViewListClientReady.visibility = View.VISIBLE
                    recycleViewListClientFollowed.visibility = View.VISIBLE

                    listAthlete.sortBy { it.surnameAthlete.toLowerCase(Locale.ROOT) }

                    recycleViewListClientFollowed.adapter =
                        RecyclerListClientTrainerAdapter(requireContext(),
                            listAthlete)

                } else {
                    textViewListClientReady.visibility = View.INVISIBLE
                    recycleViewListClientFollowed.visibility = View.INVISIBLE
                    textViewTrainerInfoBox1.visibility = View.VISIBLE
                    textViewTrainerInfoBox2.visibility = View.VISIBLE
                }
            }

            addDividerRecycler()
        }

        fabSearchAllClient.setOnClickListener {
            val intent = Intent(requireContext(), ActivityViewAllAthleteRegistered::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        if (prefs!!.getBoolean("FirstRunFragmentClientTrainer", true)
        ) {
            GuideView.Builder(requireContext())
                .setTitle(getString(R.string.viewcase_title_recycle_followed_athlete))
                .setContentText(getString(R.string.viewcase_text_recycle_followed_athlete))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(textViewListClientReady)
                .setDismissType(DismissType.outside)
                .setGuideListener {

                    GuideView.Builder(requireContext())
                        .setTitle(getString(R.string.viewcase_title_fab_followed_athlete))
                        .setContentText(getString(R.string.viewcase_text_fab_followed_athlete))
                        .setTitleTextSize(16)
                        .setContentTextSize(14)
                        .setTargetView(fabSearchAllClient)
                        .setDismissType(DismissType.outside)
                        .setGuideListener {
                            prefs!!.edit().putBoolean("FirstRunFragmentClientTrainer", false)
                                .apply()
                        }
                        .build()
                        .show()
                }
                .build()
                .show()
        }
    }

    private fun addDividerRecycler() {
        recycleViewListClientFollowed.addItemDecoration(
            HorizontalDividerItemDecoration.Builder(requireContext())
                .color(Color.WHITE)
                .margin(25, 30)
                .size(2)
                .build())
    }
}