package it.app.mytrainer.ui.fragments.registrationTrainer
/*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.firebase.SaveTrainer
import kotlinx.android.synthetic.main.fragment_data_athlete.*
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
        view.btnSelectDateAthlete.setOnClickListener {
            val name = saveTrainer.setNameTrainer(nameTrainer.text.toString())
            //Print in the field for eventual error
            if (!name) {
                nameTrainer.error = getString(R.string.ErrorName)
            }

            //Check for the surname
            val surname = saveTrainer.setSurnameTrainer(surnameTrainer.text.toString())
            //Print in the field for eventual error
            if (!surname) {
                surnameTrainer.error = getString(R.string.ErrorSurname)
            }

            //Check for the email
            val email = saveTrainer.setEmailTrainer(emailFieldRegistTrainer.text.toString())
            //Print in the field for eventual error
            if (!email) {
                emailFieldRegistTrainer.error = getString(R.string.ErrorEmail)
            }

            //Check for the pass
            val pass = saveTrainer.setPassTrainer(passwordFieldRegistTrainer.text.toString())
            //Print in the field for eventual error
            if (!pass) {
                passwordFieldRegistTrainer.error = getString(R.string.ErrorPassword)
            }

            //Check for the birth
            val birth = saveTrainer.setBirthTrainer(dateOfBirthAthlete.text.toString())
            //Print in the field for eventual error
            if (!birth) {
                dateOfBirthAthlete.error = getString(R.string.ErrorPassword)
            }
        }

        //Check for the name


        return view
    }

}*/