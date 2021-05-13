package it.app.mytrainer.ui.fragments.registrationTrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.firestore.SaveTrainer
import kotlinx.android.synthetic.main.fragment_data_athlete.view.*
import kotlinx.android.synthetic.main.fragment_data_trainer.*

class FragmentDataTrainer : Fragment() {

    //create an object of the class with the save method
    private val saveTrainer = SaveTrainer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_data_trainer, container, false)
        view.btnSelectDateAthl.setOnClickListener {
            val name = saveTrainer.setNameTrainer(nameTrainer.text.toString())
            //Print in the field for eventual error
            if (!name) {
                nameTrainer.error = getString(R.string.ErrorName)
            }

            //Check for the surname
            val surname = saveTrainer.setSurnameTrainer(surnameAthl.text.toString())
            //Print in the field for eventual error
            if (!surname) {
                surnameAthl.error = getString(R.string.ErrorrSurname)
            }

            //Check for the email
            val email = saveTrainer.setEmailTrainer(emailFieldRegistAthl.text.toString())
            //Print in the field for eventual error
            if (!email) {
                emailFieldRegistAthl.error = getString(R.string.ErrorEmail)
            }

            //Check for the pass
            val pass = saveTrainer.setPassTrainer(passwordFieldRegistAthl.text.toString())
            //Print in the field for eventual error
            if (!pass) {
                passwordFieldRegistAthl.error = getString(R.string.ErrorPassword)
            }

            //Check for the birth
            val birth = saveTrainer.setBirthTrainer(dateOfBirthAthl.text.toString())
            //Print in the field for eventual error
            if (!birth) {
                dateOfBirthAthl.error = getString(R.string.ErrorPassword)
            }
        }

        //Check for the name


        return view
    }

}