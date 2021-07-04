package it.app.mytrainer.ui.activities.home.schedule.trainer

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.ObjSearchExercise
import it.app.mytrainer.ui.adapter.RecyclerListExerciseAdapter
import kotlinx.android.synthetic.main.activity_search_exercise.*
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import java.util.*
import kotlin.collections.ArrayList

class ActivitySearchExercise : AppCompatActivity() {

    private lateinit var listExercise: ArrayList<ObjSearchExercise>
    private lateinit var listExerciseFiltered: ArrayList<ObjSearchExercise>
    private var currentItemMenuSelectedId: Int? = null
    private lateinit var fireStore: FireStore
    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_exercise)

        //Checking if is the first time that this activity has been opened
        prefs = getSharedPreferences("it.app.mytrainer", MODE_PRIVATE)

        fireStore = FireStore()
        listExercise = ArrayList()

        //Fun to update the list of available exercise on firestore
        //fireStore.createListExerciseIta()
        //fireStore.createListExerciseEng()

        if (Locale.getDefault().displayLanguage.equals("English")) {
            fireStore.downloadAvailableExerciseEng { listEng ->
                recycleViewAvailableExercise.adapter =
                    RecyclerListExerciseAdapter(this, listEng)
                listExercise = listEng
            }
        } else {
            fireStore.downloadAvailableExerciseIta { listIta ->
                recycleViewAvailableExercise.adapter =
                    RecyclerListExerciseAdapter(this, listIta)
                listExercise = listIta
            }
        }

        setFastScrollerRecycler()

        addDividerRecycler()

        topAppBarSearchExerciseBar.setNavigationOnClickListener {
            drawerLayoutSearchExercise.open()
        }

        navigationDrawerSearchExercises.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            when (menuItem.itemId) {
                R.id.radioSearchNoFilter -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchChest -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchLegs -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchBiceps -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchTriceps -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchCalves -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchBack -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchShoulders -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchAbdominals -> {
                    filterMuscle(menuItem.title.toString())
                }

                R.id.radioSearchCardio -> {
                    filterMuscle(menuItem.title.toString())
                }
            }

            currentItemMenuSelectedId = menuItem.itemId
            drawerLayoutSearchExercise.close()
            true
        }
    }

    private fun filterMuscle(muscleFilter: String) {
        if (muscleFilter != getString(R.string.no_filter)) {
            listExerciseFiltered =
                listExercise.filter { exercises -> exercises.muscle == muscleFilter } as ArrayList<ObjSearchExercise>

            recycleViewAvailableExercise.adapter =
                RecyclerListExerciseAdapter(this, listExerciseFiltered)

        } else {

            recycleViewAvailableExercise.adapter =
                RecyclerListExerciseAdapter(this, listExercise)
        }
    }

    override fun onStart() {
        super.onStart()
        searchField.doOnTextChanged { text, _, _, _ ->
            filterNameExercise(text.toString().trim().toLowerCase(Locale.ROOT))
            if (currentItemMenuSelectedId != R.id.radioSearchNoFilter) {
                navigationDrawerSearchExercises.setCheckedItem(R.id.radioSearchNoFilter)
            }
        }
    }

    private fun filterNameExercise(nameExerciseFilter: String) {
        if (nameExerciseFilter != "") {
            listExerciseFiltered =
                listExercise.filter { exercises ->
                    exercises.nameExercise!!.contains(nameExerciseFilter)
                } as ArrayList<ObjSearchExercise>

            recycleViewAvailableExercise.adapter =
                RecyclerListExerciseAdapter(this, listExerciseFiltered)

        } else {

            recycleViewAvailableExercise.adapter =
                RecyclerListExerciseAdapter(this, listExercise)
        }
    }

    private fun setFastScrollerRecycler() {
        val fastSc = FastScrollerBuilder(recycleViewAvailableExercise).useMd2Style()
        fastSc.disableScrollbarAutoHide()
        fastSc.build()
    }

    private fun addDividerRecycler() {
        recycleViewAvailableExercise.addItemDecoration(
            HorizontalDividerItemDecoration.Builder(this)
                .color(Color.WHITE)
                .margin(0, 30)
                .size(2)
                .build())
    }

    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunSearchExercise", true)
        ) {
            GuideView.Builder(this)
                .setTitle(getString(R.string.viewcase_title_search_exercise_filter))
                .setContentText(getString(R.string.viewcase_text_search_exercise_filter))
                .setTargetView(topAppBarSearchExerciseBar)
                .setDismissType(DismissType.outside)
                .setGuideListener {

                    GuideView.Builder(this)
                        .setTitle(getString(R.string.viewcase_titlekeyboard_search_exercise_filter))
                        .setContentText(getString(R.string.viewcase_textkeyboard_search_exercise_filter))
                        .setTitleTextSize(16)
                        .setContentTextSize(14)
                        .setTargetView(searchField)
                        .setDismissType(DismissType.outside)
                        .setGuideListener {
                            prefs!!.edit().putBoolean("FirstRunSearchExercise", false).apply()
                        }
                        .build()
                        .show()

                }
                .build()
                .show()
        }
    }
}