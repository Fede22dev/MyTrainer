package it.app.mytrainer.ui.activities.home.schedule.trainer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.models.ObjSearchExercise
import it.app.mytrainer.ui.adapter.RecycleListExerciseAdapter
import kotlinx.android.synthetic.main.activity_search_exercise.*
import java.util.*
import kotlin.collections.ArrayList

class ActivitySearchExercise : AppCompatActivity() {

    private lateinit var listExercise: ArrayList<ObjSearchExercise>
    private lateinit var fireStore: FireStore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_exercise)

        fireStore = FireStore()
        listExercise = ArrayList()

        //Fun to update the list of available exercise on firestore
        fireStore.createListExerciseIta()
        fireStore.createListExerciseEng()

        if (Locale.getDefault().displayLanguage.equals("English")) {
            fireStore.downloadAvailableExerciseEng { listEng ->
                recycleViewAvailableExercise.adapter =
                    RecycleListExerciseAdapter(this, listEng)
                listExercise = listEng
            }
        } else {
            fireStore.downloadAvailableExerciseIta { listIta ->
                recycleViewAvailableExercise.adapter =
                    RecycleListExerciseAdapter(this, listIta)
                listExercise = listIta
            }
        }

        topAppBarSearchExerciseBar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        nav.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.radioSearchNoFilter ->{
                    filterMuscle(getString(R.string.no_filter))
                }

                R.id.radioSearchChest ->{

                }

                R.id.radioSearchLegs ->{

                }

                R.id.radioSearchBiceps->{

                }

                R.id.radioSearchTriceps ->{

                }

                R.id.radioSearchCalves->{

                }

                R.id.radioSearchBack ->{

                }

                R.id.radioSearchShoulders->{

                }

                R.id.radioSearchAbdominals->{

                }

                R.id.radioSearchCardio ->{

                }
            }

            drawerLayout.close()
            true
        }

    }

    private fun filterMuscle(muscle: String) {
        
    }
}