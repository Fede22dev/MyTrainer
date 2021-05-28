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

class ActivityUserChoice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_choice)

        val tabsName = arrayOf(
            getString(R.string.tab_user_choice_athlete),
            getString(R.string.tab_user_choice_trainer)
        )

        tabsBarUserChoice.addTab(
            tabsBarUserChoice.newTab().setText(tabsName[0])
        )
        tabsBarUserChoice.addTab(
            tabsBarUserChoice.newTab().setText(tabsName[1])
        )

        viewPagerUserChoice.adapter =
            UserChoiceRegistrationPageAdapter(this, tabsBarUserChoice.tabCount)

        TabLayoutMediator(tabsBarUserChoice, viewPagerUserChoice) { tab, position ->
            tab.text = tabsName[position]
            tabsBarUserChoice.selectTab(tab)
        }.attach()

        tabsBarUserChoice.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerUserChoice.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

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
}