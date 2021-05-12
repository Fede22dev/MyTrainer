package it.app.mytrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class ActivityRegistrationTrainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_trainer)
    }

    fun onClickDataTra(v: View){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FragmentDataTrainer>(R.id.fragmentContainerTra)
        }
    }

    fun onClickInfoTra(v: View){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FragmentInfoTrainer>(R.id.fragmentContainerTra)
        }
    }


}