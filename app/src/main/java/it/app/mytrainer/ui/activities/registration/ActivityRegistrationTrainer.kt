package it.app.mytrainer.ui.activities.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import it.app.mytrainer.R
import it.app.mytrainer.models.Trainer
import it.app.mytrainer.ui.adapter.TrainerRegistrationPageAdapter
import it.app.mytrainer.utils.CheckRegistrationFieldUser
import kotlinx.android.synthetic.main.activity_registration_trainer.*
import kotlinx.android.synthetic.main.fragment_data_trainer.*

class ActivityRegistrationTrainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_trainer)

        //Creating the name of the tab
        val tabsName = arrayOf(
            getString(R.string.frag1RegistrationTrainerName),
            getString(R.string.frag2RegistrationTrainerName)
        )

        //Setting the name of the tab according to the fragment
        tabsBarTrainer.addTab(
            tabsBarTrainer.newTab().setText(tabsName[0])
        )
        tabsBarTrainer.addTab(
            tabsBarTrainer.newTab().setText(tabsName[1])
        )

        //Insert
        viewPagerTrainer.adapter = TrainerRegistrationPageAdapter(this, tabsBarTrainer.tabCount)

        //To avoid the problem on the tab while switching manually the fragment
        TabLayoutMediator(tabsBarTrainer, viewPagerTrainer) { tab, position ->
            tab.text = tabsName[position]
            tabsBarTrainer.selectTab(tab)
        }.attach()

        tabsBarTrainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerTrainer.currentItem = tab.position
                //To show the save button at the last page
                if (tab.position == 1) {
                    floatActionBtnSaveTrainer.visibility = View.VISIBLE
                } else {
                    floatActionBtnSaveTrainer.visibility = View.INVISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //Setting the action of the saving on FS and turning visible the progress bar
        floatActionBtnSaveTrainer.setOnClickListener {

            if (Trainer.hashMapReadyToSave()) {
                floatActionBtnSaveTrainer.visibility = View.INVISIBLE
                viewPagerTrainer.visibility = View.INVISIBLE
                tabsBarTrainer.visibility = View.INVISIBLE
                progressBarRegistrationTrainer.visibility = View.VISIBLE
                Toast.makeText(
                    this,
                    getString(R.string.RegistrationToast),
                    Toast.LENGTH_LONG
                ).show()


                //CREA ACCOUNT AUTH


                //SALVARE SU FIRESTORE

                Trainer.printHashMap()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.RegistrationErrorToast),
                    Toast.LENGTH_LONG
                ).show()
                Trainer.printHashMap()
            }
        }
    }

    //Adjust the back press button
    override fun onBackPressed() {
        if (viewPagerTrainer.currentItem == 0) {
            Trainer.clearHashMap()
            super.onBackPressed()
        } else {
            viewPagerTrainer.currentItem = viewPagerTrainer.currentItem - 1
        }
    }
}