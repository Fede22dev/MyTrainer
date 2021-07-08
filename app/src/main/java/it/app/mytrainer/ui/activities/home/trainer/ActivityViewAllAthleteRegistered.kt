package it.app.mytrainer.ui.activities.home.trainer

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.ObjAthlete
import it.app.mytrainer.ui.adapter.RecyclerListClientTrainerAdapter
import kotlinx.android.synthetic.main.activity_view_all_athlete_registered.*
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import java.util.*

/**
 * Class used see all the athlete registered
 */

class ActivityViewAllAthleteRegistered : AppCompatActivity() {

    private lateinit var fireStore: FireStore
    private var prefs: SharedPreferences? = null
    private var listAthleteNotFiltered = ArrayList<ObjAthlete>()
    private var listAthleteFiltered = ArrayList<ObjAthlete>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_athlete_registered)

        fireStore = FireStore()

        prefs = this.getSharedPreferences("it.app.mytrainer", MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()

        //Getting all the athlete registered with the method created,
        //which send back a list
        fireStore.getAllAthlete { listAthlete, result ->
            if (result) {
                listAthleteNotFiltered = listAthlete

                if (listAthlete.size > 0) {

                    listAthlete.sortBy { it.surnameAthlete.toLowerCase(Locale.ROOT) }

                    recycleViewViewAllAthleteRegistered.adapter =
                        RecyclerListClientTrainerAdapter(this, listAthlete)

                    setFastScrollerRecycler()
                } else {

                    Snackbar.make(constraintActivityViewAllAthlete,
                        getString(R.string.no_athlete_list_client),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()
                }
            }
        }

        //Downloading again the latest list
        fabRefreshListClientViewAllAthleteRegistered.setOnClickListener {
            fireStore.getAllAthlete { listAthlete, result ->
                if (result) {

                    listAthleteNotFiltered = listAthlete

                    listAthlete.sortBy { it.surnameAthlete.toLowerCase(Locale.ROOT) }

                    recycleViewViewAllAthleteRegistered.adapter =
                        RecyclerListClientTrainerAdapter(this, listAthlete)

                    setFastScrollerRecycler()

                } else {
                    Snackbar.make(constraintActivityViewAllAthlete,
                        getString(R.string.no_athlete_list_client),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()
                }
            }
        }

        addDividerRecycler()

        //Fun for the arrow back
        topAppBarViewAllAthleteRegistered.setNavigationOnClickListener {
            finish()
        }

        //Filtering the athlete
        editTextSearchAthleteRegistered.doOnTextChanged { text, _, _, _ ->
            filterNameAthlete(text.toString().trim().toLowerCase(Locale.ROOT))
        }
    }

    override fun onResume() {
        super.onResume()
        editTextSearchAthleteRegistered.setText("")

        if (prefs!!.getBoolean("FirstRunViewAllAthleteRegistered", true)
        ) {
            GuideView.Builder(this)
                .setTitle(getString(R.string.viewcase_title_fab_fragment_list_client))
                .setContentText(getString(R.string.viewcase_text_fab_fragment_list_client))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(fabRefreshListClientViewAllAthleteRegistered)
                .setDismissType(DismissType.outside)
                .setGuideListener {

                    GuideView.Builder(this)
                        .setTitle(getString(R.string.viewcase_title_recycle_fragment_list_client))
                        .setContentText(getString(R.string.viewcase_text_recycle_fragment_list_client))
                        .setTitleTextSize(16)
                        .setContentTextSize(14)
                        .setTargetView(editTextSearchAthleteRegistered)
                        .setDismissType(DismissType.outside)
                        .setGuideListener {
                            prefs!!.edit().putBoolean("FirstRunViewAllAthleteRegistered", false)
                                .apply()
                        }
                        .build()
                        .show()

                }
                .build()
                .show()
        }
    }

    //Setting the fastscroll on  the recycler
    private fun setFastScrollerRecycler() {
        val fastSc = FastScrollerBuilder(recycleViewViewAllAthleteRegistered).useMd2Style()
        fastSc.disableScrollbarAutoHide()
        fastSc.build()
    }

    private fun addDividerRecycler() {
        recycleViewViewAllAthleteRegistered.addItemDecoration(
            HorizontalDividerItemDecoration.Builder(this)
                .color(Color.WHITE)
                .margin(25, 30)
                .size(2)
                .build())
    }

    private fun filterNameAthlete(filter: String) {
        //If the filter is empty we load the whole list,
        //else we check all the name which contains the word insert
        if (filter != "") {
            listAthleteFiltered =
                listAthleteNotFiltered.filter { athlete ->
                    val nameSurname = athlete.nameAthlete + athlete.surnameAthlete
                    nameSurname.toLowerCase(Locale.ROOT).contains(filter)
                } as ArrayList<ObjAthlete>

            recycleViewViewAllAthleteRegistered.adapter =
                RecyclerListClientTrainerAdapter(this, listAthleteFiltered)

        } else {

            recycleViewViewAllAthleteRegistered.adapter =
                RecyclerListClientTrainerAdapter(this, listAthleteNotFiltered)
        }
    }
}