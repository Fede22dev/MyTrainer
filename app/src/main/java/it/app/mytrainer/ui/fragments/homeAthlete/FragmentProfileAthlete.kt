package it.app.mytrainer.ui.fragments.homeAthlete

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.utils.CheckRegistrationFieldAthlete
import kotlinx.android.synthetic.main.fragment_profile_athlete.*
import kotlinx.android.synthetic.main.fragment_profile_athlete.view.*
import java.io.ByteArrayOutputStream

class FragmentProfileAthlete : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mapAthlete: Map<String, Any>
    private lateinit var storage: StorageReference
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid
    private val TAG = "FRAGMENT_HOME_PROFILE_ATHLETE"
    private var listForCheckBox = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_athlete, container, false)

        storage = Firebase.storage.reference

        if (currentUserId != null) {
            storage.child("Photos").child(currentUserId).downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this).load(uri).into(view.imageViewPersonalAthlete)
                Log.d(TAG, "Found and downloaded the target picture for: $currentUserId")
            }
        }

        view.buttonCameraAthlete.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val fireStore = FireStore()

        currentUserId?.let {
            fireStore.getAthlete(it) { athlete ->
                if (athlete != null) {
                    mapAthlete = athlete
                    nameFieldAthleteEditable.setText(athlete["Name"].toString())
                    surnameFieldAthleteEditable.setText(athlete["Surname"].toString())
                    textViewDateDataProfileAthlete.text = athlete["BirthDate"].toString()
                    heightFieldAthleteEditable.setText(athlete["Height"].toString())
                    layoutAthleteEditTextHeightEditable.endIconMode = TextInputLayout.END_ICON_NONE
                    weightFieldAthleteEditable.setText(athlete["Weight"].toString())
                    layoutAthleteEditTextWeightEditable.endIconMode = TextInputLayout.END_ICON_NONE
                    autoTextViewDropMenuTypeOfWOProfileAthlete.setText(athlete["TypeOfWO"].toString())
                    autoTextViewDropMenuGoalProfileAthlete.setText(athlete["Goal"].toString())
                    autoTextViewDropMenuLevelProfileAthlete.setText(athlete["Level"].toString())
                    autoTextViewDropMenuNumOfWOProfileAthlete.setText(athlete["DaysOfWorkout"].toString())

                    val checkBox = athlete["Equipment"] as ArrayList<*>
                    checkBox.forEach { equip ->
                        Log.d(TAG, "---------------------------------$equip")
                        when (equip.toString()) {
                            "Dumbells", "Manubri" -> {
                                checkbox1ProfileAthlete.isChecked = true
                            }

                            "Rope", "Corde" -> {
                                checkbox2ProfileAthlete.isChecked = true
                            }

                            "Rack" -> {
                                checkbox3ProfileAthlete.isChecked = true
                            }

                            "Benches", "Panca" -> {
                                checkbox4ProfileAthlete.isChecked = true
                            }

                            "Pull-up bar" -> {
                                checkbox5ProfileAthlete.isChecked = true
                            }

                            "Dip station" -> {
                                checkbox6ProfileAthlete.isChecked = true
                            }

                            "Gym membership", "Abbonamento palestra" -> {
                                checkbox8ProfileAthlete.isChecked = true
                            }

                            "Barbell and discs", "Bilanciere e dischi" -> {
                                checkbox7ProfileAthlete.isChecked = true
                            }
                        }
                    }
                }
            }

            floatingActionButtonEditProfileAthlete.setOnClickListener {

                floatingActionButtonSaveProfileAthlete.visibility = View.VISIBLE
                floatingActionButtonEditProfileAthlete.visibility = View.INVISIBLE

                layoutAthleteEditTextNameEditable.alpha = 0.5f
                layoutAthleteEditTextSurnameEditable.alpha = 0.5f
                textViewDateDataProfileAthlete.alpha = 0.5f

                heightFieldAthleteEditable.isEnabled = true
                layoutAthleteEditTextHeightEditable.endIconMode =
                    TextInputLayout.END_ICON_CLEAR_TEXT
                weightFieldAthleteEditable.isEnabled = true
                layoutAthleteEditTextWeightEditable.endIconMode =
                    TextInputLayout.END_ICON_CLEAR_TEXT

                checkbox1ProfileAthlete.isEnabled = true
                checkbox2ProfileAthlete.isEnabled = true
                checkbox3ProfileAthlete.isEnabled = true
                checkbox4ProfileAthlete.isEnabled = true
                checkbox5ProfileAthlete.isEnabled = true
                checkbox6ProfileAthlete.isEnabled = true
                checkbox7ProfileAthlete.isEnabled = true
                checkbox8ProfileAthlete.isEnabled = true


                //SETTINGS FOR DROPDOWN TYPEOFWO
                autoTextViewDropMenuTypeOfWOProfileAthlete.isEnabled = true
                layoutDropMenuTypeOfWOProfileAthlete.endIconMode =
                    TextInputLayout.END_ICON_DROPDOWN_MENU

                val typeOfWO = arrayOf(
                    getString(R.string.weight_workout_registration_athlete),
                    getString(R.string.bodyweight_workout_registration_athlete)
                )

                val adapterTypeOfWO =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.drop_menu_item_list,
                        typeOfWO
                    )

                autoTextViewDropMenuTypeOfWOProfileAthlete.setAdapter(adapterTypeOfWO)

                //SETTINGS FOR DROPDOWN GOAL
                autoTextViewDropMenuGoalProfileAthlete.isEnabled = true
                layoutDropMenuGoalProfileAthlete.endIconMode =
                    TextInputLayout.END_ICON_DROPDOWN_MENU

                val goal = arrayOf(
                    getString(R.string.fat_loss),
                    getString(R.string.gain_muscle),
                    getString(R.string.athletic_preparation),
                    getString(R.string.self_defence),
                    getString(R.string.reduce_stress),
                    getString(R.string.endurance)
                )

                val adapterGoal =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.drop_menu_item_list,
                        goal
                    )

                autoTextViewDropMenuGoalProfileAthlete.setAdapter(adapterGoal)

                //SETTINGS FOR DROPDOWN LEVEL
                autoTextViewDropMenuLevelProfileAthlete.isEnabled = true
                layoutDropMenuLevelProfileAthlete.endIconMode =
                    TextInputLayout.END_ICON_DROPDOWN_MENU

                val level = arrayOf(
                    getString(R.string.beginner_athlete),
                    getString(R.string.intermediate_athlete),
                    getString(R.string.advanced_athlete),
                    getString(R.string.veteran_athlete)
                )

                val adapterLevel =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.drop_menu_item_list,
                        level
                    )

                autoTextViewDropMenuLevelProfileAthlete.setAdapter(adapterLevel)

                //SETTINGS FOR DROPDOWN NUMBER OF WORKOUT
                autoTextViewDropMenuNumOfWOProfileAthlete.isEnabled = true
                layoutDropMenuNumOfWOProfileAthlete.endIconMode =
                    TextInputLayout.END_ICON_DROPDOWN_MENU

                val days = arrayOf(
                    1, 2, 3, 4, 5, 6, 7
                )

                val adapterDays =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.drop_menu_item_list,
                        days
                    )

                autoTextViewDropMenuNumOfWOProfileAthlete.setAdapter(adapterDays)

                //Putting the checked one in the list
                checkbox1ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                    // Responds to checkbox being checked/unchecked
                    managerList(buttonView, isChecked)
                }

                checkbox2ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                    // Responds to checkbox being checked/unchecked
                    managerList(buttonView, isChecked)
                }

                checkbox3ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                    // Responds to checkbox being checked/unchecked
                    managerList(buttonView, isChecked)
                }

                checkbox4ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                    // Responds to checkbox being checked/unchecked
                    managerList(buttonView, isChecked)
                }

                checkbox5ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                    // Responds to checkbox being checked/unchecked
                    managerList(buttonView, isChecked)
                }

                checkbox6ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                    // Responds to checkbox being checked/unchecked
                    managerList(buttonView, isChecked)
                }

                checkbox7ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                    // Responds to checkbox being checked/unchecked
                    managerList(buttonView, isChecked)
                }

                checkbox8ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                    // Responds to checkbox being checked/unchecked
                    managerList(buttonView, isChecked)
                }

            }

            floatingActionButtonSaveProfileAthlete.setOnClickListener {

                floatingActionButtonEditProfileAthlete.visibility = View.VISIBLE
                floatingActionButtonSaveProfileAthlete.visibility = View.INVISIBLE

                layoutAthleteEditTextNameEditable.alpha = 1f
                layoutAthleteEditTextSurnameEditable.alpha = 1f
                textViewDateDataProfileAthlete.alpha = 1f

                heightFieldAthleteEditable.isEnabled = false
                layoutAthleteEditTextHeightEditable.endIconMode = TextInputLayout.END_ICON_NONE
                weightFieldAthleteEditable.isEnabled = false
                layoutAthleteEditTextWeightEditable.endIconMode = TextInputLayout.END_ICON_NONE
                autoTextViewDropMenuTypeOfWOProfileAthlete.isEnabled = false
                layoutDropMenuTypeOfWOProfileAthlete.endIconMode =
                    TextInputLayout.END_ICON_NONE
                autoTextViewDropMenuGoalProfileAthlete.isEnabled = false
                layoutDropMenuGoalProfileAthlete.endIconMode =
                    TextInputLayout.END_ICON_NONE
                autoTextViewDropMenuLevelProfileAthlete.isEnabled = false
                layoutDropMenuLevelProfileAthlete.endIconMode =
                    TextInputLayout.END_ICON_NONE
                autoTextViewDropMenuNumOfWOProfileAthlete.isEnabled = false
                layoutDropMenuNumOfWOProfileAthlete.endIconMode =
                    TextInputLayout.END_ICON_NONE
                checkbox1ProfileAthlete.isEnabled = false
                checkbox2ProfileAthlete.isEnabled = false
                checkbox3ProfileAthlete.isEnabled = false
                checkbox4ProfileAthlete.isEnabled = false
                checkbox5ProfileAthlete.isEnabled = false
                checkbox6ProfileAthlete.isEnabled = false
                checkbox7ProfileAthlete.isEnabled = false
                checkbox8ProfileAthlete.isEnabled = false


                val newHeight = heightFieldAthleteEditable.text.toString()
                val newWeight = weightFieldAthleteEditable.text.toString()
                val newTypeOfWO = autoTextViewDropMenuTypeOfWOProfileAthlete.text.toString()
                val newGoal = autoTextViewDropMenuGoalProfileAthlete.text.toString()
                val newLevel = autoTextViewDropMenuLevelProfileAthlete.text.toString()
                val newNumOfWO = autoTextViewDropMenuNumOfWOProfileAthlete.text.toString()

                if (listForCheckBox.size > 0 && CheckRegistrationFieldAthlete.checkHeight(newHeight.toInt()) && CheckRegistrationFieldAthlete.checkWeight(
                        newWeight.toInt()
                    )
                ) {
                    fireStore.updateAthlete(
                        currentUserId,
                        newHeight,
                        newWeight,
                        newTypeOfWO,
                        newGoal,
                        newLevel,
                        newNumOfWO,
                        listForCheckBox
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_edit_profile_athlete),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewPersonalAthlete.setImageBitmap(imageBitmap)

            val savePathPhoto = currentUserId?.let { storage.child("Photos").child(it) }

            val arrayByte = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayByte)
            val imageByte = arrayByte.toByteArray()

            savePathPhoto?.putBytes(imageByte)?.addOnSuccessListener {
                Log.d(TAG, "Picture uploaded successfully")
            }
        }
    }

    //FOR CHECK BOX
    private fun managerList(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            listForCheckBox.add(buttonView.text.toString())
        } else {
            listForCheckBox.remove(buttonView.text.toString())
        }
    }
}

