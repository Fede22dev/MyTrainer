package it.app.mytrainer.ui.fragments.homeTrainer

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.ui.adapter.RecyclerListClientTrainerAdapter
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
                recycleViewListClientFollowed.visibility = View.VISIBLE

                listAthlete.sortBy { it.surnameAthlete.toLowerCase(Locale.ROOT) }

                recycleViewListClientFollowed.adapter =
                    RecyclerListClientTrainerAdapter(requireContext(), listAthlete)

                setFastScrollerRecycler()

            } else {

                Snackbar.make(constraintFragmentListClient,
                    getString(R.string.no_athlete_list_client),
                    Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.app_foreground))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            }
        }

        fabRefreshListClient.setOnClickListener {
            fireStore.getAllAthlete { listAthlete, result ->
                if (result) {

                    listAthlete.sortBy { it.surnameAthlete.toLowerCase(Locale.ROOT) }

                    recycleViewListClientFollowed.adapter =
                        RecyclerListClientTrainerAdapter(requireContext(), listAthlete)

                    setFastScrollerRecycler()

                } else {
                    Snackbar.make(constraintFragmentListClient,
                        getString(R.string.no_athlete_list_client),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.app_foreground))
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .show()
                }
            }
        }

        addDividerRecycler()
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
                        .setTargetView(recycleViewListClientFollowed)
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

    private fun setFastScrollerRecycler() {
        val fastSc = FastScrollerBuilder(recycleViewListClientFollowed).useMd2Style()
        fastSc.disableScrollbarAutoHide()
        fastSc.build()
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