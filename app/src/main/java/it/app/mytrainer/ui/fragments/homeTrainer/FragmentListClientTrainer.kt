package it.app.mytrainer.ui.fragments.homeTrainer

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.adapter.RecycleListClientTrainerAdapter
import kotlinx.android.synthetic.main.fragment_list_client_trainer.*
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import java.util.*

class FragmentListClientTrainer : Fragment() {

    private lateinit var fireStore: FireStore
    private var prefs: SharedPreferences? = null

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

        fireStore.getAllAthlete { listAthlete, result ->
            if (result) {
                textViewTrainerInfoBox1.visibility = View.INVISIBLE
                textViewTrainerInfoBox2.visibility = View.INVISIBLE
                textViewListClientReady.visibility = View.VISIBLE
                recycleViewListClient.visibility = View.VISIBLE

                listAthlete.sortBy { it.surnameAthlete.toLowerCase(Locale.ROOT) }

                recycleViewListClient.adapter =
                    RecycleListClientTrainerAdapter(requireContext(), listAthlete)

                setFastScrollerRecycler()

            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.no_athlete_list_client),
                    Toast.LENGTH_SHORT).show()
            }
        }

        fabRefreshListClient.setOnClickListener {
            fireStore.getAllAthlete { listAthlete, result ->
                if (result) {

                    listAthlete.sortBy { it.surnameAthlete.toLowerCase(Locale.ROOT) }

                    recycleViewListClient.adapter =
                        RecycleListClientTrainerAdapter(requireContext(), listAthlete)

                    setFastScrollerRecycler()

                } else {
                    Toast.makeText(requireContext(),
                        getString(R.string.no_athlete_list_client),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunFragmentClientTrainer", true)
        ) {
            GuideView.Builder(requireContext())
                .setTitle(getString(R.string.viewcase_title_fab_fragment_list_client))
                .setContentText(getString(R.string.viewcase_text_fab_fragment_list_client))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(fabRefreshListClient)
                .setDismissType(DismissType.outside)
                .setGuideListener {

                    GuideView.Builder(requireContext())
                        .setTitle(getString(R.string.viewcase_title_recycle_fragment_list_client))
                        .setContentText(getString(R.string.viewcase_text_recycle_fragment_list_client))
                        .setTitleTextSize(16)
                        .setContentTextSize(14)
                        .setTargetView(recycleViewListClient)
                        .setDismissType(DismissType.outside)
                        .setGuideListener {
                            // prefs!!.edit().putBoolean("FirstRunFragmentClientTrainer", false).apply()
                        }
                        .build()
                        .show()

                }
                .build()
                .show()
        }

    }

    private fun setFastScrollerRecycler() {
        val fastSc = FastScrollerBuilder(recycleViewListClient).useMd2Style()
        fastSc.disableScrollbarAutoHide()
        fastSc.build()
    }
}