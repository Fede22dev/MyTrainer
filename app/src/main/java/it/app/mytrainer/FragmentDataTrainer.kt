package it.app.mytrainer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import kotlinx.android.synthetic.main.fragment_data_athlete.view.*
import kotlinx.android.synthetic.main.fragment_data_trainer.*

class FragmentDataTrainer : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_data_trainer, container, false)
        view.btnSelectDate.setOnClickListener{
            val intent = Intent(context, ActivityCalendar::class.java)
            startActivityForResult(intent, 2)
        }
        //create an object of the class with the save method
        val saveTrainer = SaveTrainer()

        //Check for the name
        val name = saveTrainer.setNameTrainer(nameTrainer.text.toString())
        //Print in the field for eventual error
        if(!name){ nameTrainer.error = getString(R.string.ErrorName) }

        //Check for the surname
        val surname = saveTrainer.setSurnameTrainer(surnameTrainer.text.toString())
        //Print in the field for eventual error
        if(!surname){ surnameTrainer.error = getString(R.string.ErrorrSurname)}

        //Check for the email
        val email = saveTrainer.setEmailTrainer(emailFieldRegistTra.text.toString())
        //Print in the field for eventual error
        if(!email){ emailFieldRegistTra.error = getString(R.string.ErrorEmail) }

        //Check for the pass
        val pass = saveTrainer.setPassTrainer(passwordFieldRegistTra.text.toString())
        //Print in the field for eventual error
        if(!pass){ passwordFieldRegistTra.error = getString(R.string.ErrorPassword) }

        //Check for the birth
        val birth = saveTrainer.setBirthTrainer(dateOfBirth.text.toString())
        //Print in the field for eventual error
        if(!birth){ dateOfBirth.error = getString(R.string.ErrorPassword) }




        return view
    }


}