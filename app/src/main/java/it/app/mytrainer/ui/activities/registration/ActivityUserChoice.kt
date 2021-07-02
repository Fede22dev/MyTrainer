package it.app.mytrainer.ui.activities.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.ui.activities.starter.ActivityLogin
import it.app.mytrainer.ui.adapter.UserChoiceRegistrationPageAdapter
import kotlinx.android.synthetic.main.activity_user_choice.*

/**
 * Class to manage the choice above trainer/athlete
 */


class ActivityUserChoice : AppCompatActivity() {

    private lateinit var tabsName: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_choice)

        //Setting the name of tabs
        tabsName = arrayOf(
            getString(R.string.tab_user_choice_athlete),
            getString(R.string.tab_user_choice_trainer)
        )

        //Creating the bar
        setTabBar()

        //Calling the fun for the setting of the view pager
        setViewPager()
    }

    //Adjust the back press button, to avoid eventual crash
    override fun onBackPressed() {
        if (viewPagerUserChoice.currentItem == 0) {
            FireAuth.deleteCurrentUser()
            FireAuth.signOut()
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            super.onBackPressed()
            finish()
        } else {
            viewPagerUserChoice.currentItem = viewPagerUserChoice.currentItem - 1
        }
    }

    private fun setTabBar() {
        //Setting the name of the tab according to the fragment
        tabsBarUserChoice.addTab(
            tabsBarUserChoice.newTab().setText(tabsName[0])
        )
        tabsBarUserChoice.addTab(
            tabsBarUserChoice.newTab().setText(tabsName[1])
        )

        tabsBarUserChoice.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerUserChoice.currentItem = tab.position
            }

            //Method not necessary but mandatory override
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            //Method not necessary but mandatory override
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setViewPager() {
        //Set adapter of view pager
        viewPagerUserChoice.adapter =
            UserChoiceRegistrationPageAdapter(this, tabsBarUserChoice.tabCount)

        //To avoid the problem on the tab while switching manually the fragment
        TabLayoutMediator(tabsBarUserChoice, viewPagerUserChoice) { tab, position ->
            tab.text = tabsName[position]
            tabsBarUserChoice.selectTab(tab)
        }.attach()
    }
}